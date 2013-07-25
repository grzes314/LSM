
package math.matrices;

import java.util.Arrays;
import math.utils.Numerics;

/**
 * Class Matrix is designed to represent matrices of real numbers. It supports operations
 * typically used in mathematics like multiplication, transposition, Cholesky decomposition.
 * Indexes of elements of the matrices are 1-based.
 * @author Grzegorz Los
 */
public class Matrix
{
    /**
     * Creates matrix of given size with ones on the diagonal and zeros elsewhere.
     * @param rows the number of rows in the matrix.
     * @param cols the number of columns in the matrix.
     */
    public Matrix(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        fields = new double[rows][cols];
        for (int i = 0; i < Math.min(rows, cols); ++i)
            fields[i][i] = 1.0;
        ensureDimensionOK(rows, cols);
    }
    
    private void ensureDimensionOK(int rows, int cols)
    {
        if (rows <= 0)
            throw new IllegalArgumentException("rows = " + rows);    
        if (cols <= 0)
            throw new IllegalArgumentException("rows = " + cols);    
    }

    /**
     * Creates a matrix, whose elements are copied from given array. The number of rows in
     * created matrix will equal fields.length, and the number of columns will equal
     * fields[0].length.
     * @param fields an array with elements of the matrix.
     */
    public Matrix(double[][] fields)
    {
        ensureFieldsOK(fields);
        rows = fields.length;
        cols = fields[0].length;
        this.fields = new double[rows][cols];
        copy(fields);
    }
    
    private void copy(double[][] fields)
    {
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                this.fields[i][j] = fields[i][j];
    }
    
    private void ensureFieldsOK(double[][] fields)
    {
        ensureNotNull(fields, "Given array is null");
        ensureHasRows(fields, "Matrix must have at least one row");
        ensureRowsNotNull(fields, "Rows of the array may not be null");
        ensureRectangleArray(fields, "Rows of the array must have the same size");
        ensureHasCols(fields, "Matrix must have at least one column");
    }
    
    private void ensureNotNull(Object ob, String msg)
    {
        if (ob == null)
            throw new IllegalArgumentException(msg);        
    }
    
    private void ensureHasRows(double[][] obs, String msg)
    {
        if (obs.length == 0)
            throw new IllegalArgumentException(msg);            
    }
    
    private void ensureRowsNotNull(double[][] obs, String msg)
    {
        for (int i = 0; i < obs.length; ++i)
            if (fields[i] == null)
                throw new IllegalArgumentException(msg);
    }
    
    private void ensureRectangleArray(double[][] array, String msg)
    {
        int l = array[0].length;
        for (int i = 1; i < array.length; ++i)
            if (fields[i].length != l)
                throw new IllegalArgumentException(msg);
    }
    
    private void ensureHasCols(double[][] array, String msg)
    {
        if (array[0].length == 0)
            throw new IllegalArgumentException(msg);    
    }

    /**
     * Copy constructor. Creates a matrix of the same size and the same elements.
     * @param m original matrix.
     */
    public Matrix(Matrix m)
    {
        rows = m.rows;
        cols = m.cols;
        fields = new double[rows][cols];
        copy(m.fields);
    }
    
    /**
     * Constructor used by derived Vector class. Creates a matrix with one column.
     * @param vals values of the Vector.
     */
    protected Matrix(double[] vals)
    {
        rows = vals.length;
        cols = 1;
        fields = new double[rows][cols];
        for (int i = 0; i < rows; ++i)
            fields[i][0] = vals[i];
    }
    
    /**
     * Creates a matrix which is transposition of this.
     * @return matrix which is transposition of this.
     */
    public Matrix transpose()
    {
        Matrix m = new Matrix(cols, rows);
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                m.fields[j][i] = fields[i][j];
        return m;
    }
    
    /**
     * Return true if and only if this matrix has the same number of rows and columns.
     * @return true if and only if this matrix is square matrix.
     */
    public boolean isSquare()
    {
        return rows == cols;
    }
    
    /**
     * Return true if and only if this matrix is equal to its transposition.
     * @return true if and only if this matrix is symmetric.
     */
    public boolean isSymmetric()
    {
        return equals(transpose());
    }
    
    /**
     * Returns Cholesky decomposition of the matrix. Let L = M.cholesky(), and L' be the
     * transposition of L. Then LL' = M. L is a lower triangular matrix.
     * @return Cholesky decomposition of the matrix.
     * @throws NotPositiveDefiniteMatrixException if given matrix is no positive definite.
     */
    public Matrix cholesky() throws NotPositiveDefiniteMatrixException
    {
        if (!isSquare())
            throw new NotPositiveDefiniteMatrixException();
        Matrix L = new Matrix(rows, rows);
        for (int row = 1; row <= rows; ++row)
            calculateOneRowOfCholesky(row, L);
        return L;
    }
    
    private void calculateOneRowOfCholesky(int row, Matrix L) throws NotPositiveDefiniteMatrixException
    {
        L.set(row, row, 0);
        calcNonDiagonalElementsOfCholesky(row, L);
        calcDiagonalElementOfCholesky(row, L);        
    }
    
