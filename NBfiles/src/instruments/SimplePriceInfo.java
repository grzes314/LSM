
package instruments;

/**
 *
 * @author grzes
 */
public class SimplePriceInfo implements PriceInfo
{

    public SimplePriceInfo(double price, Object model, Instr instr)
    {
        this.price = price;
        this.model = model;
        this.instr = instr;
    }

    @Override
    public Instr getInstr()
    {
        return instr;
    }

    @Override
    public Object getModel()
    {
        return model;
    }

    @Override
    public double getPrice()
    {
        return price;
    }
    
    private double price;
    private Object model;
    private Instr instr;
}
