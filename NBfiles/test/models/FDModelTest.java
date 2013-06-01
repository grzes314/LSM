
package models;

/**
 *
 * @author Grzegorz Los
 */
public class FDModelTest extends BaseForTests
{
            
    public void testEuCallExtremes()
    {
        callExtremeSpot(EU);
        callExtremeStrike(EU);
    }
    
    public void testEuCallTypical()
    {
        euCallTypical();
    }
    
    public void testEuPutExtremes()
    {
        euPutExtremeSpot();
        euPutExtremeStrike();
    }
    
    public void testEuPutTypical()
    {
        euPutTypical();
    }
        
    public void testAmCallExtremes()
    {
        callExtremeSpot(AM);
        callExtremeStrike(AM);
    }
    
    public void testAmCallTypical()
    {
        amCallTypical();
    }

    public void testAmPutExtremes()
    {
        amPutExtremeSpot();
        amPutExtremeStrike();
    }
    
    public void testAmPutTypical()
    {
        amPutTypical();
    }
    
    @Override
    protected void setModelParams(double S, double vol, double r)
    {
        model = new FDModel(S, vol, r);
    }

    @Override
    protected double price(double strike, double T, int callPut, int amEu)
    {
        int K = 100000; // time steps
        double vol = model.getVol();
        int I = (int) Math.sqrt( 0.9*K / (T*vol*vol) );
        return model.price(strike, T, I, K, (callPut == CALL), (amEu == AM));
    }

    @Override
    protected double priceBarrier(double strike, double T, int callPut,
        int amEu, double barrier, int barType)
    {
        throw new UnsupportedOperationException("FDModel does not support "
                + "barrier options");
    }
    
    @Override
    protected double priceObl(double T)
    {
        throw new UnsupportedOperationException("FDModel does not support "
                + "obligations");
    }

    private FDModel model;
}
