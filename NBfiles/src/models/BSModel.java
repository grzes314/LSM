
package models;

import static java.lang.Math.*;
import utils.Common;
import utils.RandomTools;

/**
 * Class for pricing options with BS Formula.
 * @author Grzegrorz Los
 */
public class BSModel implements ProgressObservable
{
    public BSModel()
    {
    }

    public BSModel(double S, double vol, double r)
    {
        setS(S);
        setVol(vol);
        setR(r);
    }

    public BSModel(SimpleModelParams smp)
    {
        setParams(smp);
    }
    
    public final void setParams(SimpleModelParams smp)
    {
        setS(smp.S);
        setVol(smp.vol);
        setR(smp.r);      
    }
    
    public double getS()
    {
        return S;
    }

    public final void setS(double S)
    {
        if (S < 0)
            throw new WrongParamException("S = " + S);
        this.S = S;
    }

    public double getR()
    {
        return r;
    }

    public final void setR(double r)
    {
        if (r < -1)
            throw new WrongParamException("r = " + r);
        this.r = r;
    }

    public double getVol()
    {
        return vol;
    }

    public final void setVol(double vol)
    {
        if (vol < 0)
            throw new WrongParamException("vol = " + vol);
        this.vol = vol;
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

    public double priceCall(double K, double T)
    {
        if (Common.isZero(T))
            return Common.plus(S-K);
        double d1 = (log(S/K) + (r + vol*vol/2)*T) / (vol * sqrt(T));
        double d2 = d1 - vol * sqrt(T);
        return S*rt.cndf(d1) - K*exp(-r*T)*rt.cndf(d2) ;
    }
    
    public double pricePut(double K, double T)
    {
        if (Common.isZero(T))
            return Common.plus(K-S);
        double d1 = (log(S/K) + (r + vol*vol/2)*T) / (vol * sqrt(T));
        double d2 = d1 - vol * sqrt(T);
        return K*exp(-r*T)*rt.cndf(-d2)- S*rt.cndf(-d1);
    }
    
    @Override
    public String toString()
    {
        return "BLACK-SCHOLES MODEL\n" +
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
    
    private RandomTools rt = new RandomTools();
    private double S, vol, r;
}
