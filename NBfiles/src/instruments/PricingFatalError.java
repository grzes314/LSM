
package instruments;

/**
 *
 * @author grzes
 */
public class PricingFatalError extends RuntimeException
{
    public PricingFatalError() {}
    public PricingFatalError(String mssg) {
        super(mssg);
    }

}
