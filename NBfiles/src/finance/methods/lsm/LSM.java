
package finance.methods.lsm;

import finance.instruments.Instr;
import finance.methods.common.WrongModelException;
import finance.parameters.ModelParams;
import finance.parameters.SimpleModelParams;
import finance.trajectories.Generator;
import finance.trajectories.OneTrGenerator;
import finance.trajectories.Scenario;
import finance.trajectories.Trajectory;
import finance.trajectories.Trajectory.Auxiliary;
import java.util.ArrayList;
import java.util.Collection;
import math.approx.Approx;
import math.approx.Point;
import math.approx.Polynomial;


/**
 * Class calculating option prices using Longstaff-Schwartz method. This implementation allows
 * only to price instruments depending on one asset.
 * @author Grzegorz Los
 */
public class LSM extends LSMRoot
{
    public LSM(Collection<Auxiliary> auxTrStats)
    {
        this.auxTrStats = auxTrStats;
    }

    @Override
    public void setModelParams(ModelParams mp) throws WrongModelException
    {
        try {
            smp = (SimpleModelParams) mp;
        } catch (ClassCastException ex) {
            throw new WrongModelException("This implementation of Longstaff-Schwartz method "
                    + "can price instruments only in one asset models.");
        }
    }
    
    public double getS()
    {
        return smp.S;
    }

    public void setS(double S)
    {
        smp = smp.withS(S);
    }

    @Override
    public double getR()
    {
        return smp.r;
    }

    public void setR(double r)
    {
        smp = smp.withR(r);
    }

    public double getVol()
    {
        return smp.vol;
    }

    public void setVol(double vol)
    {
        smp = smp.withVol(vol);
    }
    
    public Polynomial[] getEst()
    {
        return est;
    }
    
    @Override
    public boolean isPriceable(Instr instr)
    {
        return instr.getUnderlyings().size() <= 1;
    }


    @Override
    public void initPricing(Instr instr)
    {
        est = new Polynomial[K];
    }
    
    @Override
    public Generator makeGenerator()
    {
        return new OneTrGenerator(smp, Generator.Measure.MART, ts, auxTrStats);
    }
    
    @Override
    protected FutureEstimatedFlow calcFutureEstimatedFlow(
            Scenario[] paths, CashFlow[] realizedCF, int timeStep, Instr instr)
    {
        Collection<Point> points = prepareRegression(paths, realizedCF, timeStep, instr);
        est[timeStep] = regress(points);
        return new Future(est[timeStep]);
    }
    
    private Collection<Point> prepareRegression(Scenario[] paths,
                CashFlow[] realizedCF, int timeStep, Instr instr)
    {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < N; ++i)
        {
            double val = realizedCF[i].value(ts.nrToTime(timeStep), getR());
            if (instr.payoff(paths[i], timeStep) > 0)
                points.add(new Point(paths[i].getTr(1).price(timeStep), val));
        }
        return points;
    }
    
    private Polynomial regress(Collection<Point> points)
    {      
        if (points.isEmpty())
            return new Polynomial(0.0);
        else 
        {
            Approx a = new Approx();  
            Polynomial p = a.approximate(points, Math.min(M, points.size()));
            return p;
        }
    }

    private SimpleModelParams smp;
    private Polynomial[] est;
    private Collection<Trajectory.Auxiliary> auxTrStats;
}
class Future implements FutureEstimatedFlow
{
    public Future(Polynomial p)
    {
        this.p = p;
    }
    
    @Override
    public double getEstimation(Scenario s, int timeStep)
    {
        return p.value(s.getTr(1).price(timeStep));
    }
    
    private final Polynomial p;
}