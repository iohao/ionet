/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.*;

/**
 * UserRequestMessage
 */
@SuppressWarnings("all")
public final class UserRequestMessageEncoder
{
    public static final int BLOCK_LENGTH = 78;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final UserRequestMessageEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    private int offset;
    private int limit;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public UserRequestMessageEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public UserRequestMessageEncoder wrapAndApplyHeader(
        final MutableDirectBuffer buffer, final int offset, final MessageHeaderEncoder headerEncoder)
    {
        headerEncoder
            .wrap(buffer, offset)
            .blockLength(BLOCK_LENGTH)
            .templateId(TEMPLATE_ID)
            .schemaId(SCHEMA_ID)
            .version(SCHEMA_VERSION);

        return wrap(buffer, offset + MessageHeaderEncoder.ENCODED_LENGTH);
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        this.limit = limit;
    }

    public static int commonId()
    {
        return 1;
    }

    public static int commonSinceVersion()
    {
        return 0;
    }

    public static int commonEncodingOffset()
    {
        return 0;
    }

    public static int commonEncodingLength()
    {
        return 60;
    }

    public static String commonMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final CommonMessageEncoder common = new CommonMessageEncoder();

    public CommonMessageEncoder common()
    {
        common.wrap(buffer, offset + 0);
        return common;
    }

    public static int userIdentityId()
    {
        return 2;
    }

    public static int userIdentitySinceVersion()
    {
        return 0;
    }

    public static int userIdentityEncodingOffset()
    {
        return 60;
    }

    public static int userIdentityEncodingLength()
    {
        return 9;
    }

    public static String userIdentityMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final UserIdentityMessageEncoder userIdentity = new UserIdentityMessageEncoder();

    public UserIdentityMessageEncoder userIdentity()
    {
        userIdentity.wrap(buffer, offset + 60);
        return userIdentity;
    }

    public static int cacheConditionId()
    {
        return 3;
    }

    public static int cacheConditionSinceVersion()
    {
        return 0;
    }

    public static int cacheConditionEncodingOffset()
    {
        return 69;
    }

    public static int cacheConditionEncodingLength()
    {
        return 4;
    }

    public static String cacheConditionMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int cacheConditionNullValue()
    {
        return -2147483648;
    }

    public static int cacheConditionMinValue()
    {
        return -2147483647;
    }

    public static int cacheConditionMaxValue()
    {
        return 2147483647;
    }

    public UserRequestMessageEncoder cacheCondition(final int value)
    {
        buffer.putInt(offset + 69, value, BYTE_ORDER);
        return this;
    }


    public static int msgIdId()
    {
        return 4;
    }

    public static int msgIdSinceVersion()
    {
        return 0;
    }

    public static int msgIdEncodingOffset()
    {
        return 73;
    }

    public static int msgIdEncodingLength()
    {
        return 4;
    }

    public static String msgIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int msgIdNullValue()
    {
        return -2147483648;
    }

    public static int msgIdMinValue()
    {
        return -2147483647;
    }

    public static int msgIdMaxValue()
    {
        return 2147483647;
    }

    public UserRequestMessageEncoder msgId(final int value)
    {
        buffer.putInt(offset + 73, value, BYTE_ORDER);
        return this;
    }


    public static int stickId()
    {
        return 9;
    }

    public static int stickSinceVersion()
    {
        return 0;
    }

    public static int stickEncodingOffset()
    {
        return 77;
    }

    public static int stickEncodingLength()
    {
        return 1;
    }

    public static String stickMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte stickNullValue()
    {
        return (byte)-128;
    }

    public static byte stickMinValue()
    {
        return (byte)-127;
    }

    public static byte stickMaxValue()
    {
        return (byte)127;
    }

    public UserRequestMessageEncoder stick(final byte value)
    {
        buffer.putByte(offset + 77, value);
        return this;
    }


    private final BindingLogicServerIdsEncoder bindingLogicServerIds = new BindingLogicServerIdsEncoder(this);

