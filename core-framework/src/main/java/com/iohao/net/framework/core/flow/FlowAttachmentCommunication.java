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
package com.iohao.net.framework.core.flow;

import com.iohao.net.framework.core.codec.DataCodecManager;
import com.iohao.net.framework.protocol.OnExternalTemplateId;
import com.iohao.net.common.kit.exception.ThrowKit;

/**
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface FlowAttachmentCommunication extends FlowExternalCommunication {
    default void updateAttachment(byte[] attachment) {
        var message = this.ofExternalRequestMessage(OnExternalTemplateId.attachmentUpdate, attachment);
        this.callExternal(message);
        this.getRequest().setAttachment(attachment);
    }

    default void updateAttachmentAsync(byte[] attachment) {
        this.executeVirtual(() -> this.updateAttachment(attachment));
    }

    default void updateAttachment() {
        var attachment = this.getAttachment();
        var codec = DataCodecManager.getDataCodec();
        byte[] payload = codec.encode(attachment);
        this.updateAttachment(payload);
    }

    default void updateAttachmentAsync() {
        this.executeVirtual(this::updateAttachment);
    }

    default <T> T getAttachment(final Class<T> clazz) {
        byte[] attachment = this.getRequest().getAttachment();
        var codec = DataCodecManager.getDataCodec();
        return codec.decode(attachment, clazz);
    }

    /**
     * getAttachment
     * <p>
     * examples
     * <pre>{@code
     *     public class MyFlowContext extends FlowContext {
     *         MyAttachment attachment;
     *
     *         @Override
     *         public MyAttachment getAttachment() {
     *             if (Objects.isNull(attachment)) {
     *                 this.attachment = this.getAttachment(MyAttachment.class);
     *             }
     *
     *             return this.attachment;
     *         }
     *     }
     *
     *     public class MyAttachment {
     *         long userId;
     *         ...
     *     }
     * }
     * </pre>
     *
     * @param <T> t
     * @return attachment
     */
    default <T> T getAttachment() {
        ThrowKit.ofRuntimeException("Must be implemented by subclasses.");
        return null;
    }
}
