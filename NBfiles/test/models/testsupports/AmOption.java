
package models.testsupports;

import models.SimpleModelParams;
import models.VanillaOptionParams;

/**
 * Class provides functions useful for testing results of pricing
 * American vanilla options.
 * @author Grzegorz Los
 */
public abstract class AmOption extends EuOption
{
    @Override
    public void callTypical()
    {
        checkCalls(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                bsModel.setParams(smp);
                checkPrice(smp, vop, bsModel.price(vop.asEuropean()));
            }
        }); 
    }

    @Override
    public void putExtremeSpot()
    {
        // strike and time fixed, spot prices has extreme values
        checkPuts(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                checkPrice(smp.withS(0), vop, vop.strike); // price = K, immediete exercise
                checkPrice(smp.withS(10000), vop, 0); // price = 0
            }            
        });
    }    
    
    @Override
    public void putExtremeStrike()
    {
        // spot price and time fixed, stike has extreme values
        checkPuts(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                checkPrice(smp, vop.withStrike(0), 0); // price = 0
                checkPrice(smp, vop.withStrike(10000), 10000 - smp.S);
                  // ^^^ price = - S_0 + K*exp(-rT)
            }            
        });  
    }   
    
    @Override
    public void putExtremeTime()
    {
        //spot price and strike fixed, time take extreme values
        checkPuts(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                checkPrice(smp, vop.withT(0), math.utils.Numerics.plus(vop.strike - smp.S));
                    // ^^^ price = (K - S)+
                comparePutPrices(smp, vop.withT(1000));
            }            
        });         
    }
        
    @Override
    public void putTypical()
    {
        checkPuts(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                comparePutPrices(smp, vop);
            }
        }); 
    }
    
    @Override
    protected void makeTypicalOptionParams()
    {
        super.makeTypicalOptionParams();
        
        for (int i = 0; i < calls.length; ++i)
            calls[i] = calls[i].asAmerican();
        
        for (int i = 0; i < puts.length; ++i)
            puts[i] = puts[i].asAmerican();
    }
    
    private void comparePutPrices(SimpleModelParams smp, VanillaOptionParams vop)
    {
        bsModel.setParams(smp);
        double euPrice = bsModel.price(vop.asEuropean());
        checkPriceIsGreater(smp, vop, euPrice);
        checkPriceIsGreater(smp, vop, math.utils.Numerics.plus(vop.strike - smp.S), 0.01);        
    }
}
