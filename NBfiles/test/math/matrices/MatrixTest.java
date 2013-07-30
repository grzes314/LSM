package math.matrices;

import junit.framework.TestCase;

public class MatrixTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        
        rowsRec = 5;
        colsRec = 7;
        rect = makeRect();

        rowsSq = 5;
        colsSq = 5;
        square = makeSquare();

        rowsTr = 10;
        colsTr = 10;
        triangular = makeTriangular();
        
    }
    
    private Matrix makeSquare()
    {
        Matrix res = new Matrix(rowsSq, colsSq);
        for (int i = 1; i <= rowsSq; ++i)
            for (int j = 1; j <= colsSq; ++j)
                res.set(i, j, (i + j)/3.0);
        return res;
    }
    
    private Matrix makeRect()
    {
        Matrix res = new Matrix(rowsRec, colsRec);
        for (int i = 1; i <= rowsRec; ++i)
            for (int j = 1; j <= colsRec; ++j)
                res.set(i, j, 1.0/(i+j));
        return res;
    }

    private Matrix makeTriangular()
    {
        Matrix res = new Matrix(rowsTr, colsTr);
        for (int i = 1; i <= rowsRec; ++i)
            for (int j = 1; j <= i; ++j)
                res.set(i, j, (double)i/j);
        return res;
    }
    
    public void testGettersSetters()
    {
        assertTrue(rect.getRows() == rowsRec);
        assertTrue(rect.getCols() == colsRec);
        for (int i = 1; i <= rowsRec; ++i)
            for (int j = 1; j <= colsRec; ++j)
                assertEquals( 1.0/(i+j), rect.get(i, j), delta );
    }
    
    public void testEquals()
    {
        assertFalse( rect.equals(null) );
        assertFalse( rect.equals(new Double(5.0)) );
        assertFalse( rect.equals(square) );
        assertTrue( rect.equals(rect) );
        
        Matrix rectCopy = makeRect();
        assertTrue( rect.equals(rectCopy) );
        
        rectCopy.set(rowsRec, colsRec, 66.6);
        assertFalse( rect.equals(rectCopy) );        
    }
    
    public void testCopyConstructor()
    {
        assertTrue( rect.equals(new Matrix(rect)) );
        assertTrue( square.equals(new Matrix(square)) );
    }

    public void testTranspose()
    {
        Matrix trans = rect.transpose();
        assertTrue( rect.equals(trans.transpose()) );
        
        for (int i = 1; i <= rowsRec; ++i)
            for (int j = 1; j <= colsRec; ++j)
                assertEquals( 1.0/(i+j), trans.get(j, i), delta );
    }
    
    public void testAddition()
    {
        Matrix sum = rect.add(rect);
        for (int row = 1; row <= sum.getRows(); ++row)
            for (int col = 1; col <= sum.getCols(); ++col)
                assertEquals(rect.get(row, col)*2, sum.get(row, col), 1e-3);                
    }
    
    public void testTimes()
    {
        Matrix res = rect.times(2);
        for (int row = 1; row <= res.getRows(); ++row)
            for (int col = 1; col <= res.getCols(); ++col)
                assertEquals(rect.get(row, col)*2, res.get(row, col), 1e-3);        
    }
    
    public void testMultiplicationByMatrix()
    {
        Matrix idL = new Matrix(rowsRec, rowsRec);
        Matrix idR = new Matrix(colsRec, colsRec);
        assertTrue( idL.mult(rect).equals(rect) );
        assertTrue( rect.mult(idR).equals(rect) );
        assertTrue( rect.mult(rect.transpose()).isSymmetric() );
    }

    public void testMultiplicationByVector()
    {
        Vector vec = new Vector(rect.getCols());
        for (int i = 1; i <= vec.getRows(); ++i)
            vec.set(i, 1);
        Vector res = rect.mult(vec);
        for (int row = 1; row <= rect.getRows(); ++row)
        {
            double sum = 0;
            for (int col = 1; col <= rect.getCols(); ++col)
                sum += rect.get(row, col);
            assertEquals(sum, res.get(row), delta);
        }
    }
    
    public void testCholesky() throws NotPositiveDefiniteMatrixException
    {
        auxCholeskyNotProperMatrix();
        auxCholeskyOnOneElemMatrix();
        auxCholeskyOn2x2Matrix();
        auxCholeskyWithTriangularMatrix();
    }
    
    private void auxCholeskyNotProperMatrix()
    {
        try {
            rect.cholesky();
            TestCase.assertTrue("Cholesky decomposition on rectangle matrix should throw", false);
        } catch (NotPositiveDefiniteMatrixException e) {}
        try {
            square.cholesky();
            TestCase.assertTrue("Cholesky decomposition on not positive definite matrix " +
                    "should throw", false);
        } catch (NotPositiveDefiniteMatrixException e) {}        
    }
    
    private void auxCholeskyOnOneElemMatrix() throws NotPositiveDefiniteMatrixException
    {
        Matrix corr = new Matrix(1, 1);
        corr.set(1, 1, 4);
        assertEquals(2, corr.cholesky().get(1, 1), delta);        
    }
    
    private void auxCholeskyOn2x2Matrix() throws NotPositiveDefiniteMatrixException
    {
        Matrix corr = new Matrix(2, 2);
        double ro = 0.7;
        corr.set(1, 2, ro);
        corr.set(2, 1, ro);
        Matrix L = corr.cholesky();
        assertEquals(1, L.get(1, 1), delta);
        assertEquals(0, L.get(1, 2), delta);
        assertEquals(ro, L.get(2, 1), delta);
        assertEquals(Math.sqrt(1 - ro*ro), L.get(2, 2), delta);
    }
    
    private void auxCholeskyWithTriangularMatrix()
    {
        try {
            Matrix M = triangular.mult(triangular.transpose());
            assertTrue( triangular.equals(M.cholesky()) );
        } catch (NotPositiveDefiniteMatrixException e) {
            assertTrue("Cholesky decomposition on positive definite matrix should not throw", false);
        }    
    }

    private double delta = 1e-3;
    private int rowsSq, colsSq, rowsRec, colsRec, rowsTr, colsTr;
    private Matrix rect, square, triangular;
}
