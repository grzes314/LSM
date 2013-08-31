package finance.methods.blackscholes;

import finance.parameters.BarrierParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static java.lang.Math.*;
import static math.utils.Statistics.cndf;


/**
 * Class for pricing barrier European options which uses formula which can be found
 * in Wilmotts book. 
 * @author Grzegorz Los
 */
public class BarrierOptionPricer
{
    public BarrierOptionPricer(SimpleModelParams smp)
    {
        S = smp.S;
        vol = smp.vol;
        r = smp.r;
    }

    public BarrierOptionPricer()
    {
        S = vol = r = 0;
    }

    void setParams(SimpleModelParams smp)
    {
        S = smp.S;
        vol = smp.vol;
        r = smp.r;
    }
    
    double price(VanillaOptionParams vop, BarrierParams bp)
    {
        extractParameters(vop, bp);
        calcAuxValues(vop, bp);
        if (barrierZeroesPriceAtTime0(bp))
            return 0;
        if (vop.callOrPut == CALL)
            return priceCall(vop, bp);
        else return pricePut(vop, bp);
    }

    private boolean barrierZeroesPriceAtTime0(BarrierParams bp)
    {
        return (bp.type == BarrierParams.Type.UAO && bp.level < S)
            || (bp.type == BarrierParams.Type.DAO && bp.level > S);
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
