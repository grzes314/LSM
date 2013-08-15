
package finance.parameters;

import java.util.*;
import math.matrices.Matrix;
import math.matrices.NotPositiveDefiniteMatrixException;

/**
 * Class storing parameters of multi asset models. Numeration of assets is 1-based.
 * @author Grzegorz Los
 */
public class ConcreteParams implements ModelParams
{
    public ConcreteParams(OneAssetParams[] basicParams, Matrix corr, double r)
            throws NotPositiveDefiniteMatrixException
    {
        ensureNamesAreUnique(basicParams);
        ensureIsCorrMatrixOK(basicParams, corr);
        ensureInterestRataOK(r);
        
        this.basicParams = basicParams;
        this.corr = corr;
        this.r = r;
        makeMapping();
    }

    private void ensureNamesAreUnique(OneAssetParams[] basicParams)
    {
        Set<String> names = new TreeSet<>();
        for (OneAssetParams par: basicParams)
        {
            if (names.contains(par.name))
                throw new IllegalArgumentException("Given assets do not have unique names");
            names.add(par.name);
        }
    }

    private void ensureIsCorrMatrixOK(OneAssetParams[] basicParams, Matrix corr)
            throws NotPositiveDefiniteMatrixException
    {
        if (basicParams.length == 0 && corr == null)
            return; // model only for pricing bonds
        if (corr.getRows() != basicParams.length)
            throw new IllegalArgumentException("Sizes of the correlation matrix"
                    + " and array of basic parameteres are not suitable");
        if (!corr.isCorrelationMatrix())
            throw new NotPositiveDefiniteMatrixException("Given matrix can not be correlation matrix");
    }

    private void ensureInterestRataOK(double r)
    {
        if (r <= -1)
            throw new IllegalArgumentException("Interest rate must be greater than -1");
    }
    
    private void makeMapping()
    {
        for (int i = 0; i < basicParams.length; ++i)
            nameToNr.put(basicParams[i].name, i+1);
    }
    
    @Override
    public int getNumberOfAssets()
    {
        return basicParams.length;
    }

    @Override
    public Collection<String> getAssetsNames()
    {
        return nameToNr.keySet();
    }

    @Override
    public OneAssetParams getParams(int nr)
    {
        ensureNumberOK(nr);
        return basicParams[nr-1];
    }

    private void ensureNumberOK(int nr)
    {
        if (nr < 1 || nr > basicParams.length)
            throw new NoSuchAssetException("Asset numbers belong to the interval"
                    + "[1, getNumberOfAssets()]\nobtained: " + nr);
    }
    
    private void ensureNameOK(String name)
    {
        if (!nameToNr.containsKey(name))
            throw new NoSuchAssetException("Unknown asset \"" + name  + "\"");
    }
    
    @Override
    public OneAssetParams getParams(String name)
    {
        ensureNameOK(name);
        return basicParams[nameToNr.get(name) -1];
    }

    @Override
    public String getName(int nr)
    {
        ensureNumberOK(nr);
        return basicParams[nr-1].name;
    }

    @Override
    public int getNr(String name)
    {
        ensureNameOK(name);
        return nameToNr.get(name);
    }

    @Override
    public double getR()
    {
        return r;
    }

    @Override
    public Matrix getCorrelation()
    {
        return new Matrix(corr);
    }

    @Override
    public double getCorrelation(int nr1, int nr2)
    {
        ensureNumberOK(nr1);
        ensureNumberOK(nr2);
        return corr.get(nr1, nr2);
    }

    @Override
    public double getCorrelation(String name1, String name2)
    {
        ensureNameOK(name1);
        ensureNameOK(name2);
        return corr.get(nameToNr.get(name1), nameToNr.get(name2));
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
        final ConcreteParams other = (ConcreteParams) obj;
        if (Double.doubleToLongBits(this.r) != Double.doubleToLongBits(other.r)) {
            return false;
        }
        if (!Arrays.deepEquals(this.basicParams, other.basicParams)) {
            return false;
        }
        if (!this.corr.equals(other.corr)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.r) ^
                (Double.doubleToLongBits(this.r) >>> 32));
        hash = 19 * hash + Arrays.deepHashCode(this.basicParams);
        hash = 19 * hash + Objects.hashCode(this.corr);
        return hash;
    }
    
    private double r = 0;
    private OneAssetParams[] basicParams;
    private Matrix corr;
    private Map<String, Integer> nameToNr = new HashMap<>();
}
