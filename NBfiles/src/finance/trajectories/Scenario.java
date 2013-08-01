
package finance.trajectories;

import java.util.Collection;

/**
 * Interface for classes representing market scenarios. The market scenario
 * is a set of trajectories of all assets in the market.
 * @author Grzegorz Los
 */
public interface Scenario
{
    /**
     * Returns number of assets in the scenario.
     * @return number of assets in the scenario.
     */
    public int getNumberOfAssets();
    
    /**
     * Returns collection of all asset names in the scenario.
     * @return collection of all asset names in the scenario.
     */
    public Collection<String> getAssetsNames();
    
    /**
     * Returns trajectory of the asset specified by its number.
     * @param nr number of the asset.
     * @return trajectory of the asset specified by given number.
     */
    Trajectory getTr(int nr);
    
    /**
     * Returns trajectory of the asset specified by its name.
     * @param name name of the asset.
     * @return trajectory of the asset specified by given name.
     */
    Trajectory getTr(String name);
    
    /**
     * Returns true if and only if this Scenario contains anthitetic path. In the other
     * words it tells whether call to {@code getAnthi} will return not null
     * {@code Trajectory} rather than throw.
     * @return true if anthitetic path is available.
     */
    boolean hasAnthi();
    
    /**
     * Returns anthitetic trajectory of the asset specified by its number.
     * @param nr number of the asset.
     * @return anthitetic trajectory of the asset specified by given number.
     */
    Trajectory getAnthi(int nr);
        
    /**
     * Returns anthitetic trajectory of the asset specified by its name.
     * @param name name of the asset.
     * @return anthitetic trajectory of the asset specified by given name.
     */
    Trajectory getAnthi(String name);
    
    /**
     * Returns TimeSupport object indicating in which points trajectories
     * are simulated.
     * @return TimeSupport object indicating in which points trajectories
     * are simulated.
     */
    TimeSupport getTS();
}
