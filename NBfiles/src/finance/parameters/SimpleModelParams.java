
package finance.parameters;

import finance.methods.common.WrongParamException;
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
    public SimpleModelParams(String assetName, double S, double vol, double drift, double r)
    {
        checkArgs(S, vol, r);
        this.S = S;
        this.vol = vol;
        this.mu = drift;
        this.r = r;
        this.name = assetName;
    }
    
    public SimpleModelParams(double S, double vol, double r)
    {
        this(onlyAsset, S, vol, 0, r);
    }
    
    public SimpleModelParams withS(double newS)
    {
        return new SimpleModelParams(name, newS, vol, mu, r);
    }
        
    public SimpleModelParams withVol(double newVol)
    {
        return new SimpleModelParams(name, S, newVol, mu, r);
    }    
         
    public SimpleModelParams withMu(double newMu)
    {
        return new SimpleModelParams(name, S, vol, newMu, r);
    }    
        
    public SimpleModelParams withR(double newR)
    {
        return new SimpleModelParams(name, S, vol, mu, newR);
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
        res.add(name);
        return res;
    }

    @Override
    public OneAssetParams getParams(int nr)
    {
        ensureNrOK(nr);
        return new OneAssetParams(name, S, vol, 0);
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
        return new OneAssetParams(name, S, vol, 0);
    }
    
    private void ensureNameOK(String name)
    {
        if ( !this.name.equalsIgnoreCase(name) )
            throw new NoSuchAssetException("name = " + name);
    }

    @Override
    public String getName(int nr)
    {
        ensureNrOK(nr);
        return name;
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
    
    @Override
    public String getDesc()
    {
        return "Model with single asset\n" +
                "S = " + S + "\n" +
                "vol = " + vol + "\n" +
                "r = " + r + "\n";
    }
    
    @Override
    public String toString()
    {
        return "Model with single asset\n";
    }
    
    /**
     * Asset's name.
     */
    public final String name;
    
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
     * Interest rate in the market.
     */
    public final double r;
    
    public static final String onlyAsset = "The only asset";
}
