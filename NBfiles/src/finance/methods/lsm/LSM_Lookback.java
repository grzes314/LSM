
package finance.methods.lsm;

import finance.instruments.Instr;
import finance.methods.common.WrongModelException;
import finance.parameters.ModelParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import finance.trajectories.*;
import java.util.HashSet;

/**
 * Class calculating option prices using Longstaff-Schwartz method. This implementation allows
 * to price American lookback options.
 * @author Grzegorz Los
 */
public class LSM_Lookback extends LSMRoot
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
        
        return instr.areYou("lookback") && instr.getUnderlyings().size() <= 1;
    }

    @Override
    protected void initPricing(Instr instr)
    {
        type = instr.areYou("call") ? VanillaOptionParams.CallOrPut.CALL
                                    : VanillaOptionParams.CallOrPut.PUT;
    }

    @Override
    protected Generator makeGenerator()
    {
        return new GeneratorForLookback(smp, ts, type);
    }

    @Override
    protected FutureEstimatedFlow calcFutureEstimatedFlow(Scenario[] paths, CashFlow[] realizedCF,
        int timeStep, Instr instr)
    {
        LSM_OnTwoAssetsSupport support = new LSM_OnTwoAssetsSupport(smp.r, M);
        return support.calcFutureEstimatedFlow(paths, realizedCF, timeStep, instr);
    }
    
    protected VanillaOptionParams.CallOrPut type;
    protected SimpleModelParams smp;
}

class GeneratorForLookback extends GeneratorRoot
{

    public GeneratorForLookback(SimpleModelParams smp, TimeSupport ts, VanillaOptionParams.CallOrPut type)
    {
        HashSet<SimpleTrajectory.Auxiliary> stats = new HashSet<>();
        if (type == VanillaOptionParams.CallOrPut.CALL)
            stats.add(Trajectory.Auxiliary.CUMMIN);
        else
            stats.add(Trajectory.Auxiliary.CUMMAX);
        generator = new OneTrGenerator(smp, Generator.Measure.MART, ts);
        this.smp = smp;
        this.ts = ts;
        this.type = type;
    }
    
    @Override
    public Scenario generate(Generator.Anthi anthi)
    {
        if (anthi == Generator.Anthi.YES)
            throw new UnsupportedOperationException("Not supported yet.");
        Scenario s = generator.generate(anthi);
        Trajectory tr = s.getTr(1);
        Trajectory snd = makeSndTrajectory(tr);
        return new MultiTrScenario( ts, new String[]{"", smp.name, "###SND###"},
            new Trajectory[]{null, tr, snd} );
    }

    private Trajectory makeSndTrajectory(Trajectory tr)
    {
        if (type == VanillaOptionParams.CallOrPut.CALL)
            return makeMinTrajectory(tr);
        else
            return makeMaxTrajectory(tr);
    }    

    private Trajectory makeMinTrajectory(Trajectory tr)
    {
        SimpleTrajectory minTr = new SimpleTrajectory(ts.getK());
        double min = tr.price(0);
        for (int k = 0; k <= ts.getK(); ++k)
        {
            if (min > tr.price(k))
                min = tr.price(k);
            minTr.set(k, min);
        }
        return minTr;
    }
    
    private Trajectory makeMaxTrajectory(Trajectory tr)
    {
        SimpleTrajectory maxTr = new SimpleTrajectory(ts.getK());
        double max = tr.price(0);
        for (int k = 0; k <= ts.getK(); ++k)
        {
            if (max < tr.price(k))
                max = tr.price(k);
            maxTr.set(k, max);
        }
        return maxTr;
    }
    
    protected VanillaOptionParams.CallOrPut type;
    private SimpleModelParams smp;
    private TimeSupport ts;
    private OneTrGenerator generator;
}
