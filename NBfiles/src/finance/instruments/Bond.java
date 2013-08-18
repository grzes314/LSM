
package finance.instruments;

import finance.trajectories.Scenario;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author grzes
 */
public class Bond extends Instr
{
    public Bond(double T)
    {
        this(1.0, T);
    }

    public Bond(double nominal, double T)
    {
        super(T);
        this.nominal = nominal;
    }

    @Override
    public String getDesc()
    {
        return "Bond paying " + nominal + " after " + getT() + " years";
    }
    
    @Override
    public String toString()
    {
        return "Bond";
    }

    @Override
    protected boolean exAvail_(Scenario s, int k)
    {        
        return s.getTS().getK() == k;
    }

    @Override
    protected double payoff_(Scenario s, int k)
    {
        return nominal;
    }
    
    @Override
    public boolean areYou(String str)
    {
        if (str.equalsIgnoreCase("bond"))
            return true;
        else return false;
    }    

    public double getNominal()
    {
        return nominal;
    }
    
    @Override
    public int modificationsCount()
    {
        return 0;
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        return new ArrayList<>();
    }
    
    double nominal;
}
