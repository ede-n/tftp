package me.aynya.tftp;

import java.util.Arrays;
import javax.validation.constraints.Null;
import me.aynya.utils.ByteUtils;
import me.aynya.utils.Fault;
import net.jcip.annotations.Immutable;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * todo
 *
 * @author nede
 */
@Immutable
public class RRQ extends TftpPacket
{
    ///
    /// Constants
    ///

    private static final Type TYPE = Type.RRQ;

    ///
    /// Members
    ///

    private final String fileName;


    public RRQ(String fileName, @Null Mode mode)
    {
        super(TYPE, (mode == null) ? Mode.NETASCII : mode);
        this.fileName = checkNotNull(StringUtils.stripToNull(fileName), "fileName must be non-null.");
    }

    ///
    /// Methods
    ///

    /**
     *    2 bytes    string   1 byte     string   1 byte
     *   -----------------------------------------------
     *  | 01/02 |  Filename  |   0  |    Mode    |   0  |
     *   -----------------------------------------------
     */
    @Override
    public byte[] getRawPacket()
    {
        byte[] opcode = this.getType().value();
        byte[] fileName = this.fileName.getBytes();
        byte[] mode = this.getMode().toString().getBytes();
        byte[] rawPacket = new byte[opcode.length + fileName.length + 1 + mode.length + 1];

        int copyBeginIndexInclusive = 0;
        int copyLength = opcode.length;
        System.arraycopy(opcode, 0, rawPacket, copyBeginIndexInclusive, copyLength);

        copyBeginIndexInclusive = copyBeginIndexInclusive + copyLength;
        copyLength = fileName.length;
        System.arraycopy(fileName, 0, rawPacket, copyBeginIndexInclusive, copyLength);

        copyBeginIndexInclusive = copyBeginIndexInclusive + copyLength;
        copyLength = 1;
        rawPacket[copyBeginIndexInclusive] = ZERO_BYTE;

        copyBeginIndexInclusive = copyBeginIndexInclusive + copyLength;
        copyLength = mode.length;
        System.arraycopy(mode, 0, rawPacket, copyBeginIndexInclusive, copyLength);

        copyBeginIndexInclusive = copyBeginIndexInclusive + copyLength;
        copyLength = 1;
        rawPacket[copyBeginIndexInclusive] = ZERO_BYTE;

        return rawPacket;
    }

    public static RRQ fromRawPacket(byte[] packet)
    {
        checkArgument(packet != null, "packet must be non null");

        Type type;
        String fileName;
        Mode mode;

        try
        {
            int beginIndexInclusive = 0, endIndexExclusive = Type.OPCODE_LENGTH;
            type = Type.fromLooseValue(Arrays.copyOfRange(packet, beginIndexInclusive, endIndexExclusive));
            checkState(type == TYPE, "packet must be of type %s, given %s", TYPE.toString(), type);

            beginIndexInclusive = endIndexExclusive;
            endIndexExclusive = ByteUtils.indexOfFirstByte(packet, beginIndexInclusive, packet.length, ZERO_BYTE);
            fileName = Arrays.toString(Arrays.copyOfRange(packet, beginIndexInclusive, endIndexExclusive));

            beginIndexInclusive = endIndexExclusive + 1;
            endIndexExclusive = ByteUtils.indexOfFirstByte(packet, beginIndexInclusive, packet.length, ZERO_BYTE);
            mode = Mode.valueOf(new String(Arrays.copyOfRange(packet, beginIndexInclusive, endIndexExclusive)));
        }
        catch (RuntimeException e)
        {
            throw new Fault(e.getCause());
        }

        return new RRQ(fileName, mode);
    }

}
