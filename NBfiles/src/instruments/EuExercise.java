
package instruments;

import trajectories.Scenario;

/**
 * Instrument modificator which changes exercise availability to European.
 * @author Grzegorz Los
 */
public class EuExercise extends Modificator
{
    /**
     * Creates instrument which differ from wrapped in exercise availability.
     * @param wrapped instrument to which we want to add 
     * european exercise modification.
     */
    public EuExercise(Instr wrapped)
    {
        super(wrapped);
    }
    
    @Override
    public String desc()
    {
        return wrapped.desc() + "European exercise";
    }
    
    @Override
    public String toString()
    {
        return "European " + wrapped.toString();
    }
    
    @Override
    protected boolean exAvail_(Scenario s, int k)
    {
        return wrapped.getTS().getK() == k;
    }
}
