/* Generated SBE (Simple Binary Encoding) message codec. */
package com.iohao.net.sbe;

import org.agrona.MutableDirectBuffer;


/**
 * String with a max length of 255 bytes
 */
@SuppressWarnings("all")
public final class String255Encoder
{
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;
    public static final String SEMANTIC_VERSION = "1";
    public static final int ENCODED_LENGTH = -1;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private MutableDirectBuffer buffer;

    public String255Encoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public static int lengthEncodingOffset()
    {
        return 0;
    }

    public static int lengthEncodingLength()
    {
        return 1;
    }

    public static short lengthNullValue()
    {
        return (short)255;
    }

    public static short lengthMinValue()
    {
        return (short)0;
    }

    public static short lengthMaxValue()
    {
        return (short)254;
    }

    public String255Encoder length(final short value)
    {
        buffer.putByte(offset + 0, (byte)value);
        return this;
    }


    public static int varDataEncodingOffset()
    {
        return 1;
    }

    public static int varDataEncodingLength()
    {
        return -1;
    }

    public static byte varDataNullValue()
    {
        return (byte)0;
    }

    public static byte varDataMinValue()
    {
        return (byte)32;
    }

    public static byte varDataMaxValue()
    {
        return (byte)126;
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

        final String255Decoder decoder = new String255Decoder();
        decoder.wrap(buffer, offset);

        return decoder.appendTo(builder);
    }
}
