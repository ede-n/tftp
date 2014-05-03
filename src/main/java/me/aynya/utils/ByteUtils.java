package me.aynya.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * todo
 *
 * @author nede
 */
public class ByteUtils
{

    ///
    /// Methods
    ///

    /**
     * Design Decision:
     * When a {@code key} is not found in {@code src}, I wasn't sure if this method should return -1 or
     * throw an exception. I referred {@link String#indexOf}, which returns -1 if {@code key} cannot be found and copied
     * the same functionality. But, client code must always check that the returned value is not -1, which made client
     * code look ugly.
     *
     * NOTE: Realized that exceptions improve readability of (client) code.
     *
     * @param src
     * @param beginIndexInclusive
     * @param endIndexExclusive
     * @param searchKey
     * @return
     */
    public static int indexOfFirstByte(byte[] src, int beginIndexInclusive, int endIndexExclusive, byte searchKey)
    {
        checkNotNull(src, "src must be non null");

        checkArgument(beginIndexInclusive < endIndexExclusive,
                "beginIndexInclusive(%s) must be less than endIndexExclusive(%s)", beginIndexInclusive,
                endIndexExclusive);

        checkArgument(endIndexExclusive <= src.length, "endIndexExclusive must be less than or equal to src length");

        for (int i = beginIndexInclusive; i < endIndexExclusive; i++)
        {
            if (src[i] == searchKey)
            {
                return i;
            }
        }

        throw new KeyNotFoundException(String.format("searchKey(%s) could not be found"));
    }

}
