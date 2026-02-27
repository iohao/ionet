/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.*;

/**
 * BindingLogicServerMessage
 */
@SuppressWarnings("all")
public final class BindingLogicServerMessageEncoder
{
    public static final int BLOCK_LENGTH = 17;
    public static final int TEMPLATE_ID = 11;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final BindingLogicServerMessageEncoder parentMessage = this;
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

    public BindingLogicServerMessageEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public BindingLogicServerMessageEncoder wrapAndApplyHeader(
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

    public static int futureIdId()
    {
        return 1;
    }

    public static int futureIdSinceVersion()
    {
        return 0;
    }

    public static int futureIdEncodingOffset()
    {
        return 0;
    }

    public static int futureIdEncodingLength()
    {
        return 8;
    }

    public static String futureIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long futureIdNullValue()
    {
        return -9223372036854775808L;
    }

    public static long futureIdMinValue()
    {
        return -9223372036854775807L;
    }

    public static long futureIdMaxValue()
    {
        return 9223372036854775807L;
    }

    public BindingLogicServerMessageEncoder futureId(final long value)
    {
        buffer.putLong(offset + 0, value, BYTE_ORDER);
        return this;
    }


    public static int sourceNetIdId()
    {
        return 2;
    }

    public static int sourceNetIdSinceVersion()
    {
        return 0;
    }

    public static int sourceNetIdEncodingOffset()
    {
        return 8;
    }

    public static int sourceNetIdEncodingLength()
    {
        return 4;
    }

    public static String sourceNetIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int sourceNetIdNullValue()
    {
        return -2147483648;
    }

    public static int sourceNetIdMinValue()
    {
        return -2147483647;
    }

    public static int sourceNetIdMaxValue()
    {
        return 2147483647;
    }

    public BindingLogicServerMessageEncoder sourceNetId(final int value)
    {
        buffer.putInt(offset + 8, value, BYTE_ORDER);
        return this;
    }


    public static int externalServerIdId()
    {
        return 3;
    }

    public static int externalServerIdSinceVersion()
    {
        return 0;
    }

    public static int externalServerIdEncodingOffset()
    {
        return 12;
    }

    public static int externalServerIdEncodingLength()
    {
        return 4;
    }

    public static String externalServerIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int externalServerIdNullValue()
    {
        return -2147483648;
    }

    public static int externalServerIdMinValue()
    {
        return -2147483647;
    }

    public static int externalServerIdMaxValue()
    {
        return 2147483647;
    }

    public BindingLogicServerMessageEncoder externalServerId(final int value)
    {
        buffer.putInt(offset + 12, value, BYTE_ORDER);
        return this;
    }


    public static int operationId()
    {
        return 4;
    }

    public static int operationSinceVersion()
    {
        return 0;
    }

    public static int operationEncodingOffset()
    {
        return 16;
    }

    public static int operationEncodingLength()
    {
        return 1;
    }

    public static String operationMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte operationNullValue()
    {
        return (byte)-128;
    }

    public static byte operationMinValue()
    {
        return (byte)-127;
    }

    public static byte operationMaxValue()
    {
        return (byte)127;
    }

    public BindingLogicServerMessageEncoder operation(final byte value)
    {
        buffer.putByte(offset + 16, value);
        return this;
    }


    private final UserIdsEncoder userIds = new UserIdsEncoder(this);

    public static long userIdsId()
    {
        return 10;
    }

    public UserIdsEncoder userIdsCount(final int count)
    {
        userIds.wrap(buffer, count);
        return userIds;
    }

    public static final class UserIdsEncoder
    {
        public static final int HEADER_SIZE = 4;
        private final BindingLogicServerMessageEncoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int initialLimit;

        UserIdsEncoder(final BindingLogicServerMessageEncoder parentMessage)
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
            buffer.putShort(limit + 0, (short)8, BYTE_ORDER);
            buffer.putShort(limit + 2, (short)count, BYTE_ORDER);
        }

        public UserIdsEncoder next()
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
            return 8;
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
            return 8;
        }

        public static String valueMetaAttribute(final MetaAttribute metaAttribute)
        {
            if (MetaAttribute.PRESENCE == metaAttribute)
            {
                return "required";
            }

            return "";
        }

        public static long valueNullValue()
        {
            return -9223372036854775808L;
        }

        public static long valueMinValue()
        {
            return -9223372036854775807L;
        }

        public static long valueMaxValue()
        {
            return 9223372036854775807L;
        }

        public UserIdsEncoder value(final long value)
        {
            buffer.putLong(offset + 0, value, BYTE_ORDER);
            return this;
        }

    }

    private final LogicServerIdsEncoder logicServerIds = new LogicServerIdsEncoder(this);

    public static long logicServerIdsId()
    {
        return 12;
    }

    public LogicServerIdsEncoder logicServerIdsCount(final int count)
    {
        logicServerIds.wrap(buffer, count);
        return logicServerIds;
    }

    public static final class LogicServerIdsEncoder
    {
        public static final int HEADER_SIZE = 4;
        private final BindingLogicServerMessageEncoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int initialLimit;

        LogicServerIdsEncoder(final BindingLogicServerMessageEncoder parentMessage)
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

        public LogicServerIdsEncoder next()
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
            return 13;
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

        public LogicServerIdsEncoder value(final int value)
        {
            buffer.putInt(offset + 0, value, BYTE_ORDER);
            return this;
        }

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

        final BindingLogicServerMessageDecoder decoder = new BindingLogicServerMessageDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
