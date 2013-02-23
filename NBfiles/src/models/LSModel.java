
package models;

import approx.Approx;
import approx.Point;
import approx.Polinomial;
import instruments.Instr;
import instruments.JustPrice;
import instruments.PriceInfo;
import static java.lang.Math.exp;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * @author grzes
 */
public class LSModel implements Model
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
    public String desc()
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

    public Polinomial[] getEst()
    {
        return est;
    }
    
    
    @Override
    public PriceInfo price(Instr instr) throws WrongInstrException
    {        
        dt = instr.getTimeHorizon() / K;
        dr = r * dt;
        dvol = vol * sqrt(dt);
        T = instr.getTimeHorizon();
        est = new Polinomial[K];
        
        double paths[][] = generatePaths();
        CF[] bestCF = bestCFlows(paths, instr);
        double mean = getMean(bestCF);
        return new JustPrice(mean);
    }

    private double[][] generatePaths()
    {
        double[][] paths = new double[N][K+1];
        for (int i = 0; i < N; ++i)
        {
            paths[i][0] = S;
            for (int j = 1; j <= K; ++j)
            {
                paths[i][j] = paths[i][j-1]*exp(
                        dvol*rt.nextGaussian() + dr - dvol*dvol/2);
            }
        }
        return paths;
    }
        
    private CF[] bestCFlows(double[][] paths, Instr instr)
    {
        CF[] res = new CF[N];
        for (int i = 0; i < N; ++i)
        {
            res[i] = new CF( instr.payoff(paths[i][K],
                                    instr.getTimeHorizon() ), K);
        }
        for (int j = K-1; j > 0; --j)
        {
            Collection<Point> points = prepareRegression(paths, res, j, instr);
            est[j] = regress(points);
            updateBestCFlows(paths, instr, res, est[j], j);
        }        
        return res;
    }
    
    private void updateBestCFlows(double[][] paths, Instr instr,
            CF[] bestCF, Polinomial p, int j)
    {
        for (int i = 0; i < N; ++i)
        {
            double expected = p.value(paths[i][j]);
            double payoff = instr.payoff(paths[i][j], timeAt(j));
            if (payoff > 0 && payoff > expected)
                bestCF[i] = new CF(payoff, j);
        }
    }
    
    private Collection<Point> prepareRegression(double[][] paths,
                CF[] bestCF, int j, Instr instr)
    {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < N; ++i)
        {
            double val = bestCF[i].x * exp((j-bestCF[i].t)*dr);
            if (instr.payoff(paths[i][j], timeAt(j)) > 0)
            {
                points.add(new Point(paths[i][j], val));                
            }
        }
        return points;
    }
    
    private Polinomial regress(Collection<Point> points)
    {      
        if (points.isEmpty())
            return new Polinomial(0.0);
        else 
        {
            Approx a = new Approx();  
            Polinomial p = a.approximate(points, Math.min(M, points.size()));
            return p;
        }
    }
    
    private double getMean(CF[] bestCF)
    {
        double sum = 0;
        for (int i = 0; i < N; ++i)
            sum += bestCF[i].x * exp((-bestCF[i].t)*dr);
        return sum / N;
    }
    
    private double timeAt(int k)
    {
        return (double)k / K * T;
    }

    private double S;
    private double vol;
    private double r;
    private double dt;
    private double dvol;
    private double dr;
    private double T;
    private int N, K, M;
    RandomTools rt = new RandomTools();
    private Polinomial[] est;
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