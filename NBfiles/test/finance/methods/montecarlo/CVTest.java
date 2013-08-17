
package finance.methods.montecarlo;

import finance.parameters.SimpleModelParams;

/**
 *
 * @author Grzegorz Los
 */
public class CVTest extends MonteCarloTestBase
{
    @Override
    protected void makeMethod(SimpleModelParams smp)
    {
        method = new CV(smp, 1);
        
    }
}
