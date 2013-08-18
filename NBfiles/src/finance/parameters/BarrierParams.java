
package finance.parameters;

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
    
    public boolean isFromUp()
    {
        return type == Type.UAI || type == Type.UAO;
    }
    
    public boolean isFromDown()
    {
        return type == Type.DAI || type == Type.DAO;
    }
    
    public boolean isKnockIn()
    {
        return type == Type.UAI || type == Type.DAI;
    }
    
    public boolean isKnockOut()
    {
        return type == Type.UAO || type == Type.DAO;
    }
    
    @Override
    public String toString()
    {
        return type + "@" + level;
    }
    
    public final Type type;
    public final double level;
}