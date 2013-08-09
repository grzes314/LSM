
package finance.parameters;

import finance.methods.common.WrongParamException;
import java.util.Objects;
import math.utils.Numerics;

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

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OneAssetParams other = (OneAssetParams) obj;
        if (!Numerics.doublesEqual(this.S, other.S, 1e-3)) {
            return false;
        }
        if (!Numerics.doublesEqual(this.vol, other.vol, 1e-3)) {
            return false;
        }
        if (!Numerics.doublesEqual(this.mu, other.mu, 1e-3)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.S) ^ (Double.doubleToLongBits(this.S) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.vol) ^ (Double.doubleToLongBits(this.vol) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.mu) ^ (Double.doubleToLongBits(this.mu) >>> 32));
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
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
