
package instruments;

/**
 *
 * @author grzes
 */
public class AmOption extends Option
{
    public AmOption(int type, double strike, double timeHorizon)
    {
        super(type, strike, timeHorizon);

    }
          
    @Override
    public String desc()
    {
        return "American option\n" +
                "Type: " + (type == CALL ? "call" : "put") +
                "\nStrike: " + K +
                "\nExpiracy: " + T;
    }
    
    @Override
    public String toString()
    {
        return "Am" + (type == CALL ? "Call" : "Put") + "@" + K;
    }
    
/*    
    @Override
    public double payoff(double[] S, int t)
    {
        if (t <= 0) throw new PricingFatalError("Payoff of american option may"
                + " can't be taken at time 0.");
        if (t > T) throw new PricingFatalError("Payoff of american option may"
                + " can't be taken after expiracy.");
        return intrisnicValue(S, t);
    }
*/

}
