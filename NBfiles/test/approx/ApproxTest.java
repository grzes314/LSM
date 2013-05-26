
package approx;

import java.util.ArrayList;
import junit.framework.TestCase;

/**
 *
 * @author Grzegorz Los
 */
public class ApproxTest extends TestCase
{
    public void testPatology()
    {
        ArrayList<Point> list = null;
        try {
            approx.approximate(list, 2);
            fail("InvalidArgumentException expected");
        } catch (InvalidArgumentException ex) {}
        
        list = new ArrayList<>();
        try {
            approx.approximate(list, 2);
            fail("InvalidArgumentException expected");
        } catch (InvalidArgumentException ex) {}
        
        list.add(new Point(1,2));
        list.add(new Point(3,4));
        try {
            approx.approximate(list, -2);
            fail("InvalidArgumentException expected");
        } catch (InvalidArgumentException ex) {}
    }
    
    public void testDegZero()
    {
        ArrayList<Point> list = new ArrayList<>();
        double avg = 0;
        for (int i = 0; i < 100; ++i)
        {
            list.add( new Point(i, (i-50)*(i-50)) );
            avg += (i-50)*(i-50);
        }
        avg /= 100;
        Polynomial pol = approx.approximate(list, 0);
        assertEquals(0, pol.getDeg());
        assertEquals(avg, pol.getCoeff(0));
    }
    
    public void testInterpolation()
    {
        interpolation(2);
        interpolation(5);
        interpolation(13);
        // For bigger degrees numerical errors are becoming too large
    }
    
    public void testSomeFun()
    {
        ArrayList<Point> list = new ArrayList<>();
        double x = -5, dx = 0.5;
        for (int i = 0; i <= 20; ++i)
        {
            list.add(new Point(x, x*Math.sin(10*x)));
            x += dx;
        }
        double a[] = {  0.921611, -5.919267e-15, -0.8484988,
                        1.107727e-15, 0.1018762, -3.98073e-17 , -0.002808853 };
        Polynomial expected = new Polynomial(6, a);
        Polynomial actual = approx.approximate(list, 6);
        assertEquals(expected, actual);
    }

    @Override
    protected void setUp()
    {
        approx = new Approx();
    }
    
    private void interpolation(int degree)
    {
        double[] a = new double[degree+1];
        double sign = 1;
        for (int i = 0; i <= degree; ++i )
        {
            a[i] = sign;
            sign *= -1;
        }
        Polynomial original = new Polynomial(degree, a);
        
        double x = -1.25, dx = 2.5 / degree;
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i <= degree; ++i)
        {
            list.add(new Point(x, original.value(x)));
            x += dx;
        }
        Polynomial approximation = approx.approximate(list, degree);
        assertEquals(original, approximation);
    }
    
    private Approx approx;
}
