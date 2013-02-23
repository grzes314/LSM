
package models;

import java.util.Random;

/**
 *
 * @author grzes
 */
public class RandomTools
{

    public RandomTools()
    {
        r = new Random(System.currentTimeMillis());
    }

    public synchronized void setSeed(long seed)
    {
        r.setSeed(seed);
    }

    public int nextInt(int n)
    {
        return r.nextInt(n);
    }

    public int nextInt()
    {
        return r.nextInt();
    }

    public synchronized double nextGaussian()
    {
        return r.nextGaussian();
    }

    public double nextDouble()
    {
        return r.nextDouble();
    }
    
    /**
     * Cumulative normal distribution function.
     * @param t will return Phi(t)
     * @return Phi(t)
     */
    public double cndf(double t)
    {
        return 0.5d * (1d + erf(t / Math.sqrt(2d)));
    }
    
    /**
     * So called error function.
     * @see http://en.wikipedia.org/wiki/Error_function#Approximation_with_elementary_functions
     * @param d
     * @return 
     */
    private double erf(double x)
    {
        if (x < 0) return -erf(-x);
        double  p = 0.3275911d, a1 = 0.254829592d, a2 = -0.284496736d,
                a3 = 1.421413741d, a4 = -1.453152027d, a5 = 1.061405429d;
        double t = 1d / (1d + p*x);
        double w = ((((a5*t + a4)*t + a3)*t + a2)*t + a1)*t;
        return 1d - w*Math.exp(-x*x);
    }
       
    private Random r;


}
