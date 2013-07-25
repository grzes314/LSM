
package trajectories;

import static java.lang.Math.exp;
import static java.lang.Math.sqrt;
import math.utils.RandomTools;
import models.ObservableSupport;
import models.Progress;
import models.ProgressObservable;
import models.ProgressObserver;

/**
 * Creates a trajectory whose dynamics is given by equation dS = rSdS + vol S dS.
 * @author Grzegorz Los
 */
public class OneTrGenerator implements Generator, ProgressObservable
{

    public OneTrGenerator(double S, double m, double vol, TimeSupport ts)
    {
        this.S = S;
        this.ts = ts;
        
        dm = m * ts.getDt();
        dvol = vol * sqrt(ts.getDt());
    }

    @Override
    public Scenario[] generate(int n)
    {
        Scenario[] res = new Scenario[n];
        for (int i = 0; i < n; ++i)
        {
            res[i] = generate();
            if (i % 100 == 0)
                notifyObservers( new Progress( "Generating trajectories",
                                               (int)((double)i/n*100) ));
        }
        return res;
    }

    @Override
    public Anthitetic[] generateAnthi(int n)
    {
        Anthitetic[] res = new Anthitetic[n];
        for (int i = 0; i < n; ++i)
        {
            res[i] = generateAnthi();
            if (i % 100 == 0)
                notifyObservers( new Progress( "Generating trajectories",
                                               (int)((double)i/n*100) ));
        }
        return res;
    }

    private Scenario generate()
    {
        SimpleTrajectory tr = new SimpleTrajectory(ts.getK());
        tr.set(0, S);
        for (int j = 1; j <= ts.getK(); ++j)
        {
            tr.set(j, tr.price(j-1)*exp(
                    dvol*rt.normal() + dm - dvol*dvol/2) );
        }
        tr.setReady();
        return new OneTrScenario(ts,tr);        
    }
    
    private Anthitetic generateAnthi()
    {
        SimpleTrajectory pos = new SimpleTrajectory(ts.getK());
        SimpleTrajectory neg = new SimpleTrajectory(ts.getK());
        pos.set(0, S);
        neg.set(0, S);
        for (int j = 1; j <= ts.getK(); ++j)
        {
            double N = rt.normal();
            pos.set(j, pos.price(j-1)*exp(
                    dvol*N + dm - dvol*dvol/2) );
            neg.set(j, neg.price(j-1)*exp(
                    dvol*(-N) + dm - dvol*dvol/2) );
        }
        pos.setReady();
        neg.setReady();
        return new Anthitetic(new OneTrScenario(ts,pos), new OneTrScenario(ts,neg));        
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

    
    private double S, dm, dvol;
    private TimeSupport ts;
    private RandomTools rt = new RandomTools();
    private ObservableSupport os = new ObservableSupport();
}
