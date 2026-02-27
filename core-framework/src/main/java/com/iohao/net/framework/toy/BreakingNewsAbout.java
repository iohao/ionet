/*
 * ionet
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.net.framework.toy;

import com.iohao.net.common.kit.*;
import java.util.*;
import lombok.experimental.*;
import org.jspecify.annotations.*;

/**
 * Startup news and tips manager that randomly selects documentation links
 * to display in the banner. Supports Chinese and English locales.
 * <p>
 * Name inspired by Michael Jackson's song "Breaking News".
 *
 * @author 渔民小镇
 * @date 2023-06-10
 */
@UtilityClass
class BreakingNewsKit {

    private BreakingNews breakingNews;

    private BreakingNews getBreakingNews() {
        if (breakingNews == null) {
            if (Locale.getDefault().getLanguage().equals("zh")) {
                breakingNews = new DefaultBreakingNews();
            } else {
                breakingNews = new EnglishBreakingNews();
            }
        }

        return breakingNews;
    }

    List<News> randomNewsList() {
        List<News> news = getBreakingNews().listNews();
        Collections.shuffle(news);

        return news.stream().limit(2).toList();
    }

    News randomAdv() {
        List<News> list = getBreakingNews().listAdv();
        return RandomKit.randomEle(list);
    }

    News randomMainNews() {
        var list = getBreakingNews().listMainNews();
        return RandomKit.randomEle(list);
    }
}

/** Immutable news item with a title and URL. */
record News(String title, String url) {
    @NonNull
    @Override
    public String toString() {
        return String.format("%s - %s", title, url);
    }
}

/** Contract for providing categorized news items displayed in the startup banner. */
interface BreakingNews {
    List<News> listMainNews();

    List<News> listAdv();

    List<News> listNews();
}

/** Chinese-locale breaking news implementation. */
final class DefaultBreakingNews implements BreakingNews {

    @Override
    public List<News> listMainNews() {
        return List.of(
                new News("ionet javadoc", "https://iohao.github.io/javadoc"),
                new News("ionet issues", "https://github.com/iohao/ionet/issues"),
                new News("ionet changelog for each version", "https://iohao.github.io/ionet/docs/version_log"),
                new News("ionet release frequency: 1-2 releases per month, upgrades within a major version are always compatible", "")
        );
    }

    @Override
    public List<News> listAdv() {
        return List.of(
                new News("Online demo", "https://a.iohao.com"),
                new News("MMO", "https://iohao.github.io/ionet/docs/practices/mmo"),
                new News("Board game / room-based practice", "https://iohao.github.io/ionet/docs/practices/room_in_action")
        );
    }

