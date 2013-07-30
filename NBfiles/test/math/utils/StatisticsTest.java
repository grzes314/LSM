package math.utils;

import junit.framework.TestCase;
import math.matrices.DimensionException;
import math.matrices.Matrix;
import math.matrices.NoCorrelationException;
import math.matrices.Vector;

public class StatisticsTest extends TestCase
{
    @Override
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

/*************************************************************************************************/
/************************* Preparation of data for tests *****************************************/
    
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
        double[][] d = {{1, 3.513743e-05, 0.001522067}, //checked in R
                {3.513743e-05, 1, 1.315277e-05},
                {0.001522067, 1.315277e-05, 1}};    
        return new Matrix(d);
    }


/*************************************************************************************************/
/************************* Tests of mean called on vector ****************************************/
    
    public void testMean_Vector()
    {
        auxMean_VectorConst();
        auxMean_VectorArth();
        auxMean_VectorGeom();
        auxMean_VectorSmall();
        auxMean_VectorBig();
    }

    private void auxMean_VectorConst()
    {
        Vector vec = Sequences.constant(n, Math.PI);
        assertEquals(Math.PI, Statistics.mean(vec), 1e-3);
    }

    private void auxMean_VectorArth()
    {
        Vector vec = Sequences.arithmetic(n, -45, 0.01);
        double ex = Sequences.sumOfArithmetic(n, -45, 0.01) / n;
        assertEquals(ex, Statistics.mean(vec), 1e-3);        
    }

    private void auxMean_VectorGeom()
    {
        int len = 1000;
        Vector vec = Sequences.geometric(len, 1, 1.01);
        double ex = Sequences.sumOfGeometric(len, 1, 1.01) / len;
        assertEquals(ex, Statistics.mean(vec), 1e-3);        
    }
    
    private void auxMean_VectorSmall()
    {
        assertEquals(smallMean1(), Statistics.mean(smallVector1), 1e-3);
        assertEquals(smallMean2(), Statistics.mean(smallVector2), 1e-3);
    }
    
    private void auxMean_VectorBig()
    {
        assertEquals(bigMean1(), Statistics.mean(bigVector1), 1e-3);
        assertEquals(bigMean2(), Statistics.mean(bigVector2), 1e-3);
        assertEquals(bigMean3(), Statistics.mean(bigVector3), 1e-3);
    }

/*************************************************************************************************/
/************************* Tests of mean called on matrix ****************************************/
    
    public void testMean_Matrix()
    {
        auxMean_MatrixId();
        auxMean_MatrixConstCol();
        auxMean_MatrixConcrete();
        auxMean_MatrixSmall();
        auxMean_MatrixBig();
    }

    private void auxMean_MatrixId()
    {
        int len = 200;
        Matrix m = new Matrix(len, len);
        Vector v = Statistics.mean(m);
        for (int col = 1; col <= len; ++col)
            assertEquals(1./len, v.get(col), 1e-3);    
    }

    private void auxMean_MatrixConstCol()
    {
        Matrix m = new Matrix(200, 300);
        for (int col = 1; col <= m.getCols(); ++col)
            m.setCol(col, Sequences.constant(m.getRows(), col));
        Vector v = Statistics.mean(m);
        for (int col = 1; col <= m.getCols(); ++col)
            assertEquals(col, v.get(col), 1e-3);
    }
    
    private void auxMean_MatrixConcrete()
    {
        int len = 999;
        Matrix m = new Matrix(len, 3);
        m.setCol(1, Sequences.arithmetic(len, -(len-1)/2, 1.0));
        m.setCol(2, Sequences.geometric(len, 0.05, 1.01));
        m.setCol(3, Sequences.squares(len));
        Vector v = Statistics.mean(m);
        assertEquals(0, v.get(1), 1e-3);
        assertEquals(Sequences.sumOfGeometric(len, 0.05, 1.01)/len, v.get(2), 1e-3);
        assertEquals(Sequences.sumOfSquares(len) / len, v.get(3), 1e-3);
    }
        
    private void auxMean_MatrixSmall()
    {
        Vector mean = Statistics.mean(smallMat);
        assertTrue( mean.equals(smallMean) );
    }

    private void auxMean_MatrixBig()
    {
        Vector mean = Statistics.mean(bigMat);
        assertTrue( mean.equals(bigMean) );
    }

