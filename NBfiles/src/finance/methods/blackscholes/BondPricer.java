
package finance.methods.blackscholes;

import finance.parameters.SimpleModelParams;

/**
 *
 * @author Grzegorz Los
 */
public class BondPricer
{
    public double price(double nominal, double T)
    {
        return nominal * Math.exp(-r*T);
    }

    void setParams(SimpleModelParams smp)
    {
        r = smp.r;
    }
    
    private double r;
}
