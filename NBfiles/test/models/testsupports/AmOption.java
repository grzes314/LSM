
package models.testsupports;

import junit.framework.TestCase;
import static utils.Common.*;
import static java.lang.Math.exp;
import models.BSModel;
import static models.testsupports.VanillaOptionParams.AmOrEu.*;
import static models.testsupports.VanillaOptionParams.CallOrPut.*;

/**
 * Class provides functions useful for testing results of pricing
 * American vanilla options.
 * @author Grzegorz Los
 */
public abstract class AmOption extends TestCase
{    
    public AmOption()
    {
        bs = new BSModel();
    }
    
    public void callExtremeSpot()
    {
        // strike and time fixed, spot prices has extreme values
        setModelParams( new SimpleModelParams(0, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(100, 2, CALL, AM) ); // price = 0
        assertEquals(0, val, 1e-3);
        
        setModelParams( new SimpleModelParams(10000, 0.3, 0.1) );
        val = price( new VanillaOptionParams(100, 2, CALL, AM) ); // price = S_0 - K*exp(-rT)
        assertTrue( doublesEqual(10000 - 100*exp(-0.1*2), val) );
    }
    
    public void callExtremeStrike()
    {
        // spot price and time fixed, stike has extreme values
        setModelParams( new SimpleModelParams(100, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(0, 2, CALL, AM) ); // price = S_0
        assertTrue( doublesEqual(100, val, 1e-3) );
        
        val = price( new VanillaOptionParams(10000, 2, CALL, AM) ); // price = 0
        assertEquals(0, val, 1e-3);
    }
        
    public void callExtremeTime()
    {
        //spot price and strike fixed, time take extreme values
        double val = price( new VanillaOptionParams(90, 0, CALL, AM) ); // price = 10
        assertTrue( doublesEqual(10, val, 1e-3) );
        
        val = price( new VanillaOptionParams(90, 1000, CALL, AM) ); // price = S_0
        assertTrue( doublesEqual(100, val, 1e-2) );
    }
    
    public void callTypical()
    {
        double[] spot = {80, 100, 120};
        double[] r = {0.07, 0.04, 0.11};
        double[] vol = {0.4, 0.6, 0.5};
        double[] strike = {70, 110, 100};
        double[] T = {2.5, 0.5, 1.5};
        for (int i = 0; i < 3; ++i)
        {
            setModelParams( new SimpleModelParams(spot[i], vol[i], r[i]) );
            bs.setS(spot[i]);
            bs.setR(r[i]);
            bs.setVol(vol[i]);
            double euPrice = bs.priceCall(strike[i], T[i]);
            double amPrice = price( new VanillaOptionParams(strike[i], T[i], CALL, AM) ); 
            assertTrue( doublesEqual(euPrice, amPrice, 1e-3) );            
        }
    }   
    
    public void putExtremeSpot()
    {
        // strike and time fixed, spot prices has extreme values
        setModelParams( new SimpleModelParams(0, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(100, 2, PUT, AM) ); // price = 100, immediete exercise
        assertTrue( doublesEqual(100, val, 1e-3) );
        
        setModelParams( new SimpleModelParams(10000, 0.3, 0.1) );
        val = price( new VanillaOptionParams(100, 2, PUT, AM) ); // price = 0
        assertEquals(0, val, 1e-3);    
    }    
    
    public void putExtremeStrike()
    {
        // spot price and time fixed, stike has extreme values
        setModelParams( new SimpleModelParams(100, 0.3, 0.1) );
        double val = price( new VanillaOptionParams(0, 2, PUT, AM) ); // price = 0
        assertEquals(0, val, 1e-3);
        
        val = price( new VanillaOptionParams(10000, 2, PUT, AM) ); // price = 9900, immediete exercise
        assertTrue( doublesEqual(9900, val, 1e-3) );     
    }   
    
    public void putExtremeTime()
    {
        //spot price and strike fixed, time take extreme values
        double val = price( new VanillaOptionParams(110, 0, PUT, AM) ); // price = 10
        assertTrue( doublesEqual(10, val, 1e-3) );
        
        val = price( new VanillaOptionParams(110, 10000, PUT, AM) ); // price = 0
        assertEquals(0, val, 1e-3);        
    }
        
    public void putTypical()
    {
        double[] spot = {80, 100, 120};
        double[] r = {0.07, 0.04, 0.11};
        double[] vol = {0.6, 0.4, 0.5};
        double[] strike = {90, 110, 100};
        double[] T = {2.5, 0.5, 1.5};
        for (int i = 0; i < 3; ++i)
        {
            setModelParams( new SimpleModelParams(spot[i], vol[i], r[i]) );
            bs.setS(spot[i]);
            bs.setR(r[i]);
            bs.setVol(vol[i]);
            double euPrice = bs.pricePut(strike[i], T[i]);
            double amPrice = price( new VanillaOptionParams(strike[i], T[i], PUT, AM) ); 
            assertTrue( amPrice > euPrice - 0.001 );            
        }
    }
    
    protected abstract void setModelParams(SimpleModelParams smp);
    
    protected abstract double price(VanillaOptionParams vop);
    
    private BSModel bs;
}
