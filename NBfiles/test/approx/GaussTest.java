
package approx;

import static math.utils.Numerics.doublesEqual;
import java.util.Random;
import junit.framework.TestCase;

/**
 * Tests of method gauss from Approx class.
 * @author Grzegorz Los
 */
public class GaussTest extends TestCase
{
    public void testPatology()
    {
        try {
            double[][] system = null;
            try {
                gauss.solve(system);
                fail("InvalidArgumentException expected");
            } catch (InvalidArgumentException ex) {}
            
            system = new double[0][];
            try {
                gauss.solve(system);
                fail("InvalidArgumentException expected");
            } catch (InvalidArgumentException ex) {}
            
            system = new double[10][];
            try {
                gauss.solve(system);
                fail("InvalidArgumentException expected");
            } catch (InvalidArgumentException ex) {}
            
            for (int i = 0; i < 10; ++i)
                system[i] = new double[1];
            try {
                gauss.solve(system);
                fail("InvalidArgumentException expected");
            } catch (InvalidArgumentException ex) {}
            
            for (int i = 0; i < 10; ++i)
                system[i] = new double[5];
            system[5] = null;
            try {
                gauss.solve(system);
                fail("InvalidArgumentException expected");
            } catch (InvalidArgumentException ex) {}
            
            for (int i = 0; i < 10; ++i)
                system[i] = new double[5];
            system[5] = new double[6];
            try {
                gauss.solve(system);
                fail("InvalidArgumentException expected");
            } catch (InvalidArgumentException ex) {}
        } catch (NoSolutionException  | ManySolutionsException ex) {
            fail("InvalidArgumentException expected");
        }
    }
    
    public void testSimple()
    {
        simpleGauss1();
        simpleGauss2();
        simpleGauss3();
    }
    
    public void testSystemStaysIntact()
    {
        double[][] system = {   {1, -3, 5, 6, 7},
                                {-2, -5, 5, 7, 8},
                                {1 , 1, -1, -1, -1},
                                {-12, 0, 0, 1, -9}
                            };
        double[][] clone = new double[4][];
        for (int i = 0; i < 4; ++i)
            clone[i] = system[i].clone();
        
        try {
            gauss.solve(system);
        } catch (NoSolutionException | ManySolutionsException ex) {
        }
        
        for (int i = 0; i < clone.length; ++i)
            for (int j = 0; j < clone[i].length; ++j)
                assertTrue( system[i][j] == clone[i][j] );
    }
    
    public void testHilbert()
    {
        int m = 7;
        double[][] system = new double[m][m+1];
        for (int i = 0; i < m; ++i)
        {
            for (int j = 0; j < m; ++j)
                system[i][j] = 1.0 / (i+j+1);
            system[i][m] = 1;
        }
        
        oneSolutionExpected(system);
    }
    
    public void testQuadratic()
    {
        quadraticGauss1();
        quadraticGauss2();
        quadraticGauss3();
    }
    
    public void testManyEquations()
    {
        manyEquations1();
        manyEquations2();
        manyEquations3();
    }    
    
    public void testManyVariables()
    {
        manyVariables2();
        manyVariables3();
    }
    
    @Override
    protected void setUp()
    {
        gauss = new Gauss();
    }
        
/* *****************************************************************************
 * ************************** Auxiliary methods ********************************
 * ****************************************************************************/
    private boolean checkSolution(double[][] system, double[] result)
    {
        int eqs = system.length;
        int vars = system[0].length - 1;
        for (int i = 0; i < eqs; ++i)
        {
            double sum = 0;
            for (int j = 0; j < vars; ++j)
                sum += system[i][j] * result[j];
            if (!doublesEqual(sum, system[i][vars]) )
                return false;
        }
        return true;       
    }
    
    private double randomDouble(double limit)
    {
        return (2*r.nextInt(2) - 1) * limit * r.nextDouble();
    }
    
    private double[][] generateSystem(int eqs, int vars, double coefMax)
    {
        double[][] system = new double[eqs][vars+1];
        for (int i = 0; i < eqs; ++i)
        {
            for (int j = 0; j <= vars; ++j)
                system[i][j] = randomDouble(coefMax);
        }
        return system;
    }
    
    
    private void oneSolutionExpected(double[][] system)
    {
        double[] sol = null;
        try {
            sol = gauss.solve(system);
        } catch (NoSolutionException | ManySolutionsException ex) {
            fail("Gauss throws exception");
        }
        assertTrue( checkSolution(system, sol) );
    }
    
