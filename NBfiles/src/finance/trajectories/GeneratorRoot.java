
package finance.trajectories;

import finance.models.ObservableSupport;
import finance.models.Progress;
import finance.models.ProgressObservable;
import finance.models.ProgressObserver;

/**
 *
 * @author Grzegorz Los
 */
public abstract class GeneratorRoot implements Generator, ProgressObservable
{
    @Override
    public Scenario[] generate(int n)
    {
        return generate(n, false);
    }

    @Override
    public Scenario generate()
    {
        return generate(false);      
    }

    @Override
    public Scenario[] generate(int n, boolean anthi)
    {
        Scenario[] res = new Scenario[n];
        for (int i = 0; i < n; ++i)
        {
            res[i] = generate(anthi);
            maybeNotify(i, n);
        }
        return res;
    }

    @Override
    public Scenario generate(boolean anthi)
    {
        if (anthi)
            return generateAnthi();
        else
            return generateNoAnthi();
    }
    
    private void maybeNotify(int i, int n)
    {
        if (i % 100 == 0)
            notifyObservers( new Progress( "Generating trajectories",
                                            (int)((double)i/n*100) ));        
    }
    

    protected abstract Scenario generateAnthi();

    protected abstract Scenario generateNoAnthi();
    
    @Override
    public void removeObserver(ProgressObserver ob)
    {
        os.removeObserver(ob);
    }

    @Override
    public void notifyObservers(Progress pr)
    {
        os.notifyObservers(pr);
    }

    @Override
    public void addObserver(ProgressObserver ob)
    {
        os.addObserver(ob);
    }

    private ObservableSupport os = new ObservableSupport();
}
