
package finance.parameters;

import finance.models.WrongParamException;

/**
 * Parameters required by the most of simple models.
 * @author Grzegorz Los
 */
public class SimpleModelParams
{

    public SimpleModelParams(double S, double vol, double r)
    {
        checkArgs(S, vol, r);
        this.S = S;
        this.vol = vol;
        this.r = r;
    }
    
    public SimpleModelParams withS(double newS)
    {
        return new SimpleModelParams(newS, vol, r);
    }
        
    public SimpleModelParams withVol(double newVol)
    {
        return new SimpleModelParams(S, newVol, r);
    }    
        
    public SimpleModelParams withR(double newR)
    {
        return new SimpleModelParams(S, vol, newR);
    }
    
    private void checkArgs(double S, double vol, double r)
    {
        if (S < 0)
            throw new WrongParamException("S = " + S);
        if (vol < 0)
            throw new WrongParamException("vol = " + S);
        if (r <= -1)
            throw new WrongParamException("r = " + S);
    }
    
    public final double S;
    public final double vol;
    public final double r;
}
