---
type: Workflow Guide
title: Runtime and Generation Flows
description: End-to-end ionet workflows for startup and discovery, client requests, internal calls, broadcasts, tracing, and protocol/client code generation.
resource: run-one/src/main/java/com/iohao/net/app/RunOne.java
tags: [ionet, workflows, messaging, startup]
---
# Runtime and Generation Flows

## Startup and discovery

`RunOne` provides the clearest combined-deployment path:

```text
application-created Aeron
  -> shared Publisher.startup()
  -> optional CenterServer.onStart()
  -> NetServerBuilder.build()
  -> ExternalServer.startup(netServer)
  -> LogicServerApplication.startup(each LogicServer)
  -> NetServer.addServer(each Server)
  -> NetServer.onStart()
  -> duplicate route detection
```

`LogicServerApplication` first builds a `BarSkeleton`, then a `Server`. `ServerBuilder` extracts every action `cmdMerge`, registers the skeleton by server ID, and derives publication metadata from `CoreGlobalConfig.netId`. `NetServerBuilder` creates connection, routing, balancing, future, communication, listener, and shutdown components.

During `NetServer.onStart()`, the runtime waits for its center publication, publishes hosted server registrations, and waits for a correlated response. The center's `ConnectRequestMessageOnFragment` creates or reuses a publication for the joining `netId`, stores metadata, informs existing runtimes, and returns known servers. Runtime `ConnectResponseMessageOnFragment` delegates online state to `ServerLineKit`, which updates listeners and balancing maps.

Why this order matters:

- Action routes must exist before server metadata is advertised.
- Futures must be registered before publishing correlated requests; recent history hardened this ordering in both center registration and internal communication.
- External ingress should not accept useful traffic until route discovery has populated `FindServer`.
- `netId` must be consistent between `NetServerBuilder` and global server metadata. See [domain concepts](../domain/concepts.md#server-and-network-identity).

## Client request and response

```text
TCP/WebSocket frame
  -> transport codec -> CommunicationMessage
  -> CmdCheckHandler
  -> SocketUserSessionHandler
  -> SocketCmdAccessAuthHandler
  -> optional CmdCacheHandler
  -> UserRequestHandler
  -> FindServer.getServer(cmdMerge)
  -> Publisher queue -> SBE -> Aeron
  -> NetServerAdapter template dispatch
  -> UserRequestMessageOnFragment
  -> FlowContext + ActionCommand
  -> DefaultSkeletonThreadPipeline
  -> DefaultFlowExecutor
  -> interceptors / parse / validate / MethodHandle invoke
  -> DefaultActionAfter
  -> UserResponseMessage -> Aeron
  -> external UserResponseMessageOnFragment
  -> ExternalWriteKit -> UserSession.writeAndFlush
```

`UserRequestHandler` enriches the message with session state, the global `netId`, and the configured `UserRequestEnhance` before enqueueing it to the selected server publication. A missing route returns the error-bearing message directly to the same session.

The logic fragment adapter resolves the target `BarSkeleton` and `ActionCommand`, creates a `FlowContext`, and chooses an executor. `DefaultFlowExecutor` binds the context through `ScopedValue`, binds the trace ID for logging, and runs the framework flow. Nonfatal exceptions are translated by `ActionMethodExceptionProcess`; request-like communication invokes `ActionAfter` to create an error response, while fire-and-forget internal sends do not respond.

The concepts and extension hooks in this path are defined in [domain concepts](../domain/concepts.md) and [integrations](../integrations/extensions.md#external-gateway-and-netty-transports).

## Logic-to-logic communication

`DefaultCommunicationAggregation` implements RPC-like internal calls:

1. Resolve the target from `FindServer` using the request route or explicit identity.
2. Assign `hopCount` for cross-service execution.
3. Allocate and register a `futureId` in `FutureManager` before publishing.
4. Publish `RequestMessage` to the target server's publication.
5. Execute the target action through `RequestMessageOnFragment`.
6. Publish `ResponseMessage` back to the source server.
7. Complete the matching future in `ResponseMessageOnFragment`.

Synchronous `call` waits for `CoreGlobalConfig.timeoutMillis + 200 ms` and converts interruption, timeout, or execution failure into `internalCommunicationError`. `send` follows the same routing path with `SendMessage` but creates no future or response.

Advanced collection, logic binding, and remote EventBus operations are not all implemented by the open-source default. Check the method body before building a workflow around an interface capability.

## Broadcast and external callbacks

Broadcast messages are sent to an explicit external server when `externalServerId` is present, otherwise to servers known by the external balancer. The default broadcast trace hook records user, user-list, and multicast broadcasts. External fragment handlers resolve the target sessions and write encoded messages through `ExternalWriteKit`.

Logic code can call transport-independent `OnExternal` handlers through `ExternalRequestMessage`. The external runtime resolves the handler or built-in session operation, completes it asynchronously, and sends `ExternalResponseMessage` back to the waiting future. Built-ins include user binding/existence, attachment updates, and forced offline behavior.

Room range broadcasts reuse this core broadcast protocol; the [extensions page](../integrations/extensions.md#domain-events-and-rooms) explains the room/event composition.

## Trace propagation

Release 25.6 introduced full propagation across request, async, and transport boundaries:

- `ExternalGlobalConfig.userRequestEnhance` creates trace IDs for incoming messages.
- `DefaultSbeCodec` serializes/deserializes trace IDs in common SBE metadata.
- `DefaultFlowExecutor` installs the ID in the execution logging context.
- `FlowCommon.execute`, `executeUser`, and `executeVirtual` decorate asynchronous tasks with the current trace.
- communication decorators copy trace state into downstream calls.

When adding a new async boundary, use the existing trace decorators. When adding a new transport codec, preserve common message metadata. Broadcast tracing is a separate `BroadcastTrace` hook configured in `CoreGlobalConfig`.

## Protocol and client code generation

The generation toolchain connects runtime action metadata to client SDKs:

```text
annotated Java message types + Java source comments
  -> extension-jprotobuf analysis
  -> grouped .proto files + ProtoFileMerge metadata

BarSkeleton action/broadcast Document model + proto metadata
  -> extension-codegen language generator
  -> Beetl templates
  -> TypeScript / C# / GDScript action APIs and listeners
```

`ProtoGenerateFile` requires both readable source and loadable classes. It scans configured packages/source roots and groups output through `ProtoFileMerge`. `extension-codegen` reads the framework `Document` model and proto merge metadata to render imports, actions, broadcasts, examples, and optional error codes.

Recent commits added multiline proto comments and centralized comment/string escaping across all three client languages. A generation change should therefore verify source-comment extraction, import grouping, template escaping, and representative full output. Focused tests exist, but generated SDK compilation is not part of the current suite.

## Failure checkpoints

- Startup stalls: verify center endpoint, publication connection, `netId`, publisher startup, and center response future.
- Route error: inspect action discovery, server `cmdMerges`, center synchronization, and `DefaultLogicServerLoadBalanced` ownership.
- Missing response: inspect publisher drops, future registration/removal, source/external server IDs, and response fragment registration.
- Large message loss: compare publisher buffer, SBE payload limit, Aeron maximum message length, and fragment assembler configuration.
- Missing handler: verify the owning `CoreConfigLoader` service file loaded both codec and fragment registrations.

The operational commands and default limits for these checks are in the [engineering guide](../operations/engineering-guide.md).
