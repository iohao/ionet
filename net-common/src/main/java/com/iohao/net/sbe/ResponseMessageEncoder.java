/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;


/**
 * InternalResponseMessage
 */
@SuppressWarnings("all")
public final class ResponseMessageEncoder
{
    public static final int BLOCK_LENGTH = 71;
    public static final int TEMPLATE_ID = 4;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ResponseMessageEncoder parentMessage = this;
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

    public ResponseMessageEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public ResponseMessageEncoder wrapAndApplyHeader(
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

    public static int errorCodeId()
    {
        return 3;
    }

    public static int errorCodeSinceVersion()
    {
        return 0;
    }

    public static int errorCodeEncodingOffset()
    {
        return 69;
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

    public ResponseMessageEncoder errorCode(final short value)
    {
        buffer.putShort(offset + 69, value, BYTE_ORDER);
        return this;
    }


    public static int errorMessageId()
    {
        return 10;
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

    public ResponseMessageEncoder putErrorMessage(final DirectBuffer src, final int srcOffset, final int length)
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

    public ResponseMessageEncoder putErrorMessage(final byte[] src, final int srcOffset, final int length)
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

    public ResponseMessageEncoder errorMessage(final String value)
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
        return 11;
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

    public ResponseMessageEncoder putData(final DirectBuffer src, final int srcOffset, final int length)
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

    public ResponseMessageEncoder putData(final byte[] src, final int srcOffset, final int length)
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

        final ResponseMessageDecoder decoder = new ResponseMessageDecoder();
        decoder.wrap(buffer, offset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
