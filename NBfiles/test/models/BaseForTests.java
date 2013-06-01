
package models;

import static java.lang.Math.exp;
import junit.framework.TestCase;
import static utils.Common.doublesEqual;

/**
 *
 * @author Grzegorz Los
 */
public abstract class BaseForTests extends TestCase
{
    public BaseForTests()
    {
        bs = new BSModel();
    }
    
    protected void obligation()
    {
        setModelParams(0, 0.3, 0.1);
        double T = 0;
        for (int i = 0; i < 5; ++i)
        {
            double val = priceObl(T);
            assertTrue( doublesEqual(exp(-0.1 * T), val, 1e-3) );  
        }
    }
    
    protected void callExtremeSpot(int amEu)
    {
        // strike and time fixed, spot prices has extreme values
        setModelParams(0, 0.3, 0.1);
        double val = price(100, 2, CALL, amEu); // price = 0
        assertEquals(0, val, 1e-3);
        
        setModelParams(10000, 0.3, 0.1);
        val = price(100, 2, CALL, amEu); // price = S_0 - K*exp(-rT)
        assertTrue( doublesEqual(10000 - 100*exp(-0.1*2), val) );
    }
    
    protected void callExtremeStrike(int amEu)
    {
        // spot price and time fixed, stike has extreme values
        setModelParams(100, 0.3, 0.1);
        double val = price(0, 2, CALL, amEu); // price = S_0
        assertTrue( doublesEqual(100, val, 1e-3) );
        
        val = price(10000, 2, CALL, amEu); // price = 0
        assertEquals(0, val, 1e-3);
    }
        
    protected void callExtremeTime(int amEu)
    {
        //spot price and strike fixed, time take extreme values
        double val = price(90, 0, CALL, amEu); // price = 10
        assertTrue( doublesEqual(10, val, 1e-3) );
        
        val = price(90, 1000, CALL, amEu); // price = S_0
        assertTrue( doublesEqual(100, val, 1e-2) );
    }
    
    protected void euCallTypical()
    {
        // Expected values calculated with R program
        setModelParams(100, 0.2, 0.05);
        double val = price(100, 1, CALL, EU); 
        assertTrue( doublesEqual(10.45058, val, 1e-3) );
        
        setModelParams(100, 0.4, 0.07);
        val = price(120, 2.5, CALL, EU); 
        assertTrue( doublesEqual(24.54241, val, 1e-3) );
    }
    
    protected void euPutExtremeSpot()
    {
        // strike and time fixed, spot prices has extreme values
        setModelParams(0, 0.3, 0.1);
        double val = price(100, 2, PUT, EU); // price = K * exp(-rT)
        assertTrue( doublesEqual(100*exp(-0.1*2), val, 1e-3) );
        
        setModelParams(10000, 0.3, 0.1);
        val = price(100, 2, PUT, EU); // price = 0
        assertEquals(0, val, 1e-3);
    }       
    
    protected void euPutExtremeStrike()
    {
        // spot price and time fixed, stike has extreme values
        setModelParams(100, 0.3, 0.1);
        double val = price(0, 2, PUT, EU); // price = 0
        assertEquals(0, val, 1e-3);
        
        val = price(10000, 2, PUT, EU); // price = - S_0 + K*exp(-rT)
        assertTrue( doublesEqual(-100 + 10000*exp(-0.1*2), val, 1e-3) );
    }       
    protected void euPutExtremeTime()
    {
        //spot price and strike fixed, time take extreme values
        double val = price(110, 0, PUT, EU); // price = 10
        assertTrue( doublesEqual(10, val, 1e-3) );
        
        val = price(110, 1000, PUT, EU); // price = 0
        assertEquals(0, val, 1e-3);
    }   

    protected void euPutTypical()
    {
        // Expected values calculated with R program
        setModelParams(100, 0.2, 0.05);
        double val = price(100, 1, PUT, EU); 
        assertTrue( doublesEqual(5.573526, val, 1e-3) );
        
        setModelParams(100, 0.4, 0.07);
        val = price(120, 2.5, PUT, EU); 
        assertTrue( doublesEqual(25.27725, val, 1e-3) );
    }
    
