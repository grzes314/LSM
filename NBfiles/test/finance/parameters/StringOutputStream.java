
package finance.parameters;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 *
 * @author Grzegorz Los
 */
public class StringOutputStream extends OutputStream
{
    @Override
    public void write(int b) throws IOException
    {
        bytes.add((byte)b);
    }
    
    public String getString()
    {
        byte[] myBytes = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); ++i)
            myBytes[i] = bytes.get(i);
        bytes.clear();
        return new String(myBytes);
    }

    ArrayList<Byte> bytes = new ArrayList<>();
}
