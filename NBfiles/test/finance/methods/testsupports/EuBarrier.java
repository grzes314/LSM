
package finance.methods.testsupports;

import finance.methods.blackscholes.BlackScholes;
import finance.parameters.BarrierParams;
import static finance.parameters.BarrierParams.Type.*;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import finance.parameters.VanillaOptionParams.CallOrPut;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static finance.parameters.VanillaOptionParams.CallOrPut.PUT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import math.utils.Numerics;
import static math.utils.Numerics.doublesEqual;





/**
 * Class provides functions useful for testing results of pricing
 * European barrier options.
 * @author Grzegorz Los
 */
public abstract class EuBarrier
{

    public EuBarrier()
    {
    }

    public EuBarrier(double relativeDelta)
    {
        this.relativeDelta = relativeDelta;
    }
    
    
    public void setUp()
    {
        bs = new BlackScholes();
        makeTestCases();
    }
    
    public void testInAndOutParity()
    {  
        for (int i = 0; i < tcd.length; ++i)
        {
            setModelParams(tcd[i].smp);
            double fst = price(tcd[i].vop, tcd[i].barrier);
            double snd = price(tcd[i].vop, parityBarrier(tcd[i].barrier));
            double vanillaPrice = getBSPrice(tcd[i].smp, tcd[i].vop);
            assertTrue( doublesEqual(vanillaPrice, fst + snd, relativeDelta) );
        }
    }
    
    public void testInequalities()
    {
        for (int i = 0; i < tcd.length; ++i)
        {
            setModelParams(tcd[i].smp);
            double barrierPrice = price(tcd[i].vop, tcd[i].barrier);
            double vanillaPrice = getBSPrice(tcd[i].smp, tcd[i].vop);
            assertTrue(vanillaPrice > barrierPrice - 0.001);
            assertTrue(barrierPrice > 0);
        }
    }
    
    public void testTrivialBarrier()
    {
        for (int i = 0; i < tcd.length; ++i)
        {
            setModelParams(tcd[i].smp);
            BarrierParams bp = makeBarrierTrivial(tcd[i].barrier);
            double barrierPrice = price(tcd[i].vop, bp);
            double vanillaPrice = getBSPrice(tcd[i].smp, tcd[i].vop);
            assertTrue( Numerics.doublesEqual(vanillaPrice, barrierPrice, relativeDelta) );
        }
    }
    
    public void testImpossibleBarrier()
    {
        for (int i = 0; i < tcd.length; ++i)
        {
            setModelParams(tcd[i].smp);
            BarrierParams bp = makeBarrierImpossible(tcd[i].barrier);
            double barrierPrice = price(tcd[i].vop, bp);
            assertEquals(0, barrierPrice, 0.001);
        }
    }
    
    protected abstract void setModelParams(SimpleModelParams smp);
    
    protected abstract double price(VanillaOptionParams vop, BarrierParams bp);
       
    private void makeTestCases()
    {
        tcd = new TestCaseData[4];
        double[] spot = {100, 100, 100, 100};
        double[] r = {0.05, 0.07, 0.04, 0.11};
        double[] vol = {0.2, 0.3, 0.4, 0.2};
        double[] strike = {100, 110, 100, 110};
        double[] T = {1.0, 2.5, 0.5, 1.5};
        CallOrPut[] callOrPut = { CALL, CALL, PUT, PUT };
        BarrierParams[] barriers = {new BarrierParams(UAI, 120), 
                                    new BarrierParams(DAI, 80), 
                                    new BarrierParams(UAO, 115), 
                                    new BarrierParams(DAO, 90) };
        
        for (int i = 0; i < 4; ++i)
        {
            SimpleModelParams smp = new SimpleModelParams(spot[i], vol[i], r[i]);
            VanillaOptionParams vop = new VanillaOptionParams(strike[i], T[i], callOrPut[i]);
            tcd[i] = new TestCaseData(smp, vop, barriers[i]);
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
    
    private BarrierParams makeBarrierTrivial(BarrierParams barrier)
    {
        switch (barrier.type)
        {
            case UAI:
                return new BarrierParams(UAI, 0);
            case UAO:
                return new BarrierParams(UAO, 1000000);
            case DAI:
                return new BarrierParams(DAI, 1000000);
            case DAO:
                return new BarrierParams(DAO, 0);
            default:
                throw new RuntimeException("Impossible!");
        }
    }
        
    private BarrierParams makeBarrierImpossible(BarrierParams barrier)
    {
        switch (barrier.type)
        {
            case UAI:
                return new BarrierParams(UAI, 1000000);
            case UAO:
                return new BarrierParams(UAO, 0);
            case DAI:
                return new BarrierParams(DAI, 0);
            case DAO:
                return new BarrierParams(DAO, 1000000);
            default:
                throw new RuntimeException("Impossible!");
        }
    }
    
    private double getBSPrice(SimpleModelParams smp, VanillaOptionParams vop)
    {
        bs.setParams(smp);        
        return bs.price(vop);
    }
    
    private TestCaseData[] tcd;
    private BlackScholes bs;
    private double relativeDelta = 0.01;
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