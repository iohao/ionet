/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.*;

/**
 * EventBusMessage
 */
@SuppressWarnings("all")
public final class EventBusMessageEncoder
{
    public static final int BLOCK_LENGTH = 36;
    public static final int TEMPLATE_ID = 10;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final EventBusMessageEncoder parentMessage = this;
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

    public EventBusMessageEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public EventBusMessageEncoder wrapAndApplyHeader(
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

    public static int serverIdId()
    {
        return 1;
    }

    public static int serverIdSinceVersion()
    {
        return 0;
    }

    public static int serverIdEncodingOffset()
    {
        return 0;
    }

    public static int serverIdEncodingLength()
    {
        return 4;
    }

    public static String serverIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int serverIdNullValue()
    {
        return -2147483648;
    }

    public static int serverIdMinValue()
    {
        return -2147483647;
    }

    public static int serverIdMaxValue()
    {
        return 2147483647;
    }

    public EventBusMessageEncoder serverId(final int value)
    {
        buffer.putInt(offset + 0, value, BYTE_ORDER);
        return this;
    }


    public static int threadIndexId()
    {
        return 2;
    }

    public static int threadIndexSinceVersion()
    {
        return 0;
    }

    public static int threadIndexEncodingOffset()
    {
        return 4;
    }

    public static int threadIndexEncodingLength()
    {
        return 8;
    }

    public static String threadIndexMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long threadIndexNullValue()
    {
        return -9223372036854775808L;
    }

    public static long threadIndexMinValue()
    {
        return -9223372036854775807L;
    }

    public static long threadIndexMaxValue()
    {
        return 9223372036854775807L;
    }

    public EventBusMessageEncoder threadIndex(final long value)
    {
        buffer.putLong(offset + 4, value, BYTE_ORDER);
        return this;
    }


    public static int traceIdId()
    {
        return 3;
    }

    public static int traceIdSinceVersion()
    {
        return 0;
    }

    public static int traceIdEncodingOffset()
    {
        return 12;
    }

    public static int traceIdEncodingLength()
    {
        return 24;
    }

    public static String traceIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte traceIdNullValue()
    {
        return (byte)0;
    }

    public static byte traceIdMinValue()
    {
        return (byte)32;
    }

    public static byte traceIdMaxValue()
    {
        return (byte)126;
    }

    public static int traceIdLength()
    {
        return 24;
    }


    public EventBusMessageEncoder traceId(final int index, final byte value)
    {
        if (index < 0 || index >= 24)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 12 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String traceIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public EventBusMessageEncoder putTraceId(final byte[] src, final int srcOffset)
    {
        final int length = 24;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 12, src, srcOffset, length);

        return this;
    }

    public EventBusMessageEncoder traceId(final String src)
    {
        final int length = 24;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 12, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 12 + start, (byte)0);
        }

        return this;
    }

    public EventBusMessageEncoder traceId(final CharSequence src)
    {
        final int length = 24;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 12, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 12 + start, (byte)0);
        }

        return this;
    }

    public static int topicId()
    {
        return 4;
    }

    public static String topicCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.UTF_8.name();
    }

    public static String topicMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int topicHeaderLength()
    {
        return 1;
    }

    public EventBusMessageEncoder putTopic(final DirectBuffer src, final int srcOffset, final int length)
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

    public EventBusMessageEncoder putTopic(final byte[] src, final int srcOffset, final int length)
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

    public EventBusMessageEncoder topic(final String value)
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

    public EventBusMessageEncoder putData(final DirectBuffer src, final int srcOffset, final int length)
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

    public EventBusMessageEncoder putData(final byte[] src, final int srcOffset, final int length)
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

        final EventBusMessageDecoder decoder = new EventBusMessageDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
