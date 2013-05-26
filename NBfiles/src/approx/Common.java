package approx;

import static java.lang.Math.abs;

/**
 *
 * @author Grzegorz Los
 */
public class Common
{
    public static boolean doublesEqual(double a, double b)
    {
        if ( isZero(a) ) return isZero(b);
        if ( isZero(b) ) return isZero(a);
        double err = (a - b) / b;
        return Math.abs(err) < 1e-5;
    }
    
    /**
     * Checks if given value may be considered as zero.
     * @param d any real number.
     * @return true if and only if 'd' may be considered as zero..
     */
    public static boolean isZero(double a)
    {
        return abs(a) < 1e-9;
    }
}
