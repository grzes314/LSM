
package models.testsupports;

import junit.framework.TestCase;
import static utils.Common.doublesEqual;

/**
 * Class supporting tests of pricing bonds paying 1 unit after given time.
 * @author Grzegorz Los
 */
public abstract class Bond extends TestCase
{    
    public void negativeRates()
    {
        double[] rs = { -0.2, -0.1, -0.01 };
        double[] T = { 0.0, 1.0, 2.0, 5.0, 10.0 };
        check(rs, T);
    }
    
    public void typicalRates()
    {
        double[] rs = { 0, 0.01, 0.02, 0.05, 0.1 };
        double[] T = { 0.0, 1.0, 2.0, 5.0, 10.0 };
        check(rs, T);        
    }
    
    public void bigRates()
    {
        double[] rs = { 0.2, 0.5, 1.0 };
        double[] T = { 0.0, 1.0, 2.0, 5.0, 10.0 };
        check(rs, T);        
    }

    protected abstract void setModelParams(SimpleModelParams smp);
    
    protected abstract double price(double expiration); 
    
    private void check(double[] rs, double[] T)
    {
        for (int j = 0; j < rs.length; ++j)
        {
            setModelParams(new SimpleModelParams(100 /* not relevant */,
                                                 0.2 /* not relevant */,
                                                 rs[j] ));
            for (int i = 0; i < T.length; ++i)
            {
                double val = price(T[i]);
                assertTrue( doublesEqual(Math.exp(-0.1 * T[i]), val, 1e-3) );  
            }
        }
    }     
}
