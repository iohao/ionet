/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.*;

/**
 * ConnectRequestMessage ServerMessage
 */
@SuppressWarnings("all")
public final class ConnectRequestMessageDecoder
{
    public static final int BLOCK_LENGTH = 110;
    public static final int TEMPLATE_ID = 30;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final ConnectRequestMessageDecoder parentMessage = this;
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

    public ConnectRequestMessageDecoder wrap(
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

    public ConnectRequestMessageDecoder wrapAndApplyHeader(
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

    public ConnectRequestMessageDecoder sbeRewind()
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

    private final ServerMessageCommonDecoder common = new ServerMessageCommonDecoder();

    public ServerMessageCommonDecoder common()
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

    public long futureId()
    {
        return buffer.getLong(offset + 102, BYTE_ORDER);
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

    private final String255Decoder uri = new String255Decoder();

    public String255Decoder uri()
    {
        uri.wrap(buffer, offset + 110);
        return uri;
    }

    private final CmdMergesDecoder cmdMerges = new CmdMergesDecoder(this);

    public static long cmdMergesDecoderId()
    {
        return 10;
    }

    public static int cmdMergesDecoderSinceVersion()
    {
        return 0;
    }

    public CmdMergesDecoder cmdMerges()
    {
        cmdMerges.wrap(buffer);
        return cmdMerges;
    }

    public static final class CmdMergesDecoder
        implements Iterable<CmdMergesDecoder>, java.util.Iterator<CmdMergesDecoder>
    {
        public static final int HEADER_SIZE = 4;
        private final ConnectRequestMessageDecoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        CmdMergesDecoder(final ConnectRequestMessageDecoder parentMessage)
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

        public CmdMergesDecoder next()
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

        public java.util.Iterator<CmdMergesDecoder> iterator()
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
        
        public CmdMergesDecoder sbeSkip()
        {

            return this;
        }
    }

    private final PayloadDecoder payload = new PayloadDecoder(this);

    public static long payloadDecoderId()
    {
        return 15;
    }

    public static int payloadDecoderSinceVersion()
    {
        return 0;
    }

    /**
     * map payload
     *
     * @return PayloadDecoder : map payload
     */
    public PayloadDecoder payload()
    {
        payload.wrap(buffer);
        return payload;
    }

    /**
     * map payload
     */

    public static final class PayloadDecoder
        implements Iterable<PayloadDecoder>, java.util.Iterator<PayloadDecoder>
    {
        public static final int HEADER_SIZE = 4;
        private final ConnectRequestMessageDecoder parentMessage;
        private DirectBuffer buffer;
        private int count;
        private int index;
        private int offset;
        private int blockLength;

        PayloadDecoder(final ConnectRequestMessageDecoder parentMessage)
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

        public PayloadDecoder next()
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
            return 0;
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

        public java.util.Iterator<PayloadDecoder> iterator()
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

        public static int keyId()
        {
            return 16;
        }

        public static int keySinceVersion()
        {
            return 0;
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

        public int keyLength()
        {
            final int limit = parentMessage.limit();
            return ((short)(buffer.getByte(limit) & 0xFF));
        }

        public int skipKey()
        {
            final int headerLength = 1;
            final int limit = parentMessage.limit();
            final int dataLength = ((short)(buffer.getByte(limit) & 0xFF));
            final int dataOffset = limit + headerLength;
            parentMessage.limit(dataOffset + dataLength);

            return dataLength;
        }

        public int getKey(final MutableDirectBuffer dst, final int dstOffset, final int length)
        {
            final int headerLength = 1;
            final int limit = parentMessage.limit();
            final int dataLength = ((short)(buffer.getByte(limit) & 0xFF));
            final int bytesCopied = Math.min(length, dataLength);
            parentMessage.limit(limit + headerLength + dataLength);
            buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

            return bytesCopied;
        }

        public int getKey(final byte[] dst, final int dstOffset, final int length)
        {
            final int headerLength = 1;
            final int limit = parentMessage.limit();
            final int dataLength = ((short)(buffer.getByte(limit) & 0xFF));
            final int bytesCopied = Math.min(length, dataLength);
            parentMessage.limit(limit + headerLength + dataLength);
            buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

            return bytesCopied;
        }

        public void wrapKey(final DirectBuffer wrapBuffer)
        {
            final int headerLength = 1;
            final int limit = parentMessage.limit();
            final int dataLength = ((short)(buffer.getByte(limit) & 0xFF));
            parentMessage.limit(limit + headerLength + dataLength);
            wrapBuffer.wrap(buffer, limit + headerLength, dataLength);
        }

        public String key()
        {
            final int headerLength = 1;
            final int limit = parentMessage.limit();
            final int dataLength = ((short)(buffer.getByte(limit) & 0xFF));
            parentMessage.limit(limit + headerLength + dataLength);

            if (0 == dataLength)
            {
                return "";
            }

            final byte[] tmp = new byte[dataLength];
            buffer.getBytes(limit + headerLength, tmp, 0, dataLength);

            return new String(tmp, java.nio.charset.StandardCharsets.UTF_8);
        }

        public static int valueId()
        {
            return 17;
        }

        public static int valueSinceVersion()
        {
            return 0;
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

        public int valueLength()
        {
            final int limit = parentMessage.limit();
            return (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
        }

        public int skipValue()
        {
            final int headerLength = 4;
            final int limit = parentMessage.limit();
            final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
            final int dataOffset = limit + headerLength;
            parentMessage.limit(dataOffset + dataLength);

            return dataLength;
        }

        public int getValue(final MutableDirectBuffer dst, final int dstOffset, final int length)
        {
            final int headerLength = 4;
            final int limit = parentMessage.limit();
            final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
            final int bytesCopied = Math.min(length, dataLength);
            parentMessage.limit(limit + headerLength + dataLength);
            buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

            return bytesCopied;
        }

        public int getValue(final byte[] dst, final int dstOffset, final int length)
        {
            final int headerLength = 4;
            final int limit = parentMessage.limit();
            final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
            final int bytesCopied = Math.min(length, dataLength);
            parentMessage.limit(limit + headerLength + dataLength);
            buffer.getBytes(limit + headerLength, dst, dstOffset, bytesCopied);

            return bytesCopied;
        }

        public void wrapValue(final DirectBuffer wrapBuffer)
        {
            final int headerLength = 4;
            final int limit = parentMessage.limit();
            final int dataLength = (int)(buffer.getInt(limit, BYTE_ORDER) & 0xFFFF_FFFFL);
            parentMessage.limit(limit + headerLength + dataLength);
            wrapBuffer.wrap(buffer, limit + headerLength, dataLength);
        }

        public StringBuilder appendTo(final StringBuilder builder)
        {
            if (null == buffer)
            {
                return builder;
            }

            builder.append('(');
            builder.append("key=");
            builder.append('\'').append(key()).append('\'');
            builder.append('|');
            builder.append("value=");
            builder.append(skipValue()).append(" bytes of raw data");
            builder.append(')');

            return builder;
        }
        
        public PayloadDecoder sbeSkip()
        {
            skipKey();
            skipValue();

            return this;
        }
    }

    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final ConnectRequestMessageDecoder decoder = new ConnectRequestMessageDecoder();
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
        builder.append("[ConnectRequestMessage](sbeTemplateId=");
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
        final ServerMessageCommonDecoder common = this.common();
        if (null != common)
        {
            common.appendTo(builder);
        }
        else
        {
            builder.append("null");
        }
        builder.append('|');
        builder.append("futureId=");
        builder.append(this.futureId());
        builder.append('|');
        builder.append("cmdMerges=[");
        final int cmdMergesOriginalOffset = cmdMerges.offset;
        final int cmdMergesOriginalIndex = cmdMerges.index;
        final CmdMergesDecoder cmdMerges = this.cmdMerges();
        if (cmdMerges.count() > 0)
        {
            while (cmdMerges.hasNext())
            {
                cmdMerges.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        cmdMerges.offset = cmdMergesOriginalOffset;
        cmdMerges.index = cmdMergesOriginalIndex;
        builder.append(']');
        builder.append('|');
        builder.append("payload=[");
        final int payloadOriginalOffset = payload.offset;
        final int payloadOriginalIndex = payload.index;
        final PayloadDecoder payload = this.payload();
        if (payload.count() > 0)
        {
            while (payload.hasNext())
            {
                payload.next().appendTo(builder);
                builder.append(',');
            }
            builder.setLength(builder.length() - 1);
        }
        payload.offset = payloadOriginalOffset;
        payload.index = payloadOriginalIndex;
        builder.append(']');

        limit(originalLimit);

        return builder;
    }
    
    public ConnectRequestMessageDecoder sbeSkip()
    {
        sbeRewind();
        CmdMergesDecoder cmdMerges = this.cmdMerges();
        if (cmdMerges.count() > 0)
        {
            while (cmdMerges.hasNext())
            {
                cmdMerges.next();
                cmdMerges.sbeSkip();
            }
        }
        PayloadDecoder payload = this.payload();
        if (payload.count() > 0)
        {
            while (payload.hasNext())
            {
                payload.next();
                payload.sbeSkip();
            }
        }

        return this;
    }
}
