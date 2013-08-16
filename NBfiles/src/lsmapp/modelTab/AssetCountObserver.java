
package lsmapp.modelTab;

/**
 *
 * @author Grzegorz Los
 */
public interface AssetCountObserver
{
    public void assetAdded(String name);
    public void assetDeleted(String name);
}
