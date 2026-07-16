/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.DirectBuffer;


/**
 * BindingLogicServerMessage
 */
@SuppressWarnings("all")
public final class BindingLogicServerMessageDecoder
{
    public static final int BLOCK_LENGTH = 17;
    public static final int TEMPLATE_ID = 11;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final BindingLogicServerMessageDecoder parentMessage = this;
    private DirectBuffer buffer;
    private int offset;
    private int limit;
    int actingBlockLength;
    int actingVersion;

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

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public BindingLogicServerMessageDecoder wrap(
        final DirectBuffer buffer,
        final int offset,
        final int actingBlockLength,
        final int actingVersion)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public BindingLogicServerMessageDecoder wrapAndApplyHeader(
        final DirectBuffer buffer,
        final int offset,
        final MessageHeaderDecoder headerDecoder)
    {
        headerDecoder.wrap(buffer, offset);

        final int templateId = headerDecoder.templateId();
        if (TEMPLATE_ID != templateId)
        {
            throw new IllegalStateException("Invalid TEMPLATE_ID: " + templateId);
        }

        return wrap(
            buffer,
            offset + MessageHeaderDecoder.ENCODED_LENGTH,
            headerDecoder.blockLength(),
            headerDecoder.version());
    }

    public BindingLogicServerMessageDecoder sbeRewind()
    {
        return wrap(buffer, offset, actingBlockLength, actingVersion);
    }

    public int sbeDecodedLength()
    {
        final int currentLimit = limit();
        sbeSkip();
        final int decodedLength = encodedLength();
        limit(currentLimit);

        return decodedLength;
    }

