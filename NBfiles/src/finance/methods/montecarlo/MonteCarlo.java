
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.methods.common.*;
import finance.parameters.ModelParams;

/**
 * Base for Monte Carlo Pricers.
 * @author Grzegorz Los
 */
public abstract class MonteCarlo implements ProgressObservable, Method
{
    /**
     * Constructs MonteCarlo pricer using given parameters.
     * @param params model parameters.
     */
    public MonteCarlo(ModelParams params) 
    {
        setModelParams(params);
        N = K = 1;
    }


    @Override
    public final void setModelParams(ModelParams params)
    {
        this.params = params;
    }
    
    /**
     * Sets all parameters used by MonteCarlo pricer.
     * @param mp modele parameters.
     * @param N number of simulations.
     * @param K number of timesteps. 
     */
    public void setParams(ModelParams mp, int N, int K)
    {
        params = mp;
        this.N = N;
        this.K = K;
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

    public Result getLastResult()
    {
        return lastResult;
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
    
    @Override
    public boolean isPriceable(Instr instr)
    {
        return instr.areYou("european");
    }
    
    /**
     * Calculates value of given instrument.
     * @param instr instrument to be priced.
     * @return result of pricing.
     */
    @Override
    public double price(Instr instr)
    {
        convergence = new Convergence();
        initPricing(instr);
        for (int i = 1 ; i <= N; ++i)
        {
            oneSimulation(instr);
            maybeNotify(i);
            maybeAddConvergencePoint(i);
        }
        lastResult = currentResult(N);
        return lastResult.result;
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
    protected Result lastResult;
    private Convergence convergence;
}
