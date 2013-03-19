
package models;

import static java.lang.Math.exp;
import static java.lang.Math.max;

/**
 *
 * @author Grzegorz Los
 */
public class FDModel implements ProgressObservable
{  
    public FDModel(double S, double vol, double r, double border, int I, int K)
    {
        this.S0 = S;
        this.vol = vol;
        this.r = r;
        this.border = border;
        this.I = I;
        this.K = K;
        V = new double[2][I+1];
        A = new double[I+1];
        B = new double[I+1];
        C = new double[I+1];
        this.S = new double[I+1];
    }

    public double getS()
    {
        return S0;
    }

    public void setS(double S)
    {
        if (S <= 0)
            throw new WrongParamException("S = " + S);
        this.S0 = S;
    }

    public double getR()
    {
        return r;
    }

    public void setR(double r)
    {
        if (r < -1)
            throw new WrongParamException("r = " + r);
        this.r = r;
    }

    public double getVol()
    {
        return vol;
    }

    public void setVol(double vol)
    {
        if (vol <= 0)
            throw new WrongParamException("vol = " + vol);
        this.vol = vol;
    }

    public double price(double strike, double T, boolean call, boolean american)
    {
        this.strike = strike;
        this.T = T;
        setAuxVars();
        fillLast(call);
        for (int k = K-1; k >= 0; --k)
        {
            fillPrev(k, call);
            if (american)
                maybeExercise(k, call);
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
    }
    
    private void fillBorder(int k, boolean call)
    {
        if (call) {
            V[k%2][0] = 0;
            V[k%2][I] = border - strike*exp(-r*k*dt);
        } else {
            V[k%2][0] = strike*exp(-r*k*dt);
            V[k%2][I] = 0;            
        }
    }    
    
    private void fillPrev(int k, boolean call)
    {
        fillBorder(k, call);
        int curr = k%2, last = (k+1)%2;
        for (int i = 1; i < I; ++i)
        {
            V[curr][i] = A[i]*V[last][i-1] + 
                    (1 + B[i])*V[last][i] + C[i]*V[last][i+1];
        }
    }
    
    private void maybeExercise(int k, boolean call)
    {
        for (int i = 1; i < I; ++i)
        {
            if (call && S[i] - strike > V[k%2][i])
                V[k%2][i] = S[i] - strike;
            if (!call && strike - S[i] > V[k%2][i])
                V[k%2][i] = strike - S[i];
        }
    }
    
    private double result()
    {
        int i = (int)(S0*I/border);
        double x = S0*I/border - i;
        return (1-x)*V[0][i] + x*V[0][i+1];
    }

    private double S0, border, vol, r, strike, T;
    private int I,K;
    private double V[][], dS, dt, S[], A[], B[], C[];
    private ObservableSupport os = new ObservableSupport();
}
