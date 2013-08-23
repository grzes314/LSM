
package math.approx;

import math.matrices.Matrix;
import math.matrices.UninvertibleMatrixException;
import math.matrices.Vector;

/**
 *
 * @author Grzegorz Los
 */
public class Regresser
{
    /**
     * Solves regression problem {@code Xb = Y}.
     * @param X matrix of predictors.
     * @param Y vector of observations.
     * @return Vector {@code b}.
     */
    public Vector regress(Matrix X, Vector Y) throws UnsupportedCaseException
    {
        ensureRegressionArgsOK(X, Y);
        Matrix tr = X.transpose();
        try {
            return (tr.mult(X)).getInverted().mult(tr).mult(Y);
        } catch (UninvertibleMatrixException ex) {
            throw new UnsupportedCaseException();
        }
    }

    private void ensureRegressionArgsOK(Matrix X, Vector Y)
    {
        if (X.getRows() != Y.getRows())
            throw new IllegalArgumentException("Number of rows in X and Y differ");
        if (X.getRows() < X.getCols())
            throw new IllegalArgumentException("Number of observations is smaller "
                    + "than number of predictors.");
    }
}
