
package models;

import instruments.*;
import static java.lang.Math.*;

/**
 *
 * @author grzes
 */
public class BSModel
{
    @Override
    public PriceInfo price(double S, double vol, double r)
    {
        double K = opt.getStrike();
        double T = opt.getTimeHorizon();
        double d1 = (log(S/K) + (r + vol*vol/2)*T) / (vol * sqrt(T));
        double d2 = d1 - vol * sqrt(T);
        if (opt.getType() == Option.CALL) return priceCall(K, T, d1, d2);
        else return pricePut(K, T, d1, d2);
    }


    private PriceInfo priceCall(double K, double T, double d1, double d2)
    {
        return new JustPrice(
                S*rt.cndf(d1) - K*exp(-r*T)*rt.cndf(d2) );
    }
    
    private PriceInfo pricePut(double K, double T, double d1, double d2)
    {
        return new JustPrice(
                K*exp(-r*T)*rt.cndf(-d2)- S*rt.cndf(-d1));
    }
    
    @Override
    public String desc()
    {
        return "BLACK-SCHOLES MODEL\n" +
               "S = "+ S + "\n" +
               "vol = " + vol + "\n" +
               "r = " + r + "\n";                
    }
    
    RandomTools rt = new RandomTools();
}
