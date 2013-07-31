
package finance.parameters;

import java.io.InputStream;
import java.util.Arrays;
import junit.framework.TestCase;
import math.matrices.Matrix;

/**
 *
 * @author Grzegorz Los
 */
public class ConcreteCalibratorTest extends TestCase
{
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        prepareCalibrator();
        makeFileContents();
        makeParams();
        makeCorrelation();
    }

    private void prepareCalibrator()
    {
        calibrator = new ConcreteCalibrator(true);
        calibrator.setSep(";");
        calibrator.setDt(1./52);        
    }
    
    private void makeFileContents()
    {
        fileContents = "" +
            "Date; A; B; C" + "\n" +
            "08.07.2013; 100; 200; 50" + "\n" +
            "15.07.2013; 110; 180; 51" + "\n" +
            "22.07.2013; 130; 165; 67" + "\n" +
            "29.07.2013; 105; 175; 53" + "\n" +
            "05.08.2013; 90; 190; 42" + "\n";
    }

    private void makeParams()
    {
        // volatilities and drifts calculated in R
        params = new OneAssetParams[3];
        params[0] = new OneAssetParams("A", 90, 1.3399994, -0.4718875);
        params[1] = new OneAssetParams("B", 190, 0.6996303, -0.4220716);
        params[2] = new OneAssetParams("C", 42, 1.7481314, -0.7386123);
    }

    private void makeCorrelation()
    {
        // correlation calculated in R
        correlation = new Matrix( new double[][] {
            { 1.0000000, -0.946118,  0.9530523},
            {-0.9461180,  1.000000, -0.864330 },
            { 0.9530523, -0.864330,  1.000000 } });
    }
    
    public void testSimple() throws CorruptedStreamException
    {
        InputStream in = new StringInputStream(fileContents);
        calibrator.readAndCalc(in);
        assertTrue( Arrays.deepEquals(params, calibrator.getOneAssetParams()) );
        assertEquals( correlation, calibrator.getCorrelation() );
    }
    
    private Matrix correlation;
    private OneAssetParams[] params;
    private ConcreteCalibrator calibrator;
    private String fileContents;
}