    private void calcNonDiagonalElementsOfCholesky(int row, Matrix L)
    {
        for (int col = 1; col < row; ++col)
            calcNonDiagonalElementOfCholesky(row, col, L);
    }    
    
    private void calcNonDiagonalElementOfCholesky(int row, int col, Matrix L)
    {
        double sum = 0;
        for (int k = 1; k < col; ++k)
            sum += L.get(row, k) * L.get(col, k);
        L.set(row, col, (get(col, row) - sum) / L.get(col, col));
    }    
    
    private void calcDiagonalElementOfCholesky(int row, Matrix L) throws NotPositiveDefiniteMatrixException
    {
        double sum = 0;
        for (int k = 1; k < row; ++k)
            sum += L.get(row, k) * L.get(row, k);
        
        double x = get(row, row) - sum;
        if (x < 0)
            throw new NotPositiveDefiniteMatrixException();
        L.set(row, row, Math.sqrt(x));
    }

    /**
     * Performs matrix addition.
     * @param other a matrix standing on the right side of the multiplication.
     * @return a matrix which is the result of the multiplication.
     * @throws DimensionException when dimensions of the matrices are not suitable.
     */
    public Matrix add(Matrix other) throws DimensionException
    {
        if (cols != other.cols || rows != other.rows)
        {
            throw new DimensionException("Matrix of size " + rows + "x" + cols +
                    " cannot be added to matrix of size " + other.rows + "x" + other.cols);
        }
        return addProper(other);
    }
    
    private Matrix addProper(Matrix other)
    {
        Matrix res = new Matrix(rows, cols);
        for (int row = 1; row <= rows; ++row)
            for (int col = 1; col <= cols; ++col)
                res.set(row, col, get(row, col) + other.get(row, col));
        return res;
    }

    /**
     * Performs matrix multiplication.
     * @param other a matrix standing on the right side of the multiplication.
     * @return a matrix which is the result of the multiplication.
     * @throws DimensionException when dimensions of the matrices are not suitable.
     */
    public Matrix mult(Matrix other) throws DimensionException
    {
        if (cols != other.rows)
        {
            throw new DimensionException("Multiplied matrices do not have suitable sizes " +
                    "(columns of left = " + cols + ", rows of right = " + other.rows + ")");
        }
        try {
            Vector vec = (Vector) other;
            return multVector(vec);
        } catch (ClassCastException e){
            return multMatrices(other);
        }
    }
    
    private Matrix multMatrices(Matrix other)
    {
        int newRows = rows;
        int newCols = other.cols;
        Matrix res = new Matrix(newRows, newCols);
        for (int row = 1; row <= newRows; ++row)
            for (int col = 1; col <= newCols; ++col)
                res.set( row, col, multiplyRowAndCol(row, col, other) );
        return res;
    }

    private Vector multVector(Vector vec)
    {
        Vector res = new Vector(rows);
        for (int row = 1; row <= rows; ++row)
            res.set(row, multiplyRowAndCol(row, 1, vec));
        return res;
    }
    
    private double multiplyRowAndCol(int row, int col, Matrix other)
    {
        double res = 0;
        for (int k = 1; k <= cols ; ++k)
            res += get(row, k) * other.get(k, col);
        return res;
    }

    /**
     * Multiplies given vector by this matrix. 
     * @param vec vector which stays on the right side of the multiplication.
     * @return a vector which is the result of the multiplication.
     * @throws DimensionException  when dimensions of the matrix and vector are not suitable.
     */
    public Vector mult(Vector vec) throws DimensionException
    {
        return (Vector) mult((Matrix) vec);
    }    
        
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + cols;
        result = prime * result + Arrays.hashCode(fields);
        result = prime * result + rows;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Matrix other = (Matrix) obj;
        if (cols != other.cols)
            return false;
        if (rows != other.rows)
            return false;
        return properEquals(other);
    }
    
    private boolean properEquals(Matrix other)
    {
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                if ( !Numerics.doublesEqual(this.fields[i][j], other.fields[i][j]) )
                    return false;
        return true;
    }
    
    /**
     * Returns matrix' element at the specified position. Indexes are 1-based. 
     * @param row row of the element.
     * @param col column of the element.
     * @return element at the specified position.
     */
    public double get(int row, int col)
    {
        return fields[row-1][col-1];
    }
    
    /**
     * 
     * Sets matrix' element at the specified position. Indexes are 1-based. 
     * @param row row of the element.
     * @param col column of the element.
     * @param val new value of the element at the specified position.
     */
    public void set(int row, int col, double val)
    {
        fields[row-1][col-1] = val;
    }

    /**
     * Returns the number of rows in this matrix.
     * @return the number of rows in this matrix.
     */
    public int getRows()
    {
        return rows;
    }

    /**
     * Return the number of columns in this matrix.
     * @return the number of columns in this matrix.
     */
    public int getCols()
    {
        return cols;
    }
    
    public Vector getCol(int col)
    {
        Vector res = new Vector(rows);
        for (int row = 1; row <= rows; ++row)
            res.set(row, get(row, col));
        return res;
    }
    
    public Vector getRow(int row)
    {
        return new Vector(fields[row-1]);
    }
    
    private final double[][] fields;
    private final int rows;
    private final int cols;

}
