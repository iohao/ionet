/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class CommonMessageDecoder
{
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final int ENCODED_LENGTH = 60;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private DirectBuffer buffer;

    public CommonMessageDecoder wrap(final DirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;

        return this;
    }

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public static int futureIdEncodingOffset()
    {
        return 0;
    }

    public static int futureIdEncodingLength()
    {
        return 8;
    }

    public static int futureIdSinceVersion()
    {
        return 0;
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


    public static int cmdMergeEncodingOffset()
    {
        return 8;
    }

    public static int cmdMergeEncodingLength()
    {
        return 4;
    }

    public static int cmdMergeSinceVersion()
    {
        return 0;
    }

    public static int cmdMergeNullValue()
    {
        return -2147483648;
    }

    public static int cmdMergeMinValue()
    {
        return -2147483647;
    }

    public static int cmdMergeMaxValue()
    {
        return 2147483647;
    }

    public int cmdMerge()
    {
        return buffer.getInt(offset + 8, BYTE_ORDER);
    }


    public static int traceIdEncodingOffset()
    {
        return 12;
    }

    public static int traceIdEncodingLength()
    {
        return 24;
    }

    public static int traceIdSinceVersion()
    {
        return 0;
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

        final int pos = offset + 12 + (index * 1);

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

        buffer.getBytes(offset + 12, dst, dstOffset, length);

        return length;
    }

    public String traceId()
    {
        final byte[] dst = new byte[24];
        buffer.getBytes(offset + 12, dst, 0, 24);

        int end = 0;
        for (; end < 24 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getTraceId(final Appendable value)
    {
        for (int i = 0; i < 24; ++i)
        {
            final int c = buffer.getByte(offset + 12 + i) & 0xFF;
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


    public static int externalServerIdEncodingOffset()
    {
        return 36;
    }

    public static int externalServerIdEncodingLength()
    {
        return 4;
    }

    public static int externalServerIdSinceVersion()
    {
        return 0;
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
        return buffer.getInt(offset + 36, BYTE_ORDER);
    }


    public static int logicServerIdEncodingOffset()
    {
        return 40;
    }

    public static int logicServerIdEncodingLength()
    {
        return 4;
    }

    public static int logicServerIdSinceVersion()
    {
        return 0;
    }

    public static int logicServerIdNullValue()
    {
        return -2147483648;
    }

    public static int logicServerIdMinValue()
    {
        return -2147483647;
    }

    public static int logicServerIdMaxValue()
    {
        return 2147483647;
    }

    public int logicServerId()
    {
        return buffer.getInt(offset + 40, BYTE_ORDER);
    }


    public static int sourceServerIdEncodingOffset()
    {
        return 44;
    }

    public static int sourceServerIdEncodingLength()
    {
        return 4;
    }

    public static int sourceServerIdSinceVersion()
    {
        return 0;
    }

    public static int sourceServerIdNullValue()
    {
        return -2147483648;
    }

    public static int sourceServerIdMinValue()
    {
        return -2147483647;
    }

    public static int sourceServerIdMaxValue()
    {
        return 2147483647;
    }

    public int sourceServerId()
    {
        return buffer.getInt(offset + 44, BYTE_ORDER);
    }


    public static int netIdEncodingOffset()
    {
        return 48;
    }

    public static int netIdEncodingLength()
    {
        return 4;
    }

    public static int netIdSinceVersion()
    {
        return 0;
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
        return buffer.getInt(offset + 48, BYTE_ORDER);
    }


    public static int nanoTimeEncodingOffset()
    {
        return 52;
    }

    public static int nanoTimeEncodingLength()
    {
        return 8;
    }

    public static int nanoTimeSinceVersion()
    {
        return 0;
    }

    public static long nanoTimeNullValue()
    {
        return -9223372036854775808L;
    }

    public static long nanoTimeMinValue()
    {
        return -9223372036854775807L;
    }

    public static long nanoTimeMaxValue()
    {
        return 9223372036854775807L;
    }

    public long nanoTime()
    {
        return buffer.getLong(offset + 52, BYTE_ORDER);
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

        builder.append('(');
        builder.append("futureId=");
        builder.append(this.futureId());
        builder.append('|');
        builder.append("cmdMerge=");
        builder.append(this.cmdMerge());
        builder.append('|');
        builder.append("traceId=");
        for (int i = 0; i < traceIdLength() && this.traceId(i) > 0; i++)
        {
            builder.append((char)this.traceId(i));
        }
        builder.append('|');
        builder.append("externalServerId=");
        builder.append(this.externalServerId());
        builder.append('|');
        builder.append("logicServerId=");
        builder.append(this.logicServerId());
        builder.append('|');
        builder.append("sourceServerId=");
        builder.append(this.sourceServerId());
        builder.append('|');
        builder.append("netId=");
        builder.append(this.netId());
        builder.append('|');
        builder.append("nanoTime=");
        builder.append(this.nanoTime());
        builder.append(')');

        return builder;
    }
}
