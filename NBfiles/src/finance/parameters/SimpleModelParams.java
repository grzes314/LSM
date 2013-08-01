
package finance.parameters;

import finance.methods.WrongParamException;
import java.util.ArrayList;
import java.util.Collection;
import math.matrices.Matrix;

/**
 * Parameters required by the most of simple models. By simple model we understand
 * a model with a single asset and a constant interest rate. 
 * @author Grzegorz Los
 */
public class SimpleModelParams implements ModelParams
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
    
    @Override
    public int getNumberOfAssets()
    {
        return 1;
    }

    @Override
    public Collection<String> getAssetsNames()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(onlyAsset);
        return res;
    }

    @Override
    public OneAssetParams getParams(int nr)
    {
        ensureNrOK(nr);
        return new OneAssetParams(onlyAsset, S, vol, 0);
    }
    
    private void ensureNrOK(int nr)
    {
        if (nr != 1)
            throw new NoSuchAssetException("nr = " + nr);
    }

    @Override
    public OneAssetParams getParams(String name)
    {
        ensureNameOK(name);
        return new OneAssetParams(onlyAsset, S, vol, 0);
    }
    
    private void ensureNameOK(String name)
    {
        if ( !onlyAsset.equalsIgnoreCase(name) )
            throw new NoSuchAssetException("name = " + name);
    }

    @Override
    public String getName(int nr)
    {
        ensureNrOK(nr);
        return onlyAsset;
    }

    @Override
    public int getNr(String name)
    {
        ensureNameOK(name);
        return 1;
    }

    @Override
    public double getR()
    {
        return r;
    }

    @Override
    public Matrix getCorrelation()
    {
        return new Matrix(1,1);
    }

    @Override
    public double getCorrelation(int nr1, int nr2)
    {
        ensureNrOK(nr1);
        ensureNrOK(nr2);
        return 1.0;
    }

    @Override
    public double getCorrelation(String name1, String name2)
    {
        ensureNameOK(name1);
        ensureNameOK(name2);
        return 1.0;
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
     * Interest rate in the market.
     */
    public final double r;
    
    public static final String onlyAsset = "The only asset";
}
