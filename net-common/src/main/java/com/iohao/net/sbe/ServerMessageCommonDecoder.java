/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.*;

@SuppressWarnings("all")
public final class ServerMessageCommonDecoder
{
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final int ENCODED_LENGTH = 102;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private DirectBuffer buffer;

    public ServerMessageCommonDecoder wrap(final DirectBuffer buffer, final int offset)
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

    public static int idEncodingOffset()
    {
        return 0;
    }

    public static int idEncodingLength()
    {
        return 4;
    }

    public static int idSinceVersion()
    {
        return 0;
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

    public int id()
    {
        return buffer.getInt(offset + 0, BYTE_ORDER);
    }


    public static int serverTypeEncodingOffset()
    {
        return 4;
    }

    public static int serverTypeEncodingLength()
    {
        return 1;
    }

    public static int serverTypeSinceVersion()
    {
        return 0;
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

    public byte serverType()
    {
        return buffer.getByte(offset + 4);
    }


    public static int netIdEncodingOffset()
    {
        return 5;
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
        return buffer.getInt(offset + 5, BYTE_ORDER);
    }


    public static int nameEncodingOffset()
    {
        return 9;
    }

    public static int nameEncodingLength()
    {
        return 24;
    }

    public static int nameSinceVersion()
    {
        return 0;
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


    public byte name(final int index)
    {
        if (index < 0 || index >= 24)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 9 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String nameCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.UTF_8.name();
    }

    public int getName(final byte[] dst, final int dstOffset)
    {
        final int length = 24;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 9, dst, dstOffset, length);

        return length;
    }

    public String name()
    {
        final byte[] dst = new byte[24];
        buffer.getBytes(offset + 9, dst, 0, 24);

        int end = 0;
        for (; end < 24 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.UTF_8);
    }


    public static int tagEncodingOffset()
    {
        return 33;
    }

    public static int tagEncodingLength()
    {
        return 24;
    }

    public static int tagSinceVersion()
    {
        return 0;
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


    public byte tag(final int index)
    {
        if (index < 0 || index >= 24)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 33 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String tagCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.UTF_8.name();
    }

    public int getTag(final byte[] dst, final int dstOffset)
    {
        final int length = 24;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 33, dst, dstOffset, length);

        return length;
    }

    public String tag()
    {
        final byte[] dst = new byte[24];
        buffer.getBytes(offset + 33, dst, 0, 24);

        int end = 0;
        for (; end < 24 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.UTF_8);
    }


    public static int ipEncodingOffset()
    {
        return 57;
    }

    public static int ipEncodingLength()
    {
        return 45;
    }

    public static int ipSinceVersion()
    {
        return 0;
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


    public byte ip(final int index)
    {
        if (index < 0 || index >= 45)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        final int pos = offset + 57 + (index * 1);

        return buffer.getByte(pos);
    }


    public static String ipCharacterEncoding()
    {
        return java.nio.charset.StandardCharsets.US_ASCII.name();
    }

    public int getIp(final byte[] dst, final int dstOffset)
    {
        final int length = 45;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("Copy will go out of range: offset=" + dstOffset);
        }

        buffer.getBytes(offset + 57, dst, dstOffset, length);

        return length;
    }

    public String ip()
    {
        final byte[] dst = new byte[45];
        buffer.getBytes(offset + 57, dst, 0, 45);

        int end = 0;
        for (; end < 45 && dst[end] != 0; ++end);

        return new String(dst, 0, end, java.nio.charset.StandardCharsets.US_ASCII);
    }


    public int getIp(final Appendable value)
    {
        for (int i = 0; i < 45; ++i)
        {
            final int c = buffer.getByte(offset + 57 + i) & 0xFF;
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

        return 45;
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
        builder.append("id=");
        builder.append(this.id());
        builder.append('|');
        builder.append("serverType=");
        builder.append(this.serverType());
        builder.append('|');
        builder.append("netId=");
        builder.append(this.netId());
        builder.append('|');
        builder.append("name=");
        for (int i = 0; i < nameLength() && this.name(i) > 0; i++)
        {
            builder.append((char)this.name(i));
        }
        builder.append('|');
        builder.append("tag=");
        for (int i = 0; i < tagLength() && this.tag(i) > 0; i++)
        {
            builder.append((char)this.tag(i));
        }
        builder.append('|');
        builder.append("ip=");
        for (int i = 0; i < ipLength() && this.ip(i) > 0; i++)
        {
            builder.append((char)this.ip(i));
        }
        builder.append(')');

        return builder;
    }
}
