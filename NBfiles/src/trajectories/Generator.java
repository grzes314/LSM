
package trajectories;

/**
 * Interface for classes generating market scenarios.
 * @author Grzegorz Los
 */
public interface Generator
{
    /**
     * Generates n scenarios.
     * @param n number of scenarios.
     * @return generated market scenarios.
     */
    Scenario[] generate(int n);
    
    /**
     * Generates n pairs of anthitetic scenarios.
     * @param n number of paris of anthitetic scenarios.
     * @return generated market scenarios.
     */
    Anthitetic[] generateAnthi(int n);
    
}
