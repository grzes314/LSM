
package models;

/**
 * Parameters required by the most of simple models.
 * @author Grzegorz Los
 */
public class SimpleModelParams
{

    public SimpleModelParams(double S, double vol, double r)
    {
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
    
    public final double S;
    public final double vol;
    public final double r;
}
