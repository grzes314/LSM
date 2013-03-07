
package trajectories;

/**
 * Interface for classes representing market scenarios. The market scenario
 * is a set of trajectories of all assets in the market.
 * @author Grzegorz Los
 */
public interface Scenario
{
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
}
