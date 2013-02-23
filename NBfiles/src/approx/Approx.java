package approx;

import java.util.Collection;

/**
 *
 * @author grzes
 */
public class Approx
{
    /**
     *
     * @param map map of pairs argument - value
     * @param m degree of approximating polinomial
     * @return Approximating polinomial
     * @see http://aproks.republika.pl/#aproksymacja%20%C5%9Bredniokwadratowa
     */
    public Polinomial approximate(Collection<Point> points, int m)
    {
        //System.out.println("lets approximate");
       double[] s = new double[2*m+1];
       double[] t = new double[m+1];
       for (int i=0; i<=2*m; ++i)
       {
           s[i] = 0.0;
           for (Point p: points)
               s[i] += Math.pow(p.x, i);
       }
       for (int i=0; i<=m; ++i)
       {
           t[i] = 0.0;
           for (Point p: points)
               t[i] += Math.pow(p.x, i)*p.y;
       }
       double uklad[][] = new double[m+1][m+2];
       for (int i=0; i<=m; ++i)
       {
           for (int j=0; j<=m; ++j)
               uklad[i][j] = s[i+j];
           uklad[i][m+1] = t[i];
       }
        try {
           // print(uklad);
            double[] a = gauss(uklad, m + 1);
            return new Polinomial(m, a);
        } catch (Exception ex) {
            /*System.out.println(ex.getMessage());
            double[] b = new double[1];
            b[0] = 0;
            return new Polinomial(0,b);*/
            return approximate(points, m-1);
        }
    }

    public static double[] gauss(double[][] uklad, int n) throws Exception
    {
        double result[] = new double[n];
        for (int i=0; i<n; ++i)
        {
            try {
                rearrange(uklad, i, n);
            } catch (Exception ex) {
                throw new Exception("Can't solve system, " + ex.getMessage());
            }
            substract(uklad, i, n);
        }
        for (int i=n-1; i>=0; --i)
        {
            double res = uklad[i][n];
            for (int j=n-1; j>i; --j)
                res -= uklad[i][j]*result[j];
            result[i] = res/uklad[i][i];
        }
        return result;
    }

    private static void rearrange(double[][] uklad, int i, int n) throws Exception
    {
        if ( !isZero(uklad[i][i]) ) return ;
        for (int j=i+1; j<=n; ++j)
        {
            if (j==n) throw new Exception("Failed to rearrange rows");
            if ( !isZero(uklad[j][i]) )
            {
                double[] pom = uklad[i];
                uklad[i] = uklad[j];
                uklad[j] = pom;
                return ;
            }
        }
    }

    private static void substract(double[][] uklad, int i, int n)
    {
        for (int j=i+1; j<n; ++j)
        {
            double wsp = uklad[j][i] / uklad[i][i];
            uklad[j][i] = 0.0;
            for (int k=i+1; k<=n; ++k)
            {
                uklad[j][k] -= wsp*uklad[i][k];
            }
        }
    }

    private void print(double[][] uklad)
    {
        for (int i=0; i<uklad.length; ++i)
        {
            for (int j=0; j<uklad[i].length; ++j)
            {
                System.out.print(uklad[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean isZero(double d)
    {
        //return d <= 10e-100;
        return d == 0d;
    }
}
