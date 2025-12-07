/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.DirectBuffer;

@SuppressWarnings("all")
public final class UserIdentityMessageDecoder
{
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final int ENCODED_LENGTH = 9;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private DirectBuffer buffer;

    public UserIdentityMessageDecoder wrap(final DirectBuffer buffer, final int offset)
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

    public static int userIdEncodingOffset()
    {
        return 0;
    }

    public static int userIdEncodingLength()
    {
        return 8;
    }

    public static int userIdSinceVersion()
    {
        return 0;
    }

    public static long userIdNullValue()
    {
        return -9223372036854775808L;
    }

    public static long userIdMinValue()
    {
        return -9223372036854775807L;
    }

    public static long userIdMaxValue()
    {
        return 9223372036854775807L;
    }

    public long userId()
    {
        return buffer.getLong(offset + 0, BYTE_ORDER);
    }


    public static int verifyIdentityEncodingOffset()
    {
        return 8;
    }

    public static int verifyIdentityEncodingLength()
    {
        return 1;
    }

    public static int verifyIdentitySinceVersion()
    {
        return 0;
    }

    public static byte verifyIdentityNullValue()
    {
        return (byte)-128;
    }

    public static byte verifyIdentityMinValue()
    {
        return (byte)-127;
    }

    public static byte verifyIdentityMaxValue()
    {
        return (byte)127;
    }

    public byte verifyIdentity()
    {
        return buffer.getByte(offset + 8);
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
        builder.append("userId=");
        builder.append(this.userId());
        builder.append('|');
        builder.append("verifyIdentity=");
        builder.append(this.verifyIdentity());
        builder.append(')');

        return builder;
    }
}
