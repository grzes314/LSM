
package finance.instruments;

import finance.parameters.VanillaOptionParams.CallOrPut;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import finance.trajectories.Scenario;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Grzegorz Los
 */
public class BasketOption extends Instr
{

    public BasketOption(double T, String[] assetNames, double[] quantities,
                                  CallOrPut callOrPut, double strike)
        throws InvalidInstrParametersException
    {
        super(T);
        ensureAssetsInBasketOK(assetNames, quantities);
        this.assetNames = assetNames;
        this.quantities = quantities;
        this.type = callOrPut;
        this.strike = strike;
    }

    private void ensureAssetsInBasketOK(String[] assetNames, double[] quantities)
        throws InvalidInstrParametersException
    {
        if (assetNames.length == 0)
            throw new InvalidInstrParametersException("Array of asset names has 0 length.");
        if (assetNames.length != quantities.length)
            throw new InvalidInstrParametersException("Arrays of asset names and "
                + "quantities have different length.");
        for (int i = 0; i < assetNames.length; ++i)
        {
            if (assetNames[i] == null)
                throw new InvalidInstrParametersException("Asset names can not have null values.");
            for (int j = i + 1; j < assetNames.length; ++j)
                if (assetNames[i].equals(assetNames[j]))
                    throw new InvalidInstrParametersException("Asset names in basket can not "
                        + "be duplicated");
        }            
    }


    @Override
    public String getDesc()
    {
        return "A basket option\n" +
                "Type: " + (type == CALL ? "call" : "put") +
                "\nStrike: " + strike +
                "\nExpiry: " + getT() +
                "\nAssets in basket: " +
                listAssetsInBasket();
    }
    
    private String listAssetsInBasket()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < assetNames.length; ++i)
            sb.append("\n -- ").append(assetNames[i]).append(": ").append(quantities[i]);
        return sb.toString();
    }

    @Override
    public String toString()
    {
        return "Basket " + (type == CALL ? "Call" : "Put");
    }

    @Override
    public boolean areYou(String str)
    {
        if (str.equalsIgnoreCase("basket"))
            return true;
        else return false;
    }

    @Override
    public int modificationsCount()
    {
        return 0;
    }

    @Override
    protected boolean exAvail_(Scenario s, int k)
    {
        return true;
    }

    @Override
    protected double payoff_(Scenario s, int k)
    {
        double S = getSum(s, k);
        if (type == CALL) return Math.max(0, S - strike);
        else return Math.max(0, strike - S);
    }
    
    private double getSum(Scenario s, int k)
    {
        double sum = 0;
        for (int i = 0; i < assetNames.length; ++i)
            sum += quantities[i] * s.getTr(assetNames[i]).price(k);
        return sum;
    }

    @Override
    public Collection<String> getUnderlyings()
    {
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < assetNames.length; ++i)
            arr.add(assetNames[i]);
        return arr;
    }
    
    private String[] assetNames;
    private double[] quantities;
    private CallOrPut type;
    private double strike;
}
