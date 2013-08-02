
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
    protected Result price(Instr instr)
    {
        initPricing(instr);
        for (int i = 1 ; i <= N; ++i)
        {
            oneSimulation(instr);
            maybeNotify(i);
            maybeAddConvergencePoint(i);
        }
        return currentResult(N);
    }
    
    /**
     * From time to time call to this function will call observers
     * about the progress.
     * @param i number of the simulation currently performed.
     */
    protected void maybeNotify(int i)
    {
        if (i % 1000 == 0)
        {
            notifyObservers(new Progress("Calculating result",
                                         (int)(100L*i/N)));
        }
    }
    
    /**
     * From time to time call to this method will add to the convergence object
     * current result.
     * @param i number of the simulation currently performed.
     */
    protected void maybeAddConvergencePoint(int i)
    {
        if (i % 1000 == 0)
            convergence.add(i, currentResult(i).result);
    }
       
    /**
     * Returns name of the pricing method.
     * @return name of the pricing method.
     */
    abstract public String methodName();
    
    abstract protected void initPricing(Instr instr);

    abstract protected void oneSimulation(Instr instr);
    
    abstract protected Result currentResult(int simulation);
    
    protected ModelParams params;
    protected int N, K;
    private ObservableSupport os = new ObservableSupport();
    private Convergence convergence;
}
