
package finance.models;

import finance.models.FDModel;
import finance.parameters.VanillaOptionParams;
import finance.parameters.SimpleModelParams;
import junit.framework.TestCase;

/**
 *
 * @author Grzegorz Los
 */
public class FDModelTest extends TestCase
{
    class EuOptionSupport extends finance.models.testsupports.EuOption
    {    
        @Override
        protected void setModelParams(SimpleModelParams smp)
        {
            model = new FDModel(smp);
        }

        @Override
        protected double price(VanillaOptionParams vop)
        {
            double vol = model.getVol();
            int KI = 10000000;
            int I = (int) Math.pow(0.9*KI / (vop.T*vol*vol), 1./3);
            int K = KI / I;
            return model.price(vop, I, K);
        }     
    }
    
    class AmOptionSupport extends finance.models.testsupports.AmOption
    {    
        @Override
        protected void setModelParams(SimpleModelParams smp)
        {
            model = new FDModel(smp);
        }

        @Override
        protected double price(VanillaOptionParams vop)
        {
            double vol = model.getVol();
            int KI = 10000000;
            int I = (int) Math.pow(0.9*KI / (vop.T*vol*vol), 1./3);
            int K = KI / I;
            return model.price(vop, I, K);
        }   
    }
          
    public FDModelTest()
    {
        eos = new EuOptionSupport();
        aos = new AmOptionSupport();
    }
    
    @Override
    public void setUp() throws Exception
    {
        eos.setUp();
        aos.setUp();
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