    private void noSolutionExpected(double[][] system)
    {        
        try {
            gauss.solve(system);
        } catch (NoSolutionException ex) {
            return ; // OK!
        } catch (ManySolutionsException ex) {
            fail("gauss should have stated that there are no solutions");                
        }
        fail("gauss should have stated that there are no solutions");            
    }
    
    private void manySolutionsExpected(double[][] system)
    {
        try {
            gauss.solve(system);
        } catch (NoSolutionException ex) {
            fail("gauss should have stated that there are many solutions");    
        } catch (ManySolutionsException ex) {
            assertTrue( checkSolution(system, ex.exampleSolution) );   
            return ;
        }
        fail("gauss should have stated that there are many solutions");            
    }
    
/* ************************* Contents of most tests ****************************
 * 1 -> test with unique solution
 * 2 -> test with without solutions
 * 3 -> test with many solutions
 * ****************************************************************************/
    
    private void simpleGauss1()
    {
        /*
         * x + 2y = 1      =>  x = 1
         * 2x + y = 2      =>  y = 0
         */
        double[][] system = { {1, 2, 1},
                              {2, 1, 2} };
        oneSolutionExpected(system);         
    }
        
    private void simpleGauss2()
    {
        /*
         *   x +  y +  z = 1      =>  No solutions
         *  2x + 2y -  z = 1      =>  
         *  -x -  y  + 3z = 1     =>  
         */
        double[][] system = { {1, 1, 1, 1},
                              {2, 2, -1, 1},
                              {-1, -1, 3, 1} };
        noSolutionExpected(system);
    }
    
    private void simpleGauss3()
    {
        /*
         *   x +  y +  z = 1      =>  x = -y
         *  2x + 2y -  z = -1     =>  y = any
         *  -x -  y  + 3z = 3     =>  z = 1
         */
        double[][] system = { {1, 1, 1, 1},
                              {2, 2, -1, -1},
                              {-1, -1, 3, 3} };
        manySolutionsExpected(system);
    }

    private void quadraticGauss1()
    {
        r = new Random(0);
        double[][] system = generateSystem(n, n, 50);
        oneSolutionExpected(system);
    }
    
    private void quadraticGauss2()
    {
        r = new Random(0);
        double[][] system = generateSystem(n, n, 50);
        system[2*n/3] = system[n/3].clone();
        system[2*n/3][n] = 666;
        
        noSolutionExpected(system);      
    }
        
    private void quadraticGauss3()
    {
        r = new Random(0);
        double[][] system = generateSystem(n, n, 50);
        system[2*n/3] = system[n/3].clone();
        
        manySolutionsExpected(system);            
    }
    
    private void manyEquations1()
    {
        r = new Random(0);
        int vars = n/10;
        double[][] system_aux = generateSystem(vars, vars, 50);
        double[][] system = new double[n][];
        for (int i = 0; i < n; ++i)
            system[i] = system_aux[i%vars].clone();
        oneSolutionExpected(system);
    }
        
    private void manyEquations2()
    {
        r = new Random(0);
        int vars = n/10;
        double[][] system_aux = generateSystem(vars, vars, 50);
        double[][] system = new double[n][];
        for (int i = 0; i < n; ++i)
            system[i] = system_aux[i%vars].clone();
        system[2*n/3][vars] = 666;
        
        noSolutionExpected(system);      
    }
    
    private void manyEquations3()
    {
        r = new Random(0);
        int vars = n/10;
        double[][] system_aux = generateSystem(vars, vars, 50);
        for (int i = 0; i < vars; ++i)             // variable nr vars/5 will be 
        {                                     // eliminated together with vars/2
            system_aux[i][vars/5] = system_aux[i][vars/2];
            system_aux[i][vars] = 0;
        } 
        double[][] system = new double[n][];
        for (int i = 0; i < n; ++i)
            system[i] = system_aux[i%vars].clone();      
        
        manySolutionsExpected(system);       
    }
        
    private void manyVariables2()
    {
        r = new Random(0);
        double[][] system = generateSystem(n/2, n, 50);
        system[n/4] = system[n/10].clone();
        system[n/4][n] = 666;
        noSolutionExpected(system);   
    }
            
    private void manyVariables3()
    {
        r = new Random(0);
        double[][] system = generateSystem(n/2, n, 50);
        manySolutionsExpected(system);   
    }
    
/* *****************************************************************************
 * ****************************** Fields  **************************************
 * ****************************************************************************/
    private final int n = 500; // minimal value must be 50
    private Random r;
    private Gauss gauss;
}
