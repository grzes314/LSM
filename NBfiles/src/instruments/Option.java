
package instruments;

import trajectories.Scenario;
import trajectories.Trajectory;

/**
 * Option is a class extending Instrument to represent an american option.
 * @author Grzegorz Los
 */
public class Option implements Instr
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
        this.ts = ts;
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
        this.ts = ts;
        this.type = type;
        this.K = strike;
        this.underlyingNr = underlyingNr;
        underlying = null;
    }

    @Override
    public boolean exAvail(Scenario s, int k)
    {
        return true;
    }

    @Override
    public double payoff(Scenario s, int k)
    {
        Trajectory tr;
        if (underlying == null) tr = s.getTr(underlyingNr);
        else tr = s.getTr(underlying);
        
        if (type == CALL) return Math.max(0, tr.price(k) - K);
        else return Math.max(0, K - tr.price(k));
    }
          
    @Override
    public String desc()
    {
        return "An option\n" +
                "Type: " + (type == CALL ? "call" : "put") +
                "\nStrike: " + K +
                "\nExpiracy: " + ts.T;
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
    
    /**
     * Time support for describing time horizon of the option and time 
     * points in which it is considered.
     */
    public final TimeSupport ts;

}
