
package finance.parameters;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import math.matrices.Matrix;
import math.matrices.NotPositiveDefiniteMatrixException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author grzes
 */
public class ConcreteParamsTest
{
    @Before
    public void setUp()
    {
        n = 3;
        makeR();
        makeBasicParams();
        makeCorrelation();
        try {
            params = new ConcreteParams(basicParams, corr, r);
        } catch (NotPositiveDefiniteMatrixException ex) {
            Logger.getLogger(ConcreteParamsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void makeR()
    {
        r = -0.08;
    }

    private void makeBasicParams()
    {
        basicParams = new OneAssetParams[3];
        basicParams[0] = new OneAssetParams(names[0], 100, 0.2, 0.3);
        basicParams[1] = new OneAssetParams(names[1], 80, 0.4, -0.1);
        basicParams[2] = new OneAssetParams(names[2], 120, 0.3, -0.3);
    }

    private void makeCorrelation()
    {
        corr = new Matrix( new double[][]
            {   { 1.0,  0.7, -0.8},
                { 0.7,  1.0, -0.6},
                {-0.8, -0.6,  1.0}
            });
    }
    
    /**
     * Test of getNumberOfAssets method, of class ConcreteParams.
     */
    @Test
    public void testGetNumberOfAssets()
    {
        assertEquals(n, params.getNumberOfAssets());
    }

    /**
     * Test of getAssetsNames method, of class ConcreteParams.
     */
    @Test
    public void testGetAssetsNames()
    {
        Collection<String> coll = params.getAssetsNames();
        assertEquals(n, coll.size());
        for (String name: names)
            assertTrue(coll.contains(name));
    }

    /**
     * Test of getParams method, of class ConcreteParams.
     */
    @Test
    public void testGetParams_int()
    {
        for (int i = 1; i <= n; ++i)
            assertTrue( basicParams[i-1] == params.getParams(i) );
    }

    /**
     * Test of getParams method, of class ConcreteParams.
     */
    @Test
    public void testGetParams_String()
    {
        for (int i = 1; i <= n; ++i)
            assertTrue( basicParams[i-1] == params.getParams(names[i-1]) );
    }

    /**
     * Test of getName method, of class ConcreteParams.
     */
    @Test
    public void testGetName()
    {
        for (int i = 1; i <= n; ++i)
            assertEquals( basicParams[i-1].name, params.getName(i) );
    }

    /**
     * Test of getNr method, of class ConcreteParams.
     */
    @Test
    public void testGetNr()
    {
        for (int i = 1; i <= n; ++i)
            assertEquals( i, params.getNr(names[i-1]) );
    }

    /**
     * Test of getR method, of class ConcreteParams.
     */
    @Test
    public void testGetR()
    {
        assertEquals(r, params.getR(), 1e-3);
    }

    /**
     * Test of getCorrelation method, of class ConcreteParams.
     */
    @Test
    public void testGetCorrelation_0args()
    {
        assertEquals(corr, params.getCorrelation());
    }

    /**
     * Test of getCorrelation method, of class ConcreteParams.
     */
    @Test
    public void testGetCorrelation_int_int()
    {
        for (int i = 1; i <= n; ++i)
            for (int j = 1; j <= n; ++j)
                assertEquals(corr.get(i, j), params.getCorrelation(i, j), 1e-3);
    }

    /**
     * Test of getCorrelation method, of class ConcreteParams.
     */
    @Test
    public void testGetCorrelation_String_String()
    {
        for (int i = 1; i <= n; ++i)
            for (int j = 1; j <= n; ++j)
                assertEquals(corr.get(i, j), params.getCorrelation(names[i-1], names[j-1]), 1e-3);
    }
        
    private int n;
    private double r;
    private OneAssetParams[] basicParams;
    private String[] names = {"first", "second", "third" };
    private Matrix corr;
    private ConcreteParams params;
}
