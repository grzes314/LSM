
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.methods.testsupports.BarrierParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import junit.framework.TestCase;

/**
 *
 * @author Grzegorz Los
 */
public abstract class MonteCarloTest extends TestCase
{
    class EuOptionSupport extends finance.methods.testsupports.EuOption
    {
        public EuOptionSupport()
        {
            super(0.05);
        }
        
        @Override
        protected void setModelParams(SimpleModelParams smp)
        {
            makeMethod(smp);
        }

        @Override
        protected double price(VanillaOptionParams vop)
        {
            Instr instr = converter.makeOption(vop, SimpleModelParams.onlyAsset);
            int K = 1;
            int N = 100000;
            return method.price(instr, N, K).result;
        }       
    }
    
    class EuBarrierSupport extends finance.methods.testsupports.EuBarrier
    {

        public EuBarrierSupport()
        {
            super(0.05);
        }
        
        @Override
        protected void setModelParams(SimpleModelParams smp)
        {
            makeMethod(smp);
        }

        @Override
        protected double price(VanillaOptionParams vop, BarrierParams bp)
        {
            Instr instr = converter.makeOption(vop, SimpleModelParams.onlyAsset);
            instr = converter.addBarrier(instr, bp, SimpleModelParams.onlyAsset);
            int K = 100;
            int N = 10000;
            return method.price(instr, N, K).result;
        }        
    }
    
    abstract protected void makeMethod(SimpleModelParams smp);
    
    @Override
    public void setUp()
    {
        eos = new EuOptionSupport();
        eos.setUp();
        ebs = new EuBarrierSupport();
        ebs.setUp();
    }
    
    public void testVanillaOnSingleAssetModel()
    {
        eos.callExtremeSpot();
        eos.callExtremeStrike();
        eos.callExtremeTime();
        eos.callTypical();
        eos.putExtremeSpot();
        eos.putExtremeStrike();
        eos.putExtremeTime();
        eos.putTypical();
    }
    
    public void testBarrierOnSingleAssetModel()
    {
        ebs.testInAndOutParity();
        ebs.testInequalities();
        ebs.testTrivialBarrier();
        ebs.testImpossibleBarrier();
    }
    
    protected MonteCarlo method;
    private EuBarrierSupport ebs;
    private EuOptionSupport eos;
    Converter converter = new Converter();
}
