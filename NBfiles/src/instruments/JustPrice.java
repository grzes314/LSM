
package instruments;

/**
 *
 * @author grzes
 */
public class JustPrice implements PriceInfo
{

    public JustPrice(double d)
    {
        price = d;
    }

    @Override
    public double getPrice()
    {
        return price;
    }
    
    private double price;
}
