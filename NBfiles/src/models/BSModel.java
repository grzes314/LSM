
package models;

import instruments.*;
import static java.lang.Math.*;

/**
 *
 * @author grzes
 */
public class BSModel implements Model
{
    public BSModel(double S, double vol, double r)
    {
        this.S = S;
        this.vol = vol;
        this.r = r;
    }
    
    @Override
    public PriceInfo price(Instr instr) throws WrongInstrException
    {
        EuOption opt;
        try {
            opt = (EuOption) instr;
        } catch (ClassCastException ex) {
            throw new WrongInstrException();
        }
        return price(opt);
    }
    
    private PriceInfo price(EuOption opt)
    {
        double K = opt.getStrike();
        double T = opt.getTimeHorizon();
        double d1 = (log(S/K) + (r + vol*vol/2)*T) / (vol * sqrt(T));
        double d2 = d1 - vol * sqrt(T);
        if (opt.getType() == Option.CALL) return priceCall(K, T, d1, d2);
        else return pricePut(K, T, d1, d2);
    }

    public double getS()
    {
        return S;
    }

    public void setS(double S)
    {
        this.S = S;
    }

    public double getR()
    {
        return r;
    }

    public void setR(double r)
    {
        this.r = r;
    }

    public double getVol()
    {
        return vol;
    }

    public void setVol(double vol)
    {
        this.vol = vol;
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
    
    private double S;
    private double vol;
    private double r;
    RandomTools rt = new RandomTools();

}
