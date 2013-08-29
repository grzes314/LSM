
package finance.instruments;

import finance.trajectories.Scenario;
import java.util.Collection;

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
    public String getDesc()
    {
        return wrapped.getDesc() + "\nEuropean exercise";
    }
    
    @Override
    public String toString()
    {
        return "Eu " + wrapped.toString();
    }
    
    @Override
    protected boolean modExAvail(Scenario s, int k)
    {
        return s.getTS().getK() == k;
    }

    @Override
    public boolean areYou(String str)
    {
        if (str.equalsIgnoreCase("european")) return true;
        else if (str.equalsIgnoreCase("american")) return false;
        else return wrapped.areYou(str);
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        return wrapped.getUnderlyings();
    }
}
