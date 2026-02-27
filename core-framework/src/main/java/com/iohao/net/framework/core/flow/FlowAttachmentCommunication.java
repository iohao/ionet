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

import com.iohao.net.common.kit.exception.*;
import com.iohao.net.framework.core.codec.*;
import com.iohao.net.framework.protocol.*;

/**
 * Flow-level attachment communication for reading and writing request-scoped attachment data.
 *
 * @author 渔民小镇
 * @date 2025-10-09
 * @since 25.1
 */
public interface FlowAttachmentCommunication extends FlowExternalCommunication {
    /**
     * Update the request attachment on the external server with the given raw bytes,
     * and synchronize the local request's attachment.
     *
     * @param attachment the encoded attachment bytes
     */
    default void updateAttachment(byte[] attachment) {
        var message = this.ofExternalRequestMessage(OnExternalTemplateId.attachmentUpdate, attachment);
        this.callExternal(message);
        this.getRequest().setAttachment(attachment);
    }

    /**
     * Asynchronously update the request attachment on the external server.
     *
     * @param attachment the encoded attachment bytes
     * @see #updateAttachment(byte[])
     */
    default void updateAttachmentAsync(byte[] attachment) {
        this.executeVirtual(() -> this.updateAttachment(attachment));
    }

    /**
     * Encode the current typed attachment and update it on the external server.
     *
     * @see #updateAttachment(byte[])
     */
    default void updateAttachment() {
        var attachment = this.getAttachment();
        var codec = DataCodecManager.getDataCodec();
        byte[] payload = codec.encode(attachment);
        this.updateAttachment(payload);
    }

    /**
     * Asynchronously encode the current typed attachment and update it on the external server.
     *
     * @see #updateAttachment()
     */
    default void updateAttachmentAsync() {
        this.executeVirtual(this::updateAttachment);
    }

    /**
     * Decode the request attachment bytes into the specified type.
     *
     * @param clazz the target class to decode into
     * @param <T>   the attachment type
     * @return the decoded attachment object
     */
    default <T> T getAttachment(final Class<T> clazz) {
        byte[] attachment = this.getRequest().getAttachment();
        var codec = DataCodecManager.getDataCodec();
        return codec.decode(attachment, clazz);
    }

    /**
     * Get the typed attachment object. Must be implemented by subclasses.
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
     * @param <T> the attachment type
     * @return the attachment object
     */
    default <T> T getAttachment() {
        ThrowKit.ofRuntimeException("Must be implemented by subclasses.");
        return null;
    }
}
