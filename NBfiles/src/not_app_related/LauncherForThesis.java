
package not_app_related;

import finance.instruments.*;
import finance.methods.blackscholes.BSMethod;
import finance.methods.common.Method;
import finance.methods.common.WrongInstrException;
import finance.methods.common.WrongModelException;
import finance.methods.finitedifference.FDMethod;
import finance.methods.lsm.LSM;
import finance.methods.montecarlo.AV;
import finance.parameters.BarrierParams;
import finance.parameters.SimpleModelParams;
import static finance.parameters.SimpleModelParams.onlyAsset;
import finance.parameters.VanillaOptionParams;
import finance.parameters.VanillaOptionParams.CallOrPut;
import finance.trajectories.Dividend;
import finance.trajectories.DividendPerc;
import finance.trajectories.Generator;
import finance.trajectories.OneTrGenerator;
import finance.trajectories.Scenario;
import finance.trajectories.TimeSupport;
import finance.trajectories.Trajectory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import math.approx.Polynomial;








/**
 *
 * @author Grzegorz Los
 */
public class LauncherForThesis
{
    public static void main(String[] args) throws FileNotFoundException
    {
        LauncherForThesis l = new LauncherForThesis();
        File file = new File("/home/grzes/Printed.R");
        //PrintStream out = new PrintStream( file );
        //l.callAll(out);
        l.makeAmPutBoxPlot(System.out);
    }
    
    void callAll(PrintStream out)
    {
        
//**************************** Vanilla options ****************************************************/
        try { makeEuAndAmVanilla(out, CallOrPut.PUT); }
        catch (Throwable t) { System.err.println("makeEuAndAmVanilla(put) sie zesralo"); }
        try { makeEuAndAmVanilla(out, CallOrPut.CALL); }
        catch (Throwable t) { System.err.println("makeEuAndAmVanilla(put) sie zesralo"); }
        try { makeAmCallDividend(out); }
        catch (Throwable t) { System.err.println("makeAmCallDividend sie zesralo"); }
        try { makeAmCallDividend3D(out); }
        catch (Throwable t) { System.err.println("makeAmCallDividend3D sie zesralo"); }
        try { makeAmCallDividendValueAndPayoff(out); }
        catch (Throwable t) { System.err.println("makeAmCallDividend3D sie zesralo"); }

//**************************** European barrier options *******************************************/
        try { compareEuBarrierAndVanillaCall(out); }
        catch (Throwable t) { System.err.println("compareEuBarrierAndVanillaCall sie zesralo"); }
        try { makeEuropeanBarrierCall2D(out); }
        catch (Throwable t) { System.err.println("makeEuropeanBarrierCall2D sie zesralo"); }
        try { makeEuropeanBarrierCall3D(out); }
        catch (Throwable t) { System.err.println("makeEuropeanBarrierCall3D sie zesralo"); }
        
//**************************** American barrier options *******************************************/
        try { compareAmBarrierAndVanillaCall(out); }
        catch (Throwable t) { System.err.println("compareAmBarrierAndVanillaCall sie zesralo"); }
        try { makeAmericanBarrierCall2D(out); }
        catch (Throwable t) { System.err.println("makeAmericanBarrierCall2D sie zesralo"); }
        try { makeAmericanBarrierCall3D(out); }
        catch (Throwable t) { System.err.println("makeAmericanBarrierCall3D sie zesralo"); }
        try { compareAmAndEuBarrierAndVanillaCall(out); }
        catch (Throwable t) { System.err.println("compareAmAndEuBarrierAndVanillaCall sie zesralo"); }
        
//**************************** Forward options ****************************************************/
        try { makeAmForwardCall(out); }
        catch (Throwable t) { System.err.println("makeAmForwardCall sie zesralo"); }
        try { makeAmForwardPut(out); }
        catch (Throwable t) { System.err.println("makeAmForwardPut sie zesralo"); }
        
        
        
//**************************** Binary options *******************************************/
        try { makeEuropeanBinaryPut2D(out); }
        catch (Throwable t) { System.err.println("makeEuropeanBinaryPut2D sie zesralo"); }
        try { makeEuropeanBinaryCall3D(out); }
        catch (Throwable t) { System.err.println("makeEuropeanBinaryCall3D sie zesralo"); }
        try { makeAmericanBinaryCall3D(out); }
        catch (Throwable t) { System.err.println("makeAmericanBinaryCall3D sie zesralo"); }
        
//************************************** Other ****************************************************/
        try { makeTrajectoryWithDividends(out); }
        catch (Throwable t) { System.err.println("makeTrajectoryWithDividends sie zesralo"); }
    }
    
