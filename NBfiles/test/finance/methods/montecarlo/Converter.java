
package finance.methods.montecarlo;

import finance.instruments.Barrier;
import finance.instruments.EuExercise;
import finance.instruments.Instr;
import finance.instruments.Option;
import finance.methods.testsupports.BarrierParams;
import finance.parameters.VanillaOptionParams;

/**
 *
 * @author Grzegorz Los
 */
public class Converter
{
    public Instr makeOption(VanillaOptionParams vop, String underlying)
    {
        int callOrPut = convertCallPutDecoding(vop.callOrPut);
        return new EuExercise(
                new Option(callOrPut, vop.strike, underlying, vop.T) );
    }
    
    public Instr addBarrier(Instr instr, BarrierParams bp, String underlying)
    {
        int knock = convertKnockDecoding(bp.type);
        int from = convertFromDecoding(bp.type);
        return new Barrier(knock, from, bp.level, underlying, instr);
    }
    
    public int convertKnockDecoding(BarrierParams.Type type)
    {
        if (type == BarrierParams.Type.DAI || type == BarrierParams.Type.UAI)
            return Barrier.KNOCK_IN;
        else
            return Barrier.KNOCK_OUT;
    }
    
    public int convertFromDecoding(BarrierParams.Type type)
    {
        if (type == BarrierParams.Type.DAI || type == BarrierParams.Type.DAO)
            return Barrier.FROM_DOWN;
        else
            return Barrier.FROM_UP;
    }
    
    public int convertCallPutDecoding(VanillaOptionParams.CallOrPut callOrPut)
    {
        if (callOrPut == VanillaOptionParams.CallOrPut.CALL)
            return Option.CALL;
        else
            return Option.PUT;
    }
}
