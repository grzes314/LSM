
package instruments;

/**
 *
 * @author grzes
 */
public abstract class Option extends Instr
{
    public Option(int type, double strike, double timeHorizon)
    {
        super(timeHorizon);
        this.K = strike;
        this.type = type;
    }
    
    public double getStrike()
    {
        return K;
    }
    
    @Override
    public double payoff(double S, double t)
    {
        if (type == CALL) return Math.max(0, S - K);
        else return Math.max(0, K - S);
    }
    
    @Override
    public double intrisnicValue(double S)
    {
        return payoff(S,0);
    }
    
    public int getType()
    {
        return type;
    }
     
   /* public double intrisnicValue(double[] S, int t)
    {
        if (type == CALL) return Math.max(0, S[t] - K);
        else return Math.max(0, K - S[t]);
    }*/
    
    public static final int CALL = 0, PUT = 1;
    
    int type;
    protected double K;
}
