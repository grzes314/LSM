
package instruments;

import trajectories.Scenario;
import trajectories.TimeIncompatibleException;
import trajectories.TimeSupport;

/**
 * Base class for all financial instruments.
 * @author Grzegorz Los
 */
abstract public class Instr
{

    public Instr(TimeSupport ts)
    {
        this.ts = ts;
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
        if ( !ts.equals(s.getTS()) )
            throw new TimeIncompatibleException("TimeSupports for Scenario and "
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
     * Returns time support object which contains information about instruments
     * time horizon and number of timesteps in which it is considered.
     * @return time support object for this instrument. 
     */
    public TimeSupport getTS()
    {
        return ts;
    }

    /**
     * Sets number of time points in which payoff may be considered.
     * @param K number of time points.
     */
    public void setK(int K)
    {
        ts.setK(K);
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
     * Function usefull in determining the type of this instrument,
     * @param str
     * @return 
     */
    abstract public boolean areYou(String str);
    
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
     * Time support for describing time horizon of the instrument and time 
     * points in which it is considered.
     */
    protected final TimeSupport ts;
}
