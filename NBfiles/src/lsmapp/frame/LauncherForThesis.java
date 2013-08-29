
package lsmapp.frame;

import finance.instruments.Barrier;
import finance.instruments.EuExercise;
import finance.instruments.Instr;
import finance.instruments.Option;
import finance.methods.blackscholes.BSMethod;
import finance.methods.common.Method;
import finance.methods.common.WrongInstrException;
import finance.methods.common.WrongModelException;
import finance.parameters.BarrierParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import finance.parameters.VanillaOptionParams.CallOrPut;
import java.io.PrintStream;


/**
 *
 * @author Grzegorz Los
 */
public class LauncherForThesis
{
    public static void main(String[] args)
    {
        LauncherForThesis l = new LauncherForThesis();
        l.callAll();
    }
    
    void callAll()
    {
        PrintStream out = System.out;
        makeEuropeanBarrierCall(out);
    }

    private void makeEuropeanBarrierCall(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        //AV av = new AV(); av.setK(100); av.setN(10000);
        final Method method = bs;
        final SimpleModelParams smp = new SimpleModelParams(SimpleModelParams.onlyAsset, 100, 0.3, 0, 0);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        double[] spots = new double[20];
        for (int i = 0; i < 20; ++i)
            spots[i] = 50 + i*5;
        double[] barriers = new double[20];
        for (int i = 0; i < 20; ++i)
            barriers[i] = 100 + i*2;
        
        DataForMaker_3D maker = new DataForMaker_3D(spots, barriers)
        {
            @Override String fstName() {
                return "spot";
            }
            @Override String sndName() {
                return "barrier";
            }
            @Override String getDesc() {
                return "European up-and-out call with E=100, T=1, vol=0.3, r=0";
            }
            @Override void setFst(double d) {
                try {
                    method.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    throw new RuntimeException();
                }
            }
            @Override void setSnd(double d) {
                BarrierParams bp = new BarrierParams(BarrierParams.Type.UAO, d);
                instr = new EuExercise( new Barrier( bp, SimpleModelParams.onlyAsset,
                        new Option(vop, SimpleModelParams.onlyAsset) ) );
            }
            @Override double getPrice() {
                try {
                    return method.price(instr);
                } catch (WrongInstrException | InterruptedException ex) {
                    throw new RuntimeException();
                }
            }
            Instr instr;
        };
        String str = maker.makeData();
        out.println(str);
    }
}
abstract class DataForMaker_3D
{

    public DataForMaker_3D(double[] fstVals, double[] sndVals)
    {
        this.fstVals = fstVals;
        this.sndVals = sndVals;
    }
    abstract String fstName();
    abstract String sndName();
    abstract String getDesc();
    abstract void setFst(double d);
    abstract void setSnd(double d);
    abstract double getPrice();
    
    String makeData()
    {
        String fstLine = makeVector(fstName(), fstVals);
        String sndLine = makeVector(sndName(), sndVals);
        String values = makeResultMatrix();
        return wrapInComments(fstLine + sndLine + values);
    }
    
    private String makeVector(String name, double[] arr)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" <- c(").append(arr[0]);
        for (int i = 1; i < arr.length; ++i)
            sb.append(", ").append(arr[i]);
        sb.append(")\n");
        return sb.toString();
    }

    private String makeResultMatrix()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("prices <- matrix( c(\n");
        for (int i = 0; i < fstVals.length; ++i)
        {
            setFst(fstVals[i]);
            sb.append("    ");
            for (int j = 0; j < sndVals.length; ++j)
            {
                setSnd(sndVals[j]);
                double p = getPrice();
                sb.append(p);
                if (!((i == fstVals.length-1) && (j == sndVals.length-1)))
                    sb.append(", ");
            }
            sb.append("\n");
        }        
        sb.append("), nrow=").append(fstVals.length).append(", byrow=TRUE )\n");
        return sb.toString();
    }
    
    final double[] fstVals;
    final double[] sndVals;

    private String wrapInComments(String string)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("######################################################################\n");
        sb.append("### ").append(getDesc()).append(" ###\n");
        sb.append(string);
        sb.append("######################################################################\n");
        return sb.toString();
    }

}