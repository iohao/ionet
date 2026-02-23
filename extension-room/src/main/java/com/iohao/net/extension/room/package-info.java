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
/**
 * Provides room-based game infrastructure for board games and similar multiplayer room workflows,
 * including room management, lifecycle processes, and extensible gameplay operations.
 *
 * <p>See <a href="https://iohao.github.io/ionet/docs/extension_module/room">Board Game/Room Game</a>
 * for the overall module design.
 * <p>
 * Module Overview
 * <pre>
 * This module is a solution for board games and room-based games. It is well suited for the basic infrastructure of board games and room games. Based on this model, you can create games like Hearthstone, Sanguosha, Dou Dizhu, Mahjong, etc.
 * Or, this model is applicable to any room-based game, such as CS, Bomberman, Ludo, Tank Wars, and so on.
 *
 * If you plan to create a board game, it is recommended that you extend based on this module. This module adheres to object-oriented design principles, is loosely coupled, and highly extensible.
 * This module helps developers abstract away a lot of repetitive work, and allows for clear organizational definition of the functional module structure and development process in the project, reducing subsequent project maintenance costs.
 * </pre>
 * <p>
 * Main Problems Solved and Responsibilities
 * <pre>
 * The functional responsibilities of board games and room-based games can be divided into 3 major categories:
 * 1. Room Management Related:
 * a. Manages all rooms, queries the room list, room addition, room deletion, association between rooms and players, and room lookup (by roomId, by userId).
 * 2. Game Start Process Related:
 * a. Board games and room-based games usually have fixed processes, such as creating a room, player entering the room, player exiting the room, dissolving the room, player ready, starting the game, etc.
 * b. When starting the game, pre-start validation is required, such as whether there are enough players in the room, etc. The game only truly starts when everything complies with the business logic.
 * 3. Gameplay Operation Related:
 * a. Once the game starts, the specific operations differ between games. For example, shooting in Tank Wars, pre-war card selection and playing cards in Hearthstone, "Chi", "Peng", "Gang", "Guo", "Hu" in Mahjong, and basic attacks, defense, and skills in turn-based games.
 * b. Due to the differences in gameplay operations, our gameplay operations need to be extensible and used to handle specific gameplay. At the same time, this extension method better conforms to the Single Responsibility Principle, which lowers our subsequent extension and maintenance costs.
 *
 * The above functional responsibilities (Room Management, Process Related, Gameplay Operation Related) are relatively generic features. If every game repeats this work, it will not only be tedious but also waste huge human resources.
 *
 * The current module can effectively help developers abstract away this repetitive work, and allows for clear organizational definition of the functional module structure and development process in the project, reducing subsequent project maintenance costs.
 * More importantly, there is relevant documentation, which allows new team members to get up to speed quickly in the future.
 * </pre>
 *
 * @author 渔民小镇
 * @date 2024-05-14
 * @since 21.8
 */
package com.iohao.net.extension.room;
