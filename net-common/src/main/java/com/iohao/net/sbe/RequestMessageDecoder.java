/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.MutableDirectBuffer;
import org.agrona.DirectBuffer;


/**
 * InternalRequestMessage
 */
@SuppressWarnings("all")
public final class RequestMessageDecoder
{
    public static final int BLOCK_LENGTH = 70;
    public static final int TEMPLATE_ID = 3;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final RequestMessageDecoder parentMessage = this;
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

    public RequestMessageDecoder wrap(
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

    public RequestMessageDecoder wrapAndApplyHeader(
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

    public RequestMessageDecoder sbeRewind()
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

    private final CommonMessageDecoder common = new CommonMessageDecoder();

    public CommonMessageDecoder common()
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

    private final UserIdentityMessageDecoder userIdentity = new UserIdentityMessageDecoder();

    public UserIdentityMessageDecoder userIdentity()
    {
        userIdentity.wrap(buffer, offset + 60);
        return userIdentity;
    }

    public static int hopCountId()
    {
        return 3;
    }

    public static int hopCountSinceVersion()
    {
        return 0;
    }

    public static int hopCountEncodingOffset()
    {
        return 69;
    }

    public static int hopCountEncodingLength()
    {
        return 1;
    }

    public static String hopCountMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte hopCountNullValue()
    {
        return (byte)-128;
    }

    public static byte hopCountMinValue()
    {
        return (byte)-127;
    }

    public static byte hopCountMaxValue()
    {
        return (byte)127;
    }

    public byte hopCount()
    {
        return buffer.getByte(offset + 69);
    }


    private final BindingLogicServerIdsDecoder bindingLogicServerIds = new BindingLogicServerIdsDecoder(this);

    public static long bindingLogicServerIdsDecoderId()
    {
        return 10;
    }

    public static int bindingLogicServerIdsDecoderSinceVersion()
    {
        return 0;
    }

    public BindingLogicServerIdsDecoder bindingLogicServerIds()
    {
        bindingLogicServerIds.wrap(buffer);
        return bindingLogicServerIds;
    }

    public static final class BindingLogicServerIdsDecoder
        implements Iterable<BindingLogicServerIdsDecoder>, java.util.Iterator<BindingLogicServerIdsDecoder>
    {
        public static final int HEADER_SIZE = 4;
        private final RequestMessageDecoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        BindingLogicServerIdsDecoder(final RequestMessageDecoder parentMessage)
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

        public BindingLogicServerIdsDecoder next()
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

        public java.util.Iterator<BindingLogicServerIdsDecoder> iterator()
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
        
        public BindingLogicServerIdsDecoder sbeSkip()
        {

            return this;
        }
    }

    public static int dataId()
    {
        return 20;
    }

    public static int dataSinceVersion()
    {
        return 0;
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

    public int dataLength()
    {
        final int limit = parentMessage.limit();
        return (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
    }

    public int skipData()
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        final int dataOffset = limit + headerLength;
        parentMessage.limit(dataOffset + dataLength);

        return dataLength;
    }

    public int getData(final MutableDirectBuffer dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public int getData(final byte[] dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public void wrapData(final DirectBuffer wrapBuffer)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        parentMessage.limit(limit + headerLength + dataLength);
        wrapBuffer.wrap(buffer, limit + headerLength, dataLength);
    }

    public static int attachmentId()
    {
        return 21;
    }

    public static int attachmentSinceVersion()
    {
        return 0;
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

    public int attachmentLength()
    {
        final int limit = parentMessage.limit();
        return (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
    }

    public int skipAttachment()
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        final int dataOffset = limit + headerLength;
        parentMessage.limit(dataOffset + dataLength);

        return dataLength;
    }

    public int getAttachment(final MutableDirectBuffer dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public int getAttachment(final byte[] dst, final int dstOffset, final int length)
    {
        final int headerLength = 4;
        final int limit = parentMessage.limit();
        final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        final int bytesCopied = Math.min(length, dataLength);
        parentMessage.limit(limit + headerLength + dataLength);
        buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

        return bytesCopied;
    }

    public void wrapAttachment(final DirectBuffer wrapBuffer)
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

        final RequestMessageDecoder decoder = new RequestMessageDecoder();
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
        builder.append("[RequestMessage](sbeTemplateId=");
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
        builder.append("common=");
        final CommonMessageDecoder common = this.common();
        if (null != common)
        {
            common.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
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
        builder.append("hopCount=");
        builder.append(this.hopCount());
        builder.append('|');
        builder.append("bindingLogicServerIds=[");
        final int bindingLogicServerIdsOriginalOffset = bindingLogicServerIds.offset;
        final int bindingLogicServerIdsOriginalIndex = bindingLogicServerIds.index;
        final BindingLogicServerIdsDecoder bindingLogicServerIds = this.bindingLogicServerIds();
        if (bindingLogicServerIds.count() > 0)
        {
            while (bindingLogicServerIds.hasNext())
            {
                bindingLogicServerIds.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        bindingLogicServerIds.offset = bindingLogicServerIdsOriginalOffset;
        bindingLogicServerIds.index = bindingLogicServerIdsOriginalIndex;
        builder.append(']');
        builder.append('|');
        builder.append("data=");
        builder.append(skipData()).append(" bytes of raw data");
        builder.append('|');
        builder.append("attachment=");
        builder.append(skipAttachment()).append(" bytes of raw data");

        limit(originalLimit);

        return builder;
    }
    
    public RequestMessageDecoder sbeSkip()
    {
        sbeRewind();
        BindingLogicServerIdsDecoder bindingLogicServerIds = this.bindingLogicServerIds();
        if (bindingLogicServerIds.count() > 0)
        {
            while (bindingLogicServerIds.hasNext())
            {
                bindingLogicServerIds.next();
                bindingLogicServerIds.sbeSkip();
            }
        }
        skipData();
        skipAttachment();

        return this;
    }
}
