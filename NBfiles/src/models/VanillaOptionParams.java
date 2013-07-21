
package models;

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
    
    public VanillaOptionParams withStrike(double newStrike)
    {
        return new VanillaOptionParams(newStrike, T, callOrPut, amOrEu);
    }
    
    public VanillaOptionParams withT(double newT)
    {
        return new VanillaOptionParams(strike, newT, callOrPut, amOrEu);
    }
    
    public VanillaOptionParams asCall()
    {
        return new VanillaOptionParams(strike, T, CallOrPut.CALL, amOrEu);
    }
    
    public VanillaOptionParams asPut()
    {
        return new VanillaOptionParams(strike, T, CallOrPut.PUT, amOrEu);
    }
    
    public VanillaOptionParams asEuropean()
    {
        return new VanillaOptionParams(strike, T, callOrPut, AmOrEu.EU);
    }
    
    public VanillaOptionParams asAmerican()
    {
        return new VanillaOptionParams(strike, T, callOrPut, AmOrEu.AM);
    }
    
    public final double strike;
    public final double T;
    public final CallOrPut callOrPut;
    public final AmOrEu amOrEu;
}