    public static long bindingLogicServerIdsId()
    {
        return 10;
    }

    public BindingLogicServerIdsEncoder bindingLogicServerIdsCount(final int count)
    {
        bindingLogicServerIds.wrap(buffer, count);
        return bindingLogicServerIds;
    }

    public static final class BindingLogicServerIdsEncoder
    {
        public static final int HEADER_SIZE = 4;
        private final UserRequestMessageEncoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int initialLimit;

        BindingLogicServerIdsEncoder(final UserRequestMessageEncoder parentMessage)
        {
            this.parentMessage = parentMessage;
        }

        public void wrap(final MutableDirectBuffer buffer, final int count)
        {
            if (count < 0 || count > 65534)
            {
                throw new IllegalArgumentException("count outside allowed range: count=" + count);
            }

            if (buffer != this.buffer)
            {
                this.buffer = buffer;
            }

            index = 0;
            this.count = count;
            final int limit = parentMessage.limit();
            initialLimit = limit;
            parentMessage.limit(limit + HEADER_SIZE);
            buffer.putShort(limit + 0, (short)4, BYTE_ORDER);
            buffer.putShort(limit + 2, (short)count, BYTE_ORDER);
        }

        public BindingLogicServerIdsEncoder next()
        {
            if (index >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + sbeBlockLength());
            ++index;

            return this;
        }

        public int resetCountToIndex()
        {
            count = index;
            buffer.putShort(initialLimit + 2, (short)count, BYTE_ORDER);

            return count;
        }

        public static int countMinValue()
        {
            return 0;
        }

        public static int countMaxValue()
        {
            return 65534;
        }

        public static int sbeHeaderSize()
        {
            return HEADER_SIZE;
        }

        public static int sbeBlockLength()
        {
            return 4;
        }

        public static int valueId()
        {
            return 11;
        }

        public static int valueSinceVersion()
        {
            return 0;
        }

        public static int valueEncodingOffset()
        {
            return 0;
        }

        public static int valueEncodingLength()
        {
            return 4;
        }

        public static String valueMetaAttribute(final MetaAttribute metaAttribute)
        {
            if (MetaAttribute.PRESENCE == metaAttribute)
            {
                return "required";
            }

            return "";
        }

        public static int valueNullValue()
        {
            return -2147483648;
        }

        public static int valueMinValue()
        {
            return -2147483647;
        }

        public static int valueMaxValue()
        {
            return 2147483647;
        }

        public BindingLogicServerIdsEncoder value(final int value)
        {
            buffer.putInt(offset + 0, value, BYTE_ORDER);
            return this;
        }

    }

    public static int dataId()
    {
        return 20;
    }

    public static String dataMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int dataHeaderLength()
    {
        return 4;
    }

    public UserRequestMessageEncoder putData(final DirectBuffer src, final int srcOffset, final int length)
    {
        if (length > 61440)
        {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, BYTE_ORDER);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public UserRequestMessageEncoder putData(final byte[] src, final int srcOffset, final int length)
    {
        if (length > 61440)
        {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, BYTE_ORDER);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public static int attachmentId()
    {
        return 21;
    }

    public static String attachmentMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int attachmentHeaderLength()
    {
        return 4;
    }

    public UserRequestMessageEncoder putAttachment(final DirectBuffer src, final int srcOffset, final int length)
    {
        if (length > 1024)
        {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, BYTE_ORDER);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public UserRequestMessageEncoder putAttachment(final byte[] src, final int srcOffset, final int length)
    {
        if (length > 1024)
        {
            throw new IllegalStateException("length > maxValue for type: " + length);
        }

        final int headerLength = 4;
        final int limit = parentMessage.limit();
        parentMessage.limit(limit + headerLength + length);
        buffer.putInt(limit, length, BYTE_ORDER);
        buffer.putBytes(limit + headerLength, src, srcOffset, length);

        return this;
    }

    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        return appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final UserRequestMessageDecoder decoder = new UserRequestMessageDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
