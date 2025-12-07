<h2 align="center" style="text-align:center;">
ionet
</h2>
<p align="center">
    <strong>轻量级无锁异步化、事件驱动的架构设计；无需依赖任何第三方中间件就能支持纳秒级的分布式网络通信</strong>
    <br/>
    <strong>通过无锁、基于共享内存的环形缓冲区实现，彻底避免了内核态切换和锁竞争，达到极低延迟的关键</strong>
    <br/>
    <strong>包体小、启动快、内存占用少、更加的节约、无需配置文件、提供了优雅的路由访问权限控制</strong>
    <br/>
    <strong>可同时支持多种连接方式：WS、UDP、TCP...等；框架已支持全链路调用日志跟踪特性</strong>
    <br/>
    <strong>让开发者用一套业务代码，能轻松切换和扩展不同的数据协议：Protobuf、JSON</strong>
    <br/>
    <strong>近原生的性能；业务框架在单线程中平均每秒可以执行 1152 万次业务逻辑</strong>
    <br/>
    <strong>代码即联调文档、JSR380验证、断言 + 异常机制 = 更少的维护成本</strong>
    <br/>
    <strong>框架具备智能的同进程亲和性；开发中，业务代码可定位与跳转</strong>
    <br/>
    <strong>架构部署灵活性与多样性：既可相互独立，又可相互融合</strong>
    <br/>
    <strong>一次编写到处对接，能为客户端生成可交互的代码</strong>
    <br/>
    <strong>逻辑服之间可相互跨进程、跨机器进行通信</strong>
    <br/>
    <strong>支持玩家对业务逻辑服进行动态绑定</strong>
    <br/>
    <strong>能与任何其他框架做融合共存</strong>
    <br/>
    <strong>对 WebMVC 开发者友好</strong>
    <br/>
    <strong>无 Spring 强依赖</strong>
    <br/>
    <strong>零学习成本</strong>
    <br/>
    <strong>JavaSE</strong>
</p>
<p align="center">
	<a href="https://iohao.github.io/ionet">https://iohao.github.io/ionet</a>
</p>
<p align="center">
    <a target="_blank" href="https://www.oracle.com/java/technologies/downloads/#java25">
        <img src="https://img.shields.io/badge/JDK-25-success.svg" alt="JDK 25"/>
    </a>
    <br/>
    <a target="_blank" href="https://www.gnu.org/licenses/agpl-3.0.txt">
        <img src="https://img.shields.io/:license-AGPL3.0-blue.svg" alt="AGPL3.0"/>
    </a>
</p>


## 介绍

**语言**: [English](README.md) | 中文

你是否想要开发一个高性能、稳定、易用、自带负载均衡、避免类爆炸设计、可跨进程跨机器通信、有状态多进程的分布式的网络通信服务器呢？
如果是的话，这里向你推荐一个由 java 语言编写的分布式网络编程框架 ionet。

ionet 是一个开源的轻量级基于 Aeron 的分布式网络编程框架，框架能够做到**纳秒级别的端到端延迟**。

框架内部消息传输层采用了 Aeron + SBE 组合，真.零拷贝、零回环、零反射、零GC、零运行时解析、编解码开销几乎为零。
通过无锁、基于共享内存的环形缓冲区实现，彻底避免了内核态切换和锁竞争，这是达到极低延迟的关键。
极致的 CPU 效率，它让消息处理的速度几乎可以达到底层硬件的极限。

<br/>

**适用场景**
- 网络游戏服务器
- 物联网
- 高频行情推送
- 实时博弈：在线游戏引擎
- 电信：需要高性能数据处理的系统。
- 任何对延迟有极端要求的内部服务通信
- 实时流数据处理（如视频流、传感器数据）
- 高频金融交易、市场数据分发、订单处理。（低延迟要求极高）

## 使用示例
示例中提供了两个方法
- loginVerify: 登录业务方法。
- hello: 业务方法。
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

ionet 是一个开源的轻量级分布式网络编程框架，框架能够做到亚微秒甚至纳秒级别的端到端延迟。
极致的 CPU 效率，它让消息处理的速度达到硬件极限。

框架在打包、内存占用、启动速度等方面也是优秀的。
打 jar 包后大约 **15MB**，应用通常会在 **0.x 秒**内完成启动，内存占用小。

