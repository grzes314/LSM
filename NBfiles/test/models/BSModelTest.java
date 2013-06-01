
package models;

/**
 *
 * @author Grzegorz Los
 */
public class BSModelTest extends BaseForTests
{
    public void testEuCallExtremes() {
        callExtremeSpot(EU);
        callExtremeStrike(EU);
        callExtremeTime(EU);
    }

    public void testEuPutExtremes() {
        euPutExtremeSpot();
        euPutExtremeStrike();
        euPutExtremeTime();
    }

    public void testEuCallTypical() {
        euCallTypical();
    }

    public void testEuPutTypical() {
        euPutTypical();
    }
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        model = new BSModel();
    }

    @Override
    protected void setModelParams(double S, double vol, double r)
    {
        model.setS(S);
        model.setR(r);
        model.setVol(vol);
    }

    @Override
    protected double price(double strike, double T, int callPut, int amEu)
    {
        if (amEu == AM)
            throw new UnsupportedOperationException("BSModel can "
                    + "price only european options");
        if (callPut == CALL) return model.priceCall(strike, T);
        else return model.pricePut(strike, T);
    }
    
    @Override
    protected double priceBarrier(double strike, double T, int callPut,
        int amEu, double barrier, int barType)
    {
        throw new UnsupportedOperationException("BSModel does not support "
                + "barrier options");
    }
    
    @Override
    protected double priceObl(double T)
    {
        throw new UnsupportedOperationException("BSModel does not support "
                + "obligations");
    }
    
    private BSModel model;
}
