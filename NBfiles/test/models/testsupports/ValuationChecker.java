
package models.testsupports;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import models.SimpleModelParams;
import models.VanillaOptionParams;

/**
 *
 * @author Grzegorz Los
 */
public abstract class ValuationChecker
{
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
        assertEquals(expectedPrice, val, 1e-2);          
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
}
