## ionet
**Language**: English | [中文](README_CN.md)

Document: https://iohao.github.io/ionet

<p>
<a target="_blank" href="https://www.oracle.com/java/technologies/downloads/#java25"><img src="https://img.shields.io/badge/JDK-25-success.svg" alt="JDK 25"/></a>
<a target="_blank" href="https://www.gnu.org/licenses/agpl-3.0.txt"><img src="https://img.shields.io/:license-AGPL3.0-blue.svg" alt="AGPL3.0"/></a>
</p>


- Lightweight, Lock-free, Asynchronous, and Event-Driven Architecture: Supports nanosecond-level distributed network communication without relying on any third-party middleware.
- Ultra-Low Latency: Achieved through lock-free, shared-memory-based ring buffers, completely eliminating kernel context switching and lock contention.
- Resource Efficient: Small package size, fast startup, low memory footprint, highly economical, no configuration files needed, and provides elegant routing access permission control.
- Multi-Connection Support: Can simultaneously support various connection methods: WS (WebSocket), UDP, TCP, etc. The framework includes full-link call logging and tracing features.
- Flexible Data Protocol: Allows developers to easily switch and extend different data protocols (Protobuf, JSON) using a single set of business code.
- Near-Native Performance: The business framework can execute an average of 11.52 million business logic operations per second in a single thread.
- Reduced Maintenance Costs: Code-as-live-debugging-documentation, JSR380 validation, Assertion + Exception mechanism.
- Developer Friendly: The framework features intelligent same-process affinity; business code can be located and jumped to during development.
- Deployment Flexibility and Diversity: The architecture can be both independent and integrated.
- Write Once, Connect Everywhere: Can generate interactive code for clients.
- Cross-Process/Cross-Machine Communication: Logic services can communicate with each other across processes and machines.
- Dynamic Binding: Supports dynamic binding of players to business logic services.
- Framework Compatibility: Can be integrated and coexist with any other framework.
- WebMVC Developer Friendly
- No Strong Spring Dependency
- Zero Learning Cost
- JavaSE



## Introduction


Are you looking to develop a distributed network communication server that is **high-performance, stable, easy-to-use, built-in load balanced, avoids class explosion design, supports cross-process and cross-machine communication, and features stateful multi-processing**?

If so, we recommend **ionet**, a distributed network programming framework written in Java.

ionet is an open-source, lightweight distributed network programming framework based on Aeron, capable of achieving **nanosecond-level end-to-end latency**.

The framework's internal message transport layer utilizes the **Aeron + SBE** combination, achieving true **zero-copy, zero-ringback, zero-reflection, zero-GC, zero runtime parsing, and virtually zero encoding/decoding overhead**.
It is implemented via lock-free, shared-memory-based ring buffers, completely eliminating kernel context switching and lock contention, which is key to achieving extremely low latency.
With extreme CPU efficiency, it allows message processing speed to nearly reach the limits of the underlying hardware.

<br/>

**Applicable Scenarios**

* Online Game Servers
* Internet of Things (IoT)
* High-Frequency Market Data Push
* Real-Time Gaming/Betting: Online Game Engines
* Telecommunications: Systems requiring high-performance data processing.
* Any internal service communication with extreme latency requirements
* Real-Time Stream Data Processing (e.g., video streams, sensor data)
* High-Frequency Financial Trading, Market Data Distribution, Order Processing (extremely high low-latency requirement)

## Usage Example
The example provides two methods:
- loginVerify: Login business method.
- hello: Business method.

```java
// Action
@ActionController(HallCmd.cmd)
public class HallAction {
    @ActionMethod(HallCmd.loginVerify)
    UserMessage loginVerify(String jwt, FlowContext flowContext) {
        long userId = Math.abs(jwt.hashCode());

        flowContext.bindingUserId(userId);

        UserMessage userMessage = new UserMessage();
        userMessage.id = userId;
        userMessage.nickname = jwt;
        return userMessage;
    }
    
    @ActionMethod(HallCmd.hello)
    String hello(FlowContext flowContext) {
        return "hello " + flowContext.getUserId();
    }       
}

// Data Message
@ProtobufClass
public class UserMessage {
    public long id;
    public String nickname;
}

// Routing
public interface HallCmd {
    int cmd = 1;
    
    int loginVerify = 1;
    int hello = 2;
}
```



ionet is an open-source, lightweight distributed network programming framework that can achieve sub-microsecond or even nanosecond-level end-to-end latency.
With extreme CPU efficiency, it pushes message processing speed to the hardware limit.

