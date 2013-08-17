
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.methods.common.ObservableSupport;
import finance.methods.common.Progress;
import finance.methods.common.ProgressObserver;
import finance.parameters.ModelParams;

/**
 * Base for Monte Carlo Pricers.
 * @author Grzegorz Los
 */
public abstract class MonteCarlo implements Method
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

    public MonteCarlo() 
    {
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
    public void setAllParams(ModelParams mp, int N, int K)
    {
        params = mp;
        this.N = N;
        this.K = K;
    }

    public int getK()
    {
        return K;
    }

    public void setK(int K)
    {
        this.K = K;
    }

    public int getN()
    {
        return N;
    }

    public void setN(int N)
    {
        this.N = N;
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
        return methodName();
    }
    
    @Override
    public String getDesc()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(methodName()).append("\n")
          .append("number of simulations: ").append(N).append("\n")
          .append("number of timesteps: ").append(K).append("\n");
        return sb.toString();        
    }
    
    @Override
    public boolean isPriceable(Instr instr)
    {
        return instr.areYou("european") || instr.areYou("bond");
    }
    
    /**
     * Calculates value of given instrument.
     * @param instr instrument to be priced.
     * @return result of pricing.
     */
    @Override
    public double price(Instr instr) throws InterruptedException
    {
        convergence = new Convergence();
        initPricing(instr);
        for (int i = 1 ; i <= N; ++i)
        {
            oneSimulation(instr);
            if (Thread.interrupted())
                throw new InterruptedException();
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
