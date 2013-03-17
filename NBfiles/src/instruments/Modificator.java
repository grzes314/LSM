
package instruments;

import trajectories.Scenario;

/**
 * Modficator class is a "Decorator" from the decorator pattern.
 * @author Grzegorz Los
 */
public abstract class Modificator extends Instr
{
    /**
     * Constructor takes an object to which new properties will be added.
     * @param wrapped 
     */
    public Modificator(Instr wrapped)
    {
        super(wrapped.ts);
        this.wrapped = wrapped;
    }

    /**
     * By default payoff from modification is the same us from unmodified
     * instrument, only the possibility of exercise is changed.
     * @param s market scenario.
     * @param k number of time point.
     * @return payoff from the instrument.
     */
    @Override
    protected double payoff_(Scenario s, int k)
    {
        return wrapped.payoff_(s, k);
    }
    
    /**
     * Wrapped instrument.
     */
    protected final Instr wrapped;
}