The framework also excels in packaging, memory footprint, and startup speed.
The packaged JAR file is approximately **15MB**, applications typically complete startup within **0.x seconds**, and memory consumption is minimal.

In terms of ecosystem integration, the framework can be easily [integrated with Spring](https://iohao.github.io/ionet/docs/manual/integration_spring) (in just 4 lines of code).
Beyond Spring, it can also be **integrated** with any other framework, such as Vert.x, Quarkus, Solon, etc., to leverage their respective ecosystems.

The learning curve is very low—it can be described as **zero learning cost**. Even developers without prior network programming experience can easily get started.
Developers only need to master standard Java methods or WebMVC-related knowledge to develop business logic using the framework.

Regarding coding style, the framework offers a **MVC-like coding style** (non-intrusive Java Beans), a design that effectively **avoids class explosion**.
Furthermore, the framework provides developers with synchronous, asynchronous, and asynchronous callback methods for mutual access between logic services.
This results in very elegant code and supports full-link call logging and tracing.

For client integration, the framework offers a **"write once, connect everywhere"** capability and provides auxiliary code generation features for clients, significantly reducing the client developer's workload.
This means you only need to write Java code once to generate unified interaction interfaces for projects like Godot, UE, Unity, CocosCreator, Laya, React, Vue, Angular, and more.
The framework provides SDK support for multiple languages and corresponding [code generation](https://iohao.github.io/ionet/docs/examples/code_generate) for C#, TypeScript, GDScript, C++, and Lua, with support for extensions.

Architecturally, the framework solves the **N*N problem** created by traditional frameworks ([Comparison with Traditional Architectures](https://iohao.github.io/ionet/docs/manual_high/legacy_system)).
Traditional architectures require numerous third-party middleware components (like Redis, MQ, ZooKeeper, etc.) to expand machines and maintain overall architectural operation.
Generally, if scaling requires introducing installable middleware, your architecture or framework is likely not "lightweight."

For its lightweight nature, the framework **supports distributed network communication without relying on any third-party middleware**; it only requires a Java environment to run.
This simplifies usage and reduces deployment costs and maintenance difficulty for enterprises.
When using ionet, you only need one dependency to get the entire framework, without needing to install other services like Nginx, Redis, MQ, MySQL, ZooKeeper, Protobuf protocol compilation tools, etc.

In terms of [Architecture Flexibility](https://iohao.github.io/ionet/docs/manual_high/architecture_intro), the architecture consists of External Services and Logic Services, which can be independent or integrated.
This means the framework can **adapt to any type of game**, as simply adjusting the deployment method can meet the needs of different game types.
Making these adjustments within the framework is very simple and will not have adverse effects on existing code.

The architecture supports dynamic scaling; both External Services and Logic Services **can be dynamically added or removed**.
This allows us to easily cope with future increases or decreases in user volume.
Additionally, the architecture **supports non-disruptive user updates** thanks to its distributed design.
For instance, if Logic Service Type A needs new features, we can launch A-3, A-4, etc., which already support the new features, and then gradually take the old A-1 and A-2 offline, achieving a non-disruptive update.

In the distributed aspect, the framework's Logic Services use distributed design principles, dividing the servers into different layers such as [External Services](https://iohao.github.io/ionet/docs/manual/external_intro) and [Logic Services](https://iohao.github.io/ionet/docs/manual/logic_intro), with clear responsibilities and interfaces for each layer.
This improves code readability and maintainability and facilitates **horizontal scaling**.

Regarding the distributed development experience, developing distributed applications typically requires launching multiple processes.
This can make debugging and troubleshooting very difficult, reducing developer efficiency and increasing workload—a problem many frameworks **cannot solve**. But ionet does!
ionet supports a multi-service, single-process startup mode, which simplifies development and debugging of distributed systems for developers.

For [Ecosystem Planning](https://iohao.github.io/ionet/docs/manual_high/your_ecology), our Logic Services support running independently. Once started, they can provide functional extension and enhancement for users and other Logic Services.
We can **componentize some Logic Services** and make them into relatively generic components, **thereby enabling the possibility of functional modularization**.
This approach has several advantages:

1.  Avoids some repetitive development workload.
2.  Reduces coupling between functional modules.
3.  Better aligns with the Single Responsibility Principle, extending relatively generic functions into individual **Functional Logic Services** (e.g., Guild Logic Service, Friend Logic Service, Login Logic Service, Draw Logic Service, Announcement Logic Service, Leaderboard Logic Service, etc.).
4.  Since the module functions are independent, any functional logic service can be scaled up in the future without changing any code.
5.  These componentized Functional Logic Services are like individual weapons; accumulating enough of them forms your own ecosystem weapon library, which better helps the company compete with peers.
6.  **Lower risk of code leakage**. Traditional projects often use a monolithic structure, placing all code in one directory. This poses a significant risk, as a code leak would compromise the entire project. By modularizing functions, different developers can be responsible only for their own logic service module, thereby preventing the risk and impact of code leakage.
7.  Team administrators only need to deploy a gateway and an External Service on the internal network server, while developers can code and test their logic service modules on their local machines. This offers additional benefits:
    * Clients will not disconnect due to changes or restarts of the Logic Service.
    * Developers do not need to start other people's Logic Service modules.
    * Developers can interface between modules using the automatically generated documentation provided by the framework.

The framework features **Full-Link Call Logging and Tracing** ([Trace](https://iohao.github.io/ionet/docs/manual_high/trace)), which is highly practical in a distributed environment.
This feature assigns a unique identifier to each request and logs it, allowing for quick filtering of specific request information in the logs using that identifier.
The full-link call tracing provided by the framework is even more powerful, **supporting cross-machine and cross-process tracing**.
Simply put, from the user's request entry until its completion, no matter how many logic services the request passes through, it is accurately recorded.

Regarding communication methods, most frameworks only support push (broadcast) type communication.
The framework, however, provides a variety of [Communication Models](https://iohao.github.io/ionet/docs/manual/communication_model). By combining these communication methods, tasks that were previously difficult can be easily accomplished. All these communication methods support cross-process and cross-machine communication and feature full-link call logging and tracing.

* **From the Client's Perspective, the following communication models are provided:**
    * [request/response](https://iohao.github.io/ionet/docs/communication/request_response), Request/Response
    * request/void, Request/No Response
    * request/broadcast, Request/Broadcast Response
    * [broadcast](https://iohao.github.io/ionet/docs/communication/broadcast), Broadcast
* **Internal Communication, mainly used for inter-server, cross-server, and cross-process communication, provides the following models:**
    * [request/response](https://iohao.github.io/ionet/docs/communication/request_response), Request/Response
    * request/void, Request/No Response
    * [request/multiple_response](https://iohao.github.io/ionet/docs/communication/request_multiple_response), Simultaneous Request to Multiple Logic Services of the Same Type
    * [OnExternal](https://iohao.github.io/ionet/docs/communication/on_external), Access External Service
    * [EventBus](https://iohao.github.io/ionet/docs/communication/event_bus), Distributed Event Bus

In terms of thread safety, the framework solves the **concurrency issue** for individual users.
Even if a user logs in again, the same thread will be used to consume the business logic. It is recommended to use [Domain Events](https://iohao.github.io/ionet/docs/extension_module/domain_event) to address concurrency issues involving multiple users within the same room or business context.
[The framework provides friendly support for thread extensibility](https://iohao.github.io/ionet/docs/manual_high/thread_executor), allowing developers to easily write lock-free concurrent code, thanks to the framework's unique thread executor design and extension.
In other words, you won't be troubled by concurrency issues.

For lock-free concurrency, the framework offers an elegant and unique thread executor design. This feature allows developers to easily write lock-free, highly concurrent code.

Regarding connection methods, the framework allows developers to **use a single set of business code** to **simultaneously support** multiple connection methods without any modification.
The framework already supports TCP, WebSocket, and UDP connection methods and allows flexible switching between them.
Connection methods are extensible, and the extension operation is simple. This means that if KCP or QUIC support is added later, you can switch to KCP or QUIC regardless of whether your current project uses TCP, WebSocket, or UDP.
Even when switching to KCP or QUIC, the existing business code will not need to change.

In terms of communication protocols, the framework allows developers to easily [switch and extend different data protocols](https://iohao.github.io/ionet/docs/manual_high/data_protocol), such as Protobuf or JSON, **using a single set of business code**.
With just one line of code, you can switch from Protobuf to JSON without changing your business methods.

Regarding protocol changes, the framework allows you to **add or remove protocols** **without restarting** the External Service. This avoids both user disconnections and the pain point of having to restart all machines due to adding or removing protocols.

For protocol fragmentation, action supports automatic boxing/unboxing of primitive types, which addresses the issue of [Protocol Fragmentation](https://iohao.github.io/ionet/docs/manual_high/protocol_fragment).
Besides making your business code clearer, this feature also significantly boosts developer productivity in this area.

In terms of development experience, the framework highly prioritizes the developer's experience.
The framework provides [JSR380 Validation](https://iohao.github.io/ionet/docs/manual/jsr380), [Assertion + Exception Mechanism](https://iohao.github.io/ionet/docs/manual/assert_error_code), [Business Code Location](https://iohao.github.io/ionet/docs/core_plugin/action_debug), and action support for automatic boxing/unboxing of primitive types to solve the protocol fragmentation problem, among other rich features.
These functionalities make developers' business code clearer and more concise.

The business framework provides a [Plugin](https://iohao.github.io/ionet/docs/manual/plugin_intro) mechanism, where plugins are pluggable and extensible.
The framework comes with built-in plugins such as DebugInOut, action call statistics, business thread monitoring, call statistics for various time periods, and more.
Different plugins offer different areas of focus. For example, by using call and monitoring plugins together, we can determine **whether performance issues exist** during the development phase.
Proper utilization of each plugin allows us to identify and prevent problems early in the development phase.

For deployment, the framework supports both **multi-service, single-process** deployment and **multi-service, multi-process, multi-machine** deployment. You can switch deployment methods freely without changing the code.
In daily development, we can follow a monolithic mindset, and then selectively use a multi-process approach for production deployment.

In terms of security, all Logic Services do not need to expose ports, naturally preventing scanning attacks. Since we don't need to assign independent ports for each Logic Service, we don't have to worry about port opening permissions when using services like cloud servers.
Don't underestimate this step; these small details often waste the most developer time.
Since we don't need to manage these IP:Port combinations, **this part of the workload naturally disappears**.

For simulated client testing, the framework provides a [Stress Testing & Simulated Client Request](https://iohao.github.io/ionet/docs/extension_module/simulation_client) module.
This module is used to simulate clients, simplifying the simulation workload by only requiring the corresponding request and callback logic.
Besides simple requests, it can also be used for complex request orchestration and supports stress testing of complex business logic.
**Unlike unit testing, this module can simulate a real network environment, and the interaction with the server during the simulation test is sustainable, interactive, and supports automation.**

Using ionet can significantly help enterprises reduce huge costs.
The keyword **cost** is mentioned many times throughout the documentation, related to various stages including learning, R&D, testing, deployment, scaling, investment, and more.
In a competitive environment with equivalent resources, using the framework can save the company more resources, thus improving its survival rate.
More importantly, it avoids the possibility of "making wedding dresses for others." For details, please read the [Cost Analysis Case Study](https://iohao.github.io/ionet/services/cost_analysis).

Projects modules written by developers based on the framework are typically well-organized, thanks to the framework's **reasonable design of routing** and its provision of elegant [Access Permission Control](https://iohao.github.io/ionet/docs/manual/access_authentication) for routes.
When these modules are well-organized, it is a great help for other developers taking over the project or for subsequent maintenance ([Code Organization and Conventions](https://iohao.github.io/ionet/docs/manual_high/code_organization)).
You might not feel the power of this at the current stage, but as you delve into practical use, you will appreciate the many benefits and advantages of this design.

Projects written by developers based on the framework are typically syntactically concise, high-performance, and low-latency.
The framework requires a minimum of **JDK25**, which allows the project to benefit from the improvements brought by **Generational ZGC** as well as syntactic conciseness.
Generational ZGC comfortably meets its target of **sub-millisecond** pause times, cleaning up excess memory without impacting speed.
This prevents stuttering or crashing issues, which is equivalent to indirectly introducing a JVM tuning master into the project.

In summary, the framework is an excellent fit for online game development.
It allows you to easily create high-performance, low-latency, and easily scalable network servers, saving time and resources.
If you want to rapidly develop stunning online games, don't hesitate—choose the framework now!
The framework abstracts away much of the complex and repetitive work and provides **clear organizational definitions** for the functional module structure and development process, reducing subsequent project maintenance costs.

The framework provides excellent support across various stages, including development, deployment, client integration, stress testing & simulation testing, and more.
I trust you now have a preliminary understanding of the framework. Although many rich features and functionalities have not yet been introduced, you can gain a deeper understanding through subsequent practical use.
Thank you for reading, and we look forward to you using the framework to build your own game server.