
package instruments;

import trajectories.Scenario;
import trajectories.Trajectory;

/**
 * Option is a class extending Instrument to represent an american option.
 * @author Grzegorz Los
 */
public class Option extends Instr
{
    /**
     * Constructor taking type of the option, its strike value, underlying name,
     * and time support.
     * @param type type of the option (call or put).
     * @param strike strike value.
     * @param ts time support.
     */
    public Option(int type, double strike, String underlying, TimeSupport ts)
    {
        super(ts);
        this.type = type;
        this.K = strike;
        this.underlying = underlying;
        underlyingNr = null;
    }

    /**
     * Constructor taking type of the option, its strike value, underlying
     * number and time support.
     * @param type type of the option (call or put).
     * @param strike strike value.
     * @param ts time support.
     */
    public Option(int type, double strike, int underlyingNr, TimeSupport ts)
    {
        super(ts);
        this.type = type;
        this.K = strike;
        this.underlyingNr = underlyingNr;
        underlying = null;
    }    
    
    @Override
    public String desc()
    {
        return "An option\n" +
                "Type: " + (type == CALL ? "call" : "put") +
                "\nStrike: " + K +
                "\nExpiracy: " + ts.getT();
    }
    
    @Override
    public String toString()
    {
        return "Option" + (type == CALL ? "Call" : "Put") + "@" + K;
    }
    
    @Override
    public TimeSupport getTS()
    {
        return ts;
    }

    public int getType()
    {
        return type;
    }

    public double getStrike()
    {
        return K;
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
        if (type == CALL) return Math.max(0, stockPrice - K);
        else return Math.max(0, K - stockPrice);        
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
        
        if (type == CALL) return Math.max(0, tr.price(k) - K);
        else return Math.max(0, K - tr.price(k));
    }
      
    /**
     * Constant indicating call option.
     */
    public static final int CALL = 0;
    
    /**
     * Constant indicating put option.
     */
    public static final int PUT = 1;
    
    /**
     * Type of the option.
     */
    public final int type;
    
    /**
     * Strike value of the option.
     */
    public final double K;

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
