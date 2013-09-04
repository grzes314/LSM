
package finance.instruments;

import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;
import java.util.Collection;

/**
 *
 * @author Grzegorz Los
 */
public class Forward extends Modificator
{
    public Forward(double since, Instr wrapped)
    {
        super(wrapped);
        this.since = since;
        if (since < 0 || since > wrapped.getT())
            throw new IllegalArgumentException("Invalid \"since\" value");
    }

    @Override
    protected boolean modExAvail(Scenario s, int k)
    {
        TimeSupport ts = s.getTS();
        return ts.nrToTime(k) >= since;
    }

    @Override
    public String getDesc()
    {
        return wrapped.getDesc() + "\nForward feature: Exercise available since " + since + "\n";
    }

    @Override
    public String toString()
    {
        return "Forward " + wrapped.toString();
    }

    @Override
    public boolean areYou(String str)
    {
        if ("forward".equalsIgnoreCase(str))
            return true;
        else
            return wrapped.areYou(str);
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        return wrapped.getUnderlyings();
    }
    
    private double since;
}