    @Override
    public List<News> listNews() {
        List<News> list = new ArrayList<>();

        list.add(new News("Supporters list", "https://iohao.github.io/ionet/docs/contribute/backers"));

        // common development tips
        list.add(new News("Full-link call log tracing", "https://iohao.github.io/ionet/docs/manual/trace"));

        // licensing and practical products
        list.add(new News("Project cost analysis", "https://iohao.github.io/ionet/services/cost_analysis"));

        // architecture overview
        list.add(new News("Architecture introduction", "https://iohao.github.io/ionet/docs/overall/architecture_intro"));
        list.add(new News("ionet flexible architecture and diverse deployment", "https://iohao.github.io/ionet/docs/overall/deploy_flexible"));
        list.add(new News("Comparison with traditional architecture", "https://iohao.github.io/ionet/docs/overall/legacy_system"));

        list.add(new News("ionet request processing flow", "https://iohao.github.io/ionet/docs/overall/request_processing_procedure"));
        list.add(new News("ionet threading", "https://iohao.github.io/ionet/docs/overall/thread_executor"));
        list.add(new News("Single-server, multi-server single-process, multi-server multi-process startup", "https://iohao.github.io/ionet/docs/manual/netty_run_one"));
        list.add(new News("Code organization and conventions", "https://iohao.github.io/ionet/docs/manual_high/code_organization"));
        list.add(new News("Same-process affinity", "https://iohao.github.io/ionet/docs/manual_high/same_process"));

        // logic server
        list.add(new News("LogicServer - Dynamic binding", "https://iohao.github.io/ionet/docs/manual/binding_logic_server"));
        list.add(new News("LogicServer - Metadata and attachments", "https://iohao.github.io/ionet/docs/manual/flowcontext_attachment"));

        // external server
        list.add(new News("ExternalServer - Unified protocol description", "https://iohao.github.io/ionet/docs/manual_high/external_message"));
        list.add(new News("ExternalServer", "https://iohao.github.io/ionet/docs/overall/external_intro"));
        list.add(new News("ExternalServer - Heartbeat settings and hooks", "https://iohao.github.io/ionet/docs/external/idle"));
        list.add(new News("ExternalServer - User online/offline hooks", "https://iohao.github.io/ionet/docs/external/user_hook"));
        list.add(new News("ExternalServer - Route access permission control", "https://iohao.github.io/ionet/docs/external/access_authentication"));
        list.add(new News("ExternalServer - Response caching", "https://iohao.github.io/ionet/docs/external/cache"));
        list.add(new News("ExternalServer - WebSocket token authentication", "https://iohao.github.io/ionet/docs/external/ws_verify"));
        list.add(new News("ExternalServer - Built-in and optional Handlers", "https://iohao.github.io/ionet/docs/external/netty_handler"));

        // communication
        list.add(new News("Communication - Request multiple logic servers of the same type", "https://iohao.github.io/ionet/docs/communication/request_multiple_response"));
        list.add(new News("Communication - Access external server data and extensions", "https://iohao.github.io/ionet/docs/communication/external_biz_region"));
        list.add(new News("Communication - Distributed Event Bus", "https://iohao.github.io/ionet/docs/communication/event_bus"));

        // built-in tools
        list.add(new News("Built-in Kit - TaskKit for tasks, timing, delay and timeout monitoring", "https://iohao.github.io/ionet/docs/kit/task_kit"));
        list.add(new News("Built-in Kit - Property monitoring", "https://iohao.github.io/ionet/docs/kit/property_change_listener"));
        list.add(new News("Built-in Kit - Lightweight and controllable delayed tasks", "https://iohao.github.io/ionet/docs/kit/delay_task"));

        // extension modules
        list.add(new News("ExtendedModule - Domain Events for multi-user concurrency", "https://iohao.github.io/ionet/docs/extension_module/domain_event"));
        list.add(new News("ExtendedModule - Stress testing and simulating client requests", "https://iohao.github.io/ionet/docs/extension_module/simulation_client"));
        list.add(new News("ExtendedModule - Room for board games and room-based scenarios", "https://iohao.github.io/ionet/docs/extension_module/room"));
        list.add(new News("ExtendedModule - sdk-generate-code", "https://iohao.github.io/ionet/docs/extension_module/generate_code"));

        // plugins
        list.add(new News("Business Framework - Plugin Introduction", "https://iohao.github.io/ionet/docs/manual/plugin_intro"));
        list.add(new News("Plugin - DebugInOut", "https://iohao.github.io/ionet/docs/core_plugin/action_debug"));
        list.add(new News("Plugin - Action call statistics", "https://iohao.github.io/ionet/docs/core_plugin/action_stat"));
        list.add(new News("Plugin - Business thread monitoring", "https://iohao.github.io/ionet/docs/core_plugin/action_thread_monitor"));
        list.add(new News("Plugin - Call statistics for each time period", "https://iohao.github.io/ionet/docs/core_plugin/action_time_range"));
        list.add(new News("Plugin - Full-link call log tracing", "https://iohao.github.io/ionet/docs/core_plugin/action_trace"));

        // business framework
        list.add(new News("Business Framework - Introduction", "https://iohao.github.io/ionet/docs/core/framework"));
        list.add(new News("Business Framework - FlowContext", "https://iohao.github.io/ionet/docs/manual/flowcontext"));
        list.add(new News("Business Framework - Assertions + exceptions = clear and concise code", "https://iohao.github.io/ionet/docs/manual/assert_game_code"));
        list.add(new News("Business Framework - Enable JSR380 validation", "https://iohao.github.io/ionet/docs/core/jsr380"));
        list.add(new News("Business Framework - Resolving protocol fragmentation", "https://iohao.github.io/ionet/docs/manual/protocol_fragment"));

        return list;
    }
}

/** English-locale breaking news implementation. */
final class EnglishBreakingNews implements BreakingNews {

    @Override
    public List<News> listMainNews() {
        return List.of(
                new News("ionet javadoc", "https://iohao.github.io/javadoc"),
                new News("ionet issues", "https://github.com/iohao/ionet/issues"),
                new News("ionet version log", "https://iohao.github.io/ionet/docs/version_log"),
                new News("Releases: 1 to 2 versions are released every month, and upgrades within a major version are always compatible, such as 21.1 is upgraded to any higher version 21.x", "")
        );
    }

    @Override
    public List<News> listAdv() {
        return List.of(
                new News("online demo", "https://a.iohao.com"),
                new News("MMO", "https://iohao.github.io/ionet/docs/practices/mmo"),
                new News("Room games in action", "https://iohao.github.io/ionet/docs/practices/room_in_action")
        );
    }

