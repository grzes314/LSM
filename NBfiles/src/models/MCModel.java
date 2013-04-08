
package models;

import instruments.Instr;
import instruments.TimeSupport;
import trajectories.Anthitetic;
import trajectories.OneTrGenerator;
import trajectories.Scenario;

/**
 *
 * @author Grzegorz Los
 */
public class MCModel implements ProgressObservable
{
    public MCModel(double S, double vol, double r, int N, int K, boolean anthi) 
    {
        this.S = S;
        this.vol = vol;
        this.r = r;
        this.N = N;
        this.K = K;
        this.anthi = anthi;
        convergence = new double[N/100];
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

    public double getS()
    {
        return S;
    }

    public void setS(double S)
    {
        this.S = S;
    }

    public double getR()
    {
        return r;
    }

    public void setR(double r)
    {
        this.r = r;
    }

    public double getVol()
    {
        return vol;
    }

    public void setVol(double vol)
    {
        this.vol = vol;
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
    
    public double price(Instr instr)
    {
        if (anthi) return priceAnthi(instr);
        else return priceNoAnthi(instr);
    }

    public double[] getLastConvergence()
    {
        return convergence;
    }
        
    @Override
    public String toString()
    {
        return "CLASSIC MONTE CARLO MODEL\n" +
               "S = "+ S + "\n" +
               "vol = " + vol + "\n" +
               "r = " + r + "\n" +             
               "N = " + N + "\n" +             
               "K = " + K + "\n" +
                (anthi ? "anhtitetic paths used" : "");
    }
    
    private double priceNoAnthi(Instr instr)
    {
        Scenario[] scenarios = getScenarios(instr.getTS());
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
        OneTrGenerator gen = new OneTrGenerator(S, r, vol, ts);
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
        OneTrGenerator gen = new OneTrGenerator(S, r, vol, ts);
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
        return instr.payoff(s, K)*Math.exp(-r*instr.getTS().getT());
    }
    
    private double getPayoff(Instr instr, Anthitetic a)
    {
        return 0.5 * ( instr.payoff(a.pos, K)*Math.exp(-r*instr.getTS().getT())
                     + instr.payoff(a.neg, K)*Math.exp(-r*instr.getTS().getT()));
    }
    
    private double convergence[];
    private double S, vol, r;
    private int N, K;
    private boolean anthi;
    private ObservableSupport os = new ObservableSupport();

}
