
package finance.trajectories;

/**
 *
 * @author Grzegorz Los
 */
public class DividendPerc extends Dividend
{

    public DividendPerc(double perc, double t, String underlying)
    {
        super(t, underlying);
        this.perc = perc;
    }
    
    @Override
    public double getDividend(double S)
    {
        return perc / 100 * S;
    }
    
    public final double perc;
}