    private void makeEuAndAmVanilla(PrintStream out, CallOrPut callOrPut)
    {
        final BSMethod bs = new BSMethod();
        final FDMethod fd = new FDMethod(); fd.setI(100); fd.setK(10000);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0.08);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, callOrPut);
        final Instr am = new Option(vop, onlyAsset);
        final Instr eu = new EuExercise(am);
        double[] S = new double[100];
        for (int i = 0; i < 100; ++i)
            S[i] = 50 + 1.2 * i;
        DataForMaker_2D maker = new DataForMaker_2D(S) { 
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
                if (i == 0) return "European";
                else if (i == 1) return "American";
                else return "Payoff";
            }
            @Override String getDesc() {
                return "Am and Eu vanilla with E=100, T=1, vol=0.3, r=0.08";
            }
            @Override void setXVal(double d) {
                try {
                    bs.setModelParams(smp.withS(d));
                    fd.setModelParams(smp.withS(d));
                    currS = d;
                } catch (WrongModelException ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override double getPrice(int i) {
                try {
                    if (i == 0) return bs.price(eu);
                    else if (i == 1) return fd.price(am);
                    else return vop.intrisnicValue(currS);
                } catch (WrongInstrException | InterruptedException ex) {
                    throw new RuntimeException();
                }
            }
            double currS;
        };
        String str = maker.makeCode();
        out.println(str);
    }
        
    private void makeAmPutBoxPlot(PrintStream out)
    {
        final LSM lsm = new LSM(); lsm.setMethodParams(10000, 100, 3);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.2, 0, 0.05);
        try {
            lsm.setModelParams(smp);
        } catch (WrongModelException ex) {
            throw new RuntimeException();
        }
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.PUT);
        final Instr am = new Option(vop, onlyAsset);
        DataForBoxPlotMaker maker = new DataForBoxPlotMaker(3, 10, "", "price") {
            @Override String getLegend(int i) {
                if (i == 0) return "1,000";
                else if (i == 1) return "10,000";
                else return "100,000";
            }
            @Override String getDesc() {
                return "Creating a box plot with result discrepancy. " +
                        "America vanilla put, S=100, E=100, vol=0.2, r=0.05";
            }
            @Override void prepareForBox(int i) {
                if (i == 0) lsm.setN(1000);
                else if (i == 1) lsm.setN(10000);
                else lsm.setN(100000);
            }
            @Override double getPrice() {
                try {
                    return lsm.price(am);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException();
                }
            }
        };
        out.println(maker.makeCode());
    }
    
    private ArrayList<Dividend> makeDividends(int k, double perc)
    {
        ArrayList<Dividend> divs = new ArrayList<>();
        for (int i = 1; i <= k; ++i)
            divs.add(new DividendPerc(perc, (double)i / (k+1), onlyAsset));
        return divs;
    }    
    
    private ArrayList<Dividend> makeDividend(double t, double perc)
    {
        ArrayList<Dividend> divs = new ArrayList<>();
        divs.add(new DividendPerc(perc, t, onlyAsset));
        return divs;
    }
    
    private void makeAmCallDividend(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        final LSM[] lsm = new LSM[3];
        double[] perc = { 1, 5, 10 };
        for (int i = 0; i < 3; ++i)
        {
            lsm[i] = new LSM(new ArrayList<Trajectory.Auxiliary>() /*No auxiliary statistics */);
            lsm[i].setMethodParams(100000, 50, 3);
            lsm[i].setDividends(makeDividends(2, perc[i]));
        }
        
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.2, 0, 0.05);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        final Instr opt = new Option(vop, onlyAsset);
        final Instr eu = new EuExercise(opt);
        double[] S = new double[100];
        for (int i = 0; i < 100; ++i)
            S[i] = 50 + 1.2 * i;
        DataForMaker_2D maker = new DataForMaker_2D(S) { 
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
                if (i == 0) return "No dividends";
                else if (i == 1) return "1% dividends";
                else if (i == 2) return "5% dividends";
                else return "10% dividends";
            }
            @Override String getDesc() {
                return "American vanilla call on a stock paying dividend twice " + 
                       " with E=100, T=1, vol=0.2, r=0.05";
            }
            @Override void setXVal(double d) {
                try {
                    bs.setModelParams(smp.withS(d));
                    for (int i = 0; i < 3; ++i)
                        lsm[i].setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            @Override double getPrice(int i) {
                try {
                    if (i == 0) return bs.price(eu);
                    else return lsm[i-1].price(opt);
                } catch (WrongInstrException | InterruptedException ex) {
                    throw new RuntimeException();
                }
            }            
        };
        String str = maker.makeCode();
        out.println(str);
    }
    
    private double getPrice(Polynomial future, VanillaOptionParams vop, double S)
    {
        return Math.max(vop.intrisnicValue(S), future.value(S));
    }
    
    private void makeAmCallDividend3D(PrintStream out)
    {
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.2, 0, 0.05);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        final LSM lsm = new LSM(new ArrayList<Trajectory.Auxiliary>() /*No auxiliary statistics */);
        lsm.setMethodParams(100000, 50, 3);
        lsm.setDividends(makeDividend(0.5, 20));
        final Instr opt = new Option(vop, onlyAsset);
        try {
            lsm.setModelParams(smp);
            lsm.price(opt);
        } catch (Exception ex) {
            throw new RuntimeException();
        } 
        final TimeSupport ts = lsm.getLastTS();
        double[] S = new double[20];
        for (int i = 0; i < 20; ++i)
            S[i] = 80 + 3 * i;
        double[] T = new double[20];
        for (int i = 0; i < 20; ++i)
            T[i] = 0.2 + 0.035 * i;
        
        DataForMaker_3D maker = new DataForMaker_3D(S, T) {
            @Override String fstName() {
                return "asset price";
            }
            @Override String sndName() {
                return "time";
            }
            @Override String getDesc() {
                return "Preparing 3D chart of the price of american call " +
                        "option on a dividend paying stock.";
            }
            @Override void setFst(double d) {
                currS = d;
            }
            @Override void setSnd(double d) {
                currStep = ts.timeToNr(d);
            }
            @Override
            double getPrice() {
                Polynomial future = lsm.getEst()[currStep];
                return getPrice(future, vop, currS);
            }
            double currS;
            int currStep;
        };
        String str = maker.makeCode();
        out.println(str);
    }
    
    private void makeAmCallDividendValueAndPayoff(PrintStream out)
    {
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0.05);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        final LSM lsm = new LSM(new ArrayList<Trajectory.Auxiliary>() /*No auxiliary statistics */);
        lsm.setMethodParams(100000, 50, 3);
        lsm.setDividends(makeDividend(0.5, 20));
        final Instr opt = new Option(vop, onlyAsset);
        try {
            lsm.setModelParams(smp);
            lsm.price(opt);
        } catch (Exception ex) {
            throw new RuntimeException();
        } 
        final TimeSupport ts = lsm.getLastTS();
        double[] S = new double[50];
        for (int i = 0; i < 50; ++i)
            S[i] = 80 + 2 * i;
        double t = 0.48;
        final int timeStep = ts.timeToNr(t);
        DataForMaker_2D maker = new DataForMaker_2D(S) {
            @Override
            int getNumberOfYVals() {
                return 2;
            }
            @Override String getXLabel() {
                return "spot";
            }
            @Override String getYLabel() {
                return "";
            }
            @Override String getLegend(int i) {
                if (i == 0) return "immediate payoff";
                else return "price";
            }
            @Override String getDesc() {
                return "Comparision of options value and payoff before dividend.";
            }
            @Override void setXVal(double d) {
                currS = d;
            }
            @Override double getPrice(int i) {
                if (i == 0)
                    return vop.intrisnicValue(currS);
                else
                    return getPrice(lsm.getEst()[timeStep], vop, currS);
            }
            double currS;
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
        final LSM lsm = new LSM(auxStats); lsm.setMethodParams(100000, 50, 3);
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
        final LSM lsm = new LSM(auxStats); lsm.setMethodParams(100000, 50, 3);
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
        final LSM lsm = new LSM(auxStats); lsm.setMethodParams(100000, 50, 3);
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
        final LSM lsm = new LSM(auxStats); lsm.setMethodParams(100000, 50, 3);
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
    
    private void makeAmForwardCall(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        final LSM lsm = new LSM(new ArrayList<Trajectory.Auxiliary>());
        lsm.setMethodParams(10000, 50, 3);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0.05);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.CALL);
        double[] spots = new double[60];
        for (int i = 0; i < 60; ++i)
            spots[i] = 40 + i*2;
        final Instr am = new Forward( 0.5, new Option(vop, onlyAsset) );
        final Instr eu = new EuExercise(new Option(vop, onlyAsset));
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
                if (i == 0) return "vanilla call";
                else return "forward call";
            }
            @Override String getDesc() {
                return "Comparision of vanilla and forward call.";
            }
            @Override void setXVal(double d)  {
                try {
                    bs.setModelParams(smp.withS(d));
                    lsm.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    throw new RuntimeException();
                }
            }
            @Override double getPrice(int i) {
                try {
                    if (i == 0) 
                        return bs.price(eu);
                    else
                        return lsm.price(am);
                } catch (Exception ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException();
                }
            }
        };
        String str = maker.makeCode();
        out.println(str);
    }
    
    private void makeAmForwardPut(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        final LSM lsm = new LSM(new ArrayList<Trajectory.Auxiliary>());
        lsm.setMethodParams(10000, 50, 3);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.3, 0, 0.1);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.PUT);
        double[] spots = new double[60];
        for (int i = 0; i < 60; ++i)
            spots[i] = 40 + i*2;
        final Instr forward1 = new Forward(0.5, new Option(vop, onlyAsset));
        final Instr forward2 = new Forward(0.8, new Option(vop, onlyAsset));
        final Instr eu = new EuExercise(new Option(vop, onlyAsset));
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
                if (i == 0) return "vanilla put";
                else if (i == 1) return "forward put since 0.5";
                else return "forward put since 0.8";
            }
            @Override String getDesc() {
                return "Comparision of vanilla and forward put.";
            }
            @Override void setXVal(double d)  {
                try {
                    bs.setModelParams(smp.withS(d));
                    lsm.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    throw new RuntimeException();
                }
            }
            @Override double getPrice(int i) {
                try {
                    if (i == 0) 
                        return bs.price(eu);
                    else if (i == 1)
                        return lsm.price(forward1);
                    else
                        return lsm.price(forward2);
                } catch (Exception ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException();
                }
            }
        };
        String str = maker.makeCode();
        out.println(str);
    }
    
    private void makeEuropeanBinaryPut2D(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        final SimpleModelParams[] smp = new SimpleModelParams[4];
        for (int i = 0; i < 4; ++i)
            smp[i] = new SimpleModelParams(onlyAsset, 100, 0.1 + 0.1*i, 0, 0);
        final VanillaOptionParams vop = new VanillaOptionParams(100, 1, CallOrPut.PUT);
        double[] spots = new double[100];
        for (int i = 0; i < 100; ++i)
            spots[i] = 0 + i*2;
        final Instr instr = new Binary( new EuExercise( new Option(vop, onlyAsset) ) );
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
                if (i == 4)
                    return "intrisnic value";
                return "volatility " + (int)(100 * smp[i].vol);
            }
            @Override String getDesc() {
                return "Comparision of prices of binary put@100 with (T = 1).";
            }
            @Override void setXVal(double d) {
                    currS = d;
            }
            @Override double getPrice(int i) {
                if (i == 4)
                    return (currS < 100 ? 1 : 0);
                try {
                    bs.setModelParams(smp[i].withS(currS));
                    return bs.price(instr);
                } catch (Exception ex) { throw new RuntimeException(); }
            }
            double currS;
        };
        String str = maker.makeCode();
        out.println(str);
    }
        
    private void makeEuropeanBinaryCall3D(PrintStream out)
    {
        final BSMethod bs = new BSMethod();
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.2, 0, 0);
        double[] spots = new double[20];
        for (int i = 0; i < 20; ++i)
            spots[i] = 50 + i*6;
        double[] T = new double[20];
        for (int i = 0; i < 20; ++i)
            T[i] = i/10.0;
        
        DataForMaker_3D maker = new DataForMaker_3D(spots, T)
        {
            @Override String fstName() {
                return "spot";
            }
            @Override String sndName() {
                return "time to maturity";
            }
            @Override String getDesc() {
                return "European binary call with E=100, vol=0.2, r=0";
            }
            @Override void setFst(double d) {
                try {
                    bs.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    throw new RuntimeException();
                }
            }
            @Override void setSnd(double t) {
                VanillaOptionParams vop = new VanillaOptionParams(100, t, CallOrPut.CALL);
                instr = new EuExercise( new Binary( new Option(vop, onlyAsset) ) );
            }
            @Override double getPrice() {
                try {
                    return bs.price(instr);
                } catch (Exception ex) {
                    throw new RuntimeException();
                }
            }
            Instr instr;
        };
        String str = maker.makeCode();
        out.println(str);
    }
     
    private void makeAmericanBinaryCall3D(PrintStream out)
    {
        final LSM lsm = new LSM();
        final AV av = new AV(); av.setN(1000); av.setK(50);
        lsm.setMethodParams(100000, 50, 3);
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.2, 0, 0);
        double[] spots = new double[20];
        for (int i = 0; i < 9; ++i)
            spots[i] = 50 + i*5;
        for (int i = 0; i < 5; ++i)
            spots[9+i] = 92 + i*2;
        for (int i = 0; i < 6; ++i)
            spots[14+i] = 105 + i*6;
        double[] T = new double[20];
        for (int i = 0; i < 20; ++i)
            T[i] = i/4.0;
        
        DataForMaker_3D maker = new DataForMaker_3D(spots, T)
        {
            @Override String fstName() {
                return "spot";
            }
            @Override String sndName() {
                return "time to maturity";
            }
            @Override String getDesc() {
                return "American binary call with E=100, vol=0.2, r=0";
            }
            @Override void setFst(double d) {
                try {
                    lsm.setModelParams(smp.withS(d));
                    av.setModelParams(smp.withS(d));
                } catch (WrongModelException ex) {
                    throw new RuntimeException();
                }
            }
            @Override void setSnd(double t) {
                VanillaOptionParams vop = new VanillaOptionParams(100, t, CallOrPut.CALL);
                instr = new Binary( new Option(vop, onlyAsset) );
                instr2 = new Barrier( new BarrierParams(BarrierParams.Type.UAI, 100), onlyAsset, new Bond(1, t) );
            }
            @Override double getPrice() {
                try {
                    return lsm.price(instr);
                    //return av.price(instr2);
                } catch (Exception ex) {
                    Logger.getLogger(LauncherForThesis.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException();
                }
            }
            Instr instr, instr2;
        };
        String str = maker.makeCode();
        out.println(str);
    }
     
    private void makeTrajectoryWithDividends(PrintStream out)
    {
        final SimpleModelParams smp = new SimpleModelParams(onlyAsset, 100, 0.2, 0.1, 0);
        final TimeSupport ts = new TimeSupport(1,252);
        double[] t = new double[ts.getK()+1];
        for (int k = 0; k <= ts.getK(); ++k)
            t[k] = ts.nrToTime(k);
        OneTrGenerator gen = new OneTrGenerator(smp, Generator.Measure.REAL, ts);
        ArrayList<Dividend> divs = new ArrayList<>();
        divs.add(new DividendPerc(20, 1.0/3, onlyAsset));
        divs.add(new DividendPerc(20, 2.0/3, onlyAsset));
        gen.setDividends(divs);
        final Scenario sc = gen.generate(Generator.Anthi.NO);
        
        DataForMaker_2D maker = new DataForMaker_2D(t) {
            @Override int getNumberOfYVals() {
                return 1;
            }
            @Override String getXLabel() {
                return "time";
            }
            @Override String getYLabel() {
                return "asset price";
            }
            @Override String getLegend(int i) {
                return "trajectory";
            }
            @Override String getDesc() {
                return "Example trajectory generation";
            }
            @Override void setXVal(double d) {
                k = ts.timeToNr(d);
            }
            @Override double getPrice(int i) {
                return sc.getTr(1).price(k);
            }
            int k = 0;
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
        return "makeChart3D(" + fstName().replace(' ', '_') +
            ", " + sndName().replace(' ', '_') + ", prices, " +
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
            System.out.flush();
        }
        System.out.println();
    }
    
    double[] xVals;
    ArrayList<double[]> yVals;
    ArrayList<String> legend;
    String xLab;
    String yLab;
}


