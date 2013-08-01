
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.methods.ObservableSupport;
import finance.methods.Progress;
import finance.methods.ProgressObservable;
import finance.methods.ProgressObserver;
import finance.parameters.SimpleModelParams;
import finance.trajectories.Generator.Measure;
import finance.trajectories.OneTrGenerator;
import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;
// TODO redisign
/**
 *
 * @author Grzegorz Los
 */
public class MonteCarlo implements ProgressObservable
{
    public enum VarRedMethod
    {
        None, Anthi, Control
    }
    
    public MonteCarlo(SimpleModelParams smp) 
    {
        setParams(smp);
    }

    public final void setParams(SimpleModelParams smp)
    {
        this.smp = smp;
    }

    public int getK()
    {
        return K;
    }

    public int getN()
    {
        return N;
    }

    public double getS()
    {
        return smp.S;
    }

    public double getR()
    {
        return smp.r;
    }

    public double getVol()
    {
        return smp.vol;
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
    
    public double price(Instr instr, int N, int K, boolean anthi)
    {
        this.N = N;
        this.K = K;
        this.anthi = anthi;
        convergence = new double[N/100];
        return 0;
        //if (anthi) return priceAnthi(instr);
        //else return priceNoAnthi(instr);
    }

    public double[] getLastConvergence()
    {
        return convergence;
    }
        
    @Override
    public String toString()
    {
        return "CLASSIC MONTE CARLO MODEL\n" +
               "S = "+ smp.S + "\n" +
               "vol = " + smp.vol + "\n" +
               "r = " + smp.r + "\n" +             
               "N = " + N + "\n" +             
               "K = " + K + "\n" +
                (anthi ? "anhtitetic paths used" : "");
    }
    
    /*private double priceNoAnthi(Instr instr)
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
    
    private double convergence[];
    private SimpleModelParams smp;
    private int N, K;
    private boolean anthi;
    private ObservableSupport os = new ObservableSupport();

}
