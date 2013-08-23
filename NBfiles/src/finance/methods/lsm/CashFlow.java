
package finance.methods.lsm;

/**
 *
 * @author Grzegorz Los
 */
public class CashFlow
{
    public CashFlow(double x, Double t)
    {
        this.x = x;
        this.t = t;
    }
    
    /**
     * Returns value of the cash flow discounted or accumulated to time t.
     * @param t value of cash flow is quoted in terms of time {@code t}.
     * @param r riskless interest rate.
     * @return value of the cash flow quoted in terms of time {@code t}.
     */
    public double value(double t, double r)
    {
        return x * Math.exp(- r * (this.t - t));
    }
    
    /**
     * Cash flows value from the perspective of time 0.
     * @param r riskless interest rate.
     * @return value of the cash flow quoted in terms of time 0.
     */
    public double value(double r)
    {
        return value(0, r);
    }
    
    /**
     * Nominal amount of value in a cash flow.
     */
    public double x;
    
    /**
     * Time of cash flow.
     */
    public double t;
}