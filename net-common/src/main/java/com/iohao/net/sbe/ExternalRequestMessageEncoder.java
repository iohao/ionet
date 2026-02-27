/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.*;

/**
 * ExternalRequestMessage
 */
@SuppressWarnings("all")
public final class ExternalRequestMessageEncoder
{
    public static final int BLOCK_LENGTH = 50;
    public static final int TEMPLATE_ID = 5;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ExternalRequestMessageEncoder parentMessage = this;
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

    public ExternalRequestMessageEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public ExternalRequestMessageEncoder wrapAndApplyHeader(
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

    public ExternalRequestMessageEncoder futureId(final long value)
    {
        buffer.putLong(offset + 0, value, BYTE_ORDER);
        return this;
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
        return 8;
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
        userIdentity.wrap(buffer, offset + 8);
        return userIdentity;
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
        return 17;
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

    public ExternalRequestMessageEncoder externalServerId(final int value)
    {
        buffer.putInt(offset + 17, value, BYTE_ORDER);
        return this;
    }


    public static int netIdId()
    {
        return 4;
    }

    public static int netIdSinceVersion()
    {
        return 0;
    }

    public static int netIdEncodingOffset()
    {
        return 21;
    }

    public static int netIdEncodingLength()
    {
        return 4;
    }

    public static String netIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static int netIdNullValue()
    {
        return -2147483648;
    }

    public static int netIdMinValue()
    {
        return -2147483647;
    }

    public static int netIdMaxValue()
    {
        return 2147483647;
    }

    public ExternalRequestMessageEncoder netId(final int value)
    {
        buffer.putInt(offset + 21, value, BYTE_ORDER);
        return this;
    }


    public static int traceIdId()
    {
        return 5;
    }

    public static int traceIdSinceVersion()
    {
        return 0;
    }

    public static int traceIdEncodingOffset()
    {
        return 25;
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


    public ExternalRequestMessageEncoder traceId(final int index, final byte value)
    {
        if (index < 0 || index >= 24)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 25 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String traceIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public ExternalRequestMessageEncoder putTraceId(final byte[] src, final int srcOffset)
    {
        final int length = 24;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 25, src, srcOffset, length);

        return this;
    }

    public ExternalRequestMessageEncoder traceId(final String src)
    {
        final int length = 24;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 25, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 25 + start, (byte)0);
        }

        return this;
    }

    public ExternalRequestMessageEncoder traceId(final CharSequence src)
    {
        final int length = 24;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 25, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 25 + start, (byte)0);
        }

        return this;
    }

    public static int templateIdId()
    {
        return 6;
    }

    public static int templateIdSinceVersion()
    {
        return 0;
    }

    public static int templateIdEncodingOffset()
    {
        return 49;
    }

    public static int templateIdEncodingLength()
    {
        return 1;
    }

    public static String templateIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte templateIdNullValue()
    {
        return (byte)-128;
    }

    public static byte templateIdMinValue()
    {
        return (byte)-127;
    }

    public static byte templateIdMaxValue()
    {
        return (byte)127;
    }

    public ExternalRequestMessageEncoder templateId(final byte value)
    {
        buffer.putByte(offset + 49, value);
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

    public ExternalRequestMessageEncoder putPayload(final DirectBuffer src, final int srcOffset, final int length)
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

    public ExternalRequestMessageEncoder putPayload(final byte[] src, final int srcOffset, final int length)
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

        final ExternalRequestMessageDecoder decoder = new ExternalRequestMessageDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
