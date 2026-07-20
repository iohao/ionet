---
type: Operations Runbook
title: Build, Test, and Operate ionet
description: Engineer runbook for ionet builds, targeted tests, transport capacity, startup diagnosis, shutdown, releases, and git-informed recent behavior changes.
resource: pom.xml
tags: [ionet, operations, testing, maven]
---
# Build, Test, and Operate ionet

## Toolchain and build

The root Maven reactor requires JDK 25 and currently reports version `25.6`. Shared versions include Aeron `1.51.1`, Netty `4.1.136.Final`, JUnit `6.1.2`, JProtobuf `2.4.23`, Protobuf Java `4.35.1`, Disruptor `4.0.0`, Spring Context `7.0.8`, and Beetl `3.20.1.RELEASE`.

```bash
# Compile all modules
mvn clean compile

# Package artifacts; tests are skipped by the root default
mvn clean package

# Aggregate Javadoc
mvn clean javadoc:aggregate

# Compile one module and required reactor dependencies
mvn clean compile -pl net-server -am
```

The compiler is configured for Java 25 with parameter metadata and Lombok annotation processing. Source and Javadoc JARs are attached by the root build.

## Tests are opt-in

`pom.xml` sets `maven.test.skip=true` and uses it for Surefire, Failsafe, and compiler test skipping. Always override it:

```bash
# Whole reactor
mvn test -Dmaven.test.skip=false

# Module and upstream dependencies
mvn test -Dmaven.test.skip=false -pl net-server -am

# One class or method
mvn test -Dmaven.test.skip=false -pl net-common -Dtest=PublicationOfferKitTest
mvn test -Dmaven.test.skip=false -pl common-kit -Dtest=StrKitTest#testMethod
```

If Maven says there are no tests after a plain `mvn test`, that is expected configuration, not proof of a passing suite.

## Verification by change area

| Area | Minimum focused checks | Broaden when |
| --- | --- | --- |
| `common-kit` | Owning utility tests | Shared executor, source scanner, or trace behavior changes |
| Action execution | `core-framework` tests and compile `net-logic-server` | Protocol, thread selection, exception, or response behavior changes |
| Publisher/Aeron | `net-common` publisher queue/drain/retry/shutdown tests | SBE fields, buffer sizing, codecs, or fragment handling changes |
| Routing/discovery | `net-server` tests plus `CenterAdapterTest` | Registration metadata, online/offline, or connection lifecycle changes |
| Netty gateway | `external-netty` framing/selector/session tests | Pipeline order, auth, idle, request forwarding, or session identity changes |
| Simulation client | `extension-client` TCP/WebSocket codec/startup tests | Event-loop lifecycle or response correlation changes |
| Proto/codegen | comment tests plus inspect generated files in `target` | Imports, type mapping, templates, or language syntax changes |
| Spring/events/rooms | add focused assertions around changed behavior | Global state, threading, or room-map consistency changes |

No full client-to-action-to-response integration test was found in the initial inventory. Changes spanning gateway, SBE, routing, and action execution need a deliberate local harness or broader manual verification.

## Runtime defaults

Key mutable defaults live in `core-framework/.../CoreGlobalConfig.java`:

| Setting | Default | Operational effect |
| --- | ---: | --- |
| `timeoutMillis` | 3000 ms | Base internal/external call timeout; futures use an added 200 ms buffer |
| `enableFragmentAssembler` | `true` | Reassembles Aeron messages above MTU |
| `fragmentLimit` | 100 | Fragment poll work per operation |
| `publisherBufferSize` | 64 KiB | Whole encoded message buffer, including headers/metadata |
| `publisherQueueCapacity` | 65,536 | Queue per named publication; overflow drops newest |
| `publisherDrainLimit` | 1,024 | Fairness/work bound per publication each publisher loop |
| `publisherOfferRetryLimit` | 2 | Retries backpressure/admin-action failures; negative means unlimited |
| idle backoff | spin/yield/park | Publisher CPU/latency tradeoff |
| `broadcastTrace` | `DefaultBroadcastTrace` | Diagnostic hook for broadcast sends |

Center defaults are local IP, UDP port `30050`, stream ID `1`; peer data publications observed in registration use `aeron:ipc` with `netId` as stream ID.

