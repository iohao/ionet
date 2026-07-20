---
type: Integration Guide
title: Integrations and Extensions
description: Integration map for ionet external transports, simulation clients, Spring injection, domain events, rooms, JProtobuf generation, client SDK generation, and SPI extension points.
resource: external-core/src/main/java/com/iohao/net/external/core/ExternalServerBuilder.java
tags: [ionet, integrations, extensions, spi]
---
# Integrations and Extensions

## Extension strategy

ionet combines builders/creator interfaces for application customization with Java `ServiceLoader` for module bootstrapping. Keep dependencies pointed toward the [core architecture](../architecture/overview.md): transport-independent contracts belong in `external-core` or `core-framework`; concrete Netty behavior belongs in `external-netty`; optional ecosystem behavior belongs in `extension-*` modules.

## External gateway and Netty transports

`external-core` defines `ExternalServer`, `ExternalServerBuilder`, sessions, hooks, gateway callbacks, and transport bootstrap interfaces. The builder creates External `Server` metadata and lets an `ExternalJoinSelector` fill transport defaults. Important boundaries include:

- `ExternalServerCreator` for the server implementation.
- `MicroBootstrap` and `MicroBootstrapFlow` for connection startup/pipeline phases.
- `UserSessions` and `UserSession` for connection identity and writes.
- `UserHook`, `AccessAuthenticationHook`, and `IdleHook` for lifecycle policy.
- `UserRequestEnhance` for inbound metadata such as trace IDs.
- `OnExternal` for logic-to-gateway operations.

`external-netty` supplies TCP and WebSocket selectors through `META-INF/services/com.iohao.net.external.core.micro.ExternalJoinSelector`. TCP uses length-prefixed binary frames. WebSocket uses HTTP upgrade, optional verification, binary frames, and configurable path/frame limits. Both share the ordered pipeline described in [runtime flows](../workflows/runtime-flows.md#client-request-and-response).

To add a connection type, implement a selector and flow, register the selector through SPI, provide codecs/session behavior, and add a matching client connector when simulation support is expected. Do not extend `ExternalJoinEnum` alone and assume selection is complete; inspect `ExternalJoinSelectors` and selector tests.

## Simulation client

`extension-client` depends on `external-netty` and provides TCP/WebSocket clients for examples, load simulation, and interactive commands. `TcpClientStartup` and `WebSocketClientStartup` construct matching Netty pipelines and bind a channel to `ClientUserChannel`.

`AbstractInputCommandRegion` creates requests and assigns message IDs. `ClientUserChannel` correlates responses by message ID and dispatches broadcasts by `cmdMerge`. Customize through `ClientConnect`, `ClientUser`, input command regions, callback/payload delegates, broadcast listeners, or a replacement channel-read strategy.

Recent WebSocket work moved to a Netty connector with shared event-loop lifecycle and added embedded-channel/handshake tests. There is no visible callback timeout/expiry path, so long-lived simulation processes should manage unanswered callbacks and connector shutdown deliberately.

## Spring

`extension-spring` is a small adapter, not auto-configuration. `ActionFactoryBeanForSpring` implements `ApplicationContextAware`, resolves action controllers by class, and configures the global `DependencyInjectionPart` to recognize Spring `@Component` classes.

Applications must provide `spring-context` because the dependency is `provided`, register the factory in their context, and ensure actions are Spring beans before building `BarSkeleton`. The adapter mutates JVM-wide dependency-injection state and has no module test, so verify both Spring resolution and non-Spring startup when changing this boundary.

## Domain events and rooms

`extension-domain-event` provides an in-process asynchronous event system, separate from the distributed communication EventBus. `DomainEventSetting` registers generic `DomainEventHandler<T>` implementations. `DomainEventApplication` groups handlers by topic, creates one LMAX Disruptor per topic, and starts producers exposed through `DomainEventPublish` or `Eo`.

Customization points include `DisruptorCreator`, producer type, wait strategy, ring size, and exception handler. Generic topic discovery relies on concrete parameterized handler interfaces; indirect/raw generic handlers need careful verification. Managers/producers are static, so test isolation matters.

`extension-room` builds on `core-framework` and domain events. It supplies in-memory room/player/seat management, operation-code dispatch through `OperationFactory`, verify-then-process `OperationContext`, optional asynchronous operation handling, and targeted broadcasts. The default room operation call is synchronous; asynchronous execution requires publishing its `OperationContext`. Room changes should verify consistency across room/player maps and the [core broadcast flow](../workflows/runtime-flows.md#broadcast-and-external-callbacks).

## JProtobuf and proto generation

`extension-jprotobuf` analyzes JProtobuf-annotated classes and their Java source to generate grouped `.proto` files. The main path is:

- configure packages, source roots, and output in `ProtoGenerateFile`;
- use `ProtoJavaAnalyse` to combine reflection with source comments;
- group files/imports through `ProtoFileMerge`;
- customize naming through `ProtoGenerateSetting.fieldNameFunction` and enum values through `EnumReadable`.

Generation needs readable source and loadable classes. Public fields and concrete generic arguments are the safest supported shapes. Tests cover generation examples and multiline comment rendering, but not a broad schema compatibility suite.

## Client SDK generation

`extension-codegen` consumes `core-framework`'s action/broadcast `Document` model plus proto merge metadata. It renders TypeScript, C#, and GDScript through Beetl templates under `src/main/resources/generate`.

Language generators configure output, namespaces/proto prefixes, error-code files, command visibility, and examples. They are `final`; templates and exposed settings are the intended customization surface. `DocumentGenerateAbout` centralizes comment rendering and string escaping so templates stay consistent.

When changing generated output, check all supported languages and both action/broadcast templates. The latest changes specifically addressed multiline comments, language comment prefixes, and escaping; focused tests do not compile generated SDKs. See the generation sequence in [runtime flows](../workflows/runtime-flows.md#protocol-and-client-code-generation) and verification advice in the [engineering guide](../operations/engineering-guide.md#verification-by-change-area).

## SPI checklist

For a new core transport message or module integration:

1. Define or update protocol/SBE representation.
2. Register the Java message codec with `SbeMessageManager`.
3. Register each inbound `OnFragment` by template ID.
4. Add the module `CoreConfigLoader` to its `META-INF/services` file.
5. Verify template ID bounds and duplicate registration behavior.
6. Add a focused adapter/round-trip test with tests explicitly enabled.

For a new external transport, use the analogous `ExternalJoinSelector` service registration. The [source map](../architecture/source-map.md#spi-and-generated-sources) lists existing providers.
