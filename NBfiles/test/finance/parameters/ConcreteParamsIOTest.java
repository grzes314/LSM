
package finance.parameters;

import java.io.IOException;
import java.io.InputStream;
import math.matrices.Matrix;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Grzegorz Los
 */
public class ConcreteParamsIOTest
{
    @Before
    public void setUp()
    {
        n = 3;
        makeOriginal();
        makeTranscription();
    }

    private void makeOriginal()
    {
        makeR();
        makeBasicParams();
        makeCorrelation();
        original = new ConcreteParams(basicParams, corr, r);        
    }
    
    private void makeR()
    {
        r = -0.08;
    }

    private void makeBasicParams()
    {
        basicParams = new OneAssetParams[3];
        for (int i = 0; i < n; ++i)
            basicParams[i] = new OneAssetParams(names[i], spots[i], vols[i], drifts[i]);
    }

    private void makeCorrelation()
    {
        corr = new Matrix( new double[][]
            {   { 1.0,  0.7, -0.8},
                { 0.7,  1.0, -0.6},
                {-0.8, -0.6,  1.0}
            });
    }
    
    private void makeTranscription()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append('\n');
        for (int i = 0; i < n; ++i)
            sb.append(names[i]).append(' ')
              .append(spots[i]).append(' ')
              .append(vols[i]).append(' ')
              .append(drifts[i]).append('\n');
        for (int row = 1; row <= n; ++row)
        {
            for (int col = 1; col < n; ++col)
                sb.append(corr.get(row, col)).append(' ');
            sb.append(corr.get(row, n)).append('\n');
        }
        sb.append(r).append('\n');
        transcription = sb.toString();
    }

    /**
     * Test of read method, of class ConcreteParamsIO.
     */
    @Test
    public void testRead() throws Exception
    {
        InputStream in = new StringInputStream(transcription);
        ConcreteParams read = (ConcreteParams) io.read(in);
        assertEquals( original, read );
    }

    /**
     * Test of write method, of class ConcreteParamsIO.
     */
    @Test
    public void testWrite() throws IOException
    {
        StringOutputStream out = new StringOutputStream();
        io.write(original, out);
        String result = out.getString();
        assertEquals( transcription, result );
    }
    
    private int n = 3;
    private ConcreteParams original;
    private String transcription;
    private OneAssetParams[] basicParams;
    private String[] names = {"first", "second", "third" };
    private double[] spots = {100, 80, 120};
    private double[] vols = {0.2, 0.4, 0.3};
    private double[] drifts = {0.3, -0.1, -0.3};
    private Matrix corr;
    private double r;
    
    ConcreteParamsIO io = new ConcreteParamsIO();
}
