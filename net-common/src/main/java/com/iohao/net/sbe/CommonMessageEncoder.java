/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class CommonMessageEncoder
{
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final int ENCODED_LENGTH = 60;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private MutableDirectBuffer buffer;

    public CommonMessageEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;

        return this;
    }

    public MutableDirectBuffer buffer()
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

    public CommonMessageEncoder futureId(final long value)
    {
        buffer.putLong(offset + 0, value, BYTE_ORDER);
        return this;
    }


    public static int cmdMergeEncodingOffset()
    {
        return 8;
    }

    public static int cmdMergeEncodingLength()
    {
        return 4;
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

    public CommonMessageEncoder cmdMerge(final int value)
    {
        buffer.putInt(offset + 8, value, BYTE_ORDER);
        return this;
    }


    public static int traceIdEncodingOffset()
    {
        return 12;
    }

    public static int traceIdEncodingLength()
    {
        return 24;
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


    public CommonMessageEncoder traceId(final int index, final byte value)
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

    public CommonMessageEncoder putTraceId(final byte[] src, final int srcOffset)
    {
        final int length = 24;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 12, src, srcOffset, length);

        return this;
    }

    public CommonMessageEncoder traceId(final String src)
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

    public CommonMessageEncoder traceId(final CharSequence src)
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

    public static int externalServerIdEncodingOffset()
    {
        return 36;
    }

    public static int externalServerIdEncodingLength()
    {
        return 4;
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

    public CommonMessageEncoder externalServerId(final int value)
    {
        buffer.putInt(offset + 36, value, BYTE_ORDER);
        return this;
    }


    public static int logicServerIdEncodingOffset()
    {
        return 40;
    }

    public static int logicServerIdEncodingLength()
    {
        return 4;
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

    public CommonMessageEncoder logicServerId(final int value)
    {
        buffer.putInt(offset + 40, value, BYTE_ORDER);
        return this;
    }


    public static int sourceServerIdEncodingOffset()
    {
        return 44;
    }

    public static int sourceServerIdEncodingLength()
    {
        return 4;
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

    public CommonMessageEncoder sourceServerId(final int value)
    {
        buffer.putInt(offset + 44, value, BYTE_ORDER);
        return this;
    }


    public static int netIdEncodingOffset()
    {
        return 48;
    }

    public static int netIdEncodingLength()
    {
        return 4;
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

    public CommonMessageEncoder netId(final int value)
    {
        buffer.putInt(offset + 48, value, BYTE_ORDER);
        return this;
    }


    public static int nanoTimeEncodingOffset()
    {
        return 52;
    }

    public static int nanoTimeEncodingLength()
    {
        return 8;
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

    public CommonMessageEncoder nanoTime(final long value)
    {
        buffer.putLong(offset + 52, value, BYTE_ORDER);
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

        final CommonMessageDecoder decoder = new CommonMessageDecoder();
        decoder.wrap(buffer, offset);

        return decoder.appendTo(builder);
    }
}