## Transport capacity and loss

The publisher offers bounded latency and resource use, not durable delivery:

- A full per-publication queue drops the new message and tracks/logs drops.
- Aeron `BACK_PRESSURED` and `ADMIN_ACTION` results retry up to the configured limit; exhaustion drops and logs at sampled counts.
- Closed, disconnected, or maximum-position failures are logged and not retried.
- Encoding failures, unsupported message classes, oversize encoded messages, SBE limit violations, or Aeron maximum-length violations are dropped after logging.

Size `publisherBufferSize` above the largest payload plus headers and variable-length prefixes. The effective cap is the minimum of that buffer, SBE field constraints, and Aeron's publication maximum. Fragment assembly must remain enabled for messages above the Aeron MTU (roughly 1.4 KiB under defaults). Recent history increased SBE payload limits and then enabled fragment assembly by default to make those larger messages usable.

Monitor logs for queue drops, retry-limit warnings, disconnected publications, codec failures, and unknown publication names. Changes to these paths should preserve the tests added for offer retries and publication shutdown.

## Startup and incident checks

### Registration does not complete

1. Confirm the publisher and Aeron Media Driver are running.
2. Confirm an embedded center was enabled or a separate center is reachable at the configured IP/port.
3. Verify `CoreGlobalConfig.netId` and `NetServerBuilder.netId` are consistent.
4. Check the center publication connection and correlated registration future.
5. Confirm the relevant `CoreConfigLoader` service resources are packaged.

### Requests return route errors

1. Confirm the Action controller was explicitly added or package-scanned.
2. Inspect startup route printing in development mode.
3. Confirm the Logic `Server` advertises the expected `cmdMerge`.
4. Check center join responses and `ServerLineKit` registration.
5. Inspect the current owner in `DefaultLogicServerLoadBalanced`; duplicate routes are last-registration-wins.

### Requests disappear or time out

1. Search logs by trace ID; trace now propagates through SBE, action execution, async helpers, and downstream communication.
2. Check publisher queue/offer drops and message sizing.
3. Verify future registration occurred before publication and cleanup occurred on timeout/interruption.
4. Verify response codec and fragment handler registration.
5. Check external server/session IDs for client response write-back.

The complete message paths are in [runtime flows](../workflows/runtime-flows.md), and ownership is in the [source map](../architecture/source-map.md).

## Shutdown and recovery

`NetServerBuilder` installs `ServerOfflineMessageShutdownHook`. Publisher shutdown stops its executor and closes publications once. Recent fixes added explicit publication closure and ownership-aware routing cleanup.

Graceful shutdown is therefore the documented reliable path. The inspected `DefaultUnavailableImageHandler` does not establish crash cleanup, and no center re-registration state machine was identified. Treat abrupt process failure, Media Driver loss, or center restart as deployment-specific recovery scenarios requiring runtime validation.

## Release workflow

A manual-only repository skill lives at `.codex/skills/ionet-release/`. Invoke its helper only for an explicit release request:

```bash
python3 .codex/skills/ionet-release/scripts/ionet_release.py --dry-run
python3 .codex/skills/ionet-release/scripts/ionet_release.py
```

A real release requires `main`, clean status, `HEAD == origin/main`, the `iohao/ionet` remote, fetched tags, authenticated `gh`, and no existing release for the root POM version. It creates/pushes an annotated version tag, generates notes from the previous numeric tag, creates the GitHub release, and verifies it. Do not run it from a dirty documentation worktree.

## Recent evolution

Recent git history explains current operational emphasis:

- Publisher reliability work added retry coverage, bounded configurable offer retries, ownership-aware cleanup, and publication closure.
- Release `25.6` added trace propagation through async and SBE boundaries plus default broadcast tracing.
- SBE payload ceilings were expanded, followed by enabling fragment assembly by default.
- The simulation WebSocket client was rebuilt around Netty with shared event-loop lifecycle and focused handshake/codec tests.
- Proto and SDK generation gained multiline comment preservation and centralized escaping across C#, GDScript, and TypeScript.

When updating this wiki, inspect commits affecting the changed subsystem rather than maintaining commit-hash inventories. The [architecture overview](../architecture/overview.md) remains the canonical description of current runtime behavior.
