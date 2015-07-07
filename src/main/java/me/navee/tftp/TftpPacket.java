package me.aynya.tftp;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a tftp packet.
 *
 * @author nede
 */
public abstract class TftpPacket
{
    ///
    /// Constants
    ///

    public static final byte ZERO_BYTE = 0;

    ///
    /// Members
    ///

    private final Type type;

    private final Mode mode;


    ///
    /// Methods
    ///

    public TftpPacket(Type type, Mode mode)
    {
        this.type = checkNotNull(type, "type must be non null.");
        this.mode = checkNotNull(mode, "mode must be non null");
    }

    /**
     * NOTE:
     * If an abstract method is given protected access and its implementation overrides the access modifier to public
     * it appears great, but screws up polymorphic invocation.
     *
     * @return
     */
    public abstract byte[] getRawPacket();

    public Type getType()
    {
        return type;
    }

    public Mode getMode()
    {
        return mode;
    }

    @Override
    public String toString()
    {
        return getType().name();
    }

    public enum Type
    {
        RRQ(1),
        WRQ(2),
        DATA(3),
        ACK(4),
        ERROR(5);

        ///
        /// Methods
        ///

        private Type(int opcode)
        {
            byte[] opcodeBytes = {ZERO_BYTE, (byte) opcode};
            this.opcode = ByteBuffer.allocate(OPCODE_LENGTH).put(opcodeBytes);
        }

        public byte[] value()
        {
            return this.opcode.array();
        }

        @Override
        public String toString()
        {
           return this.name();
        }

        ///
        ///
        /// Static Members

        private static Map<ByteBuffer, Type> valueMap = new HashMap<>(values().length * 2);

        static
        {
            valueMap.clear();

            for (Type type : values())
            {
                valueMap.put(type.opcode, type);
            }
        }

        public static Type fromLooseValue(byte[] opcode)
        {
            checkArgument(opcode.length == OPCODE_LENGTH, "opcode must be of size %s", OPCODE_LENGTH);

            return valueMap.get(ByteBuffer.wrap(opcode)); //todo: what if valueMap doesn't have opcode?
        }

        ///
        /// Members
        ///

        private final ByteBuffer opcode;

        ///
        /// Constants
        ///

        public static final int OPCODE_LENGTH = 2;
    }
}
