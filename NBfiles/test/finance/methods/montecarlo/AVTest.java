
package finance.methods.montecarlo;

import finance.parameters.SimpleModelParams;

/**
 *
 * @author Grzegorz Los
 */
public class AVTest extends MonteCarloTestBase
{
    @Override
    protected void makeMethod(SimpleModelParams smp)
    {
        method = new AV(smp);
    }
}
