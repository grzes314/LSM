
package finance.methods.lsm;

import finance.instruments.Instr;
import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;
import java.util.ArrayList;
import java.util.Collection;
import math.approx.Approx;
import math.approx.Point3D;
import math.approx.Polynomial2;

/**
 * Class encapsulates some logic common for versions of LSM for several types of derivatives.
 * @author Grzegorz Los
 */
public class LSM_OnTwoAssetsSupport
{

    public LSM_OnTwoAssetsSupport(double r, int M)
    {
        this.r = r;
        this.M = M;
    }
    
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
        int N = paths.length;
        TimeSupport ts = paths[0].getTS();
        ArrayList<Point3D> points = new ArrayList<>();
        for (int i = 0; i < N; ++i)
        {
            double val = realizedCF[i].value(ts.nrToTime(timeStep), r);
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

    private double r;
    int M;
}
