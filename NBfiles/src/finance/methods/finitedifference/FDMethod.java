
package finance.methods.finitedifference;

import finance.instruments.EuExercise;
import finance.instruments.Instr;
import finance.instruments.Option;
import finance.methods.common.*;
import finance.parameters.ModelParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import finance.parameters.VanillaOptionParams.AmOrEu;

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


    private VanillaOptionParams extractOptionParams(Instr instr)
    {
        try {
            return tryExtractAmOptionParams(instr);
        } catch (ClassCastException ex) {
            try {
                return tryExtractEuOptionParams(instr);
            } catch (ClassCastException ex2) {
                throw new RuntimeException("Not vanilla option.");
            }
        }
    }
    
    private VanillaOptionParams tryExtractAmOptionParams(Instr instr)
    {
        Option o = (Option) instr;
        return extractOptionParams(o, VanillaOptionParams.AmOrEu.AM);
    }
    
    private VanillaOptionParams tryExtractEuOptionParams(Instr instr)
    {
        EuExercise e = (EuExercise) instr;
        Option o = (Option) e.getWrapped();
        return extractOptionParams(o, VanillaOptionParams.AmOrEu.EU);
    }

    private VanillaOptionParams extractOptionParams(Option option, AmOrEu amOrEu)
    {
        if (amOrEu == AmOrEu.AM)
            return option.vop.asAmerican();
        else
            return option.vop.asEuropean();
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
