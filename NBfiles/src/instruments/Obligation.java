
package instruments;

import trajectories.Scenario;

/**
 *
 * @author grzes
 */
public class Obligation implements Instr
{
    public Obligation(TimeSupport ts)
    {
        this.ts = ts;
    }

    @Override
    public String desc()
    {
        return "Obligation paying 1 after" + ts.T + " years";
    }
    
    @Override
    public String toString()
    {
        return "Obligation";
    }

    @Override
    public boolean exAvail(Scenario s, int k)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double payoff(Scenario s, int k)
    {
        if (k < ts.K) return 0d;
        else return 1d;
    }
    
    @Override
    public TimeSupport getTS()
    {
        return ts;
    }
    
    /**
     * Time support for describing time horizon of the obligation and time 
     * points in which it is considered.
     */
    private final TimeSupport ts;


}
