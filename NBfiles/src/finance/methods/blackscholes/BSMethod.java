
package finance.methods.blackscholes;

import finance.instruments.Barrier;
import finance.instruments.Bond;
import finance.instruments.Instr;
import static finance.instruments.InstrTools.*;
import finance.instruments.Option;
import finance.methods.common.*;
import finance.parameters.BarrierParams;
import finance.parameters.ModelParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import java.util.ArrayList;

/**
 * Allows the user to price {@code Instr} using Black-Scholes method if it is priceable.
 * @author Grzegorz Los
 */
public class BSMethod implements Method
{

    public BSMethod(BlackScholes method)
    {
        bs = method;
    }

    public BSMethod()
    {
        bs = new BlackScholes();
    }
    
    @Override
    public void setModelParams(ModelParams mp) throws WrongModelException
    {
        SimpleModelParams smp;
        try {
            smp = (SimpleModelParams) mp;
        } catch (ClassCastException ex) {
            throw new WrongModelException("Black-Scholes method can only be used"
                    + "with model on single asset");
        }
        bs.setParams(smp);
    }
    
    @Override
    public boolean isPriceable(Instr instr)
    {
        ArrayList<Instr> wrapped = getAllWrapped(instr);
        return mayBePriced(wrapped, instr);
    }
    
    private boolean mayBePriced(ArrayList<Instr> wrapped, Instr instr)
    {
        Instr core = getCore(wrapped);
        if (isOption(core))
        {
            if (wrapped.size() == 3)
                return instr.areYou("european") && instr.areYou("barrier")
                    && instr.getUnderlyings().size() == 1;
            if (wrapped.size() == 2)
                return instr.areYou("european");
        }
        if (isBond(core))
            return wrapped.size() == 1;
        return false;   
    }
    
    @Override
    public double price(Instr instr) throws WrongInstrException
    {
        ArrayList<Instr> wrapped = getAllWrapped(instr);
        if (!mayBePriced(wrapped, instr))
            throw new WrongInstrException("BSMethod can price only european "
                    + "vanilla and simple barrier options and bonds.");
        return price(wrapped);
    }

    private double price(ArrayList<Instr> wrapped)
    {
        BarrierParams bp = getBarrierParams(wrapped);
        return price(bp, getCore(wrapped));
    }
    
    private double price(BarrierParams bp, Object instr)
    {
        if (instr instanceof Option)
        {
            Option op = (Option) instr;
            return price(bp, op);
        }
        else if (instr instanceof Bond)
        {
            assert bp == null;
            Bond bond = (Bond) instr;
            return bs.priceBond(bond.getNominal(), bond.getT());
        }
        else throw new RuntimeException("Flow should not reach that statement");
    }

    private double price(BarrierParams bp, Option op)
    {
        VanillaOptionParams vop = op.vop;
        if (bp == null)
            return bs.price(vop);
        else
            return bs.price(vop, bp);
    }
    
    private BarrierParams getBarrierParams(ArrayList<Instr> wrapped)
    {
        for (Object ob: wrapped) {
            try {
                Barrier b = (Barrier) ob;
                return b.bp;
            } catch (ClassCastException ex) {}
        }
        return null;
    }

    @Override
    public String getDesc()
    {
        return "Black-Scholes or other analitical formula";
    }
    
    @Override
    public String toString()
    {
        return "Analitical formula";
    }

    @Override
    public void removeObserver(ProgressObserver ob)
    {
        bs.removeObserver(ob);
    }

    @Override
    public void notifyObservers(Progress pr)
    {
        bs.notifyObservers(pr);
    }

    @Override
    public void addObserver(ProgressObserver ob)
    {
        bs.addObserver(ob);
    }
    
    
    private BlackScholes bs;
}
