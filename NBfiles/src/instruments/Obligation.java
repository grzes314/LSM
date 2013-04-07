
package instruments;

import trajectories.Scenario;

/**
 *
 * @author grzes
 */
public class Obligation extends Instr
{
    public Obligation(TimeSupport ts)
    {
        super(ts);
    }

    @Override
    public String desc()
    {
        return "Obligation paying 1 after" + ts.getT() + " years";
    }
    
    @Override
    public String toString()
    {
        return "Obligation";
    }

    
    @Override
    public TimeSupport getTS()
    {
        return ts;
    }
    
    @Override
    protected boolean exAvail_(Scenario s, int k)
    {        
        return ts.getK() == k;
    }

    @Override
    protected double payoff_(Scenario s, int k)
    {
        return 1;
    }

    @Override
    public double intrisnicValue(double x)
    {
        return 1.0;
    }
    
    @Override
    public boolean areYou(String str)
    {
        if (str.equalsIgnoreCase("obligation"))
            return true;
        else return false;
    }    
}
