/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;


/**
 * ConnectResponseMessage ServerMessage
 */
@SuppressWarnings("all")
public final class ConnectResponseMessageEncoder
{
    public static final int BLOCK_LENGTH = 110;
    public static final int TEMPLATE_ID = 31;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ConnectResponseMessageEncoder parentMessage = this;
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

    public ConnectResponseMessageEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public ConnectResponseMessageEncoder wrapAndApplyHeader(
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
        return 102;
    }

    public static String commonMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final ServerMessageCommonEncoder common = new ServerMessageCommonEncoder();

    public ServerMessageCommonEncoder common()
    {
        common.wrap(buffer, offset + 0);
        return common;
    }

    public static int futureIdId()
    {
        return 2;
    }

    public static int futureIdSinceVersion()
    {
        return 0;
    }

    public static int futureIdEncodingOffset()
    {
        return 102;
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

    public ConnectResponseMessageEncoder futureId(final long value)
    {
        buffer.putLong(offset + 102, value, BYTE_ORDER);
        return this;
    }


    public static int uriId()
    {
        return 3;
    }

    public static int uriSinceVersion()
    {
        return 0;
    }

    public static int uriEncodingOffset()
    {
        return 110;
    }

    public static int uriEncodingLength()
    {
        return -1;
    }

    public static String uriMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    private final String255Encoder uri = new String255Encoder();

    public String255Encoder uri()
    {
        uri.wrap(buffer, offset + 110);
        return uri;
    }

    public static int joinServerIdId()
    {
        return 5;
    }

    public static int joinServerIdSinceVersion()
    {
        return 0;
    }

    public static int joinServerIdEncodingOffset()
    {
        return -1;
    }

    public static int joinServerIdEncodingLength()
    {
        return 4;
    }

    public static String joinServerIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int joinServerIdNullValue()
    {
        return -2147483648;
    }

    public static int joinServerIdMinValue()
    {
        return -2147483647;
    }

    public static int joinServerIdMaxValue()
    {
        return 2147483647;
    }

    public ConnectResponseMessageEncoder joinServerId(final int value)
    {
        buffer.putInt(offset + -1, value, BYTE_ORDER);
        return this;
    }


    private final CmdMergesEncoder cmdMerges = new CmdMergesEncoder(this);

    public static long cmdMergesId()
    {
        return 10;
    }

    public CmdMergesEncoder cmdMergesCount(final int count)
    {
        cmdMerges.wrap(buffer, count);
        return cmdMerges;
    }

    public static final class CmdMergesEncoder
    {
        public static final int HEADER_SIZE = 4;
        private final ConnectResponseMessageEncoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int initialLimit;

        CmdMergesEncoder(final ConnectResponseMessageEncoder parentMessage)
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

        public CmdMergesEncoder next()
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

        public CmdMergesEncoder value(final int value)
        {
            buffer.putInt(offset + 0, value, BYTE_ORDER);
            return this;
        }

    }

    private final PayloadEncoder payload = new PayloadEncoder(this);

    public static long payloadId()
    {
        return 15;
    }

    /**
     * map payload
     *
     * @param count of times the group will be encoded.
     * @return PayloadEncoder : encoder for the group.
     */
    public PayloadEncoder payloadCount(final int count)
    {
        payload.wrap(buffer, count);
        return payload;
    }

    /**
     * map payload
     */

    public static final class PayloadEncoder
    {
        public static final int HEADER_SIZE = 4;
        private final ConnectResponseMessageEncoder parentMessage;
        private MutableDirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int initialLimit;

        PayloadEncoder(final ConnectResponseMessageEncoder parentMessage)
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
            buffer.putShort(limit + 0, (short)0, BYTE_ORDER);
            buffer.putShort(limit + 2, (short)count, BYTE_ORDER);
        }

        public PayloadEncoder next()
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
            return 0;
        }

        public static int keyId()
        {
            return 16;
        }

        public static String keyCharacterEncoding()
        {
            return java.nio.charset.StandardCharsets.UTF_8.name();
        }

        public static String keyMetaAttribute(final MetaAttribute metaAttribute)
        {
            if (MetaAttribute.PRESENCE == metaAttribute)
            {
                return "required";
            }

            return "";
        }

        public static int keyHeaderLength()
        {
            return 1;
        }

        public PayloadEncoder putKey(final DirectBuffer src, final int srcOffset, final int length)
        {
            if (length > 254)
            {
                throw new IllegalStateException("length > maxValue for type: " + length);
            }

            final int headerLength = 1;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putByte(limit, (byte)length);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public PayloadEncoder putKey(final byte[] src, final int srcOffset, final int length)
        {
            if (length > 254)
            {
                throw new IllegalStateException("length > maxValue for type: " + length);
            }

            final int headerLength = 1;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putByte(limit, (byte)length);
            buffer.putBytes(limit + headerLength, src, srcOffset, length);

            return this;
        }

        public PayloadEncoder key(final String value)
        {
            final byte[] bytes = (null == value || value.isEmpty()) ? org.agrona.collections.ArrayUtil.EMPTY_BYTE_ARRAY : value.getBytes(java.nio.charset.StandardCharsets.UTF_8);

            final int length = bytes.length;
            if (length > 254)
            {
                throw new IllegalStateException("length > maxValue for type: " + length);
            }

            final int headerLength = 1;
            final int limit = parentMessage.limit();
            parentMessage.limit(limit + headerLength + length);
            buffer.putByte(limit, (byte)length);
            buffer.putBytes(limit + headerLength, bytes, 0, length);

            return this;
        }

        public static int valueId()
        {
            return 17;
        }

        public static String valueMetaAttribute(final MetaAttribute metaAttribute)
        {
            if (MetaAttribute.PRESENCE == metaAttribute)
            {
                return "required";
            }

            return "";
        }

        public static int valueHeaderLength()
        {
            return 4;
        }

        public PayloadEncoder putValue(final DirectBuffer src, final int srcOffset, final int length)
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

        public PayloadEncoder putValue(final byte[] src, final int srcOffset, final int length)
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

        final ConnectResponseMessageDecoder decoder = new ConnectResponseMessageDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
