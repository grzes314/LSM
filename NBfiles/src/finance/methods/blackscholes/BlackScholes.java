
package finance.methods.blackscholes;

import finance.methods.common.WrongParamException;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import static java.lang.Math.*;
import math.utils.Numerics;
import static math.utils.Statistics.cndf;

/**
 * Class for pricing options with BS Formula.
 * @author Grzegrorz Los
 */
public class BlackScholes
{
    public BlackScholes()
    {
    }

    public BlackScholes(SimpleModelParams smp)
    {
        setParams(smp);
    }
    
    public final void setParams(SimpleModelParams smp)
    {
        S = smp.S;
        vol = smp.vol;
        r = smp.r;
    }
    
    public double getS()
    {
        return S;
    }

    public double getR()
    {
        return r;
    }

    public double getVol()
    {
        return vol;
    }
    
    public double price(VanillaOptionParams vop)
    {
        if (vop.amOrEu == VanillaOptionParams.AmOrEu.AM)
            throw new WrongParamException("Black-Scholes formula "
                    + "should be used to price only European options");
            
        if (vop.callOrPut == VanillaOptionParams.CallOrPut.CALL)
            return priceCall(vop.strike, vop.T);
        else
            return pricePut(vop.strike, vop.T);
    }

    @Override
    public String toString()
    {
        return "BLACK-SCHOLES FORMULA\n" +
               "S = "+ S + "\n" +
               "vol = " + vol + "\n" +
               "r = " + r + "\n";                
    }
    
    private double priceCall(double K, double T)
    {
        if (Numerics.isZero(T))
            return Numerics.plus(S-K);
        double d1 = (log(S/K) + (r + vol*vol/2)*T) / (vol * sqrt(T));
        double d2 = d1 - vol * sqrt(T);
        return S*cndf(d1) - K*exp(-r*T)*cndf(d2) ;
    }
    
    private double pricePut(double K, double T)
    {
        if (Numerics.isZero(T))
            return Numerics.plus(K-S);
        double d1 = (log(S/K) + (r + vol*vol/2)*T) / (vol * sqrt(T));
        double d2 = d1 - vol * sqrt(T);
        return K*exp(-r*T)*cndf(-d2)- S*cndf(-d1);
    }
    
    private double S, vol, r;
}
