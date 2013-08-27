
package finance.methods.lsm;

import finance.instruments.Instr;
import finance.methods.common.WrongModelException;
import finance.parameters.ModelParams;
import finance.trajectories.Generator;
import finance.trajectories.MultiTrGenerator;
import finance.trajectories.Scenario;
import finance.trajectories.Trajectory.Auxiliary;
import java.util.ArrayList;
import java.util.Collection;
import math.approx.Approx;
import math.approx.Point3D;
import math.approx.Polynomial2;



/**
 * Class calculating option prices using Longstaff-Schwartz method. This implementation allows
 * only to price instruments depending on exactly two assets.
 * @author Grzegorz Los
 */
public class LSM2 extends LSMRoot
{

    public LSM2(Collection<Auxiliary> auxTrStats)
    {
        this.auxTrStats = auxTrStats;
    }
    
    @Override
    public void setModelParams(ModelParams mp) throws WrongModelException
    {
        if (mp.getNumberOfAssets() != 2)
            throw new WrongModelException("This implementation of Longstaff-Schwartz method"
                    + " can use only one or two assets models.");
        this.mp = mp;
    }
    
    @Override
    public double getR()
    {
        return mp.getR();
    }
    
    @Override
    public boolean isPriceable(Instr instr)
    {
        return instr.getUnderlyings().size() == 2;
    }

    @Override
    protected void initPricing(Instr instr)
    {
    }

    @Override
    protected Generator makeGenerator()
    {
        return new MultiTrGenerator(mp, Generator.Measure.MART, ts, auxTrStats);
    }

    @Override
    protected FutureEstimatedFlow calcFutureEstimatedFlow(Scenario[] paths, CashFlow[] realizedCF,
    int timeStep, Instr instr)
    {
        Collection<Point3D> points = prepareRegression(paths, realizedCF, timeStep, instr);
        Polynomial2 p = regress(points);
        return new Future2(p);
    }

    private Collection<Point3D> prepareRegression(Scenario[] paths, CashFlow[] realizedCF,
            int timeStep, Instr instr)
    {
        ArrayList<Point3D> points = new ArrayList<>();
        for (int i = 0; i < N; ++i)
        {
            double val = realizedCF[i].value(ts.nrToTime(timeStep), getR());
            if (instr.payoff(paths[i], timeStep) > 0)
                points.add( new Point3D( paths[i].getTr(1).price(timeStep),
                        paths[i].getTr(2).price(timeStep), val )  );
        }
        return points;
    }

    private Polynomial2 regress(Collection<Point3D> points)
    {      
        if (points.isEmpty())
            return new Polynomial2(0.0);
        else 
        {
            Approx a = new Approx();  
            Polynomial2 p = a.approximate2(points, Math.min(M, points.size()));
            return p;
        }
    }

    private ModelParams mp;
    private Collection<Auxiliary> auxTrStats;
}

class Future2 implements FutureEstimatedFlow
{
    public Future2(Polynomial2 p)
    {
        this.p = p;
    }

    @Override
    public double getEstimation(Scenario s, int timeStep)
    {
        return p.value(s.getTr(1).price(timeStep), s.getTr(2).price(timeStep));
    }
    
    Polynomial2 p;
}
