
package finance.trajectories;

import finance.trajectories.Trajectory.Auxiliary;
import finance.parameters.ModelParams;
import static java.lang.Math.exp;
import static java.lang.Math.sqrt;
import java.util.Collection;
import math.utils.RandomTools;

/**
 * Creates a trajectory whose dynamics is given by equation dS = rSdS + vol S dS.
 * @author Grzegorz Los
 */
public class OneTrGenerator extends GeneratorRoot
{
    public OneTrGenerator(ModelParams params, Measure measure, TimeSupport ts)
    {
        this.S = params.getParams(1).S;
        this.ts = ts;
        this.auxStats = SimpleTrajectory.makeAllAuxiliary();
        calcAux(params, measure);
    }
    
    public OneTrGenerator(ModelParams params, Measure measure, TimeSupport ts,
            Collection<Auxiliary> auxiliary)
    {
        this.S = params.getParams(1).S;
        this.ts = ts;
        this.auxStats = auxiliary;
        calcAux(params, measure);
    }

    private void calcAux(ModelParams params, Measure measure)
    {
        switch (measure)
        {
            case REAL:
                dm = params.getParams(1).mu * ts.getDt();
                break;
            case MART:
                dm = params.getR() * ts.getDt();
        }
        dvol = params.getParams(1).vol * sqrt(ts.getDt());
    }
    
    @Override
    public Scenario generate(Anthi anthi)
    {
        switch (anthi)
        {
            case YES:
                return generateAnthi();
            default:
                return generateNoAnthi();
        }
    }
        
    protected Scenario generateNoAnthi()
    {
        SimpleTrajectory tr = new SimpleTrajectory(ts.getK(), auxStats);
        tr.set(0, S);
        for (int j = 1; j <= ts.getK(); ++j)
        {
            tr.set(j, tr.price(j-1)*exp(
                    dvol*rt.normal() + dm - dvol*dvol/2) );
        }
        tr.setReady();
        return new OneTrScenario(ts,tr);  
    }
    
    protected Scenario generateAnthi()
    {
        SimpleTrajectory pos = new SimpleTrajectory(ts.getK(), auxStats);
        SimpleTrajectory neg = new SimpleTrajectory(ts.getK(), auxStats);
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
    
    private double S, dm, dvol;
    private TimeSupport ts;
    private RandomTools rt = new RandomTools();
    private Collection<Auxiliary> auxStats;
}
