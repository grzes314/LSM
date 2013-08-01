
package finance.trajectories;

import finance.models.ObservableSupport;
import finance.models.Progress;
import finance.models.ProgressObservable;
import finance.models.ProgressObserver;
import finance.parameters.ModelParams;
import static java.lang.Math.exp;
import static java.lang.Math.sqrt;
import math.utils.RandomTools;

/**
 * Creates a trajectory whose dynamics is given by equation dS = rSdS + vol S dS.
 * @author Grzegorz Los
 */
public class OneTrGenerator implements Generator, ProgressObservable
{
    public OneTrGenerator(ModelParams params, Measure measure, TimeSupport ts)
    {
        this.S = params.getParams(1).S;
        this.ts = ts;
        calcAux(params, measure);
    }

    private void calcAux(ModelParams params, Measure measure)
    {
        switch (measure)
        {
            case REAL:
                dm = params.getParams(1).mu * ts.getDt();
                break; // TODO Upewnic sie ze break jest potrzebny
            case MART:
                dm = params.getR() * ts.getDt();
        }
        dvol = params.getParams(1).vol * sqrt(ts.getDt());
    }
    
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
    
    private Scenario generateNoAnthi()
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
    
    private Scenario generateAnthi()
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
        return new OneTrScenario(ts, pos, neg);        
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
