
package finance.methods.lsm;

import finance.instruments.Instr;
import finance.methods.common.WrongModelException;
import finance.parameters.ModelParams;
import finance.parameters.SimpleModelParams;
import finance.trajectories.*;
import java.util.HashSet;

/**
 * Class calculating option prices using Longstaff-Schwartz method. This implementation allows
 * to price American-Asian options.
 * @author Grzegorz Los
 */
public class LSM_Asian extends LSMRoot
{
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
    
    @Override
    public double getR()
    {
        return smp.r;
    }

    @Override
    public boolean isPriceable(Instr instr)
    {
        
        return instr.areYou("asian") && instr.getUnderlyings().size() <= 1;
    }

    @Override
    protected void initPricing(Instr instr)
    {
    }

    @Override
    protected Generator makeGenerator()
    {
        return new GeneratorForAsian(smp, ts);
    }

    @Override
    protected FutureEstimatedFlow calcFutureEstimatedFlow(Scenario[] paths, CashFlow[] realizedCF,
        int timeStep, Instr instr)
    {
        LSM_OnTwoAssetsSupport support = new LSM_OnTwoAssetsSupport(smp.r, M);
        return support.calcFutureEstimatedFlow(paths, realizedCF, timeStep, instr);
    }
    
    protected SimpleModelParams smp;
}

class GeneratorForAsian extends GeneratorRoot
{
    public GeneratorForAsian(SimpleModelParams smp, TimeSupport ts)
    {
        HashSet<SimpleTrajectory.Auxiliary> stats = new HashSet<>();
        stats.add(Trajectory.Auxiliary.AVERAGE);
        generator = new OneTrGenerator(smp, Generator.Measure.MART, ts, stats);
        this.smp = smp;
        this.ts = ts;
    }
    
    @Override
    public Scenario generate(Anthi anthi)
    {
        if (anthi == Generator.Anthi.YES)
            throw new UnsupportedOperationException("Not supported yet.");
        Scenario s = generator.generate(anthi);
        Trajectory tr = s.getTr(1);
        Trajectory avg = makeAvgTrajectory(tr);
        return new MultiTrScenario( ts, new String[]{"", smp.name, "###AVG###"},
            new Trajectory[]{null, tr, avg} );
    }


    private Trajectory makeAvgTrajectory(Trajectory tr)
    {
        SimpleTrajectory avg = new SimpleTrajectory(ts.getK(), new HashSet<SimpleTrajectory.Auxiliary>());
        double sum = 0;
        for (int k = 0; k <= ts.getK(); ++k)
        {
            sum += tr.price(k);
            avg.set(k, sum / (k+1));
        }
        avg.setReady();
        return avg;
    }
    
    private SimpleModelParams smp;
    private TimeSupport ts;
    private OneTrGenerator generator;
}
