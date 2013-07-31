
package finance.models;

import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import static java.lang.Math.exp;
import static java.lang.Math.max;

/**
 * Implementation of finite difference method.
 * @author Grzegorz Los
 */
public class FDModel implements ProgressObservable
{
    /**
     * Class representing a subset of a grid used in finite difference.
     */
    public static class Grid
    {
        public Grid(int I, int K, double T, double border)
        {
            this.I = I;
            this.K = K;
            ourI = Math.min(I,1000);
            ourK = Math.min(K,100);
            this.T = T;
            this.border = border;
            V = new double[ourK+1][ourI+1];
            S = new double[ourI+1];
            for (int i = 0; i <= ourI; ++i)
                S[i] = (double)i / ourI * border; 
            
            stopping = new double[ourK+1]; 
        }
        
        /**
         * Converts time index of grid to time point. 
         * @param k index.
         * @return time point.
         */
        public double timeAt(int k)
        {
            return (double)k / ourK * T;
        }
        
        /**
         * Converts asset price index of grid to real value.
         * @param i index.
         * @return asset price.
         */
        public double assetPriceAt(int i)
        {
            return S[i];
        }
        
        public double[][] getOptionPrices()
        {
            return V;
        }

        public double[] getStopping()
        {
            return stopping;
        }
        
        /**
         * For some k's will fill one column of a grid.
         * @param k Time index from finite difference method, 0 <= k <= K.
         * @param col Column with prices 
         */
        public void fill(int k, double[] col, double stoppingPrice)
        {
            if ( (long)k * ourK % K == 0)
            {
                int t = (int)( (long)k * ourK / K );
                for (int i = 0; i <= ourI; ++i)
                {
                    int j = (int) ((long)i*I/ourI);
                    V[t][i] = col[j];
                }
                stopping[t] = stoppingPrice;
            }
        }
            
        private double payoff(double assetPrice, double strike, boolean call)
        {
            if (call) return Math.max(assetPrice - strike, 0);
            else return Math.max(strike - assetPrice, 0);
        }
    
       /**
        * Number of asset steps in method.
        */
        private int I;

       /**
        * Number of time steps in method.
        */
        private int K;   
        
       /**
        * Number of asset steps in method.
        */
        private int ourI;

       /**
        * Number of time steps in method.
        */
        private int ourK;    
        
       /**
        * Option's expiration time.
        */
        private double T;    
        
        /**
         * Asset price at maximum level of the grid.
         */
        private double border;    
        
        /**
         * The grid.
         */
        private double V[][];
        
       /**
        * Asset prices at grid points.
        */
        private double S[];
        
        /**
         * Array of asset prices with minimal (for call option) or maximal
         * (for put option) for which it is worth to exercise the option.
         */
        private double[] stopping;
    }
    
    /**
     * Constructor of the model. Takes only arguments related to market and
     * an underlying asset.
     * 
     */
    public FDModel(SimpleModelParams smp)
    {
        setParams(smp);
    }

    public final void setParams(SimpleModelParams smp)
    {
        S0 = smp.S;
        vol = smp.vol;
        r = smp.r;   
    }
    
    public double getS()
    {
        return S0;
    }

    public double getR()
    {
        return r;
    }

    public double getVol()
    {
        return vol;
    }

    public double getBorder()
    {
        return border;
    }

    public Grid getLastGrid()
    {
        return grid;
    }

    /**
     * Method pricing an option. As  arguments takes only option parameters.
     * Values related to asset or market are parameters of the model and 
     * can be set in constructor or by setters.
     * @param strike Strike value of the option.
     * @param T Option's expiration time.
     * @param I Number of steps at the asset price axis.
     * @param K Number of steps at the time axis.
     * @param call Is it a call option?
     * @param american Is it an american option?
     * @return 
     */
    public double price(VanillaOptionParams vop, int I, int K)
    {
        this.I = I;
        this.K = K;
        this.strike = vop.strike;
        this.T = vop.T;
        setAuxVars();
        grid = new Grid(I, K, T, border);
        boolean call = vop.callOrPut == VanillaOptionParams.CallOrPut.CALL;
        boolean american = vop.amOrEu == VanillaOptionParams.AmOrEu.AM;
        fillLast(call);
        for (int k = K-1; k >= 0; --k)
        {
            fillPrev(k, call, american);
            if (k % 100 == 0)
              notifyObservers(new Progress("Filling grid", (int)(100*(K-k)/K)));
        }
        return result();
    }
    
