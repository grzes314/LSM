
package finance.instruments;

import finance.parameters.VanillaOptionParams;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static finance.parameters.VanillaOptionParams.CallOrPut.PUT;
import finance.trajectories.Scenario;
import finance.trajectories.Trajectory;

/**
 * Option is a class extending Instrument to represent an American option.
 * @author Grzegorz Los
 */
public class Option extends Instr
{
    /**
     * Constructor taking type of the option, its strike value, underlying name,
     * and time support.
     * @param type type of the option (call or put).
     * @param strike strike value.
     * @param T time horizon.
     */
    public Option(VanillaOptionParams vop, String underlying)
    {
        super(vop.T);
        this.vop = vop;
        this.underlying = underlying;
        underlyingNr = null;
    }

    /**
     * Constructor taking type of the option, its strike value, underlying
     * number and time support.
     * @param type type of the option (call or put).
     * @param strike strike value.
     * @param T time horizon.
     */
    public Option(VanillaOptionParams vop, int underlyingNr)
    {
        super(vop.T);
        this.vop = vop;
        this.underlyingNr = underlyingNr;
        underlying = null;
    }    
    
    @Override
    public String desc()
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

    public Integer getUnderlyingNr()
    {
        return underlyingNr;
    }
    
    @Override
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
        Trajectory tr;
        if (underlying == null) tr = s.getTr(underlyingNr);
        else tr = s.getTr(underlying);
        
        if (getType() == CALL) return Math.max(0, tr.price(k) - getStrike());
        else return Math.max(0, getStrike() - tr.price(k));
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
    
    public final VanillaOptionParams vop;

    /**
     * Name of the underlying of the option. Note that only one of pair
     * (underlying, underlyingNr) may be not null.
     */
    public final String underlying;
    
    /**
     * Number of underlying of the option.
     */
    public final Integer underlyingNr;
}
