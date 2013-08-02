
package finance.methods.testsupports;

import finance.methods.BlackScholes;
import static finance.methods.testsupports.BarrierParams.Type.*;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import finance.parameters.VanillaOptionParams.CallOrPut;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static finance.parameters.VanillaOptionParams.CallOrPut.PUT;
import static junit.framework.Assert.assertTrue;
import static math.utils.Numerics.doublesEqual;




/**
 * Class provides functions useful for testing results of pricing
 * European barrier options.
 * @author Grzegorz Los
 */
public abstract class EuBarrier
{ 
    public void inAndOutParity()
    {
        TestCaseData[] tcd = typicalTestCasesIn; // shorter name        
        for (int i = 0; i < tcd.length; ++i)
        {
            setModelParams(tcd[i].smp);
            double fst = price(tcd[i].vop, tcd[i].barrier);
            double snd = price(tcd[i].vop, parityBarrier(tcd[i].barrier));
            double vanillaPrice = bs.price(tcd[i].vop);
            assertTrue( doublesEqual(vanillaPrice, fst + snd, 1e-3) );
        }
    }
    
    public void typicalCases()
    {
        typicalCases(typicalTestCasesIn);
        typicalCases(typicalTestCasesOut);
    }
    
    public void setUp()
    {
        makeTestCases();
    }
    
    protected abstract void setModelParams(SimpleModelParams smp);
    
    protected abstract double price(VanillaOptionParams vop, BarrierParams bp);
       
    private void makeTestCases()
    {
        typicalTestCasesIn = new TestCaseData[12];
        typicalTestCasesOut = new TestCaseData[12];
        double[] spot = {80, 100, 120};
        double[] r = {0.07, 0.04, 0.11};
        double[] vol = {0.5, 0.4, 0.2};
        double[] strike = {80, 100, 120};
        double[] T = {2.5, 0.5, 1.5};
        double[] barrierDown = {50, 75, 100};
        double[] barrierUp = {110, 125, 140};
        int next = 0;
        for (int i = 0; i < 3; ++i)
        {
            SimpleModelParams smp = new SimpleModelParams(spot[i], vol[i], r[i]);
            for (int j = 0; j < 2; ++j)
            {
                CallOrPut cp = (i == 0 ? CALL : PUT);
                VanillaOptionParams vop = new VanillaOptionParams(strike[i], T[i], cp);
                for (int k = 0; k < 2; ++k)
                {
                    BarrierParams bpi = (k == 0 ? new BarrierParams(UAI, barrierUp[i])
                                                : new BarrierParams(DAI, barrierDown[i]));
                    BarrierParams bpo = parityBarrier(bpi);
                    typicalTestCasesIn[next] = new TestCaseData(smp, vop, bpi);
                    typicalTestCasesOut[next++] = new TestCaseData(smp, vop, bpo);
                }
            }
        }
    }
    
    private BarrierParams parityBarrier(BarrierParams barrier)
    {
        switch (barrier.type)
        {
            case UAI:
                return new BarrierParams(UAO, barrier.level);
            case UAO:
                return new BarrierParams(UAI, barrier.level);
            case DAI:
                return new BarrierParams(DAO, barrier.level);
            case DAO:
                return new BarrierParams(DAI, barrier.level);
            default:
                throw new RuntimeException("Impossible!");
        }
    }
    
    private void typicalCases(TestCaseData[] typicalTestCases)
    {
        for (int i = 0; i < typicalTestCases.length; ++i)
        {
            TestCaseData tcd = typicalTestCases[i];
            setModelParams(tcd.smp);
            double barrierPrice = price(tcd.vop, tcd.barrier);
            double vanillaPrice = bs.price(tcd.vop);
            assertTrue(vanillaPrice > barrierPrice - 0.001);
            assertTrue(barrierPrice > 0);
        }
    }
    
    private TestCaseData[] typicalTestCasesIn, typicalTestCasesOut ;
    private BlackScholes bs;
}
class TestCaseData
{
    public TestCaseData(SimpleModelParams smp, VanillaOptionParams vop,
            BarrierParams barrier)
    {
        this.smp = smp;
        this.vop = vop;
        this.barrier = barrier;
    }
    final SimpleModelParams smp;
    final VanillaOptionParams vop;
    final BarrierParams barrier;
}