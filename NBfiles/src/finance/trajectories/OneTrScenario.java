
package finance.trajectories;

import finance.parameters.SimpleModelParams;
import java.util.ArrayList;
import java.util.Collection;

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
        this.anthi = new OneTrScenario(ts, anthi);
        this.anthi.anthi = this;
    }
    
    @Override
    public int getNumberOfAssets()
    {
        return 1;
    }

    @Override
    public Collection<String> getAssetsNames()
    {
        ArrayList<String> col = new ArrayList<>();
        col.add(SimpleModelParams.onlyAsset);
        return col;
    }

    @Override
    public boolean hasAnthi()
    {
        return anthi != null;
    }

    @Override
    public Scenario getAnthi()
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
    OneTrScenario anthi;
}
