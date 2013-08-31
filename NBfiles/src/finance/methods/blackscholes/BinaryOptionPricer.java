
package finance.methods.blackscholes;

import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import static java.lang.Math.*;
import math.utils.Statistics;

/**
 *
 * @author Grzegorz Los
 */
public class BinaryOptionPricer
{
    public BinaryOptionPricer(SimpleModelParams smp)
    {
        S = smp.S;
        vol = smp.vol;
        r = smp.r;
    }

    public BinaryOptionPricer()
    {
        S = vol = r = 0;
    }

    public void setParams(SimpleModelParams smp)
    {
        S = smp.S;
        vol = smp.vol;
        r = smp.r;
    }
    
    public double price(VanillaOptionParams vop)
    {
        if (vop.callOrPut == VanillaOptionParams.CallOrPut.CALL)
            return priceCall(vop.strike, vop.T);
        else
            return pricePut(vop.strike, vop.T);
    }

    private double priceCall(double E, double T)
    {
        double d2 = (log(S/E) + (r - vol*vol/2)*T) / (vol * sqrt(T));
        return exp(-r*T) * Statistics.cndf(d2);
    }

    private double pricePut(double E, double T)
    {
        double d2 = (log(S/E) + (r - vol*vol/2)*T) / (vol * sqrt(T));
        return exp(-r*T) * Statistics.cndf(-d2);
    }

    
    private double S;
    private double vol;
    private double r;
}
