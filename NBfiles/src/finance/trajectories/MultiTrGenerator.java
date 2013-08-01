
package finance.trajectories;

import finance.parameters.ModelParams;
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
        extractNecssaryData(params, measure);
    }
    
    private void extractNecssaryData(ModelParams params, Measure measure)
    {
        extractNumberOfAssets(params);
        extractNames(params);
        extractCholeskyDecomposition(params);
        extractAssetData(params, measure);
    }

    private void extractNumberOfAssets(ModelParams params)
    {
        numberOfAssets = params.getNumberOfAssets();
    }
    
    private void extractNames(ModelParams params)
    {
        names = new String[numberOfAssets+1];
        for (int i = 1; i <= numberOfAssets; ++i)
            names[i] = params.getName(i);
    }

    private void extractCholeskyDecomposition(ModelParams params)
            throws IllegalArgumentException
    {
        try {
            decomp = params.getCorrelation().cholesky();
        } catch (NotPositiveDefiniteMatrixException ex) {
            throw new IllegalArgumentException("Correlation matrix is invalid", ex);
        }
    }

    private void extractAssetData(ModelParams params, Measure measure)
    {
        extractSpot(params);
        extractDm(params, measure);
        extractDvol(params);
    }
    
    private void extractSpot(ModelParams params)
    {
        spot = new double[numberOfAssets+1];
        for (int i = 1; i <= numberOfAssets; ++i)
            spot[i] = params.getParams(i).S;
    }
    
    private void extractDm(ModelParams params, Measure measure)
    {
        dm = new double[numberOfAssets+1];
        for (int i = 1; i <= numberOfAssets; ++i)
        {
            switch (measure)
            {
                case REAL:
                    dm[i] = params.getParams(i).mu * ts.getDt();
                    break; // TODO Upewnic sie ze break jest potrzebny
                case MART:
                    dm[i] = params.getR() * ts.getDt();                    
            }
        }
    }
    
    private void extractDvol(ModelParams params)
    {
        dvol = new double[numberOfAssets+1];
        for (int i = 1; i <= numberOfAssets; ++i)
            dvol[i] = params.getParams(i).vol * Math.sqrt(ts.getDt());
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
    public Scenario generate(boolean anthi)
    {
        genAnthi = anthi;
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
            pos[i] = new SimpleTrajectory(getTimePoints());
            if (genAnthi)
                neg[i] = new SimpleTrajectory(getTimePoints());
        }
    }

    private void fillTrajectories()
    {
        callFillFirst();
        for (int k = 1; k <= getTimePoints(); ++k)
        {
            Vector Z = drawZ();
            callFillNext(k, Z);
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
            fillNext(k, neg, Z);
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
    
    private Scenario makeResult()
    {
        if (genAnthi)
           return new MultiTrScenario(ts, names, pos, neg);
        else
            return new MultiTrScenario(ts, names, pos);
    }
    
    private TimeSupport ts;
    
    private RandomTools rt = new RandomTools();
    private int numberOfAssets;
    private boolean genAnthi;
    private String names[];
    private double dm[], dvol[], spot[];
    private Matrix decomp;
    private SimpleTrajectory[] pos;
    private SimpleTrajectory[] neg;
}
