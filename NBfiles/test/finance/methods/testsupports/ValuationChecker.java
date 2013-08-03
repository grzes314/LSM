
package finance.methods.testsupports;

import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import static junit.framework.Assert.assertTrue;
import math.utils.Numerics;

/**
 *
 * @author Grzegorz Los
 */
public abstract class ValuationChecker
{

    public ValuationChecker()
    {
    }

    public ValuationChecker(double relativeDelta)
    {
        this.relativeDelta = relativeDelta;
    }
    
    public void setUp()
    {
        makeTypicalModelParams();
        makeTypicalOptionParams();
    }
    
    protected static abstract class Checker
    {
        public abstract void check(SimpleModelParams smp, VanillaOptionParams vop);
    }
    
    protected void checkCalls(Checker c)
    {
        for (int i = 0; i < simpleModelParams.length; ++i)
        {
            for (int j = 0; j < calls.length; ++j)
            {
                c.check(simpleModelParams[i], calls[j]);
            }
        }        
    }
        
    protected void checkPuts(Checker c)
    {
        for (int i = 0; i < simpleModelParams.length; ++i)
        {
            for (int j = 0; j < puts.length; ++j)
            {
                c.check(simpleModelParams[i], puts[j]);
            }
        }        
    }    
    
    protected void checkPrice(SimpleModelParams smp,
            VanillaOptionParams vop, double expectedPrice)
    {
        setModelParams( smp );
        double val = price( vop );
        assertTrue( Numerics.doublesEqual(expectedPrice, val, relativeDelta) );          
    }
           
    protected void checkPriceIsGreater(SimpleModelParams smp,
            VanillaOptionParams vop, double minimalPrice, double delta)
    {
        setModelParams( smp );
        double val = price( vop );
        assertTrue(val > minimalPrice - delta);          
    }
                  
    protected void checkPriceIsGreater(SimpleModelParams smp,
            VanillaOptionParams vop, double minimalPrice)
    {
        checkPriceIsGreater(smp, vop, minimalPrice, 0);
    }
    
    protected void checkPriceIsLower(SimpleModelParams smp,
            VanillaOptionParams vop, double minimalPrice, double delta)
    {
        setModelParams( smp );
        double val = price( vop );
        assertTrue(val < minimalPrice + delta);          
    }
    
    protected void checkPriceIsLower(SimpleModelParams smp,
            VanillaOptionParams vop, double minimalPrice)
    {
        checkPriceIsLower(smp, vop, minimalPrice, 0);
    }
    
    protected abstract void setModelParams(SimpleModelParams smp);
    
    protected abstract double price(VanillaOptionParams vop);
    
    protected abstract void makeTypicalModelParams();
    
    protected abstract void makeTypicalOptionParams();
    
    protected SimpleModelParams[] simpleModelParams;
    protected VanillaOptionParams[] puts, calls;
    private double relativeDelta = 0.001;
}
