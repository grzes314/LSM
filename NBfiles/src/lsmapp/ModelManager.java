
package lsmapp;

import finance.parameters.ConcreteParams;
import finance.parameters.ModelParams;
import finance.parameters.OneAssetParams;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import math.matrices.Matrix;
import math.matrices.NotPositiveDefiniteMatrixException;

/**
 *
 * @author Grzegorz Los
 */
class ModelManager
{
    ModelManager()
    {
    }
    
    ModelManager(ModelParams mp)
    {
        readModelParams(mp);
    }

    ModelPanel getModelPanel()
    {
        return modelPanel;
    }

    void setModelPanel(ModelPanel modelPanel)
    {
        this.modelPanel = modelPanel;
    }
    
    ModelParams toParams() throws NotPositiveDefiniteMatrixException
    {
        if (getNumberOfAssets() == 0)
            return null;
        else
            return makeConcreteModelParams();
    }

    private ModelParams makeConcreteModelParams() throws NotPositiveDefiniteMatrixException
    {
        OneAssetParams[] oap = makeOneAssetParams();
        Matrix corr = makeCorrelation(oap);
        return new ConcreteParams(oap, corr, modelPanel.getR());
    }

    private OneAssetParams[] makeOneAssetParams()
    {
        OneAssetParams[] oap = new OneAssetParams[getNumberOfAssets()];
        int i = 0;
        for (Map.Entry<String, OneAssetPanel> entry: assetPanels.entrySet())
            oap[i++] = entry.getValue().makeOneAssetParams();
        return oap;
    }

    private Matrix makeCorrelation(OneAssetParams[] oap)
    {
        Matrix corr = new Matrix(getNumberOfAssets(), getNumberOfAssets());
        for (int i = 0; i < oap.length; ++i)
        {
            Collection<Pair<String, Double>> col =
                    assetPanels.get(oap[i].name).getCorrelations();
            fillOneRowOfCorrelation(i+1, oap, corr, col);
        }
        return corr;
    }

    private void fillOneRowOfCorrelation(int row, OneAssetParams[] oap,
            Matrix corr, Collection<Pair<String, Double>> col)
    {
        for (Pair<String, Double> p: col)
        {
            int j = indexOfName(oap, p.fst);
            corr.set(row, j+1, p.snd);
        }
    }
    
    private int indexOfName(OneAssetParams[] oap, String name)
    {
        for (int i = 0; i < oap.length; ++i)
            if (name.equals(oap[i].name))
                return i;
        throw new RuntimeException();
    }

    
    int getNumberOfAssets()
    {
        return assetPanels.size();
    }
    
    Collection<String> getAssets()
    {
        return assetPanels.keySet();
    }

    OneAssetPanel getAsset(String asset)
    {
        return assetPanels.get(asset);
    }
    
    void addAsset(String asset)
    {
        ensureNewAssetNameOK(asset);
        OneAssetPanel oap = new OneAssetPanel(asset);
        updateCorrelation(asset, oap);
        assetPanels.put(asset, oap);
    }

    void deleteAsset(String name)
    {
        assetPanels.remove(name);
        for (OneAssetPanel oap: assetPanels.values())
            oap.assetDeleted(name);
    }
    
    private void updateCorrelation(String newAsset, OneAssetPanel newPanel)
    {
        newPanel.zeroCorrelations(assetPanels.keySet());
        for (String asset: assetPanels.keySet())
            assetPanels.get(asset).addNewAsset(newAsset);
    }

    private void ensureNewAssetNameOK(String asset)
    {
        if (asset == null)
            throw new NullPointerException("Asset name can not be null");
        if ("".equals(asset.trim()))
            throw new IllegalArgumentException("Asset name must not contain only spaces.");
        if (assetPanels.containsKey(asset.trim()))
            throw new IllegalArgumentException("There is already an asset with that name.");
    }

    void clear()
    {
        assetPanels.clear();
        modelPanel.reset();
    }
    
    final void readModelParams(ModelParams mp)
    {
        assetPanels.clear();
        for (String asset: mp.getAssetsNames())
            assetPanels.put( asset, new OneAssetPanel(mp.getParams(asset)) );
        readCorrelation(mp);
        modelPanel.reset();
        modelPanel.setR(mp.getR());
    }
    
    final void readCorrelation(ModelParams mp)
    {
        Matrix corr = mp.getCorrelation();
        for (int i = 1; i <= mp.getNumberOfAssets(); ++i)
        {
            String asset = mp.getName(i);
            ArrayList<Pair<String, Double>> arr = new ArrayList<>();
            for (int j = 1; j <= mp.getNumberOfAssets(); ++j)
                if (i != j)
                    arr.add(new Pair<>(mp.getName(j), corr.get(i, j)));
            assetPanels.get(asset).resetCorrelations(arr);
        }
    }

    private ModelPanel modelPanel;
    private Map<String, OneAssetPanel> assetPanels = new HashMap<>();
}
