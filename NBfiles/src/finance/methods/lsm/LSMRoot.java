
package finance.methods.lsm;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.methods.common.Progress;
import finance.methods.common.ProgressObserver;
import finance.methods.montecarlo.Result;
import finance.trajectories.Generator;
import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;
import java.util.LinkedList;
import java.util.List;
import math.matrices.Vector;
import math.utils.Statistics;

/**
 *
 * @author Grzegorz Los
 */
abstract public class LSMRoot implements Method
{
    public int getK()
    {
        return K;
    }

    public void setK(int K)
    {
        this.K = K;
    }

    public int getM()
    {
        return M;
    }

    public void setM(int M)
    {
        this.M = M;
    }

    public int getN()
    {
        return N;
    }

    public void setN(int N)
    {
        this.N = N;
    }
    
    public void setMethodParams(int N, int K, int M)
    {
        this.N = N;
        this.K = K;
        this.M = M;
    }
    
    abstract public double getR();
    
    @Override
    public double price(Instr instr) throws InterruptedException
    {        
        initPricing(instr);
        ts = new TimeSupport(instr.getT(), K);
        Generator gen = makeGenerator();
        gen.addObserver( new ProgressObserver() {
            @Override public void update(Progress pr) {
                notifyObservers(pr);
            }
        } );
        Scenario[] paths = gen.generate(N);
        CashFlow[] realizedCF = realizedCashFlows(paths, instr);
        double payoffAtTime0 = instr.payoff(paths[0], 0);
        paths = null; // resources can now be fried
        //System.gc();
        Result res = makeResult(realizedCF);
        return Math.max(res.result, payoffAtTime0);
    }
    
    abstract protected void initPricing(Instr instr);
    
    abstract protected Generator makeGenerator();
    
    protected CashFlow[] realizedCashFlows(Scenario[] paths, Instr instr)
            throws InterruptedException
    {
        CashFlow[] realizedCF = new CashFlow[N];
        for (int i = 0; i < N; ++i)
            realizedCF[i] = new CashFlow( instr.payoff(paths[i], K), ts.getT() );
        for (int j = K-1; j > 0; --j)
        {
            if (Thread.interrupted())
                throw new InterruptedException();
            FutureEstimatedFlow fef = calcFutureEstimatedFlow(paths, realizedCF, j, instr);
            updateRealizedCFs(paths, instr, realizedCF, fef, j);
            notifyObservers(new Progress("Regressing", (int)((K-j)*100/K)));
        }        
        return realizedCF;
    }

    abstract protected FutureEstimatedFlow calcFutureEstimatedFlow(
            Scenario[] paths, CashFlow[] realizedCF, int timeStep, Instr instr);
    

    private void updateRealizedCFs(Scenario[] paths, Instr instr, CashFlow[] realizedCF,
            FutureEstimatedFlow fef, int timeStep)
    {
        for (int i = 0; i < N; ++i)
        {
            double expected = fef.getEstimation(paths[i], timeStep);
            double payoff = instr.payoff(paths[i], timeStep);
            if (payoff > 0 && payoff > expected)
                realizedCF[i] = new CashFlow(payoff, ts.nrToTime(timeStep));
        }
    }
    
    protected Result makeResult(CashFlow[] realizedCF)
    {
        Vector payoffs = new Vector(N);
        for (int i = 1; i <= N; ++i)
            payoffs.set(i, realizedCF[i-1].value(getR()));
        double mean = Statistics.mean(payoffs);
        double stderr = Math.sqrt( Statistics.var(payoffs) / N );
        lastResult = new Result(mean, stderr);
        return lastResult;
    }

    @Override
    public String toString()
    {
        return "Longstaff-Schwartz method";
    }

    @Override
    public String getDesc()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Longstaff-Schwartz method").append("\n")
          .append("number of trajectories: ").append(N).append("\n")
          .append("number of time steps: ").append(K).append("\n")
          .append("degree of approximating polynomial: ").append(M).append("\n");
        return sb.toString();  
    }
        
    @Override
    public void addObserver(ProgressObserver ob)
    {
        observers.add(ob);
    }

    @Override
    public void removeObserver(ProgressObserver ob)
    {
        observers.remove(ob);
    }

    @Override
    public void notifyObservers(Progress pr)
    {
        for (ProgressObserver ob: observers)
            ob.update(pr);
    }

    public Result getLastResult()
    {
        return lastResult;
    }
    
    private Result lastResult;
    protected int N, K, M;
    protected TimeSupport ts;
    private List<ProgressObserver> observers = new LinkedList<>();
}
