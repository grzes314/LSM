
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.methods.ObservableSupport;
import finance.methods.Progress;
import finance.methods.ProgressObservable;
import finance.methods.ProgressObserver;
import finance.parameters.ModelParams;

/**
 * Base for Monte Carlo Pricers.
 * @author Grzegorz Los
 */
public abstract class MonteCarlo implements ProgressObservable
{
    /**
     * Constructs MonteCarlo pricer using given parameters.
     * @param params model parameters.
     */
    public MonteCarlo(ModelParams params) 
    {
        setParams(params);
    }

    /**
     * Sets model parameters.
     * @param params model parameters.
     */
    public final void setParams(ModelParams params)
    {
        this.params = params;
    }
    
    @Override
    public void removeObserver(ProgressObserver ob)
    {
        os.removeObserver(ob);
    }

    @Override
    public void notifyObservers(Progress pr)
    {
        os.notifyObservers(pr);
    }

    @Override
    public void addObserver(ProgressObserver ob)
    {
        os.addObserver(ob);
    }
    
    /**
     * Calculates value of given instrument using specified numbers of 
     * replications and timesteps.
     * @param instr instrument to be priced.
     * @param N number of simulations.
     * @param K number of timesteps. 
     * @return result of pricing.
     */
    public Result price(Instr instr, int N, int K)
    {
        preparePricing(N, K);
        return price(instr);
    }

    private void preparePricing(int N, int K)
    {
        this.N = N;
        this.K = K;
        convergence = new Convergence();
    }
    
    /**
     * Method doing real pricing. It may assume that {@code N} and {@code K} are already set.
     * @param instr instrument to be priced.
     * @return result of pricing.
     */
    abstract protected Result price(Instr instr);

    /**
     * Returns {@code K} -- number of timesteps in the last pricing.
     * @return number of timesteps in the last pricing.
     */
    public int getLastK()
    {
        return K;
    }
    
    /**
     * Returns {@code N} -- number of simulations in the last pricing.
     * @return number of simulations in the last pricing.
     */
    public int getLastN()
    {
        return N;
    }

    /**
     * Returns convergence to the results of last pricing.
     * @return convergence to the results of last pricing.
     */
    public Convergence getLastConvergence()
    {
        return convergence;
    }
    
    abstract public String methodName();
        
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(methodName()).append("\n")
          .append(params.toString()).append("\n")
          .append("N = ").append(N).append("\n")
          .append("K = ").append(K).append("\n");
        return sb.toString();
    }
    
    protected void maybeNotify(int i)
    {
        if (i % 1000 == 0)
        {
            notifyObservers(new Progress("Calculating result",
                                         (int)(100*((long)N-i)/N)));
        }
    }
    
    protected void maybeAddConvergencePoint(int i, double res)
    {
        if (i % 100 == 0)
            convergence.add(i, res);
    }
        
    /*private double priceNoAnthi(Instr instr)
    {
    }
    
    private double priceAnthi(Instr instr)
    {
        Anthitetic[] scenarios = getScenariosAnthi(instr.getTS());
        convergence[0] = Double.NaN;
        double res = getPayoff(instr, scenarios[0]);
        for (int i = 1; i < N; ++i)
        {
            double p = getPayoff(instr, scenarios[i]);
            res = (double)i / (i+1) * res + p/(i+1);
            if (i % 100 == 0)
            {
                convergence[i/100] = res;
                notifyObservers(new Progress("Calculating result",
                                             (int)(100*((long)N-i)/N)));
            }
        }
        return res;        
    }
    
    private Scenario[] getScenarios(TimeSupport ts)
    {
        OneTrGenerator gen = new OneTrGenerator(smp, Measure.MART, ts);
        gen.addObserver( new ProgressObserver() {
            @Override
            public void update(Progress pr)
            {
                notifyObservers(pr);
            }
        } );
        return gen.generate(N);
    }
    
    private Anthitetic[] getScenariosAnthi(TimeSupport ts)
    {
        OneTrGenerator gen = new OneTrGenerator(smp, Measure.MART, ts);
        gen.addObserver( new ProgressObserver() {
            @Override
            public void update(Progress pr)
            {
                notifyObservers(pr);
            }
        } );
        return gen.generateAnthi(N);
    }
    
    private double getPayoff(Instr instr, Scenario s)
    {
        return instr.payoff(s, K)*Math.exp(-smp.r*instr.getTS().getT());
    }
    
    private double getPayoff(Instr instr, Anthitetic a)
    {
        return 0.5 * ( instr.payoff(a.pos, K)*Math.exp(-smp.r*instr.getTS().getT())
                     + instr.payoff(a.neg, K)*Math.exp(-smp.r*instr.getTS().getT()));
    }*/
    
    protected ModelParams params;
    protected int N, K;
    private ObservableSupport os = new ObservableSupport();
    private Convergence convergence;
}
