package me.aynya.tftp;

import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.validation.constraints.Null;
import me.aynya.utils.Fault;
import net.jcip.annotations.Immutable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * todo
 *
 * @author nede
 */
@Immutable
public class DATA
        extends TftpPacket
{
    ///
    /// Constants
    ///

    private static final Type TYPE = Type.DATA;

    private static final int BLOCK_NUMBER_LENGTH = 2;

    private static final int MAX_DATA_SIZE = 512;

    ///
    /// Members
    ///

    private final int block;

    private final String data;

    ///
    /// Methods
    ///

    public DATA(int block, String data, @Null Mode mode)
    {
        super(TYPE, (mode == null) ? Mode.NETASCII : mode);
        this.block = block;
        this.data = checkNotNull(data, "data must be non-null.");
    }

    public boolean isTerminalPacket()
    {
        return (data.getBytes().length < MAX_DATA_SIZE);
    }

    /**
     *    2 bytes    2 bytes     n bytes
     *   ---------------------------------
     *  | 03    |   Block #  |    Data    |
     *   ---------------------------------
     */
    @Override
    public byte[] getRawPacket()
    {
        byte[] opcode = this.getType().value();
        byte[] block = ByteBuffer.allocate(2).put((byte) this.block).array();
        byte[] data = this.data.getBytes();
        byte[] rawPacket = new byte[opcode.length + block.length + data.length];

        int copyBeginIndexInclusive = 0;
        int copyLength = opcode.length;
        System.arraycopy(opcode, 0, rawPacket, copyBeginIndexInclusive, copyLength);

        copyBeginIndexInclusive = copyBeginIndexInclusive + copyLength;
        copyLength = block.length;
        System.arraycopy(block, 0, rawPacket, copyBeginIndexInclusive, copyLength);

        copyBeginIndexInclusive = copyBeginIndexInclusive + copyLength;
        copyLength = data.length;
        System.arraycopy(data, 0, rawPacket, copyBeginIndexInclusive, copyLength);

        return rawPacket;
    }

    public static DATA fromRawPacket(byte[] packet)
    {
        checkArgument(packet != null, "packet must be non null");

        Type type;
        int block;
        String data;

        try
        {
            int beginIndexInclusive = 0, endIndexExclusive = Type.OPCODE_LENGTH;
            type = Type.fromLooseValue(Arrays.copyOfRange(packet, beginIndexInclusive, endIndexExclusive));
            checkState(type == TYPE, "packet must be of type %s, given %s", TYPE.toString(), type);

            beginIndexInclusive = endIndexExclusive;
            endIndexExclusive = beginIndexInclusive + BLOCK_NUMBER_LENGTH;
            block = ByteBuffer.wrap(Arrays.copyOfRange(packet, beginIndexInclusive, endIndexExclusive)).getInt();

            beginIndexInclusive = endIndexExclusive;
            endIndexExclusive = packet.length;
            data = new String(Arrays.copyOfRange(packet, beginIndexInclusive, endIndexExclusive));
        }
        catch (RuntimeException e)
        {
            throw new Fault(e.getCause());
        }

        return new DATA(block, data, null);
    }

}