abstract class DataForBoxPlotMaker
{

    public DataForBoxPlotMaker(int boxes, int repetions, String xLab, String yLab)
    {
        this.boxes = boxes;
        this.repetions = repetions;
        this.xLab = xLab;
        this.yLab = yLab;
    }
    
    abstract String getLegend(int i);
    abstract String getDesc();
    abstract void prepareForBox(int i);
    abstract double getPrice();
    
    String makeCode()
    {
        calcValues();
        StringBuilder sb = new StringBuilder();
        makeFrame(sb);
        makeFunctionCall(sb);
        wrapInComments(sb);
        return sb.toString();
    }  

    private void wrapInComments(StringBuilder sb)
    {
        sb.insert(0, 
            "######################################################################\n" +
            "### " + getDesc() + " ###\n" );
        sb.append("######################################################################\n");
    }
    
    private void makeFrame(StringBuilder sb)
    {
        sb.append("frame <- data.frame(\n");
        for (int i = 0; i < boxes; ++i)
        {
            sb.append("    ").append(makeVector("box" + i, vals.get(i)));
            if (i == boxes-1)
                    sb.append("\n");
            else
                sb.append(",\n");
        }
        sb.append(")\n");
    }
    
    private String makeVector(String name, ArrayList<Double> arr)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name.replace(' ', '_')).append(" = c(").append(arr.get(0));
        for (int i = 1; i < arr.size(); ++i)
            sb.append(", ").append(arr.get(i));
        sb.append(")");
        return sb.toString();
    }
    
    private void makeFunctionCall(StringBuilder sb)
    {
        sb.append("makeBoxPlot(frame, labels=c(");
        for (int i = 0; i < boxes; ++i)
            if (i == boxes-1)
                sb.append(inQuotes(getLegend(i))).append("), ");
            else
                sb.append(inQuotes(getLegend(i))).append(", ");
        sb.append(inQuotes(xLab)).append(", ").append(inQuotes(yLab)).append(")\n");
    }
    
    private String inQuotes(String str)
    {
        return '"' + str + '"';
    }
    
    private void calcValues()
    {
        System.out.println("Doing: " + getDesc());
        for (int i = 0; i < boxes; ++i)
        {
            prepareForBox(i);
            System.out.print("Box " + i + ": "); System.out.flush();
            vals.add(new ArrayList<Double>());
            for (int j = 0; j < repetions; ++j)
            {
                vals.get(i).add(getPrice());
                System.out.print((int)(100.0 * (j+1) / repetions) + " ");
                System.out.flush();
            }
            System.out.println();
        }
    }
    ArrayList<ArrayList<Double>> vals = new ArrayList<>();
    int boxes;
    int repetions;
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
