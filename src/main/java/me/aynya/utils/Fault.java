package me.aynya.utils;

/**
 * An unplanned condition which impedes a method from achieving its intended purpose which cannot be described without
 * referring to methods internal implementation.
 *
 * @author nede
 */
public class Fault
        extends RuntimeException
{

    ///
    /// Methods
    ///

    public Fault(String message)
    {
        super(message);
    }

    public Fault(String message, Throwable cause)
    {
        super(message, cause);
    }

    public Fault(Throwable cause)
    {
        super(cause);
    }
}
