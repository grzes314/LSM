
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
        this.anthi = null;
    }

    public OneTrScenario(TimeSupport ts, Trajectory tr, Trajectory anthi)
    {
        this.ts = ts;
        this.tr = tr;
        this.anthi = anthi;
    }

    @Override
    public boolean hasAnthi()
    {
        return anthi != null;
    }

    @Override
    public Trajectory getAnthi(int nr)
    {
        if (anthi == null)
            throw new AnthiteticPathNotAvailableException();
        else
            return anthi;
    }

    @Override
    public Trajectory getAnthi(String name)
    {
        if (anthi == null)
            throw new AnthiteticPathNotAvailableException();
        else
            return anthi;
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
    Trajectory anthi;
}
