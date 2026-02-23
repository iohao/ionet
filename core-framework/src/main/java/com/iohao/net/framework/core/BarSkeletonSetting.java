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
package com.iohao.net.framework.core;

import com.iohao.net.framework.core.flow.DefaultFlowContext;
import com.iohao.net.framework.core.flow.FlowContextFactory;

/**
 * Configuration settings for the business framework skeleton ({@link BarSkeleton}).
 * Controls action routing dimensions, logging flags, validation, and pluggable
 * components such as the flow context factory and flow executor.
 *
 * @author 渔民小镇
 * @date 2021-12-20
 */
public final class BarSkeletonSetting {
    /** The default length of actions (level 1 command; main route) */
    public int cmdMaxLen = 32;
    /** The default length of sub-actions (level 2 sub-command; sub-route) */
    public int subCmdMaxLen = 96;
    /** Set to false to disable printing */
    public boolean print;
    /** Set to true for action log printing */
    public boolean printAction = true;
    /** Set to false for short name printing in action logs (class, parameter names, return values) */
    public boolean printActionShort = true;
    /** Set to true for inout log printing */
    public boolean printInout = true;
    /** Set to true for codec log printing */
    public boolean printDataCodec = true;

    /** Set to true for runners log printing */
    public boolean printRunners;

    /// Set to true to enable JSR380 validation for business parameters.
    ///
    /// For more information on enabling JSR380 validation, refer to this document:
    /// <a href="https://iohao.github.io/ionet/docs/manual/jsr380">Documentation - JSR380</a>
    ///
    /// You will need to include the following dependencies in your project's pom.xml:
    /// ```xml
    /// <!-- hibernate validator -->
    /// <dependency>
    ///     <groupId>org.hibernate.validator</groupId>
    ///     <artifactId>hibernate-validator</artifactId>
    ///     <version>9.0.1.Final</version>
    /// </dependency>
    /// <!-- EL implementation. In a Java SE environment, you must add the implementation as a dependency to your POM file -->
    /// <dependency>
    ///     <groupId>org.glassfish</groupId>
    ///     <artifactId>jakarta.el</artifactId>
    ///     <version>4.0.2</version>
    /// </dependency>
    /// <!-- jakarta -->
    ///  <dependency>
    ///      <groupId>jakarta.validation</groupId>
    ///      <artifactId>jakarta.validation-api</artifactId>
    ///      <version>${jakarta.validation-api.version}</version>
    ///  </dependency>
    ///```
    public boolean validator;
    /// Whether to automatically invoke validation on action parameters.
    public boolean validatorAutoCall;

    /** Set to true to enable documentation parsing */
    public boolean parseDoc;

    /** Factory for creating {@link FlowContext} instances per request. */
    public FlowContextFactory flowContextFactory = DefaultFlowContext::new;
    /** Executor responsible for running the flow pipeline. */
    public FlowExecutor flowExecutor = new DefaultFlowExecutor();
    /** Strategy for generating developer-facing code suggestions on errors. */
    public CodeSuggest codeSuggest = new DefaultCodeSuggest();
    /** Strategy for {@link CmdInfo} flyweight caching. */
    public CmdInfoFlyweightStrategy cmdInfoFlyweightStrategy = CmdInfoFlyweightStrategy.TWO_ARRAY;
}
