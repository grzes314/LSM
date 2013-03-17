
package trajectories;

import instruments.TimeSupport;
import static java.lang.Math.exp;
import static java.lang.Math.sqrt;
import models.RandomTools;

/**
 *
 * @author Grzegorz Los
 */
public class OneTrGenerator implements Generator
{

    public OneTrGenerator(double S, double m, double vol, TimeSupport ts)
    {
        this.S = S;
        this.m = m;
        this.vol = vol;
        this.ts = ts;
        
        dm = m * dm;
        dvol = vol * sqrt(ts.getDt());
    }

    @Override
    public Scenario[] generate(int n)
    {
        Scenario[] res = new Scenario[n];
        for (int i = 0; i < n; ++i)
        {
            res[i] = generate();
        }
        return res;
    }

    @Override
    public Anthitetic[] generateAnthi(int n)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Scenario generate()
    {
        SimpleTrajectory tr = new SimpleTrajectory(ts.getK());
        tr.set(0, S);
        for (int j = 1; j <= ts.getK(); ++j)
        {
            tr.set(j, tr.price(j-1)*exp(
                    dvol*rt.nextGaussian() + dm - dvol*dvol/2) );
        }
        return new OneTrScenario(ts,tr);        
    }

    double S, m, vol, dm, dvol;
    TimeSupport ts;
    RandomTools rt = new RandomTools();
}
