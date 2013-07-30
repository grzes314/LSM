package math.utils;

import math.matrices.Matrix;
import math.matrices.Vector;
import junit.framework.TestCase;

public class StatisticsTest extends TestCase {

    protected void setUp() throws Exception
    {
        super.setUp();
        smallVector1 = smallVector1();
        smallVector2 = smallVector2();
        smallMat = smallVector1.cbind(smallVector2);
        smallMean = smallMean();
        bigVector1 = bigVector1();
        bigVector2 = bigVector2();
        bigVector3 = bigVector3();
        bigMean = bigMean();
        bigMat = bigVector1.cbind(bigVector2).cbind(bigVector3);
        smallCov = smallCovariance();
        smallCorr = smallCorrelation();
        bigCov = bigCovariance();
        bigCorr = bigCorrelation();
    }    

    /********************************************************************************************/
    /*********************************************************************************************/
    
    private Vector smallVector1()
    {
        return new Vector( new double[]{0.64, -1.09, 1.00, 1.28, -0.38, 0.98, 
                -0.07, -2.29, -1.07, -0.95} );
    }
    
    private double smallMean1()
    {
        return -0.195;
    }
    
    private Vector smallVector2()
    {
        return new Vector( new double[]{-0.63, -2.32, 0.94, 0.9, 0.34, 0.43,
                0.62, -0.45, 0.34, -0.52} );
    }
    
    private double smallMean2()
    {
        return -0.035;
    }

    private Vector smallMean()
    {
        return new Vector( new double[]{-0.195, -0.035} );
    }

    private Matrix smallCovariance()
    {
        double d[][] = {{1.361894, 0.61765}, // checked in R
                        {0.61765, 0.97045} };
        return new Matrix(d);
    }
    
    private Matrix smallCorrelation()
    {
        double d[][] = {{1, 0.5372595}, // checked in R
                        {0.5372595, 1} };
        return new Matrix(d);
    }
    
    private Vector bigVector1()
    {
        return Sequences.sineOfNatural(n);
    }
    
    private double bigMean1()
    {
        return 0.0001633891;
    }
    
    private Vector bigVector2()
    {
        return Sequences.cosineOfNatural(n);
    }
    
    private double bigMean2()
    {
        return -0.0001255789;
    }
    
    private Vector bigVector3()
    {
        return Sequences.tangentOfNatural(n);
    }
    
    private double bigMean3()
    {
        return 0.1219532;
    }

    private Vector bigMean()
    {
        return new Vector( new double[]{0.0001633891, -0.0001255789, 0.1219532} );
    }

    private Matrix bigCovariance()
    {
        double[][] d = {{0.5000453, 1.757047e-05, 0.02068187}, //checked in R
                {1.757047e-05, 0.5000547, 0.0001787217},
                {0.02068187, 0.0001787217, 369.2348}};    
        return new Matrix(d);
    }

    private Matrix bigCorrelation()
    {
        double[][] d = {{0.5000453, 1.757047e-05, 0.02068187}, //checked in R
                {1.757047e-05, 0.5000547, 0.0001787217},
                {0.02068187, 0.0001787217, 369.2348}};    
        return new Matrix(d);
    }

    /*********************************************************************************************/
    /*********************************************************************************************/
    
    public void testMean_Vector()
    {
        auxMeanOnVectorConst();
        auxMeanOnVectorArth();
        auxMeanOnVectorGeom();
        auxMeanOnVectorSmall();
        auxMeanOnVectorBig();
    }

    private void auxMeanOnVectorConst()
    {
        Vector vec = Sequences.constant(n, Math.PI);
        assertEquals(Math.PI, Statistics.mean(vec), 1e-3);
    }

    private void auxMeanOnVectorArth()
    {
        Vector vec = Sequences.arithmetic(n, -45, 0.01);
        double ex = Sequences.sumOfArithmetic(n, -45, 0.01) / n;
        assertEquals(ex, Statistics.mean(vec), 1e-3);        
    }

    private void auxMeanOnVectorGeom()
    {
        int len = 1000;
        Vector vec = Sequences.geometric(len, 1, 1.01);
        double ex = Sequences.sumOfGeometric(len, 1, 1.01) / len;
        assertEquals(ex, Statistics.mean(vec), 1e-3);        
    }
    
    private void auxMeanOnVectorSmall()
    {
        assertEquals(smallMean1(), Statistics.mean(smallVector1), 1e-3);
        assertEquals(smallMean2(), Statistics.mean(smallVector2), 1e-3);
    }
    
    private void auxMeanOnVectorBig()
    {
        assertEquals(bigMean1(), Statistics.mean(bigVector1), 1e-3);
        assertEquals(bigMean2(), Statistics.mean(bigVector2), 1e-3);
        assertEquals(bigMean3(), Statistics.mean(bigVector3), 1e-3);
    }

    public void testMean_Matrix()
    {
        auxMeanOnIdMatrix();
        auxMeanOnConstColMatrix();
        auxMeanOnConcreteMatrix();
        auxMean_MatrixSmall();
        auxMean_MatrixBig();
    }
    
    private void auxMean_MatrixSmall()
    {
        Vector mean = Statistics.mean(smallMat);
        assertTrue( mean.equals(smallMean) ); //TODO equals for vectors
    }

