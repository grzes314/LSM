
package finance.instruments;

import finance.parameters.VanillaOptionParams;
import finance.parameters.VanillaOptionParams.CallOrPut;
import finance.trajectories.Scenario;
import java.util.ArrayList;
import java.util.Collection;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import finance.trajectories.Trajectory;

/**
 *
 * @author Grzegorz Los
 */
public class AsianOption extends Instr
{

    public AsianOption(double T, CallOrPut callOrPut, String underlying)
    {
        super(T);
        this.callOrPut = callOrPut;
        this.underlying = underlying;
    }
    
    @Override
    public String getDesc()
    {
        return "An asian option\n" +
                "Type: " + (callOrPut == CALL ? "call" : "put") +
                "\nExpiry: " + getT();
    }

    @Override
    public String toString()
    {
        return "Asian " + (callOrPut == CALL ? "Call" : "Put");
    }

    @Override
    public boolean areYou(String str)
    {
        if (str.equalsIgnoreCase("asianOption"))
            return true;
        else return false;
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
        Trajectory tr = s.getTr(underlying);
        double S = tr.price(k);
        double av = tr.average(0, k);
        if (callOrPut == CALL) return Math.max(0, S - av);
        else return Math.max(0, av - S);
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        ArrayList<String> coll = new ArrayList<>();
        coll.add(underlying);
        return coll;
    }
    
    public final VanillaOptionParams.CallOrPut callOrPut;

    /**
     * Name of the underlying of the option. Note that only one of pair
     * (underlying, underlyingNr) may be not null.
     */
    public final String underlying;
}
