
package finance.methods.montecarlo;

import finance.parameters.SimpleModelParams;

/**
 *
 * @author Grzegorz Los
 */
public class CMCTest extends MonteCarloTest
{
    @Override
    protected void makeMethod(SimpleModelParams smp)
    {
        method = new CMC(smp);
    }
}
