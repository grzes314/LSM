
package finance.trajectories;

/**
 * Interface representing value in time of a single asset.
 * @author Grzegorz Los
 */
public interface Trajectory
{    
    public enum Auxiliary
    {
        AVERAGE, CUMMIN, CUMMAX
    }
    
    /**
     * Returns number of steps in this trajecotry. Let K = getK(). 
     * Numbers of steps are {0,1,...,K}. Step 0 corresponds to time 0,
     * step K corresponds to time T (time horizon). 
     * cor
     * @return number of steps in this trajecotry
     */
    int getK();
    
    /**
     * Function returns price of the asset at the time indicated by given
     * number of timestep.
     * @param k number of timepoint.
     * @return price of the asset at time k * delta_t.
     */
    double price(int k);

    /**
     * Step with maximum price. Returns number of time step from [0,k] (inclusive)
     * for which asset prices obtained maximum value.
     * @return number of time step with maximum asset price.
     */
    int stepMax(int k);
    
    /**
     * Step with minimum price. Returns number of time step from [0,k] (inclusive)
     * for which asset prices obtained minimum value.
     * @return number of time step with minimum asset price.
     */
    int stepMin(int k);
      
    /**
     * Cumulative maximum price. Returns maximal price reached by asset up
     * to time point k.
     * @return maximal price reached by asset up
     * to time point k.
     */
    double cumMax(int k);
    
    /**
     * Cumulative minimum price. Returns minimal price reached by asset up
     * to time point k.
     * @return minimal price reached by asset up
     * to time point k.
     */
    double cumMin(int k);
        
    /**
     * Average asset price in time from timestep beg to time step end
     * (both inclusive).
     * @param beg number of first timestep considered.
     * @param end number of last timestep considered.
     * @return 
     */
    double average(int beg, int end);
}
