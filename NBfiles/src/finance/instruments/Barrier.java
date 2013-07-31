
package finance.instruments;

import finance.trajectories.Scenario;
import finance.trajectories.Trajectory;

/**
 * Class providing a way to add barrier to instrument.
 * @author Grzegorz Los
 */
public class Barrier extends Modificator
{
    /**
     * Constructor.
     * @param knock value should be KNOCK_IN or KNOCK_OUT 
     * @param from value should be FROM_UP or FROM_DOWN
     * @param level barrier value.
     * @param underlying name of the asset on which barrier is being put.
     * @param wrapped an instrument which payoff will be changed.
     */
    public Barrier(int knock, int from, double level, String underlying, Instr wrapped)
    {
        super(wrapped);
        this.knock = knock;
        this.from = from;
        this.level = level;
        this.underlying = underlying;
        underlyingNr = null;
    }
    
    /**
     * Constructor.
     * @param knock value should be KNOCK_IN or KNOCK_OUT 
     * @param from value should be FROM_UP or FROM_DOWN
     * @param level barrier value.
     * @param underlyingNr number of the asset on which barrier is being put.
     * @param wrapped an instrument which payoff will be changed.
     */
    public Barrier(int knock, int from, double level, int underlyingNr, Instr wrapped)
    {
        super(wrapped);
        this.knock = knock;
        this.from = from;
        this.level = level;
        this.underlyingNr = underlyingNr;
        underlying = null;
    }

    @Override
    public boolean exAvail_(Scenario s, int k)
    {
        Trajectory tr;
        if (underlying == null) tr = s.getTr(underlyingNr);
        else tr = s.getTr(underlying);
        
        boolean hit;
        if (from == FROM_UP) hit = tr.cumMax(k) >= level;
        else hit = tr.cumMin(k) <= level;
        
        
        return (knock == KNOCK_IN ? hit : !hit);
    }


    @Override
    public String desc()
    {
        String mssg = "\nBarrier on asset ";
        mssg += assetName() + "\n";
        mssg += barrierType();
        mssg += ", level=" + level;
        return wrapped.desc() + mssg;
    }

    @Override
    public String toString()
    {
        return wrapped.toString() + barrierType();
    }
    
    private String barrierType()
    {       
        if (knock == KNOCK_IN) {
            if (from == FROM_UP) return "Up-and-in";
            else return "Down-and-in";
        } else {
            if (from == FROM_UP) return "Up-and-out";
            else return "Down-and-out";            
        }        
    }
    
    private String assetName()
    {
        if (underlying == null) return "nr " + underlyingNr;
        else return underlying;        
    }
    
    @Override
    public boolean areYou(String str)
    {
        if (str.equalsIgnoreCase("barrier"))
            return true;
        else if (str.equalsIgnoreCase("up-and-out"))
            return (knock == KNOCK_OUT && from == FROM_UP);
        else if (str.equalsIgnoreCase("up-and-in"))
            return (knock == KNOCK_IN && from == FROM_UP);
        else if (str.equalsIgnoreCase("down-and-out"))
            return (knock == KNOCK_OUT && from == FROM_DOWN);
        else if (str.equalsIgnoreCase("down-and-in")) 
            return (knock == KNOCK_IN && from == FROM_DOWN);
        else return wrapped.areYou(str);
    }

    /**
     * Constant indicating that barrier is knock-in.
     */
    public static final int KNOCK_IN = 0;
    
    /**
     * Constant indicating that barrier is knock-out.
     */
    public static final int KNOCK_OUT = 1;
    
    /**
     * Constant indicating that barrier is from up.
     */
    public static final int FROM_UP = 2;
    
    /**
     * Constant indicating that barrier is from down.
     */
    public static final int FROM_DOWN = 3;
    
    /**
     * Value indicating if barrier is in-type or out-type.
     */
    public final int knock;
        
    /**
     * Value indicating if barrier is from up or from down.
     */
    public final int from;
    
    /**
     * Level of the barrier.
     */
    public final double level;
    
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
