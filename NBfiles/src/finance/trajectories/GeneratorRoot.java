
package finance.trajectories;

import finance.methods.common.ObservableSupport;
import finance.methods.common.Progress;
import finance.methods.common.ProgressObserver;

/**
 *
 * @author Grzegorz Los
 */
public abstract class GeneratorRoot implements Generator
{
    @Override
    public Scenario[] generate(int n) throws InterruptedException
    {
        return generate(n, Anthi.NO);
    }

    @Override
    public Scenario generate()
    {
        return generate(Anthi.NO);      
    }

    @Override
    public Scenario[] generate(int n, Anthi anthi) throws InterruptedException
    {
        Scenario[] res = new Scenario[n];
        for (int i = 0; i < n; ++i)
        {
            res[i] = generate(anthi);
            maybeNotify(i, n);
            if (Thread.interrupted())
                throw new InterruptedException();
        }
        return res;
    }
    
    private void maybeNotify(int i, int n)
    {
        if (i % 100 == 0)
            notifyObservers( new Progress( "Generating trajectories",
                                            (int)((double)i/n*100) ));        
    }
    
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
