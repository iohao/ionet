---
type: Domain Model
title: Core Runtime Concepts
description: Canonical vocabulary for ionet actions, routes, flow contexts, service identity, sessions, communication models, routing ownership, and publisher delivery semantics.
resource: core-framework/src/main/java/com/iohao/net/framework/core/BarSkeleton.java
tags: [ionet, domain, routing, actions]
---
# Core Runtime Concepts

## Action and route model

An **Action** is a Java business method discovered from an `@ActionController(cmd)` class and an `@ActionMethod(subCmd)` method. The framework combines command and subcommand into `cmdMerge`, the routing key used by gateways, server discovery, logic balancing, and action lookup.

`BarSkeletonBuilder` accepts explicit controllers or package scans, applies builder enhancements, parses methods into `ActionCommandRegions`, initializes array-based lookup, and creates the `BarSkeleton`. An `ActionCommand` carries invocation metadata; `DefaultActionMethodInvoke` performs argument parsing/validation and MethodHandle dispatch. `ActionMethodInOut` instances wrap invocation, and `ActionAfter` creates protocol responses.

A logic server advertises its action route set through `ServerBuilder`. Those routes flow through center discovery into `FindServer`. This is why action discovery, metadata registration, and runtime routing are one cross-module contract described in [runtime flows](../workflows/runtime-flows.md#startup-and-discovery).

## FlowContext and execution

`FlowContext` represents one request or internal message execution. It carries protocol metadata such as user, command, source/target server IDs, communication type, errors, trace ID, and communication helpers. `DefaultFlowExecutor` binds it with JDK `ScopedValue` so downstream framework code can access the current context without thread-local leakage.

Execution is staged:

```text
fragment adapter -> FlowContext -> thread pipeline -> flow executor
  -> interceptors -> parse/validate/invoke -> ActionAfter
```

`DefaultSkeletonThreadPipeline` selects user, simple, or virtual executors according to request metadata such as hop count. Async helpers on `FlowCommon` preserve trace context. Fatal JVM errors are rethrown; ordinary failures become framework error responses except for `INTERNAL_SEND`, which is intentionally response-free.

## Server and network identity

A `Server` is discovery metadata for one External or Logic Service. It contains:

- `id`: service identity used for direct addressing and skeleton/session association.
- `netId`: identity of the hosting net runtime.
- `pubName`: named publisher target, currently derived from `netId`.
- `type`, `tag`, `name`, and `ip`.
- advertised logic `cmdMerges` and optional payload metadata.

`CoreGlobalConfig.netId` defaults to a random value greater than 1000. `NetServerBuilder.netId` defaults from it, but can be set separately. Because `ServerBuilder` still reads the global value, configure the global ID before building hosted servers and avoid diverging builder/global IDs.

The center distributes these records; `ServerManager` stores known servers; `ConnectionManager` owns publications by net/server identity. The [architecture overview](../architecture/overview.md#control-plane-and-data-plane) separates this discovery role from message delivery.

## External sessions and users

An External Service owns `UserSessions` and concrete transport sessions. A `UserSession` is the connection-facing state for one client and carries connection attributes, binding state, user identity, and write operations. Netty's `SocketUserSessionHandler` creates/removes sessions and invokes `UserHook`; route authorization runs before forwarding.

User identity is bound explicitly, commonly from a login Action via `FlowContext.bindingUserId`. Responses carry the external server and session identity required to return to the correct connection. Broadcasts target a user, a user list, a multicast group, or all known external servers.

Transport-independent external operations use `OnExternal`, allowing logic code to inspect or mutate external session state without importing Netty. `external-core` owns these contracts, while [Netty integration](../integrations/extensions.md#external-gateway-and-netty-transports) owns channel mechanics.

## Communication models

The core protocols represent several semantics:

| Model | Behavior |
| --- | --- |
| User request/response | Client request executes an Action and returns to the originating session |
| Internal request/response | Logic service call uses `futureId` correlation and timeout handling |
| Internal send | Fire-and-forget logic call; execution errors do not produce a response |
| Broadcast | Logic service sends user/user-list/multicast messages through external services |
| External call | Logic service invokes an `OnExternal` session/gateway operation and awaits a response |
| EventBus | Framework communication API includes local/distributed event concepts; open-source remote behavior must be checked per method |

`CommunicationAggregation` is the abstraction; `DefaultCommunicationAggregation` is the concrete open-source implementation. Do not infer implementation from interface breadth: collection, binding, or remote operations may return enterprise responses.

## Routing and balancing

`FindServer` converts a message's route into a `Server`. `CmdRegions` tracks route/server associations for discovery and diagnostics. `BalancedManager` holds separate logic and external balancing strategies.

The default logic strategy indexes by tag, server ID, and `cmdMerge`. It has one value per key. Registering a second server for the same command replaces ownership; unregistering removes a key only if that server still owns it. This avoids stale-server removal deleting a newer owner, but it is not round-robin, failover, or multi-owner balancing.

Route existence is checked twice for different reasons: `CmdCheckHandler` rejects unknown client routes early, while `FindServer` selects the current logic owner immediately before publication. Routing changes should follow the cross-module checklist in the [source map](../architecture/source-map.md#routing-changes).

## Publisher and delivery semantics

`DefaultPublisher` decouples callers from Aeron using one queue per publication and a single publisher thread. Encoding happens on that thread into a reusable direct buffer. Important defaults from `CoreGlobalConfig` are:

- queue capacity: 65,536 messages per publication;
- drain limit: 1,024 messages per publication per loop;
- offer retries: 2 for Aeron backpressure/admin action;
- encoding buffer: 64 KiB;
- fragment assembly: enabled;
- fragment poll limit: 100.

These are bounded-throughput, not guaranteed-delivery semantics. A full queue drops the newest message. Retry exhaustion drops the message and emits sampled warnings. Unknown publication names also do not create delivery confirmation. Tune and monitor these values using [engineering operations](../operations/engineering-guide.md#transport-capacity-and-loss).

## Registries and lifecycle

Static registries optimize lookup and simplify module bootstrapping, but their lifecycle is the JVM lifecycle. `ServiceLoader` populates SBE codec and fragment registries when `CoreGlobalConfig` initializes. `OnFragmentManager` uses a fixed template-ID array and later registration replaces earlier ownership with a warning.

Builders expose explicit creator/hook replacement points around this global core. Use those supported boundaries rather than mutating internal maps. The catalog is in [integrations and extensions](../integrations/extensions.md).