    public int actingVersion()
    {
        return actingVersion;
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

    public long futureId()
    {
        return buffer.getLong(offset + 0, BYTE_ORDER);
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

    public int sourceNetId()
    {
        return buffer.getInt(offset + 8, BYTE_ORDER);
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

    public int externalServerId()
    {
        return buffer.getInt(offset + 12, BYTE_ORDER);
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

    public byte operation()
    {
        return buffer.getByte(offset + 16);
    }


    private final UserIdsDecoder userIds = new UserIdsDecoder(this);

    public static long userIdsDecoderId()
    {
        return 10;
    }

    public static int userIdsDecoderSinceVersion()
    {
        return 0;
    }

    public UserIdsDecoder userIds()
    {
        userIds.wrap(buffer);
        return userIds;
    }

    public static final class UserIdsDecoder
        implements Iterable<UserIdsDecoder>, java.util.Iterator<UserIdsDecoder>
    {
        public static final int HEADER_SIZE = 4;
        private final BindingLogicServerMessageDecoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        UserIdsDecoder(final BindingLogicServerMessageDecoder parentMessage)
        {
            this.parentMessage = parentMessage;
        }

        public void wrap(final DirectBuffer buffer)
        {
            if (buffer != this.buffer)
            {
                this.buffer = buffer;
            }

            index = 0;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + HEADER_SIZE);
            blockLength = (buffer.getShort(limit + 0, BYTE_ORDER) & 0xFFFF);
            count = (buffer.getShort(limit + 2, BYTE_ORDER) & 0xFFFF);
        }

        public UserIdsDecoder next()
        {
            if (index >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + blockLength);
            ++index;

            return this;
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

        public int actingBlockLength()
        {
            return blockLength;
        }

        public int actingVersion()
        {
            return parentMessage.actingVersion;
        }

        public int count()
        {
            return count;
        }

        public java.util.Iterator<UserIdsDecoder> iterator()
        {
            return this;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext()
        {
            return index < count;
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

        public long value()
        {
            return buffer.getLong(offset + 0, BYTE_ORDER);
        }


        public StringBuilder appendTo(final StringBuilder builder)
        {
            if (null == buffer)
            {
                return builder;
            }

            builder.append('(');
            builder.append("value=");
            builder.append(this.value());
            builder.append(')');

            return builder;
        }
        
        public UserIdsDecoder sbeSkip()
        {

            return this;
        }
    }

    private final LogicServerIdsDecoder logicServerIds = new LogicServerIdsDecoder(this);

    public static long logicServerIdsDecoderId()
    {
        return 12;
    }

    public static int logicServerIdsDecoderSinceVersion()
    {
        return 0;
    }

    public LogicServerIdsDecoder logicServerIds()
    {
        logicServerIds.wrap(buffer);
        return logicServerIds;
    }

    public static final class LogicServerIdsDecoder
        implements Iterable<LogicServerIdsDecoder>, java.util.Iterator<LogicServerIdsDecoder>
    {
        public static final int HEADER_SIZE = 4;
        private final BindingLogicServerMessageDecoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        LogicServerIdsDecoder(final BindingLogicServerMessageDecoder parentMessage)
        {
            this.parentMessage = parentMessage;
        }

        public void wrap(final DirectBuffer buffer)
        {
            if (buffer != this.buffer)
            {
                this.buffer = buffer;
            }

            index = 0;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + HEADER_SIZE);
            blockLength = (buffer.getShort(limit + 0, BYTE_ORDER) & 0xFFFF);
            count = (buffer.getShort(limit + 2, BYTE_ORDER) & 0xFFFF);
        }

        public LogicServerIdsDecoder next()
        {
            if (index >= count)
            {
                throw new java.util.NoSuchElementException();
            }

            offset = parentMessage.limit();
            parentMessage.limit(offset + blockLength);
            ++index;

            return this;
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

        public int actingBlockLength()
        {
            return blockLength;
        }

        public int actingVersion()
        {
            return parentMessage.actingVersion;
        }

        public int count()
        {
            return count;
        }

        public java.util.Iterator<LogicServerIdsDecoder> iterator()
        {
            return this;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext()
        {
            return index < count;
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

        public int value()
        {
            return buffer.getInt(offset + 0, BYTE_ORDER);
        }


        public StringBuilder appendTo(final StringBuilder builder)
        {
            if (null == buffer)
            {
                return builder;
            }

            builder.append('(');
            builder.append("value=");
            builder.append(this.value());
            builder.append(')');

            return builder;
        }
        
        public LogicServerIdsDecoder sbeSkip()
        {

            return this;
        }
    }

    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final BindingLogicServerMessageDecoder decoder = new BindingLogicServerMessageDecoder();
        decoder.wrap(buffer, offset, actingBlockLength, actingVersion);

        return decoder.appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final int originalLimit = limit();
        limit(offset + actingBlockLength);
        builder.append("[BindingLogicServerMessage](sbeTemplateId=");
        builder.append(TEMPLATE_ID);
        builder.append("|sbeSchemaId=");
        builder.append(SCHEMA_ID);
        builder.append("|sbeSchemaVersion=");
        if (parentMessage.actingVersion != SCHEMA_VERSION)
        {
            builder.append(parentMessage.actingVersion);
            builder.append('/');
        }
        builder.append(SCHEMA_VERSION);
        builder.append("|sbeBlockLength=");
        if (actingBlockLength != BLOCK_LENGTH)
        {
            builder.append(actingBlockLength);
            builder.append('/');
        }
        builder.append(BLOCK_LENGTH);
        builder.append("):");
        builder.append("futureId=");
        builder.append(this.futureId());
        builder.append('|');
        builder.append("sourceNetId=");
        builder.append(this.sourceNetId());
        builder.append('|');
        builder.append("externalServerId=");
        builder.append(this.externalServerId());
        builder.append('|');
        builder.append("operation=");
        builder.append(this.operation());
        builder.append('|');
        builder.append("userIds=[");
        final int userIdsOriginalOffset = userIds.offset;
        final int userIdsOriginalIndex = userIds.index;
        final UserIdsDecoder userIds = this.userIds();
        if (userIds.count() > 0)
        {
            while (userIds.hasNext())
            {
                userIds.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        userIds.offset = userIdsOriginalOffset;
        userIds.index = userIdsOriginalIndex;
        builder.append(']');
        builder.append('|');
        builder.append("logicServerIds=[");
        final int logicServerIdsOriginalOffset = logicServerIds.offset;
        final int logicServerIdsOriginalIndex = logicServerIds.index;
        final LogicServerIdsDecoder logicServerIds = this.logicServerIds();
        if (logicServerIds.count() > 0)
        {
            while (logicServerIds.hasNext())
            {
                logicServerIds.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        logicServerIds.offset = logicServerIdsOriginalOffset;
        logicServerIds.index = logicServerIdsOriginalIndex;
        builder.append(']');

        limit(originalLimit);

        return builder;
    }
    
    public BindingLogicServerMessageDecoder sbeSkip()
    {
        sbeRewind();
        UserIdsDecoder userIds = this.userIds();
        if (userIds.count() > 0)
        {
            while (userIds.hasNext())
            {
                userIds.next();
                userIds.sbeSkip();
            }
        }
        LogicServerIdsDecoder logicServerIds = this.logicServerIds();
        if (logicServerIds.count() > 0)
        {
            while (logicServerIds.hasNext())
            {
                logicServerIds.next();
                logicServerIds.sbeSkip();
            }
        }

        return this;
    }
}
