
package finance.trajectories;

import finance.parameters.ModelParams;
import finance.trajectories.Trajectory.Auxiliary;
import java.util.ArrayList;
import java.util.Collection;
import math.matrices.Matrix;
import math.matrices.NotPositiveDefiniteMatrixException;
import math.matrices.Vector;
import math.utils.RandomTools;

/**
 *
 * @author Grzegorz Los
 */
public class MultiTrGenerator extends GeneratorRoot
{
    public MultiTrGenerator(ModelParams params, Measure measure, TimeSupport ts)
    {
        this.ts = ts;
        this.auxStats = SimpleTrajectory.makeAllAuxiliary();
        this.modelParams = params;
        extractNecssaryData(measure);
    }
    
    public MultiTrGenerator(ModelParams params, Measure measure, TimeSupport ts,
            Collection<Auxiliary> auxiliary)
    {
        this.ts = ts;
        this.auxStats = auxiliary;
        this.modelParams = params;
        extractNecssaryData(measure);
    }
    
    private void extractNecssaryData(Measure measure)
    {
        extractNumberOfAssets();
        extractNames();
        extractCholeskyDecomposition();
        extractAssetData(measure);
    }

    private void extractNumberOfAssets()
    {
        numberOfAssets = modelParams.getNumberOfAssets();
    }
    
    private void extractNames()
    {
        names = new String[numberOfAssets+1];
        for (int i = 1; i <= numberOfAssets; ++i)
            names[i] = modelParams.getName(i);
    }

    private void extractCholeskyDecomposition()
            throws IllegalArgumentException
    {
        try {
            decomp = modelParams.getCorrelation().cholesky();
        } catch (NotPositiveDefiniteMatrixException ex) {
            throw new IllegalArgumentException("Correlation matrix is invalid", ex);
        }
    }

    private void extractAssetData(Measure measure)
    {
        extractSpot();
        extractDm(measure);
        extractDvol();
    }
    
    private void extractSpot()
    {
        spot = new double[numberOfAssets+1];
        for (int i = 1; i <= numberOfAssets; ++i)
            spot[i] = modelParams.getParams(i).S;
    }
    
    private void extractDm(Measure measure)
    {
        dm = new double[numberOfAssets+1];
        for (int i = 1; i <= numberOfAssets; ++i)
        {
            switch (measure)
            {
                case REAL:
                    dm[i] = modelParams.getParams(i).mu * ts.getDt();
                    break;
                case MART:
                    dm[i] = modelParams.getR() * ts.getDt();                    
            }
        }
    }
    
    private void extractDvol()
    {
        dvol = new double[numberOfAssets+1];
        for (int i = 1; i <= numberOfAssets; ++i)
            dvol[i] = modelParams.getParams(i).vol * Math.sqrt(ts.getDt());
    }
    
    private double getDt()
    {
        return ts.getDt();
    }
    
    private int getTimePoints()
    {
        return ts.getK();
    }
    
    @Override
    public Scenario generate(Anthi anthi)
    {
        genAnthi = anthi == Anthi.YES ? true : false;
        prepareTrajectories();
        fillTrajectories();
        return makeResult();
    }
    
    private void prepareTrajectories()
    {
        pos = new SimpleTrajectory[numberOfAssets+1];
        neg = genAnthi ? new SimpleTrajectory[numberOfAssets+1] : null;
        for (int i = 1; i <= numberOfAssets; ++i)
        {
            pos[i] = new SimpleTrajectory(getTimePoints(), auxStats);
            if (genAnthi)
                neg[i] = new SimpleTrajectory(getTimePoints(), auxStats);
        }
    }

    private void fillTrajectories()
    {
        callFillFirst();
        maybeDividend(0);
        for (int k = 1; k <= getTimePoints(); ++k)
        {
            Vector Z = drawZ();
            callFillNext(k, Z);
            maybeDividend(k);
        }
        callSetReady();
    }

    private void callFillFirst()
    {
        fillFirst(pos);
        if (genAnthi)
            fillFirst(neg);
    }

    private void callFillNext(int k, Vector Z)
    {
        fillNext(k, pos, Z);
        if (genAnthi)
            fillNext(k, neg, Z.times(-1));
    }

    private void callSetReady()
    {
        setReady(pos);
        if (genAnthi)
            setReady(neg);
    }

    private void setReady(SimpleTrajectory[] trs)
    {
        for (int i = 1; i <= numberOfAssets; ++i)
            trs[i].setReady();
    }
    
    private void fillFirst(SimpleTrajectory[] trs)
    {
        for (int i = 1; i <= numberOfAssets; ++i)
            trs[i].set( 0, spot[i] );
    }
        
    private Vector drawZ()
    {
        Vector Z = rt.normal(numberOfAssets);
        return decomp.mult(Z);
    }
    
    private void fillNext(int timePoint, SimpleTrajectory[] trs, Vector Z)
    {
        for (int i = 1; i <= numberOfAssets; ++i)
            trs[i].set( timePoint, trs[i].price(timePoint-1) * Math.exp(
                     dm[i] - dvol[i]*dvol[i]/2 + dvol[i]*Z.get(i) ) );
    }
        
    private void maybeDividend(int k)
    {
        ArrayList<Dividend> dividends = ds.getDivindent(k);
        for (Dividend d: dividends)
        {
            handleDividend(k, pos, d);
            if (genAnthi)
                handleDividend(k, neg, d);
        }
    }
    
    private void handleDividend(int k, SimpleTrajectory[] trs, Dividend d)
    {
        int i = modelParams.getNr(d.underlying);
        double S = trs[i].price(k);
        double v = d.getDividend(S);
        trs[i].set(k, Math.max(0, S-v));
    }
    
    private Scenario makeResult()
    {
        if (genAnthi)
           return new MultiTrScenario(ts, names, pos, neg);
        else
            return new MultiTrScenario(ts, names, pos);
    }
    
    @Override
    public void setDividends(Collection<Dividend> dividends)
    {
        ds = new DividendsSupport(ts, dividends);
    }
    
    private TimeSupport ts;
    private DividendsSupport ds;
    private ModelParams modelParams;
    private RandomTools rt = new RandomTools();
    private int numberOfAssets;
    private boolean genAnthi;
    private String names[];
    private double dm[], dvol[], spot[];
    private Matrix decomp;
    private SimpleTrajectory[] pos;
    private SimpleTrajectory[] neg;
    private Collection<Auxiliary> auxStats;

}
