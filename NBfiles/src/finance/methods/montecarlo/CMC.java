
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.parameters.ModelParams;
import finance.trajectories.*;
import finance.trajectories.Generator.Measure;
import java.util.Collection;

/**
 *
 * @author Grzegorz Los
 */
public class CMC extends MonteCarlo
{
    public CMC(ModelParams params)
    {
        super(params);
    }
    
    public CMC()
    {
        
    }
    
    @Override
    public String methodName()
    {
        return "Crude Monte Carlo";
    }
    
    @Override
    protected void initPricing(Instr instr)
    {
        TimeSupport ts = new TimeSupport(instr.getT(), K);
        gen = new MultiTrGenerator(params, Measure.MART, ts);
        sum = sumSq = 0;
    }

    @Override
    protected void oneSimulation(Instr instr)
    {
        Scenario scenario = gen.generate();
        double p = getDiscountedPayoff(instr, scenario);
        sum += p;
        sumSq += p*p;
    }
    
    @Override
    protected Result currentResult(int simulation)
    {
        double res = sum / simulation;
        double var = (sumSq - sum*sum/simulation) / (simulation-1);
        double se  = Math.sqrt(var / simulation); 
        return new Result(res, se);
    }

    private double getDiscountedPayoff(Instr instr, Scenario scenario)
    {
        return instr.payoff(scenario, K) * 
                Math.exp(-params.getR()*instr.getT());
    }
    
    @Override
    protected void passDividendsToGenerator(Collection<Dividend> dividends)
    {
        gen.setDividends(dividends);
    }
    
    private Generator gen;
    private double sum, sumSq;
}
