
package finance.trajectories;

import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Grzegorz Los
 */
public class SimpleTrajectory implements Trajectory
{
    public SimpleTrajectory(int K)
    {
        this.K = K;
        prices = new double[K+1];
        auxiliary = makeAllAuxiliary();
    }

    public SimpleTrajectory(int K, Collection<Auxiliary> auxiliary)
    {
        this.K = K;
        this.auxiliary = auxiliary;
        prices = new double[K+1];
    }

    @Override
    public int getK()
    {
        return K;
    }

    @Override
    public double price(int k)
    {
        return prices[k];
    }
    
    private void ensureReady()
    {
        if (!ready)
            throw new WrongTrajectoryState("Trajectory not set to ready");
    }    
    
    private void ensureNotReady()
    {
        if (ready)
            throw new WrongTrajectoryState("Trajectory already set to ready");
    }
    
    private void ensureIndexOK(int k)
    {
        if (k < 0 || k > K)
            throw new IllegalArgumentException("Invalid time point: " + k);
    }
    
    private void ensureIntervalOK(int beg, int end)
    {
        ensureIndexOK(beg);
        ensureIndexOK(end);
        if (beg > end)
            throw new IllegalArgumentException("Beginning of the interval greater " +
                    "than end, beg = " + beg + ", end = " +end);
    }
    
    @Override
    public int stepMax(int k)
    {
        ensureReady();
        ensureIndexOK(k);
        return stepMax[k];
    }

    @Override
    public int stepMin(int k)
    {
        ensureReady();
        ensureIndexOK(k);
        return stepMin[k];
    }

    @Override
    public double cumMax(int k)
    {
        ensureReady();
        ensureIndexOK(k);
        return cumMax[k];
    }

    @Override
    public double cumMin(int k)
    {
        ensureReady();
        ensureIndexOK(k);
        return cumMin[k];
    }

    @Override
    public double average(int beg, int end)
    {
        ensureReady();
        ensureIntervalOK(beg, end);
        if (beg == 0) {
            return cumSum[end] / (end+1);
        } else {
            return (cumSum[end] - cumSum[beg-1]) / (end+1-beg);
        }
    }
    
    /**
     * Sets value of trajectory at time point k.
     * @param k timepoint.
     * @param v value.
     */
    public void set(int k, double v)
    {
        ensureNotReady();
        prices[k] = v;
    }
    
    public void setReady()
    {   
        if (auxiliary.contains(Auxiliary.CUMMIN))
            setCumMin();
        if (auxiliary.contains(Auxiliary.CUMMAX))
            setCumMax();
        if (auxiliary.contains(Auxiliary.AVERAGE))
            setAverage();
        ready = true;        
    }
    
    private void setAverage()
    {
        cumSum = new double[K+1];
        cumSum[0] = prices[0];
        for (int i = 1; i <= K; ++i)
            cumSum[i] = cumSum[i-1] + prices[i];
    }
    
    private void setCumMin()
    {
        stepMin = new int[K+1];
        cumMin = new double[K+1];
        cumMin[0] = prices[0];    
        stepMin[0] = 0;
        for (int i = 1; i <= K; ++i)
        {
            cumMin[i] = (prices[i] < cumMin[i-1] ? prices[i] : cumMin[i-1]);
            stepMin[i] = (prices[i] < cumMin[i-1] ? i : stepMin[i-1]);
        }           
    }
    
    private void setCumMax()
    {        
        stepMax = new int[K+1];
        cumMax = new double[K+1];
        cumMax[0] = prices[0];   
        stepMax[0] = 0;
        for (int i = 1; i <= K; ++i)
        {
            cumMax[i] = (prices[i] > cumMax[i-1] ? prices[i] : cumMax[i-1]);
            stepMax[i] = (prices[i] > cumMax[i-1] ? i : stepMax[i-1]);
        }    
    }
    
    public static Collection<Auxiliary> makeAllAuxiliary()
    {
        Collection<Auxiliary> auxiliary = new HashSet<>();
        auxiliary.add(Auxiliary.AVERAGE);
        auxiliary.add(Auxiliary.CUMMIN);
        auxiliary.add(Auxiliary.CUMMAX);
        return auxiliary;
    }
       
    private int K;
    private double[] prices;
    private int[] stepMax, stepMin;
    private double[] cumMax, cumMin, cumSum;
    private boolean ready;
    private Collection<Auxiliary> auxiliary;
}
