
package models;

import static java.lang.Math.*;

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
        this.S = S;
        this.vol = vol;
        this.r = r;
    }

    public double getS()
    {
        return S;
    }

    public void setS(double S)
    {
        if (S <= 0)
            throw new WrongParamException("S = " + S);
        this.S = S;
    }

    public double getR()
    {
        return r;
    }

    public void setR(double r)
    {
        if (r < -1)
            throw new WrongParamException("r = " + r);
        this.r = r;
    }

    public double getVol()
    {
        return vol;
    }

    public void setVol(double vol)
    {
        if (vol <= 0)
            throw new WrongParamException("vol = " + vol);
        this.vol = vol;
    }

    public double priceCall(double K, double T)
    {
        double d1 = (log(S/K) + (r + vol*vol/2)*T) / (vol * sqrt(T));
        double d2 = d1 - vol * sqrt(T);
        return S*rt.cndf(d1) - K*exp(-r*T)*rt.cndf(d2) ;
    }
    
    public double pricePut(double K, double T)
    {
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
    double S, vol, r;
}
