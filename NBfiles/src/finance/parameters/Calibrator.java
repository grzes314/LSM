
package finance.parameters;

import java.io.InputStream;
import math.matrices.Matrix;

/**
 * Interface for classes reading historical prices and calculating
 * parameters basing on it.
 * @author Grzegorz Los
 */
public interface Calibrator
{
    /**
     * Reads data in a form of CSV and calculatet assets' parameters and
     * correlation between assets.
     * @param in input stream (e.g. csv file stream).
     * @throws CorruptedStreamException when any problem with reading data occurs,
     * e.g. stream is broken or data in file is invalid.
     */
    public void readAndCalc(InputStream in) throws CorruptedStreamException;
    
    /**
     * Returns assets` parameters. Method {@code readAndCalc} should be called earlier,
     * otherwise null will be returned. Moreover, if the last call to {@code readAndCalc}
     * threw then also null will be returned.
     * @return assets` parameters or null.
     */
    public OneAssetParams[] getOneAssetParams();
    
    /**
     * Returns correlation between assets. Method {@code readAndCalc} should be
     * called earlier, otherwise null will be returned. Moreover, if the last
     * call to {@code readAndCalc} threw then also null will be returned.
     * @return correlation matrix or null.
     */
    public Matrix getCorrelation();
}
