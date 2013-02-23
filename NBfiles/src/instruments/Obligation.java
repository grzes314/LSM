
package instruments;

/**
 *
 * @author grzes
 */
public class Obligation extends Instr
{

    public Obligation(double timeHorizon)
    {
        super(timeHorizon);
    }

    @Override
    public double payoff(double S, double t)
    {
        if (t < T) return 0d;
        else return 1d;
    }

    @Override
    public String desc()
    {
        return "Obligation paying 1 after" + T + " years";
    }
    
    @Override
    public String toString()
    {
        return "Obligation";
    }

    @Override
    public double intrisnicValue(double S)
    {
        return 1;
    }
}
