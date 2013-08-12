
package lsmapp.instrPanels;

import finance.instruments.Barrier;
import finance.instruments.Instr;
import finance.parameters.BarrierParams;
import finance.parameters.PartialBarrierParams;

/**
 *
 * @author Grzegorz Los
 */
public class BarrierWrapper
{

    public BarrierWrapper(BarrierParams bp, String onAsset)
    {
        this.barrierParams = bp;
        this.onAsset = onAsset;
    }

    public BarrierWrapper(PartialBarrierParams pbp, String onAsset)
    {
        this.partialBarrierParams = pbp;
        this.onAsset = onAsset;
    }
    
    public Instr addBarrier(Instr instr)
    {
        if (barrierParams == null)
            return addPartialBarrier(instr);
        else
            return addNormalBarrier(instr);
    }

    private Instr addPartialBarrier(Instr instr)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Instr addNormalBarrier(Instr instr)
    {
        return new Barrier(barrierParams, onAsset, instr);
    }
    
    public String getDescription()
    {        
        if (barrierParams == null)
            return getPartialBarrierDescription();
        else
            return getNormalBarrierDescription(barrierParams);
    }

    private String getPartialBarrierDescription()
    {
        return getNormalBarrierDescription(partialBarrierParams.barrierParams) +
                (partialBarrierParams.mod == PartialBarrierParams.Modification.ON ?
                "\nBarrier active only " : "\nBarrier inactive ") +
                "since " + partialBarrierParams.since + " until " + partialBarrierParams.until;
    }

    private String getNormalBarrierDescription(BarrierParams bp)
    {
        return getBarrierTypeString(bp.type) +
                " barrier at level " + bp.level +
                " on asset \"" + onAsset + "\".";
    }
    
    private String getBarrierTypeString(BarrierParams.Type type)
    {
        switch (type)
        {
            case UAI:
                return "Up-and-in";
            case UAO:
                return "Up-and-out";
            case DAI:
                return "Down-and-in";
            default:
                return "Down-and-out";
        }
    }
    
    @Override
    public String toString()
    {
        if (barrierParams == null)
            return "Partial " + partialBarrierParams.barrierParams.toString()
                    + " on asset " + onAsset;
        else
            return barrierParams.toString() + " on asset " + onAsset;
    }
    
    private BarrierParams barrierParams;
    private PartialBarrierParams partialBarrierParams;
    private String onAsset;
}
