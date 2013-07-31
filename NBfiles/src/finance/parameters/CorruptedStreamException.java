
package finance.parameters;

/**
 *
 * @author Grzegorz Los
 */
public class CorruptedStreamException extends Exception
{
    public CorruptedStreamException()
    {
    }
    
    public CorruptedStreamException(Throwable cause)
    {
        super(cause);
    }

    public CorruptedStreamException(String message)
    {
        super(message);
    }

    public CorruptedStreamException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
