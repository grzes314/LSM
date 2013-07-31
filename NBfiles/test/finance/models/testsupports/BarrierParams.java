
package finance.models.testsupports;

/**
 *
 * @author Grzegorz Los
 */

public class BarrierParams
{
    public enum Type
    {
        UAI, UAO, DAI, DAO
    }

    public BarrierParams(Type type, double level)
    {
        this.type = type;
        this.level = level;
    }
    
    public final Type type;
    public final double level;
}