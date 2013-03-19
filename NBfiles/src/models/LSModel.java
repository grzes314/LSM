
package models;

import approx.Approx;
import approx.Point;
import approx.Polynomial;
import instruments.Instr;
import instruments.PriceInfo;
import instruments.SimplePriceInfo;
import instruments.TimeSupport;
import static java.lang.Math.exp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import trajectories.OneTrGenerator;
import trajectories.Scenario;


/**
 *
 * @author grzes
 */
public class LSModel implements ProgressObservable
{

    public LSModel(double S, double vol, double r, int N, int K, int M) 
    {
        this.S = S;
        this.vol = vol;
        this.r = r;
        this.N = N;
        this.K = K;
        this.M = M;
    }
    
    @Override
    public String toString()
    {
        return "LONGSTAFF-SCWARTZ MODEL\n" +
               "S = "+ S + "\n" +
               "vol = " + vol + "\n" +
               "r = " + r + "\n" +             
               "N = " + N + "\n" +             
               "K = " + K + "\n" +             
               "M = " + M + "\n";                
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
    
    public PriceInfo price(Instr instr)
    {        
        est = new Polynomial[K];
        OneTrGenerator gen = new OneTrGenerator(S, r, vol, instr.getTS());
        gen.addObserver( new ProgressObserver() {
            @Override
            public void update(Progress pr)
            {
                notifyObservers(pr);
            }
        } );
        
        Scenario[] paths = gen.generate(N);
        
        CF[] bestCF = bestCFlows(paths, instr);
        double mean = getMean(bestCF, instr.getTS());
        return new SimplePriceInfo(mean, this, instr);
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
    
    private CF[] bestCFlows(Scenario[] paths, Instr instr)
    {
        CF[] res = new CF[N];
        for (int i = 0; i < N; ++i)
        {
            res[i] = new CF( instr.payoff(paths[i], K), K );
        }
        for (int j = K-1; j > 0; --j)
        {
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
        double dr = r * instr.getTS().getT() / instr.getTS().getK();
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
    
    private double getMean(CF[] bestCF, TimeSupport ts)
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
    private RandomTools rt = new RandomTools();
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