
package finance.instruments;

import finance.trajectories.Scenario;
import finance.trajectories.TimeIncompatibleException;
import math.utils.Numerics;

/**
 * Base class for all financial instruments.
 * @author Grzegorz Los
 */
abstract public class Instr
{

    /**
     * Construct Instrument with given time horizon -- expiracy of the instrument.
     * @param T time horizon.
     */
    public Instr(double T)
    {
        this.T = T;
    }
    
    /**
     * Exercise available. Answers if the instrument may be exercised
     * at time point number k, in given market scenario. 
     * @param s market scenario.
     * @param k number of time point.
     * @return true iff the instrument may be exercised.
     * @throws TimeIncompatibleException if instruments and scenarios TimeSupports are
     * not equal.
     */
    public final boolean exAvail(Scenario s, int k)
    {
        if ( !Numerics.doublesEqual(s.getTS().getT(), T) )
            throw new TimeIncompatibleException("Time horizons for Scenario and "
                    + "Instrument have to be equal");
        return exAvail_(s,k);
    }
    
    /**
     * Payoff from the instrument that would be obtained, if the instrument was
     * exercised at time point number k, in given market scenario. If 
     * instrument can not be exercised for these arguments, then 0 is returned.
     * @param s market scenario.
     * @param k number of time point.
     * @return payoff from the instrument.
     */
    public double payoff(Scenario s, int k)
    {
        if (exAvail(s,k)) return payoff_(s,k);
        else return 0;
    }
    
    /**
     * Returns time to expiry of the instrument.
     * @return time horizon.
     */
    public double getT()
    {
        return T;
    }

    public abstract double intrisnicValue(double x);
    
    /**
     * Detailed description of the financial instrument.
     * @return string with description of the instrument.
     */
    abstract public String desc();
    
    @Override
    abstract public String toString();
    
    /**
     * Function useful in determining the type of this instrument,
     * @param str
     * @return 
     */
    abstract public boolean areYou(String str);
    
    /**
     * Returns number of modifications applied to this instrument.
     * @return number of modifications applied to this instrument.
     */
    abstract public int modificationsCount();
    
    /**
     * Main contents of the method exAvail(). In overriding classes it may
     * be assumed that TimeSupports of the instrument and the scenario
     * are compatible.
     * @param s market scenario.
     * @param k number of time point.
     * @return true iff the instrument may be exercised.
     */
    abstract protected boolean exAvail_(Scenario s, int k);
    
    /**
     * Main content of the method payoff(). In overriding classes it may
     * be assumed that option can be exercised at given time point.
     * @param s market scenario.
     * @param k number of time point.
     * @return payoff from the instrument.
     */
    abstract protected double payoff_(Scenario s, int k);
        
    /**
     * Time horizon -- expiracy of the instrument.
     */
    private final double T;
}
