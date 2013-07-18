
package models.testsupports;

/**
 * Parameters of the most popular options.
 * @author Grzegorz Los
 */
public class VanillaOptionParams
{
    public enum CallOrPut
    {
        CALL, PUT
    }
    public enum AmOrEu
    {
        AM, EU
    }

    public VanillaOptionParams(double strike, double T, CallOrPut callOrPut)
    {
        this.strike = strike;
        this.T = T;
        this.callOrPut = callOrPut;
        this.amOrEu = AmOrEu.EU;
    }

    public VanillaOptionParams(double strike, double T, CallOrPut callOrPut, AmOrEu amOrEu)
    {
        this.strike = strike;
        this.T = T;
        this.callOrPut = callOrPut;
        this.amOrEu = amOrEu;
    }
    
    public final double strike;
    public final double T;
    public final CallOrPut callOrPut;
    public final AmOrEu amOrEu;
}
