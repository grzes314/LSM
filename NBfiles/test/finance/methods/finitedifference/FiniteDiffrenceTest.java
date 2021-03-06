
package finance.methods.finitedifference;

import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import junit.framework.TestCase;

/**
 *
 * @author Grzegorz Los
 */
public class FiniteDiffrenceTest extends TestCase
{
    class EuOptionSupport extends finance.methods.testsupports.EuOption
    {    
        @Override
        protected void setModelParams(SimpleModelParams smp)
        {
            model = new FiniteDifference(smp);
        }

        @Override
        protected double price(VanillaOptionParams vop)
        {
            double vol = model.getVol();
            int KI = 10000000;
            int I = (int) Math.pow(0.9*KI / (vop.T*vol*vol), 1./3);
            int K = KI / I;
            try {
                return model.price(vop, I, K);
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }     
    }
    
    class AmOptionSupport extends finance.methods.testsupports.AmOption
    {    
        @Override
        protected void setModelParams(SimpleModelParams smp)
        {
            model = new FiniteDifference(smp);
        }

        @Override
        protected double price(VanillaOptionParams vop)
        {
            double vol = model.getVol();
            int KI = 10000000;
            int I = (int) Math.pow(0.9*KI / (vop.T*vol*vol), 1./3);
            int K = KI / I;
            try {
                return model.price(vop, I, K);
            } catch (InterruptedException ex) {
                throw new RuntimeException();
            }
        }   
    }
          
    public FiniteDiffrenceTest()
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

    private FiniteDifference model;
    private EuOptionSupport eos;
    private AmOptionSupport aos;
}
