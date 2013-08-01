
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.parameters.ModelParams;
import finance.trajectories.Generator;
import finance.trajectories.Generator.Measure;
import finance.trajectories.MultiTrGenerator;
import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;

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
    
    @Override
    protected Result price(Instr instr)
    {
        makeGenerator(instr.getT());
        sum = sumSq = 0;
        for (int i = 0; i < N; ++i)
        {
            Scenario scenario = gen.generate();
            double p = getDiscountedPayoff(instr, scenario);
            sum += p;
            sumSq += p*p;
            maybeNotify(i);
        }
        double res = sum / N;
        double var = (sumSq - sum*sum/N) / (N-1);
        double se  = Math.sqrt(var / N); 
        return new Result(res, se);
    }
    
    private void makeGenerator(double timeHorizon)
    {
        TimeSupport ts = new TimeSupport(timeHorizon, K);
        gen = new MultiTrGenerator(params, Measure.MART, ts);
    }

    private double getDiscountedPayoff(Instr instr, Scenario scenario)
    {
        return instr.payoff(scenario, K) * 
                Math.exp(-params.getR()*instr.getT());
    }
    
    @Override
    public String methodName()
    {
        return "Crude Monte Carlo";
    }

    private Generator gen;
    private double sum, sumSq;

}