    protected void euBarrier()
    {
        // some data sets
        double[] spot = {80, 100, 120};
        double[] r = {0.07, 0.04, 0.11};
        double[] vol = {0.6, 0.4, 0.5};
        double[] strike = {80, 100, 120};
        double[] T = {2.5, 0.5, 1.5};
        double[] barrierDown = {50, 75, 100};
        double[] barrierUp = {110, 125, 140};
        
        for (int i = 0; i < 3; ++i)
        {
            setModelParams(spot[i], vol[i], r[i]);
            bs.setS(spot[i]);
            bs.setR(r[i]);
            bs.setVol(vol[i]);
            double without = bs.priceCall(strike[i], T[i]);
            double downIn  = priceBarrier(strike[i], T[i], CALL, EU, barrierDown[i], DAI);
            double downOut = priceBarrier(strike[i], T[i], CALL, EU, barrierDown[i], DAO);
            double upIn    = priceBarrier(strike[i], T[i], CALL, EU, barrierUp[i], UAI);
            double upOut   = priceBarrier(strike[i], T[i], CALL, EU, barrierUp[i], UAO);
            
            assertTrue( doublesEqual(without, downIn + downOut, 1e-3) );
            assertTrue( doublesEqual(without, upIn + upOut, 1e-3) );
            
            without = bs.pricePut(strike[i], T[i]);
            downIn  = priceBarrier(strike[i], T[i], PUT, EU, barrierDown[i], DAI);
            downOut = priceBarrier(strike[i], T[i], PUT, EU, barrierDown[i], DAO);
            upIn    = priceBarrier(strike[i], T[i], PUT, EU, barrierUp[i], UAI);
            upOut   = priceBarrier(strike[i], T[i], PUT, EU, barrierUp[i], UAO);
            
            assertTrue( doublesEqual(without, downIn + downOut, 1e-3) );
            assertTrue( doublesEqual(without, upIn + upOut, 1e-3) );
        }        
    }
    
    protected void amCallTypical()
    {
        double[] spot = {80, 100, 120};
        double[] r = {0.07, 0.04, 0.11};
        double[] vol = {0.4, 0.6, 0.5};
        double[] strike = {70, 110, 100};
        double[] T = {2.5, 0.5, 1.5};
        for (int i = 0; i < 3; ++i)
        {
            setModelParams(spot[i], vol[i], r[i]);
            bs.setS(spot[i]);
            bs.setR(r[i]);
            bs.setVol(vol[i]);
            double euPrice = bs.priceCall(strike[i], T[i]);
            double amPrice = price(strike[i], T[i], CALL, AM); 
            assertTrue( doublesEqual(euPrice, amPrice, 1e-3) );            
        }
    }   
    
    protected void amPutExtremeSpot()
    {
        // strike and time fixed, spot prices has extreme values
        setModelParams(0, 0.3, 0.1);
        double val = price(100, 2, PUT, AM); // price = 100, immediete exercise
        assertTrue( doublesEqual(100, val, 1e-3) );
        
        setModelParams(10000, 0.3, 0.1);
        val = price(100, 2, PUT, AM); // price = 0
        assertEquals(0, val, 1e-3);    
    }    
    
    protected void amPutExtremeStrike()
    {
        // spot price and time fixed, stike has extreme values
        setModelParams(100, 0.3, 0.1);
        double val = price(0, 2, PUT, AM); // price = 0
        assertEquals(0, val, 1e-3);
        
        val = price(10000, 2, PUT, AM); // price = 9900, immediete exercise
        assertTrue( doublesEqual(9900, val, 1e-3) );     
    }   
    
    protected void amPutExtremeTime()
    {
        //spot price and strike fixed, time take extreme values
        double val = price(110, 0, PUT, AM); // price = 10
        assertTrue( doublesEqual(10, val, 1e-3) );
        
        val = price(110, 10000, PUT, AM); // price = 0
        assertEquals(0, val, 1e-3);        
    }
        
    protected void amPutTypical()
    {
        double[] spot = {80, 100, 120};
        double[] r = {0.07, 0.04, 0.11};
        double[] vol = {0.6, 0.4, 0.5};
        double[] strike = {90, 110, 100};
        double[] T = {2.5, 0.5, 1.5};
        for (int i = 0; i < 3; ++i)
        {
            setModelParams(spot[i], vol[i], r[i]);
            bs.setS(spot[i]);
            bs.setR(r[i]);
            bs.setVol(vol[i]);
            double euPrice = bs.pricePut(strike[i], T[i]);
            double amPrice = price(strike[i], T[i], PUT, AM); 
            assertTrue( amPrice > euPrice - 0.001 );            
        }
    }  
    
    protected final int CALL = 0, PUT = 1, AM = 2, EU = 3,
            UAI = 4, UAO = 5, DAI = 6, DAO = 7;
    
    protected abstract void setModelParams(double S, double vol, double r);
    
    protected abstract double price(double strike, double T,
            int callPut, int amEu);
    
    protected abstract double priceBarrier(double strike, double T,
            int callPut, int amEu, double barrier, int barType);
    
    protected abstract double priceObl(double T);
        
    BSModel bs;
}
