package me.aynya.utils;

/**
 * RuntimeException thrown when a key being searched for is not found.
 *
 * @author nede
 */
public class KeyNotFoundException
        extends RuntimeException
{

    ///
    /// Methods
    ///

    public KeyNotFoundException(String message)
    {
        super(message);
    }

    public KeyNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public KeyNotFoundException(Throwable cause)
    {
        super(cause);
    }

}
