
package finance.parameters;

import java.util.Collection;
import math.matrices.Matrix;

/**
 * Interface for containers of multi-asset model parameters.
 * @author Grzegorz Los
 */
public interface ModelParams
{
    /**
     * Returns number of assets in the model.
     * @return number of assets in the model.
     */
    public int getNumberOfAssets();
    
    /**
     * Returns collection of all asset names in the model.
     * @return collection of all asset names in the model.
     */
    public Collection<String> getAssetsNames();
    
    /**
     * Returns basic parameters of an asset number {@code nr}.
     * @param nr number of the asset.
     * @return basic parameters of specified asset.
     * @throws NoSuchAssetException if there is no asset with given number.
     */
    public OneAssetParams getParams(int nr);
    
    /**
     * Returns basic parameters of an asset named {@code name}.
     * @param name name of the asset.
     * @return basic parameters of specified asset.
     * @throws NoSuchAssetException if there is no asset with given name.
     */
    public OneAssetParams getParams(String name);
    
    /**
     * Returns name of the asset number {@code nr}.
     * @param nr number of the asset.
     * @return name of the asset.
     * @throws NoSuchAssetException if there is no asset with given number.
     */
    public String getName(int nr);
    
    /**
     * Return number of the asset named {@code name}.
     * @param name name of the asset.
     * @return number of the asset.
     * @throws NoSuchAssetException if there is no asset with given name.
     */
    public int getNr(String name);
    
    /**
     * Returns market's interest rate.
     * @return market's interest rate.
     */
    public double getR();
    
    /**
     * Returns correlation matrix between all assets.
     * @return correlation matrix between all assets.
     */
    public Matrix getCorrelation();
    
    /**
     * Returns correlation between specified assets.
     * @param nr1 number of first asset.
     * @param nr2 number of second asset.
     * @return correlation between specified assets.
     * @throws NoSuchAssetException if there is no asset with given number.
     */
    public double getCorrelation(int nr1, int nr2);
    
    /**
     * Returns correlation between specified assets.
     * @param name1 name of first asset.
     * @param name2 name of second asset.
     * @return correlation between specified assets.
     * @throws NoSuchAssetException if there is no asset with given name.
     */
    public double getCorrelation(String name1, String name2);
}
