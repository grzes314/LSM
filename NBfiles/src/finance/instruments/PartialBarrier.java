
package finance.instruments;

import finance.parameters.PartialBarrierParams;
import finance.trajectories.Scenario;
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
            .append(pbp.mod == PartialBarrierParams.Modification.ON ? "Active " : "Disabled ")
            .append("since time ").append(pbp.since)
            .append("until time ").append(pbp.until);
        return wrapped.getDesc() + sb.toString();
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean areYou(String str)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected boolean exAvail_(Scenario s, int k)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private final String assetName;
    private final PartialBarrierParams pbp;
}
