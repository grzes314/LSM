
package models;

import junit.framework.TestCase;
import models.testsupports.SimpleModelParams;
import models.testsupports.VanillaOptionParams;
import static models.testsupports.VanillaOptionParams.CallOrPut.CALL;

/**
 *
 * @author Grzegorz Los
 */
public class FDModelTest extends TestCase
{
    class EuOptionSupport extends models.testsupports.EuOption
    {    
        @Override
        protected void setModelParams(SimpleModelParams smp)
        {
            model = new FDModel(smp.S, smp.vol, smp.r);
        }

        @Override
        protected double price(VanillaOptionParams vop)
        {
            double vol = model.getVol();
            int KI = 100000000;
            int I = (int) Math.pow(0.9*KI / (vop.T*vol*vol), 1/3);
            int K = KI / I;
            return model.price(vop.strike, vop.T, I, K, (vop.callOrPut == CALL), false);
        }     
    }
    
    class AmOptionSupport extends models.testsupports.AmOption
    {    
        @Override
        protected void setModelParams(SimpleModelParams smp)
        {
            model = new FDModel(smp.S, smp.vol, smp.r);
        }

        @Override
        protected double price(VanillaOptionParams vop)
        {
            double vol = model.getVol();
            int KI = 100000000;
            int I = (int) Math.pow(0.9*KI / (vop.T*vol*vol), 1/3);
            int K = KI / I;
            return model.price(vop.strike, vop.T, I, K, (vop.callOrPut == CALL), true);
        }   
    }
          
    public FDModelTest()
    {
        eos = new EuOptionSupport();
        aos = new AmOptionSupport();
    }
    
    public void testEuCallExtremes()
    {
        eos.callExtremeSpot();
        eos.callExtremeStrike();
    }
    
    public void testEuCallTypical()
    {
        eos.callTypical();
    }
    
    public void testEuPutExtremes()
    {
        eos.putExtremeSpot();
        eos.putExtremeStrike();
    }
    
    public void testEuPutTypical()
    {
        eos.putTypical();
    }
        
    public void testAmCallExtremes()
    {
        aos.callExtremeSpot();
        aos.callExtremeStrike();
    }
    
    public void testAmCallTypical()
    {
        aos.callTypical();
    }

    public void testAmPutExtremes()
    {
        aos.putExtremeSpot();
        aos.putExtremeStrike();
    }
    
    public void testAmPutTypical()
    {
        aos.putTypical();
    }

    private FDModel model;
    private EuOptionSupport eos;
    private AmOptionSupport aos;
}
