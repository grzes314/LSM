
package models;

import junit.framework.TestCase;
import models.testsupports.SimpleModelParams;
import models.testsupports.VanillaOptionParams;
import static models.testsupports.VanillaOptionParams.CallOrPut.*;
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
            model.setS(smp.S);
            model.setR(smp.r);
            model.setVol(smp.vol);
        }

        @Override
        protected double price(VanillaOptionParams sop)
        {
            if (sop.callOrPut == CALL)
                return model.priceCall(sop.strike, sop.T);
            else
                return model.pricePut(sop.strike, sop.T);
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

    public void testEuCallTypical() {
        eos.callTypical();
    }

    public void testEuPutTypical() {
        eos.putTypical();
    }
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        model = new BSModel();
        eos = new EuOptionSupport();
    }

    private EuOptionSupport eos;
    private BSModel model;
}
