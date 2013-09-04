
package finance.trajectories;

/**
 *
 * @author Grzegorz Los
 */
public class DividendNominal extends Dividend
{

    public DividendNominal(double quota, double t, String underlying)
    {
        super(t, underlying);
        this.quota = quota;
    }
    

    @Override
    public double getDividend(double S)
    {
        return quota;
    }
    
    public double quota;
}
