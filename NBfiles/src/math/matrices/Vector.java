package math.matrices;

import java.util.Iterator;

public class Vector extends Matrix implements Iterable<Double>
{
    public class VecIterator implements  Iterator<Double>
    {
        @Override
        public boolean hasNext()
        {
            return row < getSize();
        }

        @Override
        public Double next()
        {
            return get(++row);
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException("Remove does not "
                    + "have sense for constant size vectors.");
        }         
        int row = 0;
    };
        
    public Vector(int rows)
    {
        super(rows, 1);
        set(1, 0);
    }
    
    public Vector(double[] vals)
    {
        super(vals);
    }
    
    public Vector(Vector original)
    {
        super(original);
    }
    
    public final int getSize()
    {
        return getRows();
    }
    
    public final double get(int row)
    {
        return get(row, 1);
    }
    
    public final void set(int row, double val)
    {
        set(row, 1, val);
    }

    public Vector add(Vector other) throws DimensionException
    {
        if (getRows() != other.getRows())
            throw new DimensionException("Cannot add vector of diffrent lengths");
        Vector res = new Vector(getRows());
        for (int row = 1; row <= getRows(); ++row)
            res.set(row, get(row) + other.get(row));
        return res;
    }
    
    @Override
    public Vector times(double t)
    {
        Vector res = new Vector(getRows());
        for (int row = 1; row <= getRows(); ++row)
            res.set(row, get(row)*t);
        return res;
    }

    @Override
    public VecIterator iterator()
    {
        return new VecIterator();
    }
}