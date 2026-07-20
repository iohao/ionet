---
type: Architecture Overview
title: Runtime Architecture
description: Architecture of ionet's external gateway, Aeron/SBE runtime, logic execution layer, discovery center, startup composition, and JVM-wide registries.
resource: pom.xml
tags: [ionet, architecture, aeron, netty]
---
# Runtime Architecture

## Layered design

ionet separates client transport from business execution. The dependency direction is intentionally inward toward reusable Java business abstractions:

```text
common-kit
  -> core-framework
    -> net-common
      -> net-server
        -> net-logic-server
        -> external-core
          -> external-netty
      -> net-center

run-one -> net-center + external-netty + net-logic-server
```

`core-framework` owns the Action programming model and does not depend on Netty. `net-common` adds Aeron/SBE transport primitives. `net-server` composes routing, connections, futures, load-balancing state, and message dispatch. `external-core` and `external-netty` form the client-facing gateway, while `net-logic-server` converts transport fragments into action executions. See the [source map](source-map.md) for module entrypoints.

## Runtime roles

### External Service

The External Service terminates TCP or WebSocket connections, maintains `UserSession` state, applies route checks and access hooks, and forwards valid requests. `ExternalServerBuilder` creates transport-neutral settings; `external-netty` supplies the built-in Netty selectors and pipelines through `ServiceLoader`.

The default custom pipeline is ordered as route existence check, session lifecycle, route authorization, optional command cache, then `UserRequestHandler`. This gateway participates directly in the [client request flow](../workflows/runtime-flows.md#client-request-and-response).

### Logic Service

A Logic Service supplies a configured `BarSkeleton` and `Server` definition through `LogicServer`. `LogicServerApplication` builds both, extracts action routes, and records the logic server and skeleton in JVM-wide managers. It owns business actions, not socket concerns.

The Action runtime described in [domain concepts](../domain/concepts.md#action-and-route-model) parses controllers at startup and dispatches requests through a thread pipeline, `FlowContext`, interceptors, method invocation, exception translation, and response creation.

### Net Server

`NetServerBuilder` is the composition root for the internal runtime. It assembles:

- `ConnectionManager` and Aeron publications/subscriptions.
- `FindServer`, `CmdRegions`, and `BalancedManager` for routing state.
- `FutureManager` and `DefaultCommunicationAggregation` for correlated calls.
- `SkeletonThreadPipeline` for choosing business executors.
- listeners and shutdown hooks for online/offline transitions.

The built setting is injected into participating handlers. This makes `net-server` the main ownership boundary for changes to discovery state, route selection, internal communication, and shutdown behavior.

### Center Service

The center is a discovery coordinator, not a data-path proxy. Joining runtimes send server metadata and command routes. `ConnectRequestMessageOnFragment` stores the joining connection, informs existing peers, and returns known servers to the newcomer. Each runtime processes responses through `ServerLineKit`, which updates `ServerManager`, command regions, listeners, and balancing maps.

The embedded center is optional in `RunOne`, but a built `NetServer` still creates a center publication and waits for registration; a separate reachable center is therefore required when the embedded one is disabled. Registration uses UDP endpoint defaults, while inspected peer publications use `aeron:ipc`; cross-host data-plane behavior remains a documented evidence gap in the [quickstart backlog](../quickstart.md#backlog).

## Control plane and data plane

```text
Control plane
NetServer -> center publication -> Center Service
Center Service -> server metadata -> all NetServers
NetServer -> local routing and publication maps

Data plane
External/Logic Service -> named Publisher queue
Publisher thread -> SBE encode -> Aeron publication
NetServerAdapter -> template-id fragment handler
Fragment handler -> gateway session or action runtime
```

The `Publisher` isolates producers from Aeron offers using per-publication queues and one encoding thread. SBE codecs are selected by message class; inbound handlers are selected by template ID. `CoreConfigLoader` implementations register codecs and handlers during `CoreGlobalConfig` initialization. The [engineering guide](../operations/engineering-guide.md#transport-capacity-and-loss) explains the resulting sizing and loss semantics.

## Combined startup

`RunOne` is a composition helper, not a business application framework:

1. Require an application-created `Aeron` instance.
2. Start the shared publisher.
3. Optionally start an in-process center.
4. Build `NetServer` runtime dependencies.
5. Start the external server with the net-server setting.
6. Convert each `LogicServer` into a `Server` plus `BarSkeleton`.
7. Start registration/polling and detect duplicate routes.

This ordering ensures route metadata exists before registration and client traffic. The exact sequence and response path live in [runtime flows](../workflows/runtime-flows.md#startup-and-discovery).

## Global state and extension boundaries

Several extension mechanisms are deliberately process-wide:

- `CoreGlobalConfig` and `NetCommonGlobalConfig` hold mutable defaults and shared services.
- `SbeMessageManager` and `OnFragmentManager` are global codec/handler registries; later duplicate fragment registration replaces the earlier handler.
- `BarSkeletonManager`, `LogicServerManager`, `ServerManager`, and `CommunicationKit` expose shared runtime state.
- `ExternalServerSingle.userSessions` is the common external write-back location.
- Spring integration mutates the global `DependencyInjectionPart`.

Use existing builders and creator interfaces for replacement components. New internal message types require both an SBE codec registration and an inbound fragment handler registration, normally through a module `CoreConfigLoader`. Optional customization points are cataloged in [integrations and extensions](../integrations/extensions.md).

## Current implementation boundaries

- `DefaultLogicServerLoadBalanced` stores one active server per `cmdMerge`, tag, and ID. Duplicate command ownership is replacement, not round-robin or fallback balancing.
- `DefaultCommunicationAggregation` implements common request/response, send, external call, and broadcast paths, but some collection, binding, and remote EventBus methods are enterprise placeholders.
- Graceful shutdown publishes offline state and closes publications. Runtime crash detection and center reconnection were not established from inspected source.
- `netId` exists both in `CoreGlobalConfig`-derived server metadata and `NetServerBuilder`; configure them consistently because server publication names derive from the global ID.

These boundaries are operationally significant and should be checked before treating README-level distributed claims as proof of a specific deployment topology.

## Change guidance

- Keep action execution changes in `core-framework` unless transport metadata must change.
- Keep route/discovery/future changes in `net-server` and inspect the corresponding center fragments.
- Keep session and hook contracts transport-neutral in `external-core`; put Netty handlers and codecs in `external-netty`.
- When changing a protocol field, trace Java protocol objects, SBE schema/generated codecs, codec registration, and every relevant fragment consumer.
- When changing startup, verify both `RunOne` and independently assembled center/net/external/logic services.
