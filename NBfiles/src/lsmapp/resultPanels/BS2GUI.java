
package lsmapp.resultPanels;

import finance.instruments.Instr;
import finance.methods.blackscholes.BSMethod;
import finance.methods.common.Method;
import finance.parameters.ModelParams;

/**
 *
 * @author Grzegorz Los
 */
public class BS2GUI implements ResultHandler
{
    public BS2GUI(ResultDisplay displayer)
    {
        this.displayer = displayer;
    }
    
    public Instr getInstr()
    {
        return instr;
    }

    @Override
    public void setInstr(Instr instr)
    {
        this.instr = instr;
    }

    public BSMethod getMethod()
    {
        return method;
    }

    @Override
    public void setMethod(Method method)
    {
        this.method = (BSMethod) method;
    }

    public ModelParams getModelParams()
    {
        return modelParams;
    }

    @Override
    public void setModelParams(ModelParams modelParams)
    {
        this.modelParams = modelParams;
    }

    @Override
    public void setAll(Method method, ModelParams mp, Instr instr)
    {
        this.instr = instr;
        this.modelParams = mp;
        this.method = (BSMethod) method;
    }
    
    @Override
    public void result(double price)
    {
        showResults(price);
        reset();
    }
    
    public void reset()
    {
        method = null;
        instr = null;
    }
    
    private void showResults(double price)
    {
        InfoPanel ip = new InfoPanel(method.getDesc(), modelParams.getDesc(),
                                     instr.getDesc(), price);
        displayer.addResults(instr.toString() + ": " + method.toString(), ip);
    }
        
    private final ResultDisplay displayer;
    private BSMethod method;
    private Instr instr;
    private ModelParams modelParams;
}
