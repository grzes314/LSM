
package math.approx;

import math.matrices.Vector;

/**
 *
 * @author Grzegorz Los
 */
public class Polynomial2
{
    /**
     * Constructor which creates double argument polynomial of degree deg from the given
     * coefficients list. This list represents following numbers:
     * {#code a[0,0], a[0,1], ..., a[0,deg], a[1,0], a[1,1], ..., a[1,deg-1], ...,
     * a[deg-1, 0], a[deg-1, 1], a[deg, 0]}. By the {@code x^k y^l} stands {@code a[k,l]}.
     * Length of array {@code coef} must equal {@code (deg+1)(deg+2)/2}.
     * @param coef array with coefficients of the polynomial in the form described above.
     * @param deg degree of the polynomial.
     * @throws IllegalArgumentException when length of array {@code 
     */
    public Polynomial2(double[] coef, int deg)
    {
        ensureCoefHasProperLength(coef, deg);
        this.deg = deg;
        a = new double[deg+1][];
        for (int i = 0; i <= deg; ++i)
        {
            a[i] = new double[deg - i + 1];
            for (int j = 0; j <= deg - i; ++j)
                a[i][j] = coef[i+j]; //TODO Blad!!
        }
    }
    
    /**
     * Constructor which work similarly to the above one, but is gives coefficients
     * in the Vector instead in the array.
     * @param coefs
     * @param deg 
     */
    public Polynomial2(Vector coef, int deg)
    {
        ensureCoefHasProperLength(coef, deg);
        this.deg = deg;
        a = new double[deg+1][];
        for (int i = 0; i <= deg; ++i)
        {
            a[i] = new double[deg - i + 1];
            for (int j = 0; j <= deg - i; ++j)
                a[i][j] = coef.get(i+j+1);
        }
    }
    
    /**
     * Creates constant polynomial.
     * @param constant value of the polynomial in each point of the domain.
     */
    public Polynomial2(double constant)
    {
        this.deg = 0;
        a = new double[1][1];
        a[0][0] = constant;
    }

    public int getDeg()
    {
        return deg;
    }
    
    public double value(double x, double y)
    {
        double x_pow = 1;
        double sum = 0;
        for (int i = 0; i <= deg; ++i)
        {
            sum += valueOfSubPolynomial(i, x_pow, y);
            x_pow *= x;
        }
        return sum;
    }
    
    private double valueOfSubPolynomial(int xDeg, double x_pow, double y)
    {
        double subsum = 0;
        for (int j = deg - xDeg; j >= 0; --j)
            subsum = subsum*y + a[xDeg][j];
        return x_pow * subsum;
    }
    
    private void ensureCoefHasProperLength(double[] coef, int deg)
    {
        int l = (deg+1) * (deg+2) / 2;
        if (l != coef.length)
            throw new IllegalArgumentException("Coefficients array should have length " + l);
    }    
    
    private void ensureCoefHasProperLength(Vector coef, int deg)
    {
        int l = (deg+1) * (deg+2) / 2;
        if (l != coef.getSize())
            throw new IllegalArgumentException("Coefficients array should have length " + l);
    }
    
    private double[][] a;
    
    private int deg;
}
