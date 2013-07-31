
package finance.parameters;

import finance.models.WrongParamException;

/**
 * Parameters of one asset: spot price, volatility and drift.
 * @author Grzegorz Los
 */
public class OneAssetParams
{
    public OneAssetParams(String name, double S, double vol, double mu)
    {
        checkArgs(S, vol);
        this.S = S;
        this.vol = vol;
        this.mu = mu;
        this.name = name;
    }
    
    private void checkArgs(double S, double vol)
    {
        if (S < 0)
            throw new WrongParamException("S = " + S);
        if (vol < 0)
            throw new WrongParamException("vol = " + S);
    }
    
    /**
     * Asset's spot price.
     */
    public final double S;
    
    /**
     * Asset's volatility.
     */
    public final double vol;
    
    /**
     * Asset's drift.
     */
    public final double mu;
    
    /**
     * Asset's name.
     */
    public final String name;
}
