package approx;


/**
 *
 * @author grzes
 */
public class Polinomial
{
    public Polinomial(double c)
    {
        deg = 0;
        a = new double[1];
        a[0] = c;
    }

    public Polinomial(int deg, double[] b)
    {
        this.deg = deg;
        a = new double[deg+1];
        for (int i=0; i<=deg; ++i)
            a[i] = b[i];
    }
    
    public double value(double arg)
    {
        double res = a[deg];
        for (int i=deg-1; i>=0; --i)
            res = res*arg + a[i];
        return res;
    }
    
    public double solve(double beg, double end) throws NoSolutionException
    {
        return solve(beg, end, 0, 10e-10d);
    }
         
    public double solve(double beg, double end, double y) throws NoSolutionException
    {
        return solve(beg, end, y, 10e-10d);
    }
            
    public double solve(double beg, double end, double y, double eps)
            throws NoSolutionException
    {
        double v1 = value(beg) - y;
        double v2 = value(end) - y;
        if (v1 == 0d) return beg;
        else if (v2 == 0d) return end;
        else if (v1*v2 > 0) throw new NoSolutionException();
        
        while (end - beg > eps)
        {
            double mid = 0.5*(beg + end);
            double v = value(mid) - y;
            if ( v == 0d) return mid;
            if ( (v1 > 0 && v > 0) || (v1 < 0 && v < 0) ) {
                beg = mid;
                v1 = v;
            } else {
                end = mid;
                v2 = v;
            }            
        }
        return beg;
    }
    
    int deg;
    double[] a;
}
