
package models.testsupports;

import static java.lang.Math.exp;
import junit.framework.TestCase;
import static models.testsupports.VanillaOptionParams.CallOrPut.CALL;
import static models.testsupports.VanillaOptionParams.CallOrPut.PUT;
import static utils.Common.doublesEqual;
import static utils.Common.doublesEqual;

/**
 * Class provides functions useful for testing results of pricing
 * European vanilla options.
 * @author Grzegorz Los
 */
public abstract class EuOption extends TestCase
{    
    public void callExtremeSpot()
    {
        // strike and time fixed, spot price has extreme values
        setModelParams( new SimpleModelParams(0, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(100, 2, CALL) );
        assertEquals(0, val, 1e-3); // price = 0
        
        setModelParams( new SimpleModelParams(10000, 0.3, 0.1) );
        val = price( new VanillaOptionParams(100, 2, CALL) );
        assertTrue( doublesEqual(10000 - 100*exp(-0.1*2), val) ); // price = S_0 - K*exp(-rT)
    }
    
    public void callExtremeStrike()
    {
        // spot price and time fixed, stike has extreme values
        setModelParams( new SimpleModelParams(100, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(0, 2, CALL) );
        assertTrue( doublesEqual(100, val, 1e-3) ); // price = S_0
        
        val = price( new VanillaOptionParams(10000, 2, CALL) );
        assertEquals(0, val, 1e-3); // price = 0
    }
        
    public void callExtremeTime()
    {
        //spot price and strike fixed, time take extreme values
        setModelParams( new SimpleModelParams(100, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(90, 0, CALL) );
        assertTrue( doublesEqual(10, val, 1e-3) ); // price = 10
        
        val = price( new VanillaOptionParams(90, 1000, CALL) );
        assertTrue( doublesEqual(100, val, 1e-2) ); // price = S_0
    }
    
    public void callTypical()
    {
        // Expected values calculated with R program
        setModelParams( new SimpleModelParams(100, 0.2, 0.05) );
        double val = price( new VanillaOptionParams(100, 1, CALL) ); 
        assertTrue( doublesEqual(10.45058, val, 1e-3) );
        
        setModelParams( new SimpleModelParams(100, 0.4, 0.07) );
        val = price( new VanillaOptionParams(120, 2.5, CALL) ); 
        assertTrue( doublesEqual(24.54241, val, 1e-3) );
    }
    
    public void putExtremeSpot()
    {
        // strike and time fixed, spot price has extreme values
        setModelParams( new SimpleModelParams(0, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(100, 2, PUT) );
        assertTrue( doublesEqual(100*exp(-0.1*2), val, 1e-3) ); // price = K * exp(-rT)
        
        setModelParams( new SimpleModelParams(10000, 0.3, 0.1) );
        val = price( new VanillaOptionParams(100, 2, PUT) );
        assertEquals(0, val, 1e-3); // price = 0
    }       
    
    public void putExtremeStrike()
    {
        // spot price and time fixed, stike has extreme values
        setModelParams( new SimpleModelParams(100, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(0, 2, PUT) ); 
        assertEquals(0, val, 1e-3); // price = 0
        
        val = price( new VanillaOptionParams(10000, 2, PUT) );
        assertTrue( doublesEqual(-100 + 10000*exp(-0.1*2), val, 1e-3) ); // price = - S_0 + K*exp(-rT)
    }       
    public void putExtremeTime()
    {
        //spot price and strike fixed, time take extreme values
        setModelParams( new SimpleModelParams(100, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(110, 0, PUT) );
        assertTrue( doublesEqual(10, val, 1e-3) ); // price = 10
        
        val = price( new VanillaOptionParams(110, 1000, PUT) );
        assertEquals(0, val, 1e-3); // price = 0
    }   

    public void putTypical()
    {
        // Expected values calculated with R program
        setModelParams( new SimpleModelParams(100, 0.2, 0.05) );
        double val = price( new VanillaOptionParams(100, 1, PUT) ); 
        assertTrue( doublesEqual(5.573526, val, 1e-3) );
        
        setModelParams( new SimpleModelParams(100, 0.4, 0.07) );
        val = price( new VanillaOptionParams(120, 2.5, PUT) ); 
        assertTrue( doublesEqual(25.27725, val, 1e-3) );
    }
    
    protected abstract void setModelParams(SimpleModelParams smp);
    
    protected abstract double price(VanillaOptionParams vop);
}
