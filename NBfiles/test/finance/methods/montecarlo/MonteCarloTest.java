
package finance.methods.montecarlo;

import finance.instruments.Barrier;
import finance.instruments.EuExercise;
import finance.instruments.Instr;
import finance.instruments.Option;
import finance.parameters.BarrierParams;
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
            Instr instr = new EuExercise(
                    new Option(vop, SimpleModelParams.onlyAsset) );
            int K = 1;
            int N = 100000;
            method.setK(K);
            method.setN(N);
            return method.price(instr);
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
            Instr instr = new EuExercise(
                    new Option(vop, SimpleModelParams.onlyAsset) );
            instr = new Barrier(bp, SimpleModelParams.onlyAsset, instr);
            int K = 100;
            int N = 10000;
            method.setK(K);
            method.setN(N);
            return method.price(instr);
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
}
