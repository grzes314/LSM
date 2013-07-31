
package finance.parameters;

import java.io.InputStream;

/**
 *
 * @author Grzegorz Los
 */
public class StringInputStream extends InputStream
{

    public StringInputStream(String str)
    {
        bytes = str.getBytes();
        next = 0;
    }
    
    @Override
    public int read()
    {
        if (next < bytes.length)
            return bytes[next++];
        else return -1;
    }
    
    private byte[] bytes;
    private int next;
}
