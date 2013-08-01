
package finance.methods.montecarlo;

import finance.instruments.Instr;
import finance.parameters.ModelParams;

/**
 *
 * @author Grzegorz Los
 */
public class AV extends MonteCarlo
{
    public AV(ModelParams params)
    {
        super(params);
    }

    @Override
    protected Result price(Instr instr)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String methodName()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
