
package lsmapp.modelTab;

/**
 *
 * @author Grzegorz Los
 */
public interface AssetCountInfo
{
    public void addAssetCountObserver(AssetCountObserver observer);
    public void removeAssetCountObserver(AssetCountObserver observer);
    public void informAboutDeletion(String assetName);
    public void informAboutAddition(String assetName);
}
