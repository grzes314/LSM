
package math.approx;

import junit.framework.TestCase;

/**
 *
 * @author Grzegorz Los
 */
public class Polynomial2Test extends TestCase
{
    @Override
    public void setUp()
    {
        constant = new Polynomial2(5.0);
        makeAllOnes();
        makeSmallRandom();
    }
    
    private void makeAllOnes()
    {
        int n = 50;
        int m = (n+1) * (n+2) / 2;
        double[] a = new double[m];
        for (int i = 0; i < m; ++i)
            a[i] = 1;
        allOnes = new Polynomial2(a, n);
    }
    
    private void makeSmallRandom()
    {
        int n = 2;
        smallRandomCoefs          //y^0 y^1 y^2
                   = new double[] { -1,  2, -1,  // x^0
                                    -3,  2,      // x^1
                                    -2 };        // x^2
        smallRandom = new Polynomial2(smallRandomCoefs, n);
    }
    
    public void testConstPolynomial()
    {
        assertEquals(5.0, constant.value(0, 0), delta);
        assertEquals(5.0, constant.value(1, 0), delta);
        assertEquals(5.0, constant.value(0, 1), delta);
        assertEquals(5.0, constant.value(1, 1), delta);
        assertEquals(5.0, constant.value(Math.PI, 2.34), delta);
        assertEquals(5.0, constant.value(3434324, 2131231), delta);
    }
    
    public void testAllOnes()
    {
        assertEquals(1.0, allOnes.value(0, 0), delta);
        double x = 0.9, y = 1.1;
        int n = allOnes.getDeg();
        double sum = 0;
        for (int i = 0; i <= n; ++i)
            for (int j = 0; j <= n-i; ++j)
                sum += Math.pow(x, i) * Math.pow(y, j);
        assertEquals(sum, allOnes.value(x, y), delta);
    }
    
    public void testSmallRandom()
    {
        double sum = 0;
        int n = smallRandom.getDeg();
        double x = 2.5, y = -3.3;
        int nextInd = 0;
        for (int i = 0; i <= n; ++i)
            for (int j = 0; j <= n-i; ++j)
                sum += smallRandomCoefs[nextInd++] * Math.pow(x, i) * Math.pow(y, j);
        assertEquals(sum, smallRandom.value(x, y), delta);
    }
    
    private double delta = 0.0001;
    private Polynomial2 constant, allOnes, smallRandom;
    private double[] smallRandomCoefs;
}