    @Override
    public String toString()
    {
        return "FINITE DIFFERENCE MODEL\n" +
               "S = "+ S0 + "\n" +
               "vol = " + vol + "\n" +
               "r = " + r + "\n" +
               "K = " + K + "\n" +
               "I = " + I + "\n";                
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
    
    private void setAuxVars()
    {
        V = new double[2][I+1];
        A = new double[I+1];
        B = new double[I+1];
        C = new double[I+1];
        this.S = new double[I+1];
        border = 3*max(S0, strike);
        dS = border/I;
        dt = T/K;
        double v1 = dt/dS/dS;
        double v2 = dt/dS;
        double a,b,c;
        for (int i = 0; i <= I; ++i)
        {
            S[i] = i * dS;
            a = 0.5 * vol*vol * S[i]*S[i];
            b = r * S[i];
            c = -r;
            A[i] = v1 * a - 0.5 * v2 * b;
            B[i] = -2 * v1 * a + dt * c;
            C[i] = v1 * a + 0.5 * v2 * b;
        }
    }
    
    private void fillLast(boolean call)
    {
        for (int i = 0; i <= I; ++i)
            V[K%2][i] = (call ? max(0, S[i]-strike) : max(0, strike-S[i]));
        grid.fill(K, V[K%2], strike);
    }
    
    private void fillBorder(int k, boolean call)
    {
        if (call) {
            V[k%2][0] = 0;
            V[k%2][I] = border - strike*exp(-r*(K-k)*dt);
        } else {
            V[k%2][0] = strike*exp(-r*(K-k)*dt);
            V[k%2][I] = 0;            
        }
    }    
    
    private void fillPrev(int k, boolean call, boolean american)
    {
        fillBorder(k, call);
        int curr = k%2, last = (k+1)%2;
        for (int i = 1; i < I; ++i)
        {
            V[curr][i] = A[i]*V[last][i-1] + 
                    (1 + B[i])*V[last][i] + C[i]*V[last][i+1];
        }
        if (american)
        {
            double stop = maybeExercise(k, call);
            grid.fill(k, V[curr], stop);
        }
        else grid.fill(k, V[curr], strike);
    }
    
    private double maybeExercise(int k, boolean call)
    {
        double stop = (call ? Double.POSITIVE_INFINITY : 0);
        for (int i = 0; i <= I; ++i)
        {
            if (call && S[i] - strike > V[k%2][i])
            {
                V[k%2][i] = S[i] - strike;
                if (stop > S[i]) stop = S[i];
            }
            if (!call && strike - S[i] > V[k%2][i])
            {
                V[k%2][i] = strike - S[i];
                if (stop < S[i]) stop = S[i];
            }
        }
        return stop;
    }
    
    private double result()
    {
        int i = (int)(S0*I/border);
        double x = S0*I/border - i;
        return (1-x)*V[0][i] + x*V[0][i+1];
    }

    /**
     * Spot price for which option value is calculated.
     */
    private double S0;
    
    /**
     * Asset price at maximum level of the grid;
     */
    private double border;
    
    /**
     * Assets volatility.
     */
    private double  vol;
    
    /**
     * Interest rate.
     */
    private double r;
    
    /**
     * Strike value of the option.
     */
    private double strike;
    
    /**
     * Option's expiration time.
     */
    private double T;
    
    /**
     * Number of asset steps.
     */
    private int I;
    
    /**
     * Number of time steps.
     */
    private int K;
    
    /**
     * Asset prices at grid points.
     */
    private double S[];
    
    /**
     * Array of size 2 x (I+1). Holds value of the option from previous time step
     * and allows to fill in values of current time step.
     */
    private double V[][];
    
    /**
     * Grid from last pricing.
     */
    private Grid grid;
    
    /*
     * Values and arrays auxiliary for pricing. 
     */
    private double dS, dt, A[], B[], C[];
    
    
    private ObservableSupport os = new ObservableSupport();
}
