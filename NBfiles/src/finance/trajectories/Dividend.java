
package finance.trajectories;

/**
 *
 * @author Grzegorz Los
 */
public abstract class Dividend
{

    public Dividend(double t, String underlying)
    {
        this.t = t;
        this.underlying = underlying;
    }
    
    abstract public double getDividend(double S);
    
    public boolean isItTime(int timeStep, TimeSupport ts)
    {
        return timeStep == ts.timeToNr(t);
    }
    
    public int getTimeStep(TimeSupport ts)
    {
        return ts.timeToNr(t);
    }
    
    public final double t;
    public final String underlying;
}