/*************************************************************************************************/
/************************** Tests of variance calculation ****************************************/
   
    public void testVar()
    {
        auxVar_Const();
        auxVar_Intersperse();
        auxVar_Small();
        auxVar_Big();
        auxVar_Patology();
    }

    private void auxVar_Const()
    {
        Vector v = Sequences.constant(n, 7);
        assertEquals(0, Statistics.var(v), 1e-3);
    }

    private void auxVar_Intersperse()
    {
        Vector v = Sequences.intersperse(n);
        assertEquals(1, Statistics.var(v), 1e-2);
    }
    
    private void auxVar_Small()
    {
        assertEquals(smallCov.get(1, 1), Statistics.var(smallVector1), 1e-3);
        assertEquals(smallCov.get(2, 2), Statistics.var(smallVector2), 1e-3);
    }
    
    private void auxVar_Big()
    {
        assertEquals(bigCov.get(1, 1), Statistics.var(bigVector1), 1e-3);
        assertEquals(bigCov.get(2, 2), Statistics.var(bigVector2), 1e-3);
        assertEquals(bigCov.get(3, 3), Statistics.var(bigVector3), 1e-3);
    }
    
    private void auxVar_Patology()
    {
        try {
            Vector v = new Vector(new double[]{3.4});
            Statistics.var(v);
            fail("Calling var on one elemenent vector should throw");
        } catch (DimensionException ex) {
            // OK!
        }
    }
    
/*************************************************************************************************/
/************************* Tests of covariance called on vectors *********************************/
   
    public void testCov_Vectors()
    {
        auxCov_Vectors_sameVector();
        auxCov_Vectors_Dependent();
        auxCov_Vectors_Small();
        auxCov_Vectors_Big();
        auxCov__Vectors_Patology();
    }
    
    private void auxCov_Vectors_sameVector()
    {
        Vector copy = new Vector(bigVector1);
        assertEquals(   Statistics.var(bigVector1),
                        Statistics.cov(bigVector1, copy),
                        1e-3 );
    }

    private void auxCov_Vectors_Dependent()
    {
        Vector v1 = Sequences.intersperse(n); //variance equals 1
        Vector v2 = v1.times(7.5);
        assertEquals(7.5, Statistics.cov(v1, v2), 1e-3);
    }
    
    private void auxCov_Vectors_Small()
    {
        assertEquals(smallCov.get(1, 2), Statistics.cov(smallVector1, smallVector2), 1e-3);
    }

    private void auxCov_Vectors_Big()
    {
        assertEquals(bigCov.get(1, 2), Statistics.cov(bigVector1, bigVector2), 1e-3);
        assertEquals(bigCov.get(1, 3), Statistics.cov(bigVector1, bigVector3), 1e-3);
        assertEquals(bigCov.get(2, 3), Statistics.cov(bigVector2, bigVector3), 1e-3);
    }
    
    private void auxCov__Vectors_Patology()
    {
        Vector v1 = new Vector(new double[]{1, 2, 3, 4});
        Vector v2 = new Vector(new double[]{1, 2, 3});
        Vector v3 = new Vector(new double[]{1});
        Vector v4 = new Vector(new double[]{4});
        try {
            Statistics.cov(v1, v2);
            fail("Calling cov for vectors of different sizes should throw");
        } catch (DimensionException ex) {
            // OK!
        }
        try {
            Statistics.cov(v3, v4);
            fail("Calling cov on one elemenent vectors should throw");
        } catch (DimensionException ex) {
            // OK!
        }
    }
