
package finance.methods.lsm;

import finance.trajectories.Scenario;

/**
 *
 * @author Grzegorz Los
 */
public interface FutureEstimatedFlow
{
    public double getEstimation(Scenario s, int timeStep);
}