在生态融合方面，框架可以很方便的[与 Spring 集成](https://iohao.github.io/ionet/docs/manual/integration_spring)（4 行代码）。
除了 Spring 外，还能与任何其他的框架做**融合**，如 Vert.x、Quarkus、Solon ...等，从而使用其他框架的相关生态。

在学习成本方面非常低，可以说是**零学习成本**，即使没有网络编程经验也能轻松上手。
开发者只需掌握普通的 java 方法或 webMVC 相关知识，就能使用框架开发业务。

在编码风格上，框架为开发者提供了类 MVC 的编码风格（无入侵的 Java Bean ），这种设计方式很好的**避免了类爆炸**。
同时，框架为开发者提供了同步、异步、异步回调的方法，用于逻辑服之间的相互访问。
这使得开发者所编写的代码会非常的优雅，并且具备全链路调用日志跟踪。

与客户端对接方面，框架具备**一次编写到处对接**的能力，为客户端提供了代码生成的辅助功能，能够帮助客户端开发者减少巨大的工作量。
这将意味着，你只需要编写一次 java 代码，就能为 Godot、UE、Unity、CocosCreator、Laya、React、Vue、Angular ...等项目生成统一的交互接口。
框架提供了多种语言的 SDK 支持及相关语言的[代码生成](https://iohao.github.io/ionet/docs/examples/code_generate)，分别是 C#、TypeScript、GDScript、C++、Lua，并支持扩展。

框架在架构上解决了传统框架所产生的 **N\*N 问题**（[与传统架构对比](https://iohao.github.io/ionet/docs/manual_high/legacy_system)）。
传统架构在扩展机器时，需要借助很多第三方中间件，如：Redis、MQ、ZooKeeper ...等，才能满足整体架构的运作。
通常，只要引入了需要安装的中间件才能做到扩展的，那么你的架构或者说框架，基本上与轻量级无缘了。

在轻量级方面，框架**不依赖任何第三方中间件**就能支持分布式网络通信，只需要 java 环境就可以运行。
这意味着在使用上简单了，在部署上也为企业减少了部署成本、维护难度。
使用 ionet 时，只需一个依赖即可获得整个框架，而无需安装其他服务，如 Nginx、Redis、MQ、Mysql、ZooKeeper、Protobuf 协议编译工具 ...等。

在[架构灵活性](https://iohao.github.io/ionet/docs/manual_high/architecture_intro)方面，架构由对外服和逻辑服组成，两者既可相互独立，又可相互融合。
这意味着框架可以**适应任何类型的游戏**，因为只需通过调整部署方式，就可以满足不同类型的游戏需求。
在框架中进行这些调整工作非常简单，而且不会对现有代码产生不良影响。

架构是可以动态扩缩的，对外服和逻辑服**支持动态增加和减少**。
无论未来用户数量增加或减少，我们都能够轻松应对。
同时，架构是**支持用户无感知更新**的，这得益于分布式设计。
举例来说，如果 A 类型的逻辑服需要增加一些新功能，我们可以启动 A-3、A-4 等已经支持了新功能的服务器，然后逐步将之前的 A-1 和 A-2 下线，从而实现了无感知的更新。

在分布式方面，框架的逻辑服使用了分布式设计思想，将服务器分为[对外服](https://iohao.github.io/ionet/docs/manual/external_intro)、[逻辑服](https://iohao.github.io/ionet/docs/manual/logic_intro)等不同层次，并且每一层都有明确的职责和接口。
这样可以提高代码可读性和可维护性，并且方便进行**水平扩展**。

在分布式开发体验方面，通常在开发分布式应用时是需要启动多个进程的。
这会让调试与排查问题变得非常困难，从而降低开发者的效率、增加工作量等，这也是很多框架都**解决不了的问题**，但 ionet 做到了！
ionet 支持多服单进程的启动方式，这使得开发者在开发和调试分布式系统时更加简单。

在[生态规划](https://iohao.github.io/ionet/docs/manual_high/your_ecology)方面，我们的逻辑服是支持独立运行的，启动后就可以为用户和其他逻辑服提供功能上的扩展与增强。
我们可以将一些**逻辑服组件化**，并制作成相对通用的组件，**从而实现功能模块化的可能性**。
这么做有几个优点

1. 避免一些重复开发的工作量。
2. 减少各功能模块的耦合。
3. 更符合单一职责的设计，将相对通用的功能扩展成一个个的**功能逻辑服**。如，公会逻辑服、好友逻辑服、登录逻辑服、抽奖逻辑服、公告逻辑服、排行榜逻辑服...等。
4. 由于模块功能是独立，那么将来可以对任意的功能逻辑服进行扩容，且不需要改动任何代码。
5. 这些组件化后的功能逻辑服就好比一件件武器，积累得足够多时就形成了自己的生态武器库，可以更好的帮助公司与同行竞争。
6. **代码泄漏机率更小**。传统的项目通常采用单机结构，把所有的代码放在一个目录中。这样做有很大的风险，因为如果代码泄漏了，就会泄漏整个项目的内容。当功能模块化后，可以让不同的开发人员只负责自己的逻辑服模块，从而避免代码泄漏的风险和影响。
7. 团队管理员只需要在内网服务器上部署一个网关和对外服，而开发人员就可以在本机上编码和测试自己的逻辑服模块。这样还有以下好处
    - 客户端不会因为逻辑服的变更或重启而断开连接。
    - 开发人员不需要启动其他人的逻辑服模块。
    - 开发人员可以通过框架自动生成的文档来进行模块间的对接。

框架具备[全链路调用日志跟踪](https://iohao.github.io/ionet/docs/manual_high/trace)特性，这在分布式下非常的实用。
该特性为每个请求分配一个唯一标识，并记录在日志中，通过唯一标识可以快速的在日志中过滤出指定请求的信息。
框架提供的全链路调用日志跟踪特性更是强大，**支持跨机器、跨进程**。
简单的说，从用户的请求进来到结束，无论该请求经过了多少个逻辑服，都能精准记录。

在通信方式方面，大部分框架只能支持推送（广播）这一类型的通信方式。
框架则提供了多种[通信模型](https://iohao.github.io/ionet/docs/manual/communication_model)，通过对各种通信方式的组合使用，可以简单完成以往难以完成的工作，并且这些通信方式都支持跨进程、跨机器通信，且具备全链路调用日志跟踪。

- **在客户端的角度，提供了如下的通信模型**
    - [request/response](https://iohao.github.io/ionet/docs/communication/request_response)，请求/响应
    - request/void，请求/无响应
    - request/broadcast，请求/广播响应
    - [broadcast](https://iohao.github.io/ionet/docs/communication/broadcast)，广播
- **内部通信主要用于服务器内部之间的通信，跨服、跨进程通信。提供了如下的通信模型**
    - [request/response](https://iohao.github.io/ionet/docs/communication/request_response)，请求/响应
    - request/void，请求/无响应
    - [request/multiple_response](https://iohao.github.io/ionet/docs/communication/request_multiple_response)，同时请求同类型多个逻辑服
    - [OnExternal](https://iohao.github.io/ionet/docs/communication/on_external)，访问对外服
    - [EventBus](https://iohao.github.io/ionet/docs/communication/event_bus)，分布式事件总线

在线程安全方面，框架为开发者解决了单个用户的**并发问题**。
即使用户重新登录后，也会使用相同的线程来消费业务，并推荐使用[领域事件](https://iohao.github.io/ionet/docs/extension_module/domain_event)来解决同一房间或业务内多个用户的并发问题。
[框架在线程的扩展性](https://iohao.github.io/ionet/docs/manual_high/thread_executor)上提供了友好的支持，开发者可以很容易的编写出无锁并发代码，这得益于框架独有的线程执行器设计与扩展。
换句话说，你不会因为并发问题烦恼。

在无锁并发方面，框架提供了优雅、独特的线程执行器设计。通过该特性，开发者能轻易的编写出无锁高并发的代码。

在连接方式方面，框架允许开发者**使用一套业务代码**，**同时支持**多种连接方式，无需进行任何修改。
框架已经支持了 TCP、WebSocket 和 UDP 连接方式，并且也支持在这几种连接方式之间进行灵活切换。
连接方式是可扩展的，并且扩展操作也很简单，这意味着之后如果支持了 KCP、QUIC，无论你当前项目使用的是 TCP、WebSocket、UDP，都可以切换成 KCP、QUIC。
即使切换到 KCP、QUIC 的连接方式，现有的业务代码也无需改变。

在通信协议方面，框架让开发者**用一套业务代码**，就能轻松[切换和扩展不同的数据协议](https://iohao.github.io/ionet/docs/manual_high/data_protocol)，如 Protobuf、JSON 等。
只需一行代码，就可以从 Protobuf 切换到 JSON，无需改变业务方法。

在增减协议方面，框架可以让你在**新增或减少协议**时，**无需重启**对外服。这样既能避免用户断线，又能避免因新增、减少协议而重启所有机器的痛点。

在协议碎片方面，action 支持自动装箱、拆箱基础类型特性，用于解决[协议碎片](https://iohao.github.io/ionet/docs/manual_high/protocol_fragment)的问题。
同时该特性除了能使你的业务代码更加清晰以外，还能大幅提高开发者在该环节的生产力。

在开发体验方面，框架非常注重开发者的开发体验。
框架提供了 [JSR380 验证](https://iohao.github.io/ionet/docs/manual/jsr380)、[断言 + 异常机制](https://iohao.github.io/ionet/docs/manual/assert_error_code)、[业务代码定位](https://iohao.github.io/ionet/docs/core_plugin/action_debug)，action 支持自动装箱、拆箱基础类型，用于解决协议碎片的问题 ...等。
诸多丰富的功能，使得开发者的业务代码更加的清晰、简洁。

业务框架提供了[插件](https://iohao.github.io/ionet/docs/manual/plugin_intro)机制，插件是可插拨、可扩展的。
框架内置提供了 DebugInOut、action 调用统计、业务线程监控插件、各时间段调用统计插件...等插件。
不同的插件提供了不同的关注点，比如我们可以使用调用、监控等插件相互配合，可以让我们在开发阶段就知道**是否存在性能问题**。
合理利用好各个插件，可以让我们在开发阶段就能知道问题所在，提前发现问题，提前预防问题。

在部署方面，框架支持**多服单进程**的方式部署，也支持**多服多进程**多机器的方式部署，在部署方式上可以随意的切换而不需要更改代码。
日常中我们可以按照单体思维开发，到了生产可以选择性的使用多进程的方式部署。

在安全方面，所有的逻辑服不需要开放端口，天然地避免了扫描攻击。由于不需要为每个逻辑服分配独立的端口，那么我们在使用诸如云服务器之类的服务时，就不需要担心端口开放权限的问题了。
别小看这一个环节，通常这些小细节最浪费开发者的时间。
由于我们不需要管理这些 IP:Port，**这部分的工作量就自然地消失了**。

在模拟客户端测试方面，框架提供了[压测&模拟客户端请求](https://iohao.github.io/ionet/docs/extension_module/simulation_client)模块。
此模块是用于模拟客户端，简化模拟工作量，只需要编写对应请求与回调。
除了可以模拟简单的请求外，通常还可以做一些复杂的请求编排，并支持复杂业务的压测。
**与单元测试不同的是，该模块可以模拟真实的网络环境，并且在模拟测试的过程中与服务器的交互是可持续的、可互动的，同时也是支持自动化的**。

使用 ionet，可以显著的帮助企业减少巨额成本。
文档中，**成本**关键字提到了很多次，各个阶段均有关联，包括了学习、研发、测试、部署、扩展、投入 ...等各阶段。
在同等资源的竞争下，使用 框架能为公司节省更多的资源，从而提高了自身的生存率。
更重要的是避免了为其他公司做嫁衣的可能性，具体可阅读[成本分析案例](https://iohao.github.io/ionet/services/cost_analysis)。

开发者基于框架编写的项目模块，通常是条理清晰的，得益于框架对**路由的合理设计**，同时也为路由提供了优雅的[访问权限控制](https://iohao.github.io/ionet/docs/manual/access_authentication)。
当我们整理好这些模块后，对于其他开发者接管项目或后续的维护中，会是一个不错的帮助（[代码组织与约定](https://iohao.github.io/ionet/docs/manual_high/code_organization)）。
或许现阶段你感受不到这块的威力，随着你深入地使用实践就能体会到这么设计的诸多好处与优势。

开发者基于框架编写的项目，通常是语法简洁的、高性能的、低延迟的。
框架最低要求使用 **JDK25**，这样即可以让项目享受到**分代 ZGC** 带来的改进，还能享受语法上的简洁。
分代 ZGC 远低于其**亚毫秒级**暂停时间的目标，可以在不影响速度的情况下，清理掉多余的内存。
这样就不会出现卡顿或者崩溃的问题了，相当于在项目中变相的引入了一位 JVM 调优大师。

综上所述，框架是一个非常适合网络游戏开发的框架。
可以让你轻松地创建高性能、低延迟、易扩展的网络服务器，并且节省时间和资源。
如果你想要快速地开发出令人惊艳的网络游戏，请不要犹豫，立即选择框架吧！
框架屏蔽了很多复杂且重复性的工作，并可为项目中的功能模块结构、开发流程等进行**清晰的组织定义**，减少了后续的项目维护成本。

框架在开发、部署、与客户端对接、压测&模拟测试 ...等各个阶段都提供了很好的支持。
相信你已经对框架有了一个初步的了解，虽然还有很多丰富的功能与特性没有介绍到，但你可以通过后续的实践过程中来深入了解。
感谢你的阅读，并期待你使用 框架来打造自己的游戏服务器。