
package finance.trajectories;

import finance.methods.ProgressObservable;

/**
 * Interface for classes generating market scenarios.
 * @author Grzegorz Los
 */
public interface Generator extends ProgressObservable
{
    public enum Measure
    {
        REAL, MART
    }
    
    public enum Anthi
    {
        YES, NO
    }
    
    /**
     * Generates n scenarios (without anthitetic paths).
     * @param n number of scenarios.
     * @return generated market scenarios.
     */
    public Scenario[] generate(int n);
    
    /**
     * Generates n scenarios.
     * @param n number of scenarios.
     * @param anthi should the anthitetic paths be included?
     * @return generated market scenarios.
     */
    public Scenario[] generate(int n, Anthi anthi);
    
    /**
     * Generates a single scenario (without anthitetic path).
     * @param n number of scenarios.
     * @return generated market scenario.
     */
    public Scenario generate();    
    
    /**
     * Generates a single scenario.
     * @param anthi should the anthitetic paths be included?
     * @return generated market scenario.
     */
    public Scenario generate(Anthi anthi);
}
