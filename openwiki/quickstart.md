---
type: Engineering Guide
title: ionet Engineering Quickstart
description: Entry point for engineers working on ionet, a JDK 25 multi-module distributed networking framework built around Netty, Aeron, SBE, and an action-based execution model.
resource: README.md
tags: [ionet, java, networking, quickstart]
---
# ionet Engineering Quickstart

## What this repository is

ionet is an AGPL-3.0 Java framework for low-latency distributed network services. Client connections enter through a Netty-based External Service, requests are routed over Aeron/SBE to Logic Services, and business methods are exposed through `@ActionController` and `@ActionMethod`. The framework supports combined single-process development and separated service deployment without requiring business actions to depend on Netty.

The repository is a Maven reactor at version `25.6` with 15 modules. It requires JDK 25. The root `pom.xml` is the authority for module membership, dependency versions, and build behavior; `README.md` and `README_CN.md` describe public capabilities and link to the user documentation.

## Start here

1. Confirm JDK 25 and Maven are active:

   ```bash
   java -version
   mvn -version
   ```

2. Compile the reactor from the repository root:

   ```bash
   mvn clean compile
   ```

3. Run tests explicitly. The root sets `maven.test.skip=true`, so plain `mvn test` does not exercise the suite:

   ```bash
   mvn test -Dmaven.test.skip=false
   ```

4. For a focused module change, include upstream reactor dependencies when needed:

   ```bash
   mvn test -Dmaven.test.skip=false -pl net-server -am
   mvn test -Dmaven.test.skip=false -pl net-common -Dtest=PublicationOfferKitTest
   ```

5. Read the concept page before changing a cross-module contract, then trace the concrete path in source. Static global registries and SPI initialization make apparently local changes capable of affecting the whole JVM.

## Mental model

```text
Client TCP/WebSocket
  -> external-netty pipeline
  -> external-core sessions and gateway contracts
  -> net-server route selection and Aeron publication
  -> net-logic-server fragment adapter
  -> core-framework action execution
  -> Aeron response
  -> external session write-back

net-center <-> startup discovery and server metadata synchronization
```

The [architecture overview](architecture/overview.md) explains how the control and data planes fit together. Its runtime sequences are detailed in [runtime flows](workflows/runtime-flows.md), while [domain concepts](domain/concepts.md) defines `cmdMerge`, `FlowContext`, server identity, communication models, and publisher semantics.

## Find the right area

| Change | Start with | Verify with |
| --- | --- | --- |
| Action parsing, invocation, validation, interceptors, response creation | `core-framework` | Core framework tests plus a logic-server flow test |
| SBE encoding, Aeron publication, queues, backpressure | `net-common` | Publisher and codec tests; check message-size limits |
| Discovery, routing, load-balancing state, correlated internal calls | `net-server`, then `net-center` | Server/center adapter and routing tests |
| Adapt transport messages into action execution | `net-logic-server` | Fragment and `DefaultFlowExecutor` behavior |
| Sessions, authentication hooks, external callbacks | `external-core` | External fragment paths and downstream transport tests |
| TCP/WebSocket pipelines | `external-netty` | Embedded-channel codec, selector, and session tests |
| Combined development startup | `run-one` | Registration order and duplicate-route detection |
| Spring, rooms, domain events, clients, proto/code generation | `extension-*` | The owning extension's focused tests and generated output |

The [source map](architecture/source-map.md) gives module dependencies and high-signal entrypoints. [Integrations and extensions](integrations/extensions.md) documents optional modules and supported customization boundaries. [Engineering operations](operations/engineering-guide.md) covers testing, runtime defaults, diagnosis, release checks, and recent evolution.

## Important current constraints

- Route lookup is `cmdMerge -> Server`; the open-source default keeps one active owner for a duplicate command route, with the latest registration replacing the prior owner.
- The center is not in the normal request path, but a `NetServer` still connects and registers with a reachable center. `RunOne.enableCenterServer()` provides the in-process development option.
- Publisher queues are bounded by default. Queue overflow and Aeron offer retry exhaustion drop messages and log sampled warnings; callers do not receive delivery acknowledgements.
- Fragment assembly is enabled by default. The publisher encoding buffer defaults to 64 KiB, and the effective maximum also depends on SBE field and Aeron publication limits.
- Several registries and configuration objects are static JVM-wide. Multiple independent runtimes in one JVM can share or replace codecs, fragment handlers, skeletons, communication state, and sessions.
- Some advanced `DefaultCommunicationAggregation` methods return enterprise-function responses rather than implementing collection, binding, or remote event-bus behavior.

## Documentation map

- [Architecture overview](architecture/overview.md): layers, dependency direction, startup composition, control/data planes, and constraints.
- [Source map](architecture/source-map.md): module ownership, key files, SPI resources, and change hotspots.
- [Runtime flows](workflows/runtime-flows.md): startup, request/response, internal calls, broadcasts, tracing, and generation.
- [Domain concepts](domain/concepts.md): routing, action execution, identity, sessions, communication, and transport semantics.
- [Integrations and extensions](integrations/extensions.md): Netty transports, clients, Spring, events, rooms, protobuf, and SDK generation.
- [Engineering operations](operations/engineering-guide.md): build/test commands, runtime tuning, incident checks, release workflow, and git-informed evolution.

## Source precedence

For implementation questions, prefer current source and `pom.xml` over prose. Public usage documentation lives at <https://iohao.github.io/ionet/en/docs/intro>; this wiki is an engineering map of the local repository. `openwiki/INSTRUCTIONS.md` is the user-authored documentation brief and must not be rewritten by routine wiki updates.

## Backlog

- **Cross-host data plane** — `net-center/.../ConnectRequestMessageOnFragment.java` and `net-common/.../AeronConst.java`: peer publications inspected here use `aeron:ipc`; verify the intended multi-machine transport setup with an executable deployment example before documenting it as supported by this implementation path.
- **Failure recovery** — `net-server/.../DefaultUnavailableImageHandler.java` and center registration code: graceful offline propagation is visible, but crash detection and center restart/re-registration behavior need runtime evidence.
- **End-to-end test topology** — `external-netty/src/test` and `extension-client/src/test`: focused codec/session tests exist, but a full client-to-action-to-response harness was not found in the initial pass.
