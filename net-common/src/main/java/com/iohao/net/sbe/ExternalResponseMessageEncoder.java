/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.*;

/**
 * ExternalResponseMessage
 */
@SuppressWarnings("all")
public final class ExternalResponseMessageEncoder
{
    public static final int BLOCK_LENGTH = 14;
    public static final int TEMPLATE_ID = 6;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ExternalResponseMessageEncoder parentMessage = this;
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

    public ExternalResponseMessageEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public ExternalResponseMessageEncoder wrapAndApplyHeader(
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

    public ExternalResponseMessageEncoder futureId(final long value)
    {
        buffer.putLong(offset + 0, value, BYTE_ORDER);
        return this;
    }


    public static int errorCodeId()
    {
        return 2;
    }

    public static int errorCodeSinceVersion()
    {
        return 0;
    }

    public static int errorCodeEncodingOffset()
    {
        return 8;
    }

    public static int errorCodeEncodingLength()
    {
        return 2;
    }

    public static String errorCodeMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static short errorCodeNullValue()
    {
        return (short)-32768;
    }

    public static short errorCodeMinValue()
    {
        return (short)-32767;
    }

    public static short errorCodeMaxValue()
    {
        return (short)32767;
    }

    public ExternalResponseMessageEncoder errorCode(final short value)
    {
        buffer.putShort(offset + 8, value, BYTE_ORDER);
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
        return 10;
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

    public ExternalResponseMessageEncoder externalServerId(final int value)
    {
        buffer.putInt(offset + 10, value, BYTE_ORDER);
        return this;
    }


    public static int errorMessageId()
    {
        return 9;
    }

    public static String errorMessageCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.UTF_8.name();
    }

    public static String errorMessageMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int errorMessageHeaderLength()
    {
        return 1;
    }

    public ExternalResponseMessageEncoder putErrorMessage(final DirectBuffer src, final int srcOffset, final int length)
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

    public ExternalResponseMessageEncoder putErrorMessage(final byte[] src, final int srcOffset, final int length)
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

    public ExternalResponseMessageEncoder errorMessage(final String value)
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

    public static int payloadId()
    {
        return 10;
    }

    public static String payloadMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int payloadHeaderLength()
    {
        return 4;
    }

    public ExternalResponseMessageEncoder putPayload(final DirectBuffer src, final int srcOffset, final int length)
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

    public ExternalResponseMessageEncoder putPayload(final byte[] src, final int srcOffset, final int length)
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

        final ExternalResponseMessageDecoder decoder = new ExternalResponseMessageDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
