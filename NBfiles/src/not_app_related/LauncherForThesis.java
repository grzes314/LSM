
package not_app_related;

import finance.instruments.Barrier;
import finance.instruments.EuExercise;
import finance.instruments.Instr;
import finance.instruments.Option;
import finance.methods.blackscholes.BSMethod;
import finance.methods.common.Method;
import finance.methods.common.WrongInstrException;
import finance.methods.common.WrongModelException;
import finance.methods.finitedifference.FDMethod;
import finance.methods.lsm.LSM;
import finance.parameters.BarrierParams;
import finance.parameters.SimpleModelParams;
import static finance.parameters.SimpleModelParams.onlyAsset;
import finance.parameters.VanillaOptionParams;
import finance.parameters.VanillaOptionParams.CallOrPut;
import finance.trajectories.Trajectory;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
//        try { makeEuAndAmPut(out); }
//        catch (Throwable t) { System.err.println("makeEuAndAmPut sie zesralo"); }
        
//        try { compareEuBarrierAndVanillaCall(out); }
//        catch (Throwable t) { System.err.println("compareEuBarrierAndVanillaCall sie zesralo"); }
//        try { makeEuropeanBarrierCall2D(out); }
//        catch (Throwable t) { System.err.println("makeEuropeanBarrierCall2D sie zesralo"); }
//        try { makeEuropeanBarrierCall3D(out); }
//        catch (Throwable t) { System.err.println("makeEuropeanBarrierCall3D sie zesralo"); }
        
