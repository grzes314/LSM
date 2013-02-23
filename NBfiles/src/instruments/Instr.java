
package instruments;

/**
 *
 * @author grzes
 */
public abstract class Instr
{
    public Instr(double timeHorizon)
    {
        T = timeHorizon;
    }
    
  /*  public boolean maybeExercised(int t)
    {
        return t == T;
    }*/
    
    abstract public double payoff(double S, double t);
    abstract public double intrisnicValue(double S);
    
    public double getTimeHorizon() { return T; }
            
    protected double T; //time horizon

    abstract public String desc();
    
    @Override
    public String toString()
    {
        return "Unknown Instrument";
    }
}
