
package finance.methods.testsupports;

import finance.methods.blackscholes.BlackScholes;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static finance.parameters.VanillaOptionParams.CallOrPut.PUT;
import static java.lang.Math.exp;
import static math.utils.Numerics.plus;

/**
 * Class provides functions useful for testing results of pricing
 * European vanilla options.
 * @author Grzegorz Los
 */
public abstract class EuOption extends ValuationChecker
{
    public EuOption()
    {
    }

    public EuOption(double relativeDelta)
    {
        super(relativeDelta);
    }
    
    public void callExtremeSpot()
    {
        // strike and time fixed, spot price has extreme values
        checkCalls(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                checkPrice(smp.withS(0), vop, 0); // price = 0
                checkPrice(smp.withS(10000), vop,
                         10000 - vop.strike * exp(-smp.r * vop.T)); // price = S_0 - K*exp(-rT)
            }            
        });                
    }
    
    public void callExtremeStrike()
    {
        // spot price and time fixed, stike has extreme values
        checkCalls(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                checkPrice(smp, vop.withStrike(0), smp.S); // price = S_0
                checkPrice(smp, vop.withStrike(10000), 0); // price = 0
            }            
        });     
    }
        
    public void callExtremeTime()
    {
        //spot price and strike fixed, time take extreme values
        checkCalls(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                checkPrice(smp, vop.withT(0), plus(smp.S - vop.strike)); // price = (S - K)+
                //checkPrice(smp, vop.withT(1000), smp.S); // price = S_0
            }            
        });  
    }
    
    public void callTypical()
    {
        checkCalls(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                bsModel.setParams(smp);
                checkPrice(smp, vop, bsModel.price(vop));
            }
        }); 
    }
    
    public void putExtremeSpot()
    {
        // strike and time fixed, spot price has extreme values
        checkPuts(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                checkPrice(smp.withS(0), vop, vop.strike * exp(-smp.r * vop.T));
                   // ^^^ price = K * exp(-rT)
                checkPrice(smp.withS(10000), vop, 0); // price = 0
            }            
        }); 
    }       
    
    public void putExtremeStrike()
    {
        // spot price and time fixed, stike has extreme values
        checkPuts(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                checkPrice(smp, vop.withStrike(0), 0); // price = 0
                checkPrice(smp, vop.withStrike(10000), // price = - S_0 + K*exp(-rT)
                        -smp.S + 10000 * exp(- smp.r * vop.T)); 
            }            
        });
    }      
    
    public void putExtremeTime()
    {
        //spot price and strike fixed, time take extreme values
        checkPuts(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                checkPrice(smp, vop.withT(0), plus(vop.strike - smp.S)); // price = (K - S)+
                checkPrice(smp, vop.withT(1000), 0); // price = 0
            }            
        });  
    }   

    public void putTypical()
    {
        checkCalls(new Checker(){
            @Override
            public void check(SimpleModelParams smp, VanillaOptionParams vop) {
                bsModel.setParams(smp);
                checkPrice(smp, vop, bsModel.price(vop));
            }
        }); 
    }
    
    @Override
    protected void makeTypicalModelParams()
    {
        /*double[] S = {100, 100, 100};
        double[] vol = {0.2, 0.3, 0.4};
        double[] r = {0.05, 0.1, 0.01};*/
        double[] S = {100};
        double[] vol = {0.2};
        double[] r = {0.05};
        
        simpleModelParams = new SimpleModelParams[1];
        for (int i = 0; i < simpleModelParams.length; ++i)
            simpleModelParams[i] = new SimpleModelParams(S[i], vol[i], r[i]); 
    }
    
    @Override
    protected void makeTypicalOptionParams()
    {
        double[] strike = {100, 80, 120};
        double[] T = {1.0, 2.0, 0.5};
        puts = new VanillaOptionParams[3];
        calls = new VanillaOptionParams[3];
        for (int i = 0; i < puts.length; ++i)
        {
            puts[i] = new VanillaOptionParams(strike[i], T[i], PUT);
            calls[i] = new VanillaOptionParams(strike[i], T[i], CALL);
        }
    }
    
    protected BlackScholes bsModel = new BlackScholes();
}
