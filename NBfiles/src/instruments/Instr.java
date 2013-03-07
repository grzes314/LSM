
package instruments;

import trajectories.Scenario;

/**
 * Base class for all financial instruments.
 * @author Grzegorz Los
 */
public interface Instr
{    
    /**
     * Exercies available. Answers if the instrument may be exercised
     * at time point number k, in given market scenario. 
     * @param s market scenario.
     * @param k number of time point.
     * @return true iff the instrument may be exercised.
     */
    abstract public boolean exAvail(Scenario s, int k);
    
    /**
     * Payoff from the instrument that would be obtained, if instrument was
     * exercised at time point number k, in given market scenario. If 
     * instrument can not be exercised for these arguments, then 0 is returned.
     * @param s market scenario.
     * @param k number of time point.
     * @return payoff from the instrument.
     */
    abstract public double payoff(Scenario s, int k);
    
    /**
     * Returns time support object which contains information about instruments
     * time horizon and number of timesteps in which it is considered.
     * @return time support object for this instrument. 
     */
    abstract public TimeSupport getTS();

    /**
     * Detailed description of the financial instrument.
     * @return string with description of the instrument.
     */
    abstract public String desc();
    
    @Override
    abstract public String toString();
}
