---
type: Source Map
title: Module and Source Map
description: Practical ownership map for ionet's 15 Maven modules, core entrypoints, SPI resources, tests, and cross-module change hotspots.
resource: pom.xml
tags: [ionet, modules, source-map, maintenance]
---
# Module and Source Map

## Dependency spine

The root `pom.xml` declares every reactor module and shared dependency/plugin versions. Follow module POM dependencies before moving responsibilities across boundaries. The [architecture overview](overview.md) explains why these layers exist; this page answers where to look.

## Core and runtime modules

| Module | Ownership | High-signal sources |
| --- | --- | --- |
| `common-kit` | General collections, attributes, concurrency, executors, timers, source inspection, strings, and trace utilities | `com/iohao/net/common/kit/**`; tests mirror utility packages |
| `core-framework` | Action annotations and parsing, `BarSkeleton`, `FlowContext`, protocol objects, codecs, communication APIs, plugins, documents | `CoreGlobalConfig`, `BarSkeletonBuilder`, `DefaultFlowExecutor`, `core/flow/internal`, `communication`, `protocol` |
| `net-common` | Aeron/SBE codecs and registries, publisher queues, offer retries, adapters, shared transport constants | `DefaultPublisher`, `PublisherMessageKit`, `PublicationOfferKit`, `OnFragmentManager`, `SbeMessageManager`, `AeronConst` |
| `net-server` | Runtime composition, center/peer connections, server discovery state, routing, balancing, internal communication, shutdown | `NetServerBuilder`, `NetServerSetting`, `DefaultCommunicationAggregation`, `DefaultFindServer`, `connection`, `fragment`, `balanced` |
| `net-logic-server` | Adapters from user/internal SBE messages into logic actions and back | `LogicServerApplication`, `fragment/AbstractRequestOnFragment`, request/send/response fragment handlers |
| `net-center` | Discovery coordinator and server/publication metadata synchronization | `CenterServerBuilder`, `CenterAdapter`, `fragment/ConnectRequestMessageOnFragment` |
| `external-core` | Transport-neutral external gateway, sessions, hooks, external callbacks, external Aeron fragment handlers | `ExternalServerBuilder`, `ExternalSetting`, `session`, `hook`, `net/external`, `net/fragment` |
| `external-netty` | Built-in TCP/WebSocket server implementations, codecs, channel pipelines, socket sessions | `DefaultExternalServer`, `micro/*BootstrapFlow`, `handler`, `session`, join selectors |
| `run-one` | Single-JVM composition of center, external, and multiple logic services | `com/iohao/net/app/RunOne.java` |

The core request sequence crosses these modules as documented in [runtime flows](../workflows/runtime-flows.md). Route and identity terminology is centralized in [domain concepts](../domain/concepts.md).

## Optional extension modules

| Module | Ownership | High-signal sources |
| --- | --- | --- |
| `extension-client` | Simulation/test clients, TCP/WebSocket connectors, commands, callbacks, broadcast listeners | `join/*ClientStartup`, `ClientConnects`, `AbstractInputCommandRegion`, `user/ClientUserChannel` |
| `extension-domain-event` | In-process asynchronous domain events backed by LMAX Disruptor | `DomainEventApplication`, `DomainEventSetting`, `DomainEventPublish`, `Eo` |
| `extension-room` | In-memory rooms, players/seats, operation dispatch, targeted user broadcasts | `Room`, `RoomService`, `SimpleRoomService`, `operation`, `DefaultRangeBroadcast` |
| `extension-jprotobuf` | Generate grouped `.proto` files from JProtobuf-annotated Java types and source comments | `ProtoGenerateFile`, `ProtoJavaAnalyse`, `ProtoFileMerge`, `ProtoGenerateSetting` |
| `extension-codegen` | Generate TypeScript, C#, and GDScript client APIs from framework documents | language `*DocumentGenerate` classes, `DocumentGenerateAbout`, `src/main/resources/generate` |
| `extension-spring` | Resolve action controllers from a Spring `ApplicationContext` | `ActionFactoryBeanForSpring` |

