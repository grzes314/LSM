
package finance.parameters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for classes reading (and writing) model's parameters from (to) input
 * (output) streams like files.
 * @author Grzegorz Los
 */
public interface ParamsIO
{
    /**
     * Reads {@code ModelParams} from given input stream. 
     * @param in input stream containing {@code ModelParams} written in format
     * specific for deriving classes.
     * @return Read parameters.
     * @throws CorruptedStreamException if input stream is not valid.
     */
    ModelParams read(InputStream in) throws CorruptedStreamException;
    
    /**
     * Writes given {@code ModelParams} to given {@code OutputStream} in format
     * specific for deriving class.
     * @param params parameters to be written.
     * @param out output stream to which parameters are written.
     * @throws IOException when an error with output stream occurs.
     */
    void write(ModelParams params, OutputStream out) throws IOException;
}