    @Override
    public List<News> listNews() {
        List<News> list = new ArrayList<>();

        list.add(new News("Backers", "https://iohao.github.io/ionet/docs/contribute/backers"));

        // common development tips
        list.add(new News("Full-link call log tracking", "https://iohao.github.io/ionet/docs/manual/trace"));

        // licensing and practical products
        list.add(new News("Project cost analysis", "https://iohao.github.io/ionet/services/cost_analysis"));

        // architecture overview
        list.add(new News("ionet - Architecture Introduction", "https://iohao.github.io/ionet/docs/overall/architecture_intro"));
        list.add(new News("ionet - Architecture Diversity", "https://iohao.github.io/ionet/docs/overall/deploy_flexible"));
        list.add(new News("ionet - Comparison with traditional architecture", "https://iohao.github.io/ionet/docs/overall/legacy_system"));

        list.add(new News("ionet - Request Processing Flow", "https://iohao.github.io/ionet/docs/overall/request_processing_procedure"));
        list.add(new News("ionet - Thread related", "https://iohao.github.io/ionet/docs/overall/thread_executor"));
        list.add(new News("Startup methods for single server single process, multiple servers single process, and multiple servers multiple processes", "https://iohao.github.io/ionet/docs/manual/netty_run_one"));
        list.add(new News("Code Organization and Conventions", "https://iohao.github.io/ionet/docs/manual_high/code_organization"));
        list.add(new News("Same-process affinity", "https://iohao.github.io/ionet/docs/manual_high/same_process"));

        // logic server
        list.add(new News("GameLogicServer - Dynamically bind game logic server", "https://iohao.github.io/ionet/docs/manual/binding_logic_server"));
        list.add(new News("GameLogicServer - Meta information, additional information", "https://iohao.github.io/ionet/docs/manual/flowcontext_attachment"));

        // external server
        list.add(new News("ExternalServer - Unified Protocol Description", "https://iohao.github.io/ionet/docs/manual_high/external_message"));
        list.add(new News("ExternalServer", "https://iohao.github.io/ionet/docs/overall/external_intro"));
        list.add(new News("ExternalServer - Heartbeat settings and heartbeat hooks", "https://iohao.github.io/ionet/docs/external/idle"));
        list.add(new News("ExternalServer - User online and offline hooks", "https://iohao.github.io/ionet/docs/external/user_hook"));
        list.add(new News("ExternalServer - Routing access control", "https://iohao.github.io/ionet/docs/external/access_authentication"));
        list.add(new News("ExternalServer - Game cache for external servers", "https://iohao.github.io/ionet/docs/external/cache"));
        list.add(new News("ExternalServer - ws token Authentication and verification", "https://iohao.github.io/ionet/docs/external/ws_verify"));
        list.add(new News("ExternalServer - Built-in and optional Handler", "https://iohao.github.io/ionet/docs/external/netty_handler"));

        // communication
        list.add(new News("Communication - Request the communication results of multiple logical servers of the same type", "https://iohao.github.io/ionet/docs/communication/request_multiple_response"));
        list.add(new News("Communication - Get game data and expansion for external servers", "https://iohao.github.io/ionet/docs/communication/external_biz_region"));
        list.add(new News("Communication - Distributed Event Bus", "https://iohao.github.io/ionet/docs/communication/event_bus"));

        // built-in tools
        list.add(new News("Built-in Kit - TaskKit is a tool module that combines tasks, time, delay monitoring, timeout monitoring, etc.", "https://iohao.github.io/ionet/docs/kit/task_kit"));
        list.add(new News("Built-in Kit - Property monitoring", "https://iohao.github.io/ionet/docs/kit/property_change_listener"));
        list.add(new News("Built-in Kit - Lightweight and controllable delayed tasks", "https://iohao.github.io/ionet/docs/kit/delay_task"));

        // extension modules
        list.add(new News("ExtendedModule - Domain Events - Can solve the concurrency problem of multiple people doing the same business", "https://iohao.github.io/ionet/docs/extension_module/domain_event"));
        list.add(new News("ExtendedModule - Stress testing & simulating client requests", "https://iohao.github.io/ionet/docs/extension_module/simulation_client"));
        list.add(new News("ExtendedModule - Room - Extension modules for board games and rooms", "https://iohao.github.io/ionet/docs/extension_module/room"));
        list.add(new News("ExtendedModule - sdk-generate-code", "https://iohao.github.io/ionet/docs/extension_module/room"));

        // plugins
        list.add(new News("Business Framework - Plugin Introduction", "https://iohao.github.io/ionet/docs/manual/plugin_intro"));
        list.add(new News("Plugin - DebugInOut", "https://iohao.github.io/ionet/docs/core_plugin/action_debug"));
        list.add(new News("Plugin - Action call statistics", "https://iohao.github.io/ionet/docs/core_plugin/action_stat"));
        list.add(new News("Plugin - Business thread monitoring", "https://iohao.github.io/ionet/docs/core_plugin/action_thread_monitor"));
        list.add(new News("Plugin - Call statistics for each time period", "https://iohao.github.io/ionet/docs/core_plugin/action_time_range"));
        list.add(new News("Plugin - Full-link call log tracking", "https://iohao.github.io/ionet/docs/core_plugin/action_trace"));

        // business framework
        list.add(new News("Business Framework - Introduction", "https://iohao.github.io/ionet/docs/core/framework"));
        list.add(new News("Business Framework - FlowContext", "https://iohao.github.io/ionet/docs/manual/flowcontext"));
        list.add(new News("Business Framework - Assertions + exceptions = clear and concise code", "https://iohao.github.io/ionet/docs/manual/assert_game_code"));
        list.add(new News("Business Framework - Enable JSR380 validation specification", "https://iohao.github.io/ionet/docs/core/jsr380"));
        list.add(new News("Business Framework - Resolving protocol fragmentation", "https://iohao.github.io/ionet/docs/manual/protocol_fragment"));

        return list;
    }
}