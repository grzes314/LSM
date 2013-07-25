package math.utils;

import junit.framework.TestCase;
import math.matrices.Matrix;
import math.matrices.Vector;

public class StatisticsTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        stat = new Statistics();
    }

    public void testMeanOnVector()
    {
        auxMeanOnVectorConst();
        auxMeanOnVectorArth();
    }

    private void auxMeanOnVectorConst()
    {
        int n = 100;
        Vector vec = new Vector(100);
        for (int row = 1; row <= n; ++row)
            vec.set(row, Math.PI);
        assertEquals(Math.PI, stat.mean(vec), 1e-3);
    }

    private void auxMeanOnVectorArth()
    {
        int n = 100;
        Vector vec = new Vector(100);
        for (int row = 1; row <= n; ++row)
            vec.set(row, row);
        assertEquals((n+1.0)/2, stat.mean(vec), 1e-3);        
    }

    public void testMeanOnMatrix()
    {
        auxMeanOnIdMatrix();
        auxMeanOnConstColMatrix();
    }

    private void auxMeanOnIdMatrix()
    {
        int n = 100;
        Matrix m = new Matrix(n, n);
        Vector v = stat.mean(m);
        for (int col = 1; col <= n; ++col)
            assertEquals(1./n, v.get(col), 1e-3);    
    }

    private void auxMeanOnConstColMatrix()
    {
        // TODO Auto-generated method stub

    }

    public void testCndf()
    {
        assertEquals(0, stat.cndf(-1e9), 1e-3);
        assertEquals(1, stat.cndf(1e9), 1e-3);
        assertEquals(0.5, stat.cndf(0), 1e-3);
    }

    Statistics stat;
}
