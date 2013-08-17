
package finance.methods.lsm;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.methods.common.Progress;
import finance.methods.common.ProgressObserver;
import finance.methods.common.WrongModelException;
import finance.parameters.ModelParams;
import finance.parameters.SimpleModelParams;
import finance.trajectories.Generator;
import finance.trajectories.OneTrGenerator;
import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;
import static java.lang.Math.exp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import math.approx.Approx;
import math.approx.Point;
import math.approx.Polynomial;




/**
 * Class calculating option prices using Longstaff-Schwartz method. This implementation allows
 * only to price instruments depending on one asset.
 * @author Grzegorz Los
 */
public class LSM implements Method
{

    public LSM()
    {
    }
    
    public LSM(double S, double vol, double r, int N, int K, int M) 
    {
        this.S = S;
        this.vol = vol;
        this.r = r;
        this.N = N;
        this.K = K;
        this.M = M;
    }

    @Override
    public void setModelParams(ModelParams mp) throws WrongModelException
    {
        SimpleModelParams smp;
        try {
            smp = (SimpleModelParams) mp;
        } catch (ClassCastException ex) {
            throw new WrongModelException("Finite Diffrence method can price instruments"
                + " only in one asset models.");
        }
        S = smp.S;
        vol = smp.vol;
        r = smp.r;
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

    public Polynomial[] getEst()
    {
        return est;
    }
    

    @Override
    public boolean isPriceable(Instr instr)
    {
        return instr.getUnderlyings().size() <= 1;
    }
    
    @Override
    public double price(Instr instr) throws InterruptedException
    {        
        est = new Polynomial[K];
        ts = new TimeSupport(instr.getT(), K);
        OneTrGenerator gen = new OneTrGenerator(new SimpleModelParams(S, vol, r),
                Generator.Measure.MART, ts);
        gen.addObserver( new ProgressObserver() {
            @Override public void update(Progress pr) {
                notifyObservers(pr);
            }
        } );
        
        Scenario[] paths = gen.generate(N);
        
        CF[] bestCF = bestCFlows(paths, instr);
        double mean = getMean(bestCF);
        return mean;
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
    
    private CF[] bestCFlows(Scenario[] paths, Instr instr) throws InterruptedException
    {
        CF[] res = new CF[N];
        for (int i = 0; i < N; ++i)
        {
            res[i] = new CF( instr.payoff(paths[i], K), K );
        }
        for (int j = K-1; j > 0; --j)
        {
            if (Thread.interrupted())
                throw new InterruptedException();
            Collection<Point> points = prepareRegression(paths, res, j, instr);
            est[j] = regress(points);
            updateBestCFlows(paths, instr, res, est[j], j);
            
            notifyObservers(new Progress("Regressing", (int)((K-j)*100/K)));
        }        
        return res;
    }
    
    private void updateBestCFlows(Scenario[] paths, Instr instr,
            CF[] bestCF, Polynomial p, int j)
    {
        for (int i = 0; i < N; ++i)
        {
            double expected = p.value(paths[i].getTr(1).price(j));
            double payoff = instr.payoff(paths[i], j);
            if (payoff > 0 && payoff > expected)
                bestCF[i] = new CF(payoff, j);
        }
    }
    
    private Collection<Point> prepareRegression(Scenario[] paths,
                CF[] bestCF, int j, Instr instr)
    {
        ArrayList<Point> points = new ArrayList<>();
        double dr = r * ts.getT() / ts.getK();
        for (int i = 0; i < N; ++i)
        {
            double val = bestCF[i].x * exp((j-bestCF[i].t)*dr);
            if (instr.payoff(paths[i], j) > 0)
            {
                points.add(new Point(paths[i].getTr(1).price(j), val));                
            }
        }
        return points;
    }
    
    private Polynomial regress(Collection<Point> points)
    {      
        if (points.isEmpty())
            return new Polynomial(0.0);
        else 
        {
            Approx a = new Approx();  
            Polynomial p = a.approximate(points, Math.min(M, points.size()));
            return p;
        }
    }
    
    private double getMean(CF[] bestCF)
    {
        double sum = 0;
        double dr = r * ts.getT() / ts.getK();
        for (int i = 0; i < N; ++i)
            sum += bestCF[i].x * exp((-bestCF[i].t)*dr);
        return sum / N;
    }

    private double S;
    private double vol;
    private double r;
    
    private int N, K, M;
    private TimeSupport ts;
    private Polynomial[] est;

    private List<ProgressObserver> observers = new LinkedList<>();
}

class CF
{
    public CF(double x, int t)
    {
        this.x = x;
        this.t = t;
    }
    public double x;
    public int t;
}