See [integrations and extensions](../integrations/extensions.md) for workflow and customization details.

## Startup and dispatch entrypoints

- Combined startup: `run-one/src/main/java/com/iohao/net/app/RunOne.java`.
- Internal runtime composition: `net-server/src/main/java/com/iohao/net/server/NetServerBuilder.java`.
- Logic application adaptation: `net-logic-server/src/main/java/com/iohao/net/server/logic/LogicServerApplication.java`.
- External gateway composition: `external-core/src/main/java/com/iohao/net/external/core/ExternalServerBuilder.java`.
- Netty ingress: `external-netty/src/main/java/com/iohao/net/external/core/netty/handler/UserRequestHandler.java`.
- Business runtime composition: `core-framework/src/main/java/com/iohao/net/framework/core/BarSkeletonBuilder.java`.
- Center join handling: `net-center/src/main/java/com/iohao/net/center/fragment/ConnectRequestMessageOnFragment.java`.
- Runtime online/offline state: `net-server/src/main/java/com/iohao/net/server/fragment/ServerLineKit.java`.

## SPI and generated sources

`CoreGlobalConfig` triggers `Preloading`, which loads `CoreConfigLoader` providers. Provider declarations live under `src/main/resources/META-INF/services/com.iohao.net.framework.core.CoreConfigLoader` in `net-center`, `net-server`, `net-logic-server`, and `external-core`. They register module-specific SBE codecs and fragment consumers.

`external-netty` registers TCP and WebSocket `ExternalJoinSelector` implementations in its own service file. When adding a transport, provide the selector/flow implementation and SPI declaration; do not add Netty knowledge to `external-core`.

`net-common/src/main/java/com/iohao/net/sbe` contains generated SBE codecs. Treat broad mechanical changes there as schema/code-generation output and inspect the handwritten codec/registration layer separately. Code-generation templates for clients live under `extension-codegen/src/main/resources/generate/{ts,csharp,gdscript}`.

## Tests and evidence

Tests are conventional `src/test/java` JUnit tests but are disabled by the root property unless explicitly enabled. Stronger focused coverage exists for:

- Publisher queues, drain strategy, retries, shutdown, and Aeron test publications in `net-common/src/test`.
- Routing, connection management, communication aggregation, and server adapters in `net-server/src/test`.
- Netty framing, selectors, sessions, and WebSocket client setup in `external-netty/src/test` and `extension-client/src/test`.
- Generated proto comments and client-code comment escaping in `extension-jprotobuf/src/test` and `extension-codegen/src/test`.

Some extension tests are examples with weak or no assertions, and no full socket-to-action integration harness was found. Choose checks using [engineering operations](../operations/engineering-guide.md#verification-by-change-area).

## Cross-module hotspots

### Protocol or SBE field changes

Inspect protocol objects in `core-framework`, handwritten codecs in runtime modules, generated classes under `net-common/com/iohao/net/sbe`, `CoreConfigLoader` registrations, and every `OnFragment` consumer. Fragment template IDs must fit the fixed `OnFragmentManager` table.

### Routing changes

Trace `CmdInfo/cmdMerge` and action parsing in `core-framework`, route extraction in `ServerBuilder`, center metadata serialization, `CmdRegions`, `DefaultFindServer`, and both logic/external balancing implementations. Current duplicate-route behavior is described in [domain concepts](../domain/concepts.md#routing-and-balancing).

### Request execution changes

Read `AbstractRequestOnFragment`, `DefaultSkeletonThreadPipeline`, `BarSkeleton`, `DefaultFlowExecutor`, `FlowExecutorKit`, `DefaultActionMethodInvoke`, and `DefaultActionAfter` as one pipeline. Trace propagation now crosses `FlowContext`, async decorators, and SBE, so it must be preserved.

### External transport changes

Keep connection-independent contracts in `external-core`; change TCP/WebSocket framing and handlers in `external-netty`; then check `extension-client` for a matching client codec/connector.

### Documentation generation changes

`extension-jprotobuf` provides proto metadata used by `extension-codegen`. Verify comments, imports, escaping, and output in every supported language template, not only the Java helper.
