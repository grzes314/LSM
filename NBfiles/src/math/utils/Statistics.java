package math.utils;

import math.matrices.Matrix;
import math.matrices.Vector;

public class Statistics
{   
    /**
     * This class is not supposed to be instantiated.
     */
    private Statistics()
    {
    }
    
    /**
     * Cumulative normal distribution function.
     * @param t will return Phi(t)
     * @return Phi(t)
     */
    public static  double cndf(double t)
    {
        return 0.5d * (1d + erf(t / Math.sqrt(2d)));
    }

    /**
     * Returns sample mean.
     * @param v vector with a sample.
     * @return sample mean.
     */
    public static double mean(Vector v)
    {
        double sum = 0.0;
        for (int row = 1; row <= v.getRows(); ++row)
            sum += v.get(row);
        return sum / v.getRows();
    }
    
    /**
     * Computes means of all variables in given matrix. Columns of the matrix
     * correspond two variables. Rows of the matrix correspond to observations (or individuals).
     * @param m Matrix with observations.
     * @return vector containing mean of each column of the matrix.
     */
    public static Vector mean(Matrix m)
    {
        Vector res = new Vector(m.getCols());
        for (int row = 1; row <= res.getRows(); ++row)
            res.set(row, mean(m.getCol(row)));
        return res;
    }
    
    /**
     * Computes sample variance.
     * @param sample vector with observations.
     * @return sample variance.
     */
    public static double var(Vector sample)
    {
        return 0;
    }
    
    /**
     * Computes covariance between two samples.
     * @param s1 vector with values of first variable.
     * @param s2 vector with values of second variable.
     * @return sample covariance.
     */
    public static double cov(Vector s1, Vector s2)
    {
        return 0;
    }
    
    /**
     * Computes covariance between all variables in given matrix. Columns of the matrix
     * correspond two variables. Rows of the matrix correspond to observations (or individuals).
     * @param m Matrix with observations.
     * @return covariance matrix.
     */
    public static Matrix cov(Matrix m)
    {
        return new Matrix(1,1);
    }
    
    /**
     * Computes correlation between two samples.
     * @param s1 vector with values of first variable.
     * @param s2 vector with values of second variable.
     * @return sample correlation.
     */
    public static double corr(Vector s1, Vector s2)
    {
        return 0;
    }
    
    /**
     * Computes correlation between all variables in given matrix. Columns of the matrix
     * correspond two variables. Rows of the matrix correspond to observations (or individuals).
     * @param m Matrix with observations.
     * @return correlation matrix.
     */
    public static Matrix corr(Matrix m)
    {
        return new Matrix(1,1);
    }
    
    /**
     * So called error function.
     * @see http://en.wikipedia.org/wiki/Error_function#Approximation_with_elementary_functions
     * @param d
     * @return
     */
    private static double erf(double x)
    {
        if (x < 0) return -erf(-x);
        double p = 0.3275911d, a1 = 0.254829592d, a2 = -0.284496736d,
                a3 = 1.421413741d, a4 = -1.453152027d, a5 = 1.061405429d;
        double t = 1d / (1d + p*x);
        double w = ((((a5*t + a4)*t + a3)*t + a2)*t + a1)*t;
        return 1d - w*Math.exp(-x*x);
    }
}
