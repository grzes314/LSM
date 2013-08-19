
package lsmapp.instrPanels;

import finance.instruments.Barrier;
import finance.instruments.Instr;
import finance.instruments.PartialBarrier;
import finance.parameters.BarrierParams;
import finance.parameters.PartialBarrierParams;
import finance.parameters.PartialBarrierParams.PartType;

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
        this.partialBarrierParams = null;
    }

    public BarrierWrapper(PartialBarrierParams pbp, String onAsset)
    {
        this.partialBarrierParams = pbp;
        this.onAsset = onAsset;
        this.barrierParams = null;
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
        return new PartialBarrier(partialBarrierParams, onAsset, instr);
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
                (partialBarrierParams.type == PartType.EARLY ? "\nBarrier active only until time "
                                                             : "\nBarrier active only since time ")
                + partialBarrierParams.borderTime + ".";
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
                    + " on asset \"" + onAsset +"\"";
        else
            return barrierParams.toString() + " on asset \"" + onAsset + "\"";
    }

    public boolean isUsing(String asset)
    {
        return asset.equals(onAsset);
    }
    
    public final BarrierParams barrierParams;
    public final PartialBarrierParams partialBarrierParams;
    public final String onAsset;
}
