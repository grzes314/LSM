
package instruments;

import trajectories.Scenario;

/**
 * Modficator class is a "Decorator" from the decorator pattern.
 * @author Grzegorz Los
 */
public abstract class Modificator implements Instr
{
    /**
     * Constructor takes an object to which new properties will be added.
     * @param wrapped 
     */
    public Modificator(Instr wrapped)
    {
        this.wrapped = wrapped;
    }
    
    @Override
    public double payoff(Scenario s, int k)
    {
        if (exAvail(s,k)) return wrapped.payoff(s, k);
        else return 0;
    }    

    @Override
    public TimeSupport getTS()
    {
        return wrapped.getTS();
    }
    
    /**
     * Wrapped instrument.
     */
    protected final Instr wrapped;
}
