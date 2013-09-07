
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.methods.blackscholes.BlackScholes;
import finance.parameters.ModelParams;
import finance.parameters.OneAssetParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import finance.trajectories.Dividend;
import finance.trajectories.Generator;
import finance.trajectories.MultiTrGenerator;
import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;
import java.util.Collection;
import math.matrices.Vector;
import math.utils.Statistics;

/**
 *
 * @author Grzegorz Los
 */
public class CV extends MonteCarlo
{
    public enum ControlVariate
    {
        Stock, VanillaCall, VanillaPut
    }

    /**
     * Constructor.
     * @param params model parameters
     * @param cv flag indicating what should be the control variate.
     * @param controlAssetNr number of the asset corresponding to the control variate.
     * @param strike strike value of the option taken as a control variate, important only
     * if ControlVariate is vanilla option value.
     */
    public CV(ModelParams params, ControlVariate cv, String controlAssetName, double strike)
    {
        super(params);
        this.cv = cv;
        this.controlAssetName = controlAssetName;
        this.strike = strike;
    }

    /**
     * Constructor.
     * @param cv flag indicating what should be the control variate.
     * @param controlAssetNr number of the asset corresponding to the control variate.
     * @param strike strike value of the option taken as a control variate, important only
     * if ControlVariate is vanilla option value.
     */
    public CV(ControlVariate cv, String controlAssetName, double strike)
    {
        this.cv = cv;
        this.controlAssetName = controlAssetName;
        this.strike = strike;
    }
    
    @Override
    public String methodName()
    {
        return "Monte Carlo with control variates";
    }

    @Override
    protected void initPricing(Instr instr)
    {
        TimeSupport ts = new TimeSupport(instr.getT(), K);
        gen = new MultiTrGenerator(params, Generator.Measure.MART, ts);
        payoffs = new Vector(N);
        controls = new Vector(N);
        simulation = 1;
        calcExpectedValueOfCV(instr.getT());
    }
    
    private void calcExpectedValueOfCV(double T)
    {
        switch (cv)
        {
            case VanillaPut:
                expectedValueOfCV = calcExpectedValueOfOption(
                        T, VanillaOptionParams.CallOrPut.PUT );
                break;
            case VanillaCall:
                expectedValueOfCV = calcExpectedValueOfOption(
                        T, VanillaOptionParams.CallOrPut.CALL );
                break;
            default:
                expectedValueOfCV = calcExpectedValueOfStock();
        }
    }
    
    private double calcExpectedValueOfOption(double T, VanillaOptionParams.CallOrPut callOrPut)
    {
        OneAssetParams oap = params.getParams(controlAssetName);
        SimpleModelParams smp = new SimpleModelParams(oap.S, oap.vol, params.getR());
        VanillaOptionParams vop = new VanillaOptionParams(
                strike, T, callOrPut );
        BlackScholes bs = new BlackScholes(smp);
        return bs.price(vop);
    }    
    
    private double calcExpectedValueOfStock()
    {
        OneAssetParams oap = params.getParams(controlAssetName);
        return oap.S;
    }
    
    @Override
    protected void oneSimulation(Instr instr)
    {
        Scenario scenario = gen.generate();
        setPayoff(instr, scenario);
        setControl(instr, scenario);
        simulation++;
    }

    protected void setPayoff(Instr instr, Scenario scenario)
    {
        double p = getDiscountedPayoff(instr, scenario);
        payoffs.set(simulation, p);
    }

    private double getDiscountedPayoff(Instr instr, Scenario scenario)
    {
        return instr.payoff(scenario, K) * 
                Math.exp(-params.getR()*instr.getT());
    }

    private void setControl(Instr instr, Scenario scenario)
    {
        double S = scenario.getTr(controlAssetName).price(scenario.getTS().getK());
        double discount = Math.exp(-params.getR()*instr.getT());
        switch (cv)
        {
            case VanillaCall:
                controls.set(simulation, discount * Math.max(S - strike, 0));
                break;
            case VanillaPut:
                controls.set(simulation, discount * Math.max(strike - S, 0));
                break;
            default:
                controls.set(simulation, discount * S);
        }
    }

    @Override
    protected Result currentResult(int simulation)
    {
        if (simulation < N)
        {
            /*Vector H = payoffs.subvector(1, simulation-1);
            Vector C = controls.subvector(1, simulation-1);
            return adjustPayoffsToControl(H, C);*/
            return new Result(-1, 0);
        }
        else
            return adjustPayoffsToControl(payoffs, controls);
    }
    
    private Result adjustPayoffsToControl(Vector subpayoffs, Vector subcontrols)
    {
        double c = getC(subpayoffs, subcontrols);
        Vector adjusted = new Vector(subpayoffs.getSize());
        for (int row = 1; row <= adjusted.getSize(); ++row)
        {
            double adjustedValue = subpayoffs.get(row) +
                    c * (subcontrols.get(row) - expectedValueOfCV);
            adjusted.set(row, adjustedValue);
        }
        return makeResult(adjusted);
    }
    
    private double getC(Vector H, Vector C)
    {
        double numarator = - Statistics.cov(H, C);
        double denominator = Statistics.var(C);
        if (denominator == 0)
            return 0;
        else
            return numarator / denominator;
    }
    
    private Result makeResult(Vector adjusted)
    {
        double mean = Statistics.mean(adjusted);
        double var = Statistics.var(adjusted);
        double se = Math.sqrt( var / simulation );
        return new Result(mean, se);
    }

    @Override
    protected void passDividendsToGenerator(Collection<Dividend> dividends)
    {
        gen.setDividends(dividends);
    }
    
    private Generator gen;
    private ControlVariate cv;
    private Vector payoffs, controls;
    private int simulation;
    private String controlAssetName;
    private double strike;
    private double expectedValueOfCV;
}
