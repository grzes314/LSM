
package finance.parameters;

import finance.methods.common.WrongParamException;

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
        this(strike, T, callOrPut, AmOrEu.EU);
    }

    public VanillaOptionParams(double strike, double T, CallOrPut callOrPut, AmOrEu amOrEu)
    {
        checkArgs(strike, T);
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
    
    private void checkArgs(double strike, double T)
    {
        if (strike < 0)
            throw new WrongParamException("strike = " + strike);
        if (T < 0)
            throw new WrongParamException("T = " + T);
    }
    
    public final double strike;
    public final double T;
    public final CallOrPut callOrPut;
    public final AmOrEu amOrEu;
}
