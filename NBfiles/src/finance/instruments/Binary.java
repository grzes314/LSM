
package finance.instruments;

import finance.trajectories.Scenario;
import java.util.Collection;

/**
 *
 * @author Grzegorz Los
 */
public class Binary extends Modificator
{

    public Binary(Instr wrapped)
    {
        super(wrapped);
    }
    
    @Override
    protected boolean modExAvail(Scenario s, int k)
    {
        return true;
    }

    @Override
    public String getDesc()
    {
        return "Binary payoff";
    }

    @Override
    public String toString()
    {
        return "Binary";
    }

    @Override
    public boolean areYou(String str)
    {
        if ("binary".equalsIgnoreCase(str))
            return true;
        else return wrapped.areYou(str);
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        return wrapped.getUnderlyings();
    }

}
