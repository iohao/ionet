/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public final class ServerMessageCommonEncoder
{
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final int ENCODED_LENGTH = 102;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private MutableDirectBuffer buffer;

    public ServerMessageCommonEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public static int idEncodingOffset()
    {
        return 0;
    }

    public static int idEncodingLength()
    {
        return 4;
    }

    public static int idNullValue()
    {
        return -2147483648;
    }

    public static int idMinValue()
    {
        return -2147483647;
    }

    public static int idMaxValue()
    {
        return 2147483647;
    }

    public ServerMessageCommonEncoder id(final int value)
    {
        buffer.putInt(offset + 0, value, BYTE_ORDER);
        return this;
    }


    public static int serverTypeEncodingOffset()
    {
        return 4;
    }

    public static int serverTypeEncodingLength()
    {
        return 1;
    }

    public static byte serverTypeNullValue()
    {
        return (byte)-128;
    }

    public static byte serverTypeMinValue()
    {
        return (byte)-127;
    }

    public static byte serverTypeMaxValue()
    {
        return (byte)127;
    }

    public ServerMessageCommonEncoder serverType(final byte value)
    {
        buffer.putByte(offset + 4, value);
        return this;
    }


    public static int netIdEncodingOffset()
    {
        return 5;
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

    public ServerMessageCommonEncoder netId(final int value)
    {
        buffer.putInt(offset + 5, value, BYTE_ORDER);
        return this;
    }


    public static int nameEncodingOffset()
    {
        return 9;
    }

    public static int nameEncodingLength()
    {
        return 24;
    }

    public static byte nameNullValue()
    {
        return (byte)0;
    }

    public static byte nameMinValue()
    {
        return (byte)32;
    }

    public static byte nameMaxValue()
    {
        return (byte)126;
    }

    public static int nameLength()
    {
        return 24;
    }


    public ServerMessageCommonEncoder name(final int index, final byte value)
    {
        if (index < 0 || index >= 24)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 9 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String nameCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.UTF_8.name();
    }

    public ServerMessageCommonEncoder putName(final byte[] src, final int srcOffset)
    {
        final int length = 24;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 9, src, srcOffset, length);

        return this;
    }

    public ServerMessageCommonEncoder name(final String src)
    {
        final int length = 24;
        final byte[] bytes = (null == src || src.isEmpty()) ? org.agrona.collections.ArrayUtil.EMPTY_BYTE_ARRAY : src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(offset + 9, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(offset + 9 + start, (byte)0);
        }

        return this;
    }

    public static int tagEncodingOffset()
    {
        return 33;
    }

    public static int tagEncodingLength()
    {
        return 24;
    }

    public static byte tagNullValue()
    {
        return (byte)0;
    }

    public static byte tagMinValue()
    {
        return (byte)32;
    }

    public static byte tagMaxValue()
    {
        return (byte)126;
    }

    public static int tagLength()
    {
        return 24;
    }


    public ServerMessageCommonEncoder tag(final int index, final byte value)
    {
        if (index < 0 || index >= 24)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 33 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String tagCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.UTF_8.name();
    }

    public ServerMessageCommonEncoder putTag(final byte[] src, final int srcOffset)
    {
        final int length = 24;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 33, src, srcOffset, length);

        return this;
    }

    public ServerMessageCommonEncoder tag(final String src)
    {
        final int length = 24;
        final byte[] bytes = (null == src || src.isEmpty()) ? org.agrona.collections.ArrayUtil.EMPTY_BYTE_ARRAY : src.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes.length > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + bytes.length);
        }

        buffer.putBytes(offset + 33, bytes, 0, bytes.length);

        for (int start = bytes.length; start < length; ++start)
        {
            buffer.putByte(offset + 33 + start, (byte)0);
        }

        return this;
    }

    public static int ipEncodingOffset()
    {
        return 57;
    }

    public static int ipEncodingLength()
    {
        return 45;
    }

    public static byte ipNullValue()
    {
        return (byte)0;
    }

    public static byte ipMinValue()
    {
        return (byte)32;
    }

    public static byte ipMaxValue()
    {
        return (byte)126;
    }

    public static int ipLength()
    {
        return 45;
    }


    public ServerMessageCommonEncoder ip(final int index, final byte value)
    {
        if (index < 0 || index >= 45)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 57 + (index * 1);
        buffer.putByte(pos, value);

        return this;
    }

    public static String ipCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public ServerMessageCommonEncoder putIp(final byte[] src, final int srcOffset)
    {
        final int length = 45;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + srcOffset);
        }

        buffer.putBytes(offset + 57, src, srcOffset, length);

        return this;
    }

    public ServerMessageCommonEncoder ip(final String src)
    {
        final int length = 45;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("String too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 57, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 57 + start, (byte)0);
        }

        return this;
    }

    public ServerMessageCommonEncoder ip(final CharSequence src)
    {
        final int length = 45;
        final int srcLength = null == src ? 0 : src.length();
        if (srcLength > length)
        {
            throw new IndexOutOfBoundsException("CharSequence too large for copy: byte length=" + srcLength);
        }

        buffer.putStringWithoutLengthAscii(offset + 57, src);

        for (int start = srcLength; start < length; ++start)
        {
            buffer.putByte(offset + 57 + start, (byte)0);
        }

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

        final ServerMessageCommonDecoder decoder = new ServerMessageCommonDecoder();
        decoder.wrap(buffer, offset);

        return decoder.appendTo(builder);
    }
}
