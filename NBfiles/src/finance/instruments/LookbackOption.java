
package finance.instruments;

import finance.parameters.VanillaOptionParams.CallOrPut;
import static finance.parameters.VanillaOptionParams.CallOrPut.*;
import finance.trajectories.Scenario;
import finance.trajectories.Trajectory;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Grzegorz Los
 */
public class LookbackOption extends Instr
{

    public LookbackOption(double T, CallOrPut callOrPut, String underlying)
    {
        super(T);
        this.callOrPut = callOrPut;
        this.underlying = underlying;
    }

    @Override
    public String getDesc()
    {
        return "A lookback option\n" +
                "Type: " + (callOrPut == CALL ? "call" : "put") +
                "\nExpiry: " + getT();
    }

    @Override
    public String toString()
    {
        return "Lookback " + (callOrPut == CALL ? "Call" : "Put");
    }

    @Override
    public boolean areYou(String str)
    {
        if (str.equalsIgnoreCase("lookback"))
            return true;
        else
            return false;
    }

    @Override
    public int modificationsCount()
    {
        return 0;
    }

    @Override
    protected boolean exAvail_(Scenario s, int k)
    {
        return true;
    }

    @Override
    protected double payoff_(Scenario s, int k)
    {
        if (callOrPut == CALL)
            return callPayoff(s.getTr(underlying), k);
        else
            return putPayoff(s.getTr(underlying), k);
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        ArrayList<String> arr = new ArrayList<>();
        arr.add(underlying);
        return arr;
    }

    private double callPayoff(Trajectory tr, int k)
    {
        return tr.price(k) - tr.cumMin(k);
    }

    private double putPayoff(Trajectory tr, int k)
    {
        return tr.cumMax(k) - tr.price(k);
    }
    
    public final CallOrPut callOrPut;

    /**
     * Name of the underlying of the option. Note that only one of pair
     * (underlying, underlyingNr) may be not null.
     */
    public final String underlying;
}
