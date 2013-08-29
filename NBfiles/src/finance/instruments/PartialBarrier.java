
package finance.instruments;

import finance.parameters.PartialBarrierParams;
import finance.parameters.PartialBarrierParams.PartType;
import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;
import finance.trajectories.Trajectory;
import java.util.Collection;

/**
 *
 * @author Grzegorz Los
 */
public class PartialBarrier extends Modificator
{
    public PartialBarrier(PartialBarrierParams pbp, String assetName, Instr wrapped)
    {
        super(wrapped);
        this.assetName = assetName;
        this.pbp = pbp;
    }
    
    @Override
    public String getDesc()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\nPartial barrier on asset \"")
            .append(assetName).append("\"\n")
            .append(barrierType()).append(", level=")
            .append(pbp.barrierParams.level).append("\n")
            .append(getPartialTypeDesc());
        return wrapped.getDesc() + sb.toString();
    }    
    
    private String getPartialTypeDesc()
    {
        if (pbp.type == PartType.EARLY)
            return getEarlyBarrierDesc();
        else
            return getLateBarrierDesc();
    }
    
    private String getEarlyBarrierDesc()
    {
        return "Active only until time " + pbp.borderTime;
    }
    
    private String getLateBarrierDesc()
    {
        return "Active only since time " + pbp.borderTime;
    }
    
    private String barrierType()
    {
        switch (pbp.barrierParams.type)
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

    @Override
    public String toString()
    {
        return "Partial " + pbp.barrierParams.type + "@" + pbp.barrierParams.level;
    }

    @Override
    public boolean areYou(String str)
    {
        if (str.equalsIgnoreCase("partialBarrier"))
            return true;
        else
            return wrapped.areYou(str);
    }

    @Override
    protected boolean modExAvail(Scenario s, int k)
    {
        int border = calcBorderTimeStep(s.getTS());
        Trajectory tr = s.getTr(assetName);
        boolean hit = (pbp.type == PartType.EARLY ? wasEarlyBarrierHit(tr, k, border)
                                                  : wasLateBarrierHit(tr, k, border));
        if (hit) return pbp.barrierParams.isKnockIn();
        else return pbp.barrierParams.isKnockOut();
    }

    private int calcBorderTimeStep(TimeSupport ts)
    {
        return ts.timeToNr(pbp.borderTime);
    }
    
    
    private boolean wasEarlyBarrierHit(Trajectory tr, int k, int border)
    {
        int m = Math.min(k, border);
        double level = pbp.barrierParams.level;
        if (pbp.barrierParams.isFromUp())
            return tr.cumMax(m) > level;
        else
            return tr.cumMin(m) < level;
    }
    
    private boolean wasLateBarrierHit(Trajectory tr, int k, int border)
    {
        if (k < border)
            return false;
        double level = pbp.barrierParams.level;
        for (int i = border;  i <= k; ++i) //TODO pomyslec nad interfacem ktory poprawi efektywnosc
            if (pbp.barrierParams.isFromUp() && tr.price(k) > level)
                return true;
            else if (pbp.barrierParams.isFromDown() && tr.price(k) < level)
                return true;
        return false;
    }
    
    @Override
    public Collection<String> getUnderlyings()
    {
        Collection<String> coll = wrapped.getUnderlyings();
        if (!coll.contains(assetName))
            coll.add(assetName);
        return coll;
    }
   
    private final String assetName;
    private final PartialBarrierParams pbp;
}
