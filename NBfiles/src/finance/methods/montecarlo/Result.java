
package finance.methods.montecarlo;

/**
 * Record storing result of Monte Carlo estimation: estimator, variance and standard error.
 * @author Grzegorz Los
 */
public class Result
{
    public Result(double result, double stderr)
    {
        this.result = result;
        this.stderr = stderr;
    }
    public final double result;
    public final double stderr;
}
