
package lsmapp.resultPanels;

import finance.instruments.Instr;
import finance.methods.blackscholes.BlackScholes;

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

    public void setInstr(Instr instr)
    {
        this.instr = instr;
    }

    public BlackScholes getMethod()
    {
        return method;
    }

    public void setMethod(BlackScholes method)
    {
        this.method = method;
    }
    
    public void setMethodAndInstr(BlackScholes method, Instr instr)
    {
        this.method = method;
        this.instr = instr;        
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
        displayer.addResults(instr.toString(), Auxiliary.basicInfo(method, instr, price));
    }
        
    private final ResultDisplay displayer;
    private BlackScholes method;
    private Instr instr;
}
