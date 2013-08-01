
package finance.trajectories;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author grzes
 */
public class SimpleTrajectoryTest
{
    
    @Before
    public void setUp()
    {
        makeData();
        makeTrajectories();
    }
    
    private void makeData()
    {
        K = 9;
        prices = new double[] { 3, 5, 4, 2, 3, 6, 7, 6, 4, 5 };
        cumMax = new double[] { 3, 5, 5, 5, 5, 6, 7, 7, 7, 7 };
        stepMax = new int[]   { 0, 1, 1, 1, 1, 5, 6, 6, 6, 6 };
        cumMin = new double[] { 3, 3, 3, 2, 2, 2, 2, 2, 2, 2 };
        stepMin = new int[]   { 0, 0, 0, 3, 3, 3, 3, 3, 3, 3 };
        cumSum = new double[] { 3, 8, 12, 14, 17, 23, 30, 36, 40, 45 };        
    }
    
    private void makeTrajectories()
    {
        tr = new SimpleTrajectory(K);
        tr_notReady = new SimpleTrajectory(K);
        for (int i = 0; i <= K; ++i)
        {
            tr.set(i, prices[i]);
            tr_notReady.set(i, prices[i]);
        }
        tr.setReady();
    }
    /**
     * Test of getK method, of class SimpleTrajectory.
     */
    @Test
    public void testGetK()
    {
        assertEquals( K, tr.getK() );
    }

    /**
     * Test of price method, of class SimpleTrajectory.
     */
    @Test
    public void testPrice()
    {
        for (int i = 0; i <= K; ++i)
            assertEquals( prices[i], tr.price(i), 1e-3 );
    }

    /**
     * Test of stepMax method, of class SimpleTrajectory.
     */
    @Test
    public void testStepMax()
    {
        for (int i = 0; i <= K; ++i)
            assertEquals( stepMax[i], tr.stepMax(i) );
    }

    /**
     * Test of stepMin method, of class SimpleTrajectory.
     */
    @Test
    public void testStepMin()
    {
        for (int i = 0; i <= K; ++i)
            assertEquals( stepMin[i], tr.stepMin(i) );
    }

    /**
     * Test of cumMax method, of class SimpleTrajectory.
     */
    @Test
    public void testCumMax()
    {
        for (int i = 0; i <= K; ++i)
            assertEquals( cumMax[i], tr.cumMax(i), 1e-3 );
    }

    /**
     * Test of cumMin method, of class SimpleTrajectory.
     */
    @Test
    public void testCumMin()
    {
        for (int i = 0; i <= K; ++i)
            assertEquals( cumMin[i], tr.cumMin(i), 1e-3 );
    }

    /**
     * Test of average method, of class SimpleTrajectory.
     */
    @Test
    public void testAverege()
    {
        for (int i = 0; i <= K; ++i)
            for (int j = i; j <= K; ++j)
            assertEquals( calcAvarage(i, j), tr.average(i, j), 1e-3 );
    }

    private double calcAvarage(int i, int j)
    {
        double sum = 0;
        for (int k = i; k <= j; ++k)
            sum += prices[k];
        return sum / (j - i + 1);
    }
    
    /**
     * Test of setReady method, of class SimpleTrajectory.
     */
    @Test
    public void testSetReady()
    {
        auxSetReady_Before();
        auxSetReady_After();
    }

    private void auxSetReady_After()
    {
        try {
            tr.set(K/2, 0);
            fail("WrongTrajectoryState shoul be thrown");
        } catch (WrongTrajectoryState ex){}
    }

    private void auxSetReady_Before()
    {
        try {
            tr_notReady.stepMax(K/2);
            fail("WrongTrajectoryState shoul be thrown");
        } catch (WrongTrajectoryState ex){}
    }
    
    private SimpleTrajectory tr, tr_notReady;       
    private int K;
    private double[] prices;
    private int[] stepMax, stepMin;
    private double[] cumMax, cumMin, cumSum;
}
