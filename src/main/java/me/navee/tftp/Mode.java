package me.aynya.tftp;

/**
 * @author nede
 */
public enum Mode
{
    NETASCII,
    OCTET,
    @Deprecated MAIL;

    @Override
    public String toString()
    {
        return name().toLowerCase();
    }

}
