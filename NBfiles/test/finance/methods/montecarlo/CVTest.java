
package finance.methods.montecarlo;

import finance.methods.montecarlo.CV.ControlVariate;
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
        method = new CV(smp,  ControlVariate.Stock, smp.name, smp.S);
        
    }
}
