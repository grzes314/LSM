
package finance.instruments;

import finance.parameters.VanillaOptionParams;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static finance.parameters.VanillaOptionParams.CallOrPut.PUT;
import finance.trajectories.Scenario;
import finance.trajectories.Trajectory;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Option is a class extending Instrument to represent an American option.
 * @author Grzegorz Los
 */
public class Option extends Instr
{
    /**
     * Constructor taking type of the option, its strike value, underlying name,
     * and time support.
     */
    public Option(VanillaOptionParams vop, String underlying)
    {
        super(vop.T);
        this.vop = vop;
        this.underlying = underlying;
    }
    
    @Override
    public String getDesc()
    {
        return "An option\n" +
                "Type: " + (vop.callOrPut == CALL ? "call" : "put") +
                "\nStrike: " + getStrike() +
                "\nExpiracy: " + getT();
    }
    
    @Override
    public String toString()
    {
        return (getType() == CALL ? "Call" : "Put") + "@" + getStrike();
    }

    public VanillaOptionParams.CallOrPut getType()
    {
        return vop.callOrPut;
    }

    public double getStrike()
    {
        return vop.strike;
    }

    public String getUnderlying()
    {
        return underlying;
    }
    
    public double intrisnicValue(double stockPrice)
    {
        if (getType() == CALL) return Math.max(0, stockPrice - getStrike());
        else return Math.max(0, getStrike() - stockPrice);        
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
        
        return (vop.intrisnicValue(tr.price(k)));
    }

    @Override
    public boolean areYou(String str)
    {
        if (str.equalsIgnoreCase("option"))
            return true;
        else if (str.equalsIgnoreCase("american"))
            return true;
        if (str.equalsIgnoreCase("call"))
            return getType() == CALL;
        if (str.equalsIgnoreCase("put"))
            return getType() == PUT;
        else return false;
    }
    
    @Override
    public int modificationsCount()
    {
        return 0;
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        ArrayList<String> coll = new ArrayList<>();
        coll.add(underlying);
        return coll;
    }
    
    public final VanillaOptionParams vop;

    /**
     * Name of the underlying of the option. Note that only one of pair
     * (underlying, underlyingNr) may be not null.
     */
    public final String underlying;
}
