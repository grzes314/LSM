
package math.approx;

import junit.framework.TestCase;
import math.matrices.Matrix;
import math.matrices.Vector;

/**
 *
 * @author Grzegorz Los
 */
public class RegresserTest extends TestCase
{
    @Override
    public void setUp()
    {
        reg = new Regresser();
    }
    
    public void testOnSomveData() throws UnsupportedCaseException
    {
        double[][] data = new double[][]{
            { 1.0, 3.5, 1.4 },
            { 1.0, 3.0, 1.4 },
            { 1.0, 3.2, 1.3 },
            { 1.0, 3.1, 1.5 },
            { 1.0, 3.6, 1.4 },
            { 1.0, 3.9, 1.7 },
            { 1.0, 3.4, 1.4 },
            { 1.0, 3.4, 1.5 },
            { 1.0, 2.9, 1.4 },
            { 1.0, 3.1, 1.5 },
            { 1.0, 3.7, 1.5 },
            { 1.0, 3.4, 1.6 },
            { 1.0, 3.0, 1.4 },
            { 1.0, 3.0, 1.1 },
            { 1.0, 4.0, 1.2 },
            { 1.0, 4.4, 1.5 },
            { 1.0, 3.9, 1.3 },
            { 1.0, 3.5, 1.4 },
            { 1.0, 3.8, 1.7 },
            { 1.0, 3.8, 1.5 }
        };
        double[] observations = new double[] {5.1, 4.9, 4.7, 4.6, 5.0, 5.4, 4.6, 5.0, 4.4, 4.9,
            5.4, 4.8, 4.8, 4.3, 5.8, 5.7, 5.4, 5.1, 5.7, 5.1};
        double ex1 = 1.5298, ex2 = 0.8921, ex3 = 0.2792;
        Vector vec = reg.regress(new Matrix(data), new Vector(observations));
        assertEquals(ex1, vec.get(1), 0.0001);
        assertEquals(ex2, vec.get(2), 0.0001);
        assertEquals(ex3, vec.get(3), 0.0001);
    }
    
    Regresser reg;
}
