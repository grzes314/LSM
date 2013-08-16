
package lsmapp.modelTab;

import finance.parameters.ConcreteParams;
import finance.parameters.ModelParams;
import finance.parameters.OneAssetParams;
import finance.parameters.SimpleModelParams;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import math.matrices.Matrix;
import math.matrices.NotPositiveDefiniteMatrixException;

/**
 *
 * @author Grzegorz Los
 */
public class ModelManager implements AssetCountInfo
{
    public ModelManager()
    {
        initTableModelListener();
    }
    
    public ModelManager(ModelParams mp)
    {
        fromModelParams(mp);
        initTableModelListener();
    }

    private void initTableModelListener()
    {
        tableModelListener = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e)
            {
                CorrChangeEvent ccev = (CorrChangeEvent) e;
                    handleCorrChange(ccev.assetSource, ccev.assetChanged, ccev.value);
            }
        };
    }
    
    private void handleCorrChange(String assetSource, String assetChanged, double value)
    {
        assetPanels.get(assetChanged).changeCorrelation(assetSource, value);
    }
            
    public ModelPanel getModelPanel()
    {
        return modelPanel;
    }

    public void setModelPanel(ModelPanel modelPanel)
    {
        if (this.modelPanel != null)
            removeAssetCountObserver(this.modelPanel);
        
        this.modelPanel = modelPanel;
        if (!observers.contains(modelPanel))
            addAssetCountObserver(modelPanel);
    }
    
    public ModelParams toParams() throws NotPositiveDefiniteMatrixException
    {
        if (getNumberOfAssets() == 0)
            return new SimpleModelParams(0, 0, modelPanel.getR());
        else if (getNumberOfAssets() == 1)
            return makeSimpleModelParams();
        else
            return makeConcreteModelParams();
    }

    private SimpleModelParams makeSimpleModelParams()
    {
        OneAssetParams[] oap = makeOneAssetParams();
        return new SimpleModelParams(oap[0].name, oap[0].S,
            oap[0].vol, modelPanel.getR());
    }
    
    private ConcreteParams makeConcreteModelParams()
        throws NotPositiveDefiniteMatrixException
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

    
    public int getNumberOfAssets()
    {
        return assetPanels.size();
    }
    
    public Collection<String> getAssets()
    {
        return assetPanels.keySet();
    }

    public OneAssetPanel getAsset(String asset)
    {
        return assetPanels.get(asset);
    }
    
    public void addAsset(String asset)
    {
        OneAssetPanel oap = new OneAssetPanel(asset);
        addAsset(asset, oap);
    }
    
    private void addAsset(String asset, OneAssetPanel newPanel)
    {
        ensureNewAssetNameOK(asset);
        newPanel.addTableModelListener(tableModelListener);
        assetPanels.put(asset, newPanel);
        informAboutAddition(asset);
        addAssetCountObserver(newPanel);        
    }

    public void deleteAsset(String name)
    {
        OneAssetPanel oap = assetPanels.get(name);
        if (oap != null)
        {
            removeAssetCountObserver(oap);
            assetPanels.remove(name);
            informAboutDeletion(name);
        }
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

    public void clear()
    {
        for (String name: assetPanels.keySet())
            deleteAsset(name);
    }
    
    public final void fromModelParams(ModelParams mp)
    {
        assetPanels.clear();
        for (String asset: mp.getAssetsNames())
        {
            OneAssetPanel oap = new OneAssetPanel(mp.getParams(asset));
            addAsset(asset, oap);
        }
        readCorrelation(mp);
        modelPanel.reset();
        modelPanel.setR(mp.getR());
    }
    
    public final void readCorrelation(ModelParams mp)
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

    @Override
    public void addAssetCountObserver(AssetCountObserver observer)
    {
        observers.add(observer);
    }

    @Override
    public void removeAssetCountObserver(AssetCountObserver observer)
    {
        observers.remove(observer);
    }

    @Override
    public void informAboutDeletion(String assetName)
    {
        for (AssetCountObserver ob: observers)
            ob.assetDeleted(assetName);
    }

    @Override
    public void informAboutAddition(String assetName)
    {
        for (AssetCountObserver ob: observers)
            ob.assetAdded(assetName);
    }
    
    private ModelPanel modelPanel;
    private Map<String, OneAssetPanel> assetPanels = new HashMap<>();
    private  TableModelListener tableModelListener;
    private ArrayList<AssetCountObserver> observers = new ArrayList<>();
}
