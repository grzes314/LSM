package math.matrices;

public class Vector extends Matrix
{
    public Vector(int rows)
    {
        super(rows, 1);
        set(1, 0); //TODO call to virtual method in constructor
    }
    
    public Vector(double[] vals)
    {
        super(vals);
    }
    
    public double get(int row)
    {
        return get(row, 1);
    }
    
    public void set(int row, double val)
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
}
