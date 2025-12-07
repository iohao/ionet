/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;


/**
 * ExternalRequestMessage
 */
@SuppressWarnings("all")
public final class ExternalRequestMessageDecoder
{
    public static final int BLOCK_LENGTH = 50;
    public static final int TEMPLATE_ID = 5;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ExternalRequestMessageDecoder parentMessage = this;
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

    public ExternalRequestMessageDecoder wrap(
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

    public ExternalRequestMessageDecoder wrapAndApplyHeader(
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

    public ExternalRequestMessageDecoder sbeRewind()
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

    private final UserIdentityMessageDecoder userIdentity = new UserIdentityMessageDecoder();

    public UserIdentityMessageDecoder userIdentity()
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

    public int externalServerId()
    {
        return buffer.getInt(offset + 17, BYTE_ORDER);
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

    public int netId()
    {
        return buffer.getInt(offset + 21, BYTE_ORDER);
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


    public byte traceId(final int index)
    {
        if (index < 0 || index >= 24)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 25 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String traceIdCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getTraceId(final byte[] dst, final int dstOffset)
    {
        final int length = 24;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 25, dst, dstOffset, length);

        return length;
    }

    public String traceId()
    {
        final byte[] dst = new byte[24];
        buffer.getBytes(offset + 25, dst, 0, 24);

        int end = 0;
        for (; end < 24 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getTraceId(final Appendable value)
    {
        for (int i = 0; i < 24; ++i)
        {
            final int c = buffer.getByte(offset + 25 + i) & 0xFF;
            if (c == 0)
            {
                return i;
            }

            try
            {
                value.append(c > 127 ? '?' : (char)c);
            }
            catch (final java.io.IOException ex)
            {
                throw new java.io.UncheckedIOException(ex);
            }
        }

        return 24;
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

    public byte templateId()
    {
        return buffer.getByte(offset + 49);
    }


    public static int payloadId()
    {
        return 10;
    }

    public static int payloadSinceVersion()
    {
        return 0;
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

    public int payloadLength()
    {
        final int limit = parentMessage.limit();
        return (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
    }

    public int skipPayload()
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        final int dataOffset = limit + headerLength;
        parentMessage.limit(dataOffset + dataLength);

        return dataLength;
    }

    public int getPayload(final MutableDirectBuffer dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public int getPayload(final byte[] dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public void wrapPayload(final DirectBuffer wrapBuffer)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        parentMessage.limit(limit + headerLength + dataLength);
        wrapBuffer.wrap(buffer, limit + headerLength, dataLength);
    }

    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final ExternalRequestMessageDecoder decoder = new ExternalRequestMessageDecoder();
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
        builder.append("[ExternalRequestMessage](sbeTemplateId=");
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
        builder.append("userIdentity=");
        final UserIdentityMessageDecoder userIdentity = this.userIdentity();
        if (null != userIdentity)
        {
            userIdentity.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("externalServerId=");
        builder.append(this.externalServerId());
        builder.append('|');
        builder.append("netId=");
        builder.append(this.netId());
        builder.append('|');
        builder.append("traceId=");
        for (int i = 0; i < traceIdLength() && this.traceId(i) > 0; i++)
        {
            builder.append((char)this.traceId(i));
        }
        builder.append('|');
        builder.append("templateId=");
        builder.append(this.templateId());
        builder.append('|');
        builder.append("payload=");
        builder.append(skipPayload()).append(" bytes of raw data");

        limit(originalLimit);

        return builder;
    }
    
    public ExternalRequestMessageDecoder sbeSkip()
    {
        sbeRewind();
        skipPayload();

        return this;
    }
}
