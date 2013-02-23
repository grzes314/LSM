
package instruments;

/**
 *
 * @author grzes
 */
public class EuOption extends Option
{
    public EuOption(int type, double strike, double timeHorizon)
    {
        super(type, strike, timeHorizon);

    }
        
    @Override
    public double payoff(double S, double t)
    {
        if (t < T) return 0;
        if (type == CALL) return Math.max(0, S - K);
        else return Math.max(0, K - S);
    }
      
    @Override
    public String desc()
    {
        return "European option\n" +
                "Type: " + (type == CALL ? "call" : "put") +
                "\nStrike: " + K +
                "\nExpiracy: " + T;
    }
    
    @Override
    public String toString()
    {
        return "Eu" + (type == CALL ? "Call" : "Put") + "@" + K;
    }
    
  /*  @Override
    public double payoff(double[] S, int t)
    {
        if (t != T) throw new PricingFatalError("Payoff of european option may"
                + " be take only at expiracy time.");
        return intrisnicValue(S, t);
    }
*/

}
