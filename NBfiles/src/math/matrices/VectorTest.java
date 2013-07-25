package math.matrices;

import junit.framework.TestCase;

public class VectorTest extends TestCase {
	
	public void testGetter()
	{
		Vector vec = new Vector(new double[]{1, 2, 3});
		for (int row = 1; row <= vec.getRows(); ++row)
			assertEquals(row, vec.get(row), 1e-3);
	}

	public void testSetter()
	{
		Vector vec = new Vector(10);
		for (int row = 1; row <= vec.getRows(); ++row)
			vec.set(row, row*row - 10);
		for (int row = 1; row <= vec.getRows(); ++row)
			assertEquals(row*row - 10, vec.get(row), 1e-3);
	}
	
	public void testAddition() throws DimensionException
	{
		Vector vec1 = new Vector(10);
		Vector vec2 = new Vector(10);
		for (int row = 1; row <= vec1.getRows(); ++row)
		{
			vec1.set(row, row*row - 10);
			vec2.set(row, (row-5)*(row-5)*(row-5));
		}
		Vector sum = vec1.add(vec2);
		assertEquals(10, sum.getRows());
		for (int row = 1; row <= sum.getRows(); ++row)
			assertEquals((row-5)*(row-5)*(row-5) + row*row - 10,
					sum.get(row), 1e-3);
	}
}