//        try { compareAmBarrierAndVanillaCall(out); }
//        catch (Throwable t) { System.err.println("compareAmBarrierAndVanillaCall sie zesralo"); }
//        try { makeAmericanBarrierCall2D(out); }
//        catch (Throwable t) { System.err.println("makeAmericanBarrierCall2D sie zesralo"); }
//        try { makeAmericanBarrierCall3D(out); }
//        catch (Throwable t) { System.err.println("makeAmericanBarrierCall3D sie zesralo"); }
        try { compareAmAndEuBarrierAndVanillaCall(out); }
        catch (Throwable t) { System.err.println("compareAmAndEuBarrierAndVanillaCall sie zesralo"); }
    }
    
    private void makeEuAndAmPut(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        final FDMethod fd = new FDMethod(); fd.setI(100); fd.setK(10000);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0.08);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.PUT);
        final Instr am = new Option(vop, onlyAsset);
        final Instr eu = new EuExercise(am);
        double[] S = new double[100];
        for (int i = 0; i < 100; ++i)
            S[i] = 50 + 1.2 * i;
        DataForMaker_2D maker = new DataForMaker_2D(S) { 
            @Override int getNumberOfYVals() {
                return 2;
            }
            @Override String getXLabel() {
                return "spot";
            }
            @Override String getYLabel() {
                return "price";
            }
            @Override String getLegend(int i) {
                if (i == 0) return "European";
                else return "American";
            }
            @Override String getDesc() {
                return "Am and Eu vanilla put with E=100, T=1, vol=0.3, r=0.08";
            }
            @Override void setXVal(double d) {
                try {
                    bs.setModelParams(smp.withS(d));
                    fd.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override double getPrice(int i) {
                try {
                    if (i == 0) return bs.price(eu);
                    else return fd.price(am);
                } catch (WrongInstrException | InterruptedException ex) {
                    throw new RuntimeException();
                }
            }            
        };
        String str = maker.makeCode();
        out.println(str);
    }
    
    private void compareEuBarrierAndVanillaCall(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        double[] spots = new double[55];
        for (int i = 0; i < 55; ++i)
            spots[i] = 40 + i*2;
        BarrierParams bp = new BarrierParams(BarrierParams.Type.UAO, 140);
        final Instr vanilla = new EuExercise( new Option(vop, onlyAsset) );
        final Instr barrier = new Barrier( bp, onlyAsset, vanilla );
        DataForMaker_2D maker = new DataForMaker_2D(spots) {
            @Override int getNumberOfYVals() {
                return 2;
            }
            @Override String getXLabel() {
                return "spot";
            }
            @Override String getYLabel() {
                return "price";
            }
            @Override String getLegend(int i) {
                if (i == 0) return "vanilla option";
                else return "barrier option";
            }
            @Override String getDesc() {
                return "Comparision of the price of call@100 with and without UAO barrier (vol=0.3, r=0).";
            }
            @Override void setXVal(double d) {
                try {
                    bs.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override double getPrice(int i) {
                try {
                    if (i == 0) return bs.price(vanilla);
                    else return bs.price(barrier);
                } catch (Exception ex) { throw new RuntimeException(); }
            }
        };
        String str = maker.makeCode();
        out.println(str);
    }
        
    private void makeEuropeanBarrierCall2D(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        double[] spots = new double[60];
        for (int i = 0; i < 60; ++i)
            spots[i] = 40 + i*2;
        final Instr[] instr = new Instr[4];
        for (int i = 0; i < 4; ++i)
        {
            BarrierParams bp = new BarrierParams(BarrierParams.Type.UAO, 110 + 10*i);
            instr[i] = new Barrier( bp, onlyAsset, new EuExercise( new Option(vop, onlyAsset) ) );
        }
        DataForMaker_2D maker = new DataForMaker_2D(spots) {
            @Override int getNumberOfYVals() {
                return 4;
            }
            @Override String getXLabel() {
                return "spot";
            }
            @Override String getYLabel() {
                return "price";
            }
            @Override String getLegend(int i) {
                return "barrier " + (110 + 10*i);
            }
            @Override String getDesc() {
                return "Comparision of prices of call@100 with UAO barriers (vol=0.3, r=0).";
            }
            @Override void setXVal(double d) {
                try {
                    bs.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override double getPrice(int i) {
                try {
                    return bs.price(instr[i]);
                } catch (Exception ex) { throw new RuntimeException(); }
            }
        };
        String str = maker.makeCode();
        out.println(str);
    }
    
    private void makeEuropeanBarrierCall3D(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        //AV av = new AV(); av.setK(100); av.setN(10000);
        final Method method = bs;
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        double[] spots = new double[20];
        for (int i = 0; i < 20; ++i)
            spots[i] = 50 + i*6;
        double[] barriers = new double[20];
        for (int i = 0; i < 20; ++i)
            barriers[i] = 100 + i*3;
        
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
                instr = new EuExercise( new Barrier( bp, onlyAsset,
                        new Option(vop, onlyAsset) ) );
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
        String str = maker.makeCode();
        out.println(str);
    }
        
    private void compareAmBarrierAndVanillaCall(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        ArrayList<Trajectory.Auxiliary> auxStats = new ArrayList<>();
        auxStats.add(Trajectory.Auxiliary.CUMMAX);
        final LSM lsm = new LSM(auxStats); lsm.setMethodParams(1000, 10, 2);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        double[] spots = new double[60];
        for (int i = 0; i < 60; ++i)
            spots[i] = 40 + i*2;
        BarrierParams bp = new BarrierParams(BarrierParams.Type.UAO, 140);
        final Instr vanilla = new EuExercise( new Option(vop, onlyAsset) );
        final Instr barrier = new Barrier( bp, onlyAsset, new Option(vop, onlyAsset) );
        DataForMaker_2D maker = new DataForMaker_2D(spots) {
            @Override int getNumberOfYVals() {
                return 2;
            }
            @Override String getXLabel() {
                return "spot";
            }
            @Override String getYLabel() {
                return "price";
            }
            @Override String getLegend(int i) {
                if (i == 0) return "vanilla option";
                else return "barrier option";
            }
            @Override String getDesc() {
                return "Comparision of the price of american call@100 with and without UAO barrier (vol=0.3, r=0).";
            }
            @Override void setXVal(double d) {
                try {
                    bs.setModelParams(smp.withS(d));
                    lsm.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override double getPrice(int i) {
                try {
                    if (i == 0) return bs.price(vanilla);
                    else return lsm.price(barrier);
                } catch (Exception ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException();
                }
            }
        };
        String str = maker.makeCode();
        out.println(str);
    }
    
    private void makeAmericanBarrierCall2D(PrintStream out)
    {
        ArrayList<Trajectory.Auxiliary> auxStats = new ArrayList<>();
        auxStats.add(Trajectory.Auxiliary.CUMMAX);
        final LSM lsm = new LSM(auxStats); lsm.setMethodParams(10000, 10, 2);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        double[] spots = new double[60];
        for (int i = 0; i < 60; ++i)
            spots[i] = 40 + i*2;
        final Instr[] instr = new Instr[4];
        for (int i = 0; i < 4; ++i)
        {
            BarrierParams bp = new BarrierParams(BarrierParams.Type.UAO, 110 + 10*i);
            instr[i] = new Barrier( bp, onlyAsset, new Option(vop, onlyAsset) );
        }
        DataForMaker_2D maker = new DataForMaker_2D(spots) {
            @Override int getNumberOfYVals() {
                return 4;
            }
            @Override String getXLabel() {
                return "spot";
            }
            @Override String getYLabel() {
                return "price";
            }
            @Override String getLegend(int i) {
                return "barrier " + (110 + 10*i);
            }
            @Override String getDesc() {
                return "Comparision of prices of american call@100 with UAO barriers (vol=0.3, r=0).";
            }
            @Override void setXVal(double d) {
                try {
                    lsm.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override double getPrice(int i) {
                try {
                    return lsm.price(instr[i]);
                } catch (Exception ex) { throw new RuntimeException(); }
            }
        };
        String str = maker.makeCode();
        out.println(str);
    }
        
    private void makeAmericanBarrierCall3D(PrintStream out)
    {
        ArrayList<Trajectory.Auxiliary> auxStats = new ArrayList<>();
        auxStats.add(Trajectory.Auxiliary.CUMMAX);
        final LSM lsm = new LSM(auxStats); lsm.setMethodParams(50000, 10, 2);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        double[] spots = new double[20];
        for (int i = 0; i < 20; ++i)
            spots[i] = 50 + i*6;
        double[] barriers = new double[20];
        for (int i = 0; i < 20; ++i)
            barriers[i] = 100 + i*3;
        
        DataForMaker_3D maker = new DataForMaker_3D(spots, barriers)
        {
            @Override String fstName() {
                return "spot";
            }
            @Override String sndName() {
                return "barrier";
            }
            @Override String getDesc() {
                return "American up-and-out call with E=100, T=1, vol=0.3, r=0";
            }
            @Override void setFst(double d) {
                try {
                    lsm.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    throw new RuntimeException();
                }
            }
            @Override void setSnd(double d) {
                BarrierParams bp = new BarrierParams(BarrierParams.Type.UAO, d);
                instr = new Barrier( bp, onlyAsset, new Option(vop, onlyAsset) );
            }
            @Override double getPrice() {
                try {
                    return lsm.price(instr);
                } catch (Exception ex) {
                    throw new RuntimeException();
                }
            }
            Instr instr;
        };
        String str = maker.makeCode();
        out.println(str);
    }
        
    private void compareAmAndEuBarrierAndVanillaCall(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        ArrayList<Trajectory.Auxiliary> auxStats = new ArrayList<>();
        auxStats.add(Trajectory.Auxiliary.CUMMAX);
        final LSM lsm = new LSM(auxStats); lsm.setMethodParams(1000, 10, 2);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        double[] spots = new double[60];
        for (int i = 0; i < 60; ++i)
            spots[i] = 40 + i*2;
        BarrierParams bp = new BarrierParams(BarrierParams.Type.UAO, 140);
        final Instr vanilla = new EuExercise( new Option(vop, onlyAsset) );
        final Instr euBarrier = new Barrier( bp, onlyAsset, vanilla );
        final Instr amBarrier = new Barrier( bp, onlyAsset, new Option(vop, onlyAsset) );
        DataForMaker_2D maker = new DataForMaker_2D(spots) {
            @Override int getNumberOfYVals() {
                return 3;
            }
            @Override String getXLabel() {
                return "spot";
            }
            @Override String getYLabel() {
                return "price";
            }
            @Override String getLegend(int i) {
                if (i == 0) return "vanilla call";
                else if (i == 1) return "barrier american call";
                else return "barrier european call";
            }
            @Override String getDesc() {
                return "Comparision of the prices am/eu call@100 with and without UAO barrier (vol=0.3, r=0).";
            }
            @Override void setXVal(double d) {
                try {
                    bs.setModelParams(smp.withS(d));
                    lsm.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override double getPrice(int i) {
                try {
                    if (i == 0) return bs.price(vanilla);
                    else if (i == 1) return lsm.price(amBarrier);
                    else return bs.price(euBarrier);
                } catch (Exception ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException();
                }
            }
        };
        String str = maker.makeCode();
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
    
    String makeCode()
    {
        System.out.println("Doing:  " + getDesc());
        String fstLine = makeVector(fstName(), fstVals);
        String sndLine = makeVector(sndName(), sndVals);
        String values = makeResultMatrix();
        String chartMaker = makeCallToChartMaker();
        return wrapInComments(fstLine + sndLine + values + chartMaker);
    }
    
    private String makeVector(String name, double[] arr)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name.replace(' ', '_')).append(" <- c(").append(arr[0]);
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
            System.out.print((100.0 * (i+1) / fstVals.length) + " ");
        }        
        sb.append("), nrow=").append(fstVals.length).append(", byrow=TRUE )\n");
        System.out.println();
        return sb.toString();
    }
    
    private String makeCallToChartMaker()
    {
        return "makeChart3D(" + fstName() +", " + sndName() + ", prices, " +
            "\"" + fstName() + "\", \"" + sndName() + "\", \"prices\")\n"; 
    }

    private String wrapInComments(String string)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("######################################################################\n");
        sb.append("### ").append(getDesc()).append(" ###\n");
        sb.append(string);
        sb.append("######################################################################\n");
        return sb.toString();
    }
    
    final double[] fstVals;
    final double[] sndVals;
}


abstract class DataForMaker_2D
{
    public DataForMaker_2D(double[] xVals)
    {
        this.xVals = xVals;
    }
    
    abstract int getNumberOfYVals();
    abstract String getXLabel();
    abstract String getYLabel();
    abstract String getLegend(int i);
    abstract String getDesc();
    abstract void setXVal(double d);
    abstract double getPrice(int i);
    
    String makeCode()
    {
        setAuxFields();
        CodeFor2DChart codeMaker = new CodeFor2DChart(xVals, yVals, legend, xLab, yLab);
        return wrapInComments(codeMaker.makeCode());
    }

    private void setAuxFields()
    {
        xLab = getXLabel();
        yLab = getYLabel();
        makeLegend();
        calcValues();
    }    

    private String wrapInComments(String string)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("######################################################################\n");
        sb.append("### ").append(getDesc()).append(" ###\n");
        sb.append(string);
        sb.append("######################################################################\n");
        return sb.toString();
    }

    private void makeLegend()
    {
        int n = getNumberOfYVals();
        legend = new ArrayList<>();
        for (int i = 0; i < n; ++i)
            legend.add( getLegend(i));
    }

    private void calcValues()
    {
        System.out.println("Doing: " + getDesc());
        int n = getNumberOfYVals();
        yVals = new ArrayList<>();
        for (int i = 0; i < n; ++i)
            yVals.add(new double[xVals.length]);
        for (int j = 0; j < xVals.length; ++j)
        {
            setXVal(xVals[j]);
            for (int i = 0; i < n; ++i)
            {
                double[] arr = yVals.get(i);
                arr[j] = getPrice(i);
            }
            System.out.print((int)(100.0 * (j+1) / xVals.length) + " ");
        }
        System.out.println();
    }
    
    double[] xVals;
    ArrayList<double[]> yVals;
    ArrayList<String> legend;
    String xLab;
    String yLab;
}

class CodeFor2DChart
{
    public CodeFor2DChart(double[] xVals, ArrayList<double[]> yVals,
                                           ArrayList<String> legend, String xLab, String yLab)
    {
        this.xVals = xVals;
        this.yVals = yVals;
        this.legend = legend;
        this.xLab = xLab;
        this.yLab = yLab;
    }
    
    public String makeCode()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("frame <- data.frame(\n");
        sb.append("    ").append(makeColumn(xLab, xVals));
        for (int i = 0; i < yVals.size(); ++i)
            sb.append(",\n    ").append(makeColumn(legend.get(i), yVals.get(i)));
        sb.append("\n)\n");
        sb.append("makeChart2D(frame, \"").append(xLab).append("\", \"").append(yLab).append("\")\n");
        return sb.toString();
    }
    
    private String makeColumn(String name, double[] arr)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name.replace(' ', '_')).append(" = c(").append(arr[0]);
        for (int i = 1; i < arr.length; ++i)
            sb.append(", ").append(arr[i]);
        sb.append(")");
        return sb.toString();
    }
    
    double[] xVals;
    ArrayList<double[]> yVals;
    ArrayList<String> legend;
    String xLab;
    String yLab;
}
