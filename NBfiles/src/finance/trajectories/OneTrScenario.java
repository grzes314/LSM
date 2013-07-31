
package finance.trajectories;

/**
 *
 * @author Grzegorz Los
 */
public class OneTrScenario implements Scenario
{
    public OneTrScenario(TimeSupport ts, Trajectory tr)
    {        
        this.ts = ts;
        this.tr = tr;
    }
    
    @Override
    public Trajectory getTr(int nr)
    {
        return tr;
    }

    @Override
    public Trajectory getTr(String name)
    {
        return tr;
    }

    @Override
    public TimeSupport getTS()
    {
        return ts;
    }
    
    TimeSupport ts;
    Trajectory tr;
}
