
package finance.instruments;

import finance.parameters.BarrierParams;
import finance.trajectories.Scenario;
import finance.trajectories.Trajectory;
import java.util.Collection;

/**
 * Class providing a way to add barrier to instrument.
 * @author Grzegorz Los
 */
public class Barrier extends Modificator
{
    /**
     * Constructor.
     * @param bp barrier parameters.
     * @param underlying name of the asset on which barrier is being put.
     * @param wrapped an instrument which payoff will be changed.
     */
    public Barrier(BarrierParams bp, String underlying, Instr wrapped)
    {
        super(wrapped);
        this.bp = bp;
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
    public Barrier(BarrierParams bp, int underlyingNr, Instr wrapped)
    {
        super(wrapped);
        this.bp = bp;
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
        if (bp.isFromUp())
            hit = tr.cumMax(k) >= bp.level;
        else
            hit = tr.cumMin(k) <= bp.level;
        
        return (bp.isKnockIn() ? hit : !hit);
    }


    @Override
    public String getDesc()
    {
        String mssg = "\nBarrier on asset ";
        mssg += assetName() + "\n";
        mssg += barrierType();
        mssg += ", level=" + bp.level;
        return wrapped.getDesc() + mssg;
    }

    @Override
    public String toString()
    {
        return wrapped.toString() + barrierType();
    }
    
    private String barrierType()
    {
        switch (bp.type)
        {
            case UAI:
                return "Up-and-in";
            case UAO:
                return "Up-and-out";
            case DAI:
                return "Down-and-in";
            case DAO:
                return "Down-and-out";
            default:
                throw new RuntimeException("Flow should not reach that statement");
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
            return (bp.type == BarrierParams.Type.UAO);
        else if (str.equalsIgnoreCase("up-and-in"))
            return (bp.type == BarrierParams.Type.UAI);
        else if (str.equalsIgnoreCase("down-and-out"))
            return (bp.type == BarrierParams.Type.DAO);
        else if (str.equalsIgnoreCase("down-and-in")) 
            return (bp.type == BarrierParams.Type.DAI);
        else return wrapped.areYou(str);
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        Collection<String> coll = wrapped.getUnderlyings();
        if (!coll.contains(underlying))
            coll.add(underlying);
        return coll;
    }

    
    public final BarrierParams bp;
    
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
