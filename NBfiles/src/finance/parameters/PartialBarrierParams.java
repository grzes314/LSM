
package finance.parameters;

/**
 *
 * @author glos
 */
public class PartialBarrierParams
{
    public enum Modification
    {
        OFF, ON
    }

    public PartialBarrierParams(BarrierParams barrierParams,
            Modification mod, double since, double until)
    {
        this.barrierParams = barrierParams;
        this.mod = mod;
        this.since = since;
        this.until = until;
    }
    
    public final BarrierParams barrierParams;
    public final Modification mod;
    public final double since;
    public final double until;
}
