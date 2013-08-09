
package finance.methods.blackscholes;

import finance.methods.common.Progress;
import finance.methods.common.ProgressObservable;
import finance.methods.common.ProgressObserver;
import finance.methods.common.WrongParamException;
import finance.parameters.BarrierParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static java.lang.Math.*;
import math.utils.Numerics;
import static math.utils.Statistics.cndf;

/**
 * Class for pricing options with BS Formula.
 * @author Grzegrorz Los
 */
public class BlackScholes implements ProgressObservable
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
    
    public double price(VanillaOptionParams vop, BarrierParams bp)
    {
        BarrierOptionPricer bop = new BarrierOptionPricer( new SimpleModelParams(S, vol, r) );
        return bop.price(vop, bp);
    }
    
    public double priceBond(double nominal, double T)
    {
        return nominal * exp(-r*T);
    }

    @Override
    public String toString()
    {
        return "BLACK-SCHOLES FORUMULA\n" +
               "S = "+ S + "\n" +
               "vol = " + vol + "\n" +
               "r = " + r + "\n";                
    }
    
    @Override
    public void addObserver(ProgressObserver ob) {}
    @Override
    public void removeObserver(ProgressObserver ob) {}
    @Override
    public void notifyObservers(Progress pr) {}
    
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

/**
 * Class for pricing barrier European options which uses formula which can be found
 * in Wilmotts book. 
 * @author Grzegorz Los
 */
class BarrierOptionPricer
{
    BarrierOptionPricer(SimpleModelParams smp)
    {
        S = smp.S;
        vol = smp.vol;
        r = smp.r;
    }
    
    double price(VanillaOptionParams vop, BarrierParams bp)
    {
        extractParameters(vop, bp);
        calcAuxValues(vop, bp);
        if (vop.callOrPut == CALL)
            return priceCall(vop, bp);
        else return pricePut(vop, bp);
    }

    private void extractParameters(VanillaOptionParams vop, BarrierParams bp)
    {
        E = vop.strike;
        T = vop.T;
        Sb = bp.level;
    }

    private void calcAuxValues(VanillaOptionParams vop, BarrierParams bp)
    {
        a = pow(Sb / S, -1 + 2*r/vol/vol);
        b = pow(Sb / S, 1 + 2*r/vol/vol);
        d1 = ( log(S/E) + (r + vol*vol/2)*T ) / (vol * sqrt(T));
        d2 = ( log(S/E) + (r - vol*vol/2)*T ) / (vol * sqrt(T));
        d3 = ( log(S/Sb) + (r + vol*vol/2)*T ) / (vol * sqrt(T));
        d4 = ( log(S/Sb) + (r - vol*vol/2)*T ) / (vol * sqrt(T));
        d5 = ( log(S/Sb) - (r - vol*vol/2)*T ) / (vol * sqrt(T));
        d6 = ( log(S/Sb) - (r + vol*vol/2)*T ) / (vol * sqrt(T));
        d7 = ( log(S*E/Sb/Sb) - (r - vol*vol/2)*T ) / (vol * sqrt(T));
        d8 = ( log(S*E/Sb/Sb) - (r + vol*vol/2)*T ) / (vol * sqrt(T));
    }
    
    static private double N(double t)
    {
        return cndf(t);
    }

    private double priceCall(VanillaOptionParams vop, BarrierParams bp)
    {
        switch (bp.type)
        {
            case UAO:
                return S * (N(d1) - N(d3) - b*(N(d6) - N(d8))) +
                        - E * exp(-r*T) * (N(d2) - N(d4) - a*(N(d5) - N(d7)));
            case UAI:
                return S * (N(d3) + b*(N(d6) - N(d8))) +
                        - E * exp(-r*T) * (N(d4) + a*(N(d5) - N(d7)));
            case DAO:
                if (E > Sb)
                    return S * (N(d1) - b*(1 - N(d8))) +
                        - E * exp(-r*T) * (N(d2) - a*(1 - N(d7)));
                else
                    return S * (N(d3) - b*(1 - N(d6))) +
                        - E * exp(-r*T) * (N(d4) - a*(1 - N(d5)));
            case DAI:
                if (E > Sb)
                    return S * b*(1 - N(d8)) +
                        - E * exp(-r*T) * a*(1 - N(d7));
                else 
                    return S * (N(d1) - N(d3) + b*(1 - N(d6))) +
                        - E * exp(-r*T) * (N(d2) - N(d4) + a*(1 - N(d5)));
            default:
                throw new RuntimeException();
        }
    }

    private double pricePut(VanillaOptionParams vop, BarrierParams bp)
    {
        switch (bp.type)
        {
            case UAO:
                if (E > Sb)
                    return - S * (1 - N(d3) - b*N(d6)) + 
                            + E * exp(-r*T) * (1 - N(d4) - a*N(d5));
                else
                    return - S * (1 - N(d1) - b*N(d8)) +
                            + E * exp(-r*T) * (1 - N(d2) - a*N(d7));
            case UAI:
                if (E > Sb)
                    return - S * (N(d3) - N(d1) + b*N(d6)) +
                            + E * exp(-r*T) * (N(d4) - N(d2) + a*N(d5));
                else
                    return - S * b * N(d8) + E * exp(-r*T) * a * N(d7);
            case DAO:
                return - S * (N(d3) - N(d1) - b*(N(d8) - N(d6))) +
                        + E * exp(-r*T) * (N(d4) - N(d2) - a*(N(d7) - N(d5)));
            case DAI:
                return - S * (1 - N(d3) + b*(N(d8) - N(d6))) +
                        + E * exp(-r*T) * (1 - N(d4) + a*(N(d7) - N(d5)));
            default:
                throw new RuntimeException();
        }
    }
    
    double S, vol, r, Sb, E, T;
    double a, b, d1, d2, d3, d4, d5, d6, d7, d8;
}
