
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.parameters.ModelParams;
import finance.trajectories.Generator;
import finance.trajectories.MultiTrGenerator;
import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;

/**
 *
 * @author Grzegorz Los
 */
public class AV extends MonteCarlo
{
    public AV(ModelParams params)
    {
        super(params);
    }

    @Override
    public String methodName()
    {
        return "Monte Carlo with anthitetic variates";
    }

    @Override
    protected void initPricing(Instr instr)
    {
        TimeSupport ts = new TimeSupport(instr.getT(), K);
        gen = new MultiTrGenerator(params, Generator.Measure.MART, ts);
        sum = sumSq = 0;
    }

    @Override
    protected void oneSimulation(Instr instr)
    {
        Scenario scenario = gen.generate(Generator.Anthi.YES);
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
        Scenario pos = scenario, neg = scenario.getAnthi();
        return 0.5 * ( instr.payoff(pos, K)*Math.exp(-params.getR()*instr.getT())
                     + instr.payoff(neg, K)*Math.exp(-params.getR()*instr.getT()) );
    }
    
    private Generator gen;
    private double sum, sumSq;
}