/*************************************************************************************************/
/************************* Tests of covariance called on matrix **********************************/

    public void testCov_Matrix()
    {
        auxCov_Matrix_sameVector();
        auxCov_Matrix_Dependent();
        auxCov_Matrix_Small();
        auxCov_Matrix_Big();
        auxCov_Matrix_Patology();
    }
    
    private void auxCov_Matrix_sameVector()
    {
        Vector v1 = Sequences.intersperse(n);
        Vector v2 = new Vector(v1);
        Matrix m = v1.cbind(v2);
        Matrix cov = Statistics.cov(m);
        assertEquals( 2, cov.getCols() );
        assertEquals( 2, cov.getRows() );
        for (int row = 1; row <= 2; ++row)
            for (int col = 1; col <= 2; ++col)
                assertEquals(1, cov.get(row, col), 1e-3);
    }

    private void auxCov_Matrix_Dependent()
    {
        Vector v1 = bigVector1;
        Vector v2 = v1.times(2);
        Vector v3 = v1.times(4);
        Matrix cov = Statistics.cov(v1.cbind(v2).cbind(v3));
        assertEquals( 3, cov.getCols() );
        assertEquals( 3, cov.getRows() );
        assertTrue( cov.isSymmetric() );
        double var = Statistics.var(v1);
        assertEquals( 2*var, cov.get(1, 2), 1e-3 );
        assertEquals( 4*var, cov.get(1, 3), 1e-3 );
        assertEquals( 8*var, cov.get(2, 3), 1e-3 );
    }

    private void auxCov_Matrix_Small()
    {
        Matrix cov = Statistics.cov(smallMat);
        assertEquals( smallCov, cov );
    }

    private void auxCov_Matrix_Big()
    {
        Matrix cov = Statistics.cov(bigMat);
        assertEquals( bigCov, cov );
    }
    
    private void auxCov_Matrix_Patology()
    {
        try {
            Matrix m = new Matrix(
                    new double[][] { {1, 2, 3, 4} } );
            Statistics.cov(m);
            fail("Calling cov on one row matrix should throw");
        } catch (DimensionException ex) {
            // OK!
        }
    }
/*************************************************************************************************/
/************************* Tests of correlation called on vectors ********************************/
        
    public void testCorr_Vectors()
    {
        auxCorr_Vectors_Small();
        auxCorr_Vectors_Big();
        auxCorr_Vectors_Patology();
    }
    
    private void auxCorr_Vectors_Small()
    {
        assertEquals(smallCorr.get(1, 2), Statistics.corr(smallVector1, smallVector2), 1e-3);
    }

    private void auxCorr_Vectors_Big()
    {
        assertEquals(bigCorr.get(1, 2), Statistics.corr(bigVector1, bigVector2), 1e-3);
        assertEquals(bigCorr.get(1, 3), Statistics.corr(bigVector1, bigVector3), 1e-3);
        assertEquals(bigCorr.get(2, 3), Statistics.corr(bigVector2, bigVector3), 1e-3);
    }    
    
    private void auxCorr_Vectors_Patology()
    {
        try {
            Vector v1 = new Vector( new double[] {1, 2, 3, 4} );
            Vector v2 = new Vector( new double[] {2, 2, 2, 2} );
            Statistics.corr(v1, v2);
            fail("Calling corr on const vector should throw");
        } catch (NoCorrelationException ex) {
            // OK!
        }
    }
    
/*************************************************************************************************/
/************************ Tests of correlation called on matrix **********************************/
    
    public void testCorr_Matrix()
    {
        auxCorr_Matrix_Small();
        auxCorr_Matrix_Big();
        auxCorr_Matrix_Patalogy();
    }
    
    private void auxCorr_Matrix_Small()
    {
        Matrix corr = Statistics.corr(smallMat);
        assertEquals( smallCorr, corr );
    }

    private void auxCorr_Matrix_Big()
    {
        Matrix corr = Statistics.corr(bigMat);
        assertEquals( bigCorr, corr );
    }
    
    private void auxCorr_Matrix_Patalogy()
    {
        try {
            Matrix m = new Matrix(new double[][]
                                    {   {1, 2, 3, 4},
                                        {2, 2, 4, 5},
                                        {0, 2, 2, 3}    });
            Statistics.corr(m);
            fail("Calling corr on matrix with const column should throw");
        } catch (NoCorrelationException ex) {
            // OK!
        }
    }
    
/*************************************************************************************************/
/************** Tests of cumulative standard normal distribuant function *************************/

    public void testCndf()
    {
        assertEquals(0, Statistics.cndf(-1e9), 1e-3);
        assertEquals(1, Statistics.cndf(1e9), 1e-3);
        assertEquals(0.5, Statistics.cndf(0), 1e-3);
        assertEquals(0.975, Statistics.cndf(1.959964), 1e-3);
    }
    
/*************************************************************************************************/
/************** Tests of cumulative standard normal distribuant function *************************/

    final int n = 10000; //size of vectors for big tests
    Vector smallVector1, smallVector2, bigVector1, bigVector2, bigVector3, smallMean, bigMean;
    Matrix smallMat, bigMat, smallCov, smallCorr, bigCov, bigCorr;
}