    private void auxMean_MatrixBig()
    {
        Vector mean = Statistics.mean(bigMat);
        assertTrue( mean.equals(bigMean) );
    }

    private void auxMeanOnIdMatrix()
    {
        Matrix m = new Matrix(n, n);
        Vector v = Statistics.mean(m);
        for (int col = 1; col <= n; ++col)
            assertEquals(1./n, v.get(col), 1e-3);    
    }

    private void auxMeanOnConstColMatrix()
    {
        Matrix m = new Matrix(200, 300);
        for (int col = 1; col <= m.getCols(); ++col)
            m.setCol(col, Sequences.constant(m.getRows(), col));
        Vector v = Statistics.mean(m);
        for (int col = 1; col <= m.getCols(); ++col)
            assertEquals(col, v.get(col), 1e-3);
    }
    
    private void auxMeanOnConcreteMatrix()
    {
        int len = 999;
        Matrix m = new Matrix(len, 3);
        m.setCol(1, Sequences.arithmetic(len, -len/2, 1.0));
        m.setCol(2, Sequences.geometric(len, 0.05, 1.01));
        m.setCol(3, Sequences.squares(len));
        Vector v = Statistics.mean(m);
        assertEquals(0, v.get(1), 1e-3);
        assertEquals(Sequences.sumOfGeometric(len, 0.05, 1.01)/len, v.get(2), 1e-3);
        assertEquals(Sequences.sumOfSquares(len) / len, v.get(3), 1e-3);
    }
    
    
    public void testVar()
    {
        auxTestVarOnConst();
        auxTestVarOnIntersperse();
        //TODO testVar_small(), testVar_big();
    }

    private void auxTestVarOnConst()
    {
        Vector v = Sequences.constant(n, 7);
        assertEquals(0, Statistics.var(v), 1e-3);
    }

    private void auxTestVarOnIntersperse()
    {
        Vector v = Sequences.intersperse(n);
        assertEquals(1, Statistics.var(v), 1e-2);
    }
    
    public void testCovar_Vectors()
    {
        auxTestCovar_Vectors_sameVector();
        auxTestCovar_Vectors_Dependent();
        auxTestCovar_Vectors_Small();
        auxTestCovar_Vectors_Big();
    }
    
    private void auxTestCovar_Vectors_sameVector()
    {
        Vector v1 = Sequences.sineOfNatural(n);
        Vector v2 = new Vector(v1);
        assertEquals(   Statistics.var(v1),
                        Statistics.covar(v1, v2),
                        1e-3 );
    }

    private void auxTestCovar_Vectors_Dependent()
    {
        int n = 10000;
        Vector v1 = Sequences.intersperse(n);
        Vector v2 = v1.times(2);
        assertEquals(2, Statistics.covar(v1, v2), 1e-2);
    }
    
    private void auxTestCovar_Vectors_Small()
    {
        Matrix cov = Statistics.covar(smallMat);
        assertTrue( smallCov.equals(cov) );
    }

    private void auxTestCovar_Vectors_Big()
    {
        //TODO
    }
    
    public void testCovar_Matrix()
    {
        auxTestCovar_Matrix_sameVector();
        auxTestCovar_Matrix_Dependent();
        auxTestCovar_Matrix_Small();
        auxTestCovar_Matrix_Big();
    }
    
    private void auxTestCovar_Matrix_sameVector()
    {
        int n = 10000;
        Vector v1 = Sequences.intersperse(n);
        Vector v2 = new Vector(v1);
        Matrix m = v1.cbind(v2);
        Matrix cov = Statistics.covar(m);
        assertEquals( 2, cov.getCols() );
        assertEquals( 2, cov.getRows() );
        for (int row = 1; row <= 2; ++row)
            for (int col = 1; col <= 2; ++col)
                assertEquals(1, cov.get(row, col));
    }

    private void auxTestCovar_Matrix_Dependent()
    {
        int n = 10000;
        Vector v1 = Sequences.sineOfNatural(n);
        Vector v2 = v1.times(2);
        Vector v3 = v1.times(4);
        Matrix cov = Statistics.covar(v1.cbind(v2).cbind(v3));
        assertEquals( 3, cov.getCols() );
        assertEquals( 3, cov.getRows() );
        assertTrue( cov.isSymmetric() );
        double var = Statistics.var(v1);
        assertEquals( 2*var, cov.get(1, 2), 1e-3 );
        assertEquals( 4*var, cov.get(1, 3), 1e-3 );
        assertEquals( 2*var, cov.get(2, 3), 1e-3 );
    }

    private void auxTestCovar_Matrix_Small()
    {
        //TODO
    }

    private void auxTestCovar_Matrix_Big()
    {
        //TODO
    }

    public void testCndf()
    {
        assertEquals(0, Statistics.cndf(-1e9), 1e-3);
        assertEquals(1, Statistics.cndf(1e9), 1e-3);
        assertEquals(0.5, Statistics.cndf(0), 1e-3);
    }
    
    final int n = 10000; //size of vectors for big tests
    Vector smallVector1, smallVector2, bigVector1, bigVector2, bigVector3, smallMean, bigMean;
    Matrix smallMat, bigMat, smallCov, smallCorr, bigCov, bigCorr;
}
