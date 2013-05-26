package approx;

import java.util.Collection;


/**
 * Class for calculation of polynomial approximating any set of points.
 * @author Grzegorz Los
 */
public class Approx
{
    /**
     * Computes the polynomial approximating any set of points.
     * @param points collection of pairs argument - value
     * @param m degree of approximating polynomial
     * @return Approximating polynomial
     * @see http://www.math.uni.wroc.pl/~s221063/thesis/index.html
     */
    public Polynomial approximate(Collection<Point> points, int m)
    {
        ensureArgsOK(points, m);
        if (points.size() <= m)
            m = points.size() - 1;
        double[] s = calculateS(points, m);
        double[] t = calculateT(points, m);
        double system[][] = new double[m+1][m+2];
        for (int i = 0; i <= m; ++i)
        {
            for (int j = 0; j <= m; ++j)
                system[i][j] = s[i+j];
            system[i][m+1] = t[i];
        }
        try {
            double[] a = gauss.solve(system, true); // for efficiency modify system
            return new Polynomial(m, a);
        } catch (ManySolutionsException ex) {
            return new Polynomial(m, ex.exampleSolution);
        } catch (NoSolutionException ex) {
            /*
             * It would be worth to conduct some extra investigation
             * to check when this situation is possible. Since we can always
             * find approximating polynomial of degree 0, hence for now it may
             * be sufficient to try again with smaller degree -- such procedure
             * has to end.
             */
            return approximate(points, m-1);
        }
    }
    
    /**
     * Checks if arguments given to method 'approximate' are valid.
     * @param points points to be approximated.
     * @param m degree of approximating polynomial
     * @throws InvalidArgumentException when points are null or empty, or
     * m is negative.
     */
    private void ensureArgsOK(Collection<Point> points, int m)
    {
        if (points == null)
            throw new InvalidArgumentException("points can not be null");
        if (points.isEmpty())
            throw new InvalidArgumentException("points can not be empty");
        if (m < 0)
            throw new InvalidArgumentException("degree can not be negative");
    }
    
    /**
     * Calculates parameters s for regression. Their meaning is described
     * under given link.
     * @param points points to be approximated.
     * @param m degree of approximating polynomial.
     * @return parameters s.
     * @see http://www.math.uni.wroc.pl/~s221063/thesis/index.html
     */
    private double[] calculateS(Collection<Point> points, int m)
    {
        double[] s = new double[2*m+1];
        for (int i = 0; i <= 2*m; ++i)
            s[i] = 0.0;
        for (Point p: points)
        {
            double power = 1;
            for (int i = 0; i <= 2*m; ++i)
            {
                s[i] += power;
                power *= p.x;
            }
        }
        return s;
    }
    
    /**
     * Calculates parameters t for regression. Their meaning is described
     * under given link.
     * @param points points to be approximated.
     * @param m degree of approximating polynomial.
     * @return parameters t.
     * @see http://www.math.uni.wroc.pl/~s221063/thesis/index.html
     */
    private double[] calculateT(Collection<Point> points, int m)
    {
        double[] t = new double[m+1];
        for (int i=0; i<=m; ++i)
           t[i] = 0.0;
        for (Point p: points)
        {
            double power = 1;
            for (int i = 0; i <= m; ++i)
            {
                t[i] += power * p.y;
                power *= p.x;
            }
        }
        return t;
    }
    
    /**
     * Object used to solve system of linear equations.
     */
    private Gauss gauss = new Gauss();
}
