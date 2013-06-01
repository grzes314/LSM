
package approx;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import static utils.Common.doublesEqual;

/**
 *
 * @author Grzegorz Los
 */
public class PolynomialTest extends TestCase
{
    public void testValueCalculation() throws AssertionFailedError
    {
        double[] coeffs1 = {4, 3, 2, 1};
        Polynomial P1 = new Polynomial(3, coeffs1);
        assertTrue( doublesEqual(4.0, P1.value(0)) );
        assertTrue( doublesEqual(194.0, P1.value(5.0)) );
        
        double[] coeffs2 = {1, -1, 1, -1, 1, -1, 1, -1, 1, -1};
        Polynomial P2 = new Polynomial(9, coeffs2);
        assertTrue( doublesEqual(1.0, P2.value(0)) );
        double res = 1, x = -123456789.987654321, xn = 1;
        for (int i = 1; i < 10; ++i) {
            xn *= x;
            res += (i%2 == 0 ? 1 : -1) * xn;
        }
        assertTrue( doublesEqual(res, P2.value(-123456789.987654321)) );
    }  
    
    public void testFindingRoots() throws AssertionFailedError
    {
        double[] coeffs1 = {0, -2, 1};
        Polynomial P1 = new Polynomial(2, coeffs1);
        try {
            assertTrue( doublesEqual(2.0, P1.solve(1,10)) );
        } catch (NoSolutionException ex) {
            fail("solve has thrown NoSolutionException");
        }
        
        
        double[] coeffs2 = {1, 1, 2, 3, 5, 8, 13, 21, 34, 55};
        Polynomial P2 = new Polynomial(9, coeffs2);
        double r = 0;
        try {
            r = P2.solve(-10e6, 10e6);
        } catch (NoSolutionException ex) {
            fail("solve has thrown NoSolutionException");
        }
        assertTrue( doublesEqual(0, P2.value(r)) );
    }    
}
