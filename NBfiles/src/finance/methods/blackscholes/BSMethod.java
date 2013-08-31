
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Allows the user to price {@code Instr} using Black-Scholes method if it is priceable.
 * @author Grzegorz Los
 */
public class BSMethod implements Method
{
    public BSMethod()
    {
        blackScholes = new BlackScholes();
        bondPricer = new BondPricer();
        binaryPricer = new BinaryOptionPricer();
        barrierPricer = new BarrierOptionPricer();
    }

    public BSMethod(SimpleModelParams smp)
    {
        this();
        try {
            setModelParams(smp);
        } catch (WrongModelException ex) {
            Logger.getLogger(BSMethod.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("BSMethod should accept SimpleModelParams.");
        }
    }
    
    @Override
    public final void setModelParams(ModelParams mp) throws WrongModelException
    {
        SimpleModelParams smp;
        try {
            smp = (SimpleModelParams) mp;
        } catch (ClassCastException ex) {
            throw new WrongModelException("Black-Scholes method can only be used"
                    + "with model on single asset");
        }
        blackScholes.setParams(smp);
        binaryPricer.setParams(smp);
        barrierPricer.setParams(smp);
        bondPricer.setParams(smp);
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
            {
                if (!instr.areYou("european"))
                    return false;
                if (instr.areYou("barrier"))
                    return instr.getUnderlyings().size() == 1;
                if (instr.areYou("binary"))
                    return true;
            }
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
                    + "vanilla, barrier, binary options and bonds.");
        return price(instr, wrapped);
    }

    private double price(Instr instr, ArrayList<Instr> wrapped)
    {
        BarrierParams bp = getBarrierParams(wrapped);
        return price(bp, instr, getCore(wrapped));
    }
    
    private double price(BarrierParams bp, Instr instr, Instr core)
    {
        if (core instanceof Option)
        {
            Option op = (Option) core;
            return price(bp, instr, op);
        }
        else if (core instanceof Bond)
        {
            assert bp == null;
            Bond bond = (Bond) core;
            return bondPricer.price(bond.getNominal(), bond.getT());
        }
        else throw new RuntimeException("Flow should not reach that statement");
    }

    private double price(BarrierParams bp, Instr instr, Option op)
    {
        VanillaOptionParams vop = op.vop;
        if (bp == null)
        {
            if (instr.areYou("binary"))
                return binaryPricer.price(vop);
            else
                return blackScholes.price(vop);
        }
        else
            return barrierPricer.price(vop, bp);
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
    public void removeObserver(ProgressObserver ob){}
    @Override
    public void notifyObservers(Progress pr) {}
    @Override
    public void addObserver(ProgressObserver ob){}
    
    
    private BlackScholes blackScholes;
    private BondPricer bondPricer;
    private BinaryOptionPricer binaryPricer;
    private BarrierOptionPricer barrierPricer;
}
