
package finance.instruments;

import finance.trajectories.Scenario;

/**
 *
 * @author grzes
 */
public class Bond extends Instr
{
    public Bond(double T)
    {
        super(T);
    }

    @Override
    public String desc()
    {
        return "Bond paying 1 after" + getT() + " years";
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
        if (str.equalsIgnoreCase("bond"))
            return true;
        else return false;
    }    
}
