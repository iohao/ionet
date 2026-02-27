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
package com.iohao.net.framework.core.doc;

import com.baidu.bjf.remoting.protobuf.annotation.*;
import com.iohao.net.common.kit.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.core.codec.*;
import com.iohao.net.framework.protocol.wrapper.*;
import java.util.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Builder for constructing {@link BroadcastDocument} instances that describe
 * server-push routes, their data types, and method metadata.
 *
 * @author 渔民小镇
 * @date 2024-07-05
 * @since 21.11
 */
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PACKAGE)
public class BroadcastDocumentBuilder {
    /** The route (command info) for this broadcast. */
    final CmdInfo cmdInfo;

    /** Business data type class. */
    Class<?> dataClass;
    @Setter(AccessLevel.PRIVATE)
    String dataClassName;
    /** Description of the broadcast data parameter. */
    String dataDescription;

    @Setter(AccessLevel.PACKAGE)
    boolean list;

    /** Broadcast method name, used only during client code generation. */
    String methodName;
    /** Broadcast (push) description. */
    String methodDescription;

    BroadcastDocumentBuilder(CmdInfo cmdInfo) {
        this.cmdInfo = cmdInfo;
    }

    /**
     * Set the broadcast data type as a List of the given class.
     *
     * @param dataClass the element type of the list
     * @return this builder
     */
    public BroadcastDocumentBuilder setDataClassList(Class<?> dataClass) {
        return this.setDataClassList(dataClass, null);
    }

    /**
     * Set the broadcast data type as a List with an optional description.
     *
     * @param dataClass       the element type of the list
     * @param dataDescription optional description of the data
     * @return this builder
     */
    public BroadcastDocumentBuilder setDataClassList(Class<?> dataClass, String dataDescription) {
        this.list = true;
        this.dataClass = dataClass;
        this.dataDescription = dataDescription;

        WrapperKit.optionalValueRecord(dataClass).ifPresentOrElse(valueRecord -> {
            this.dataClassName = valueRecord.getValueListClazz().getSimpleName();

            if (StrKit.isEmpty(dataDescription)) {
                this.dataDescription = this.dataClassName;
            }
        }, () -> {
            String simpleName = ByteValueList.class.getSimpleName();
            String simpleNameActualClazz = dataClass.getSimpleName();
            this.dataClassName = String.format("%s<%s>", simpleName, simpleNameActualClazz);
        });

        return this;
    }

    /**
     * Set the broadcast data type.
     *
     * @param dataClass the data type class
     * @return this builder
     */
    public BroadcastDocumentBuilder setDataClass(Class<?> dataClass) {
        return setDataClass(dataClass, null);
    }

    /**
     * Set the broadcast data type with an optional description.
     *
     * @param dataClass       the data type class
     * @param dataDescription optional description of the data
     * @return this builder
     */
    public BroadcastDocumentBuilder setDataClass(Class<?> dataClass, String dataDescription) {

        this.dataClass = dataClass;
        this.dataDescription = dataDescription;

        WrapperKit.optionalValueRecord(dataClass).ifPresentOrElse(valueRecord -> {
            this.dataClassName = valueRecord.getValueClazz().getSimpleName();

            if (StrKit.isEmpty(dataDescription)) {
                this.dataDescription = this.dataClassName;
            }
        }, () -> this.dataClassName = dataClass.getSimpleName());

        return this;
    }

    private String getMethodName() {
        // If no broadcast method name is specified, use a default naming convention
        return methodName == null
                ? "Method_%d_%d".formatted(cmdInfo.cmd(), cmdInfo.subCmd())
                : methodName;
    }

    /**
     * Build the broadcast document.
     *
     * @return the constructed {@link BroadcastDocument}
     */
    public BroadcastDocument build() {
        String theMethodName = getMethodName();

        extractedPreparedProto();

        var broadcastDocument = new BroadcastDocument(this.cmdInfo);
        broadcastDocument.methodDescription = this.methodDescription;
        broadcastDocument.methodName = theMethodName;
        broadcastDocument.cmdMethodName = StrKit.firstCharToLowerCase(theMethodName);
        broadcastDocument.dataClass = this.dataClass;
        broadcastDocument.dataClassName = this.dataClassName;
        broadcastDocument.dataDescription = this.dataDescription;
        broadcastDocument.dataIsList = this.list;

        return broadcastDocument;
    }

    private void extractedPreparedProto() {
        if (DataCodecManager.getDataCodec() instanceof ProtoDataCodec) {
            Optional.ofNullable(this.dataClass)
                    .filter(clazz -> Objects.nonNull(clazz.getAnnotation(ProtobufClass.class)))
                    .ifPresent(ProtoKit::create);
        }
    }

    /**
     * Build the broadcast document and register it with {@link DocumentHelper}.
     */
    public void buildToDocument() {
        DocumentHelper.addBroadcastDocument(this.build());
    }
}
