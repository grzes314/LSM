
package finance.methods.finitedifference;

import finance.instruments.Instr;
import static finance.instruments.InstrTools.extractOptionParams;
import finance.methods.common.*;
import finance.parameters.ModelParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
/**
 *
 * @author Grzegorz Los
 */
public class FDMethod implements Method
{
    @Override
    public void setModelParams(ModelParams mp) throws WrongModelException
    {
        SimpleModelParams smp;
        try {
            smp = (SimpleModelParams) mp;
        } catch (ClassCastException ex) {
            throw new WrongModelException("Finite Diffrence method can price instruments"
                + " only in one asset models.");
        }
        fd.setParams(smp);
    }

    @Override
    public double price(Instr instr) throws WrongInstrException, InterruptedException
    {
        if (!isPriceable(instr))
            throw new WrongInstrException("Finite Difference can price only "
                + "european and american vanilla options.");
        VanillaOptionParams vop = extractOptionParams(instr);
        return fd.price(vop, I, K);
    }

    @Override
    public boolean isPriceable(Instr instr)
    {
        if (!instr.areYou("option"))
            return false;
        int c = instr.modificationsCount();
        if (c == 0)
            return true;
        else if (c == 1)
            return instr.areYou("european");
        else
            return false;
    }

    @Override
    public String getDesc()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Finite difference").append("\n")
          .append("number of price steps: ").append(I).append("\n")
          .append("number of time steps: ").append(K).append("\n");
        return sb.toString();  
    }
    
    @Override
    public String toString()
    {
        return "Finite Difference";
    }

    @Override
    public void removeObserver(ProgressObserver ob)
    {
        fd.removeObserver(ob);
    }

    @Override
    public void notifyObservers(Progress pr)
    {
        fd.notifyObservers(pr);
    }

    @Override
    public void addObserver(ProgressObserver ob)
    {
        fd.addObserver(ob);
    }

    public FiniteDifference getFD()
    {
        return fd;
    }

    public int getI()
    {
        return I;
    }

    public void setI(int I)
    {
        this.I = I;
    }

    public int getK()
    {
        return K;
    }

    public void setK(int K)
    {
        this.K = K;
    }
    
    private int I = 1;
    private int K = 1;
    private FiniteDifference fd = new FiniteDifference();    
}
