
package models;

import junit.framework.TestCase;
import static models.VanillaOptionParams.CallOrPut.CALL;
import static models.VanillaOptionParams.CallOrPut.PUT;
import static utils.Common.doublesEqual;
/**
 *
 * @author Grzegorz Los
 */
public class BSModelTest extends TestCase
{
    class EuOptionSupport extends models.testsupports.EuOption
    {
        @Override
        protected void setModelParams(SimpleModelParams smp)
        {
            model.setParams(smp);
        }

        @Override
        protected double price(VanillaOptionParams vop)
        {
            return model.price(vop);
        }       
    }
    
    public void testEuCallExtremes() {
        eos.callExtremeSpot();
        eos.callExtremeStrike();
        eos.callExtremeTime();
    }

    public void testEuPutExtremes() {
        eos.putExtremeSpot();
        eos.putExtremeStrike();
        eos.putExtremeTime();
    }

    public void testEuCallTypical()
    {
        // We cannot use method from EuSupport to test if typical options are priced
        // correctly, because EuSupport actually uses BSModel for checking prices.
        // Hence expected values were calculated with standalone R program.
        eos.setModelParams( new SimpleModelParams(100, 0.2, 0.05) );
        double val = eos.price( new VanillaOptionParams(100, 1, CALL) ); 
        assertTrue( doublesEqual(10.45058, val, 1e-3) );
        
        eos.setModelParams( new SimpleModelParams(100, 0.4, 0.07) );
        val = eos.price( new VanillaOptionParams(120, 2.5, CALL) ); 
        assertTrue( doublesEqual(24.54241, val, 1e-3) );
    }

    public void testEuPutTypical()
    {
        eos.setModelParams( new SimpleModelParams(100, 0.2, 0.05) );
        double val = eos.price( new VanillaOptionParams(100, 1, PUT) ); 
        assertTrue( doublesEqual(5.573526, val, 1e-3) );
        
        eos.setModelParams( new SimpleModelParams(100, 0.4, 0.07) );
        val = eos.price( new VanillaOptionParams(120, 2.5, PUT) ); 
        assertTrue( doublesEqual(25.27725, val, 1e-3) );
    }
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        model = new BSModel();
        eos = new EuOptionSupport();
        eos.setUp();
    }

    private EuOptionSupport eos;
    private BSModel model;
}
