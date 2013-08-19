
package finance.parameters;

/**
 * Parameters describing partial barriers.
 * @author Grzegorz Los
 */
public class PartialBarrierParams
{
    public enum PartType
    {
        EARLY, LATE
    }

    public PartialBarrierParams(BarrierParams barrierParams, PartType type, double borderTime)
    {
        this.barrierParams = barrierParams;
        this.type = type;
        this.borderTime = borderTime;
    }
    
    public final BarrierParams barrierParams;
    public final PartType type;
    public final double borderTime;
}
