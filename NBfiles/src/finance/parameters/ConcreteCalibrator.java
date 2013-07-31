
package finance.parameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import math.matrices.Matrix;
import math.matrices.Vector;
import math.utils.Statistics;

/**
 *
 * @author Grzegorz Los
 */
public class ConcreteCalibrator implements Calibrator
{
    private abstract class ReadAction
    {
        abstract void read() throws Exception;
        abstract int linesRead();
    }
    
    public ConcreteCalibrator()
    {
        this(false, 1./252);
    }

    public ConcreteCalibrator(boolean dateInFirstColumn)
    {
        this(dateInFirstColumn, 1./252);
    }

    public ConcreteCalibrator(double dt)
    {
        this(false, dt);
    }

    public ConcreteCalibrator(boolean dateInFirstColumn, double dt)
    {
        this.dateInFirstColumn = dateInFirstColumn;
        this.dt = dt;
    }
    
    
    public double getDt()
    {
        return dt;
    }

    public void setDt(double dt)
    {
        this.dt = dt;
    }

    public boolean isDateInFirstColumn()
    {
        return dateInFirstColumn;
    }

    public void setDateInFirstColumn(boolean dateInFirstColumn)
    {
        this.dateInFirstColumn = dateInFirstColumn;
    }

    public String getSep()
    {
        return sep;
    }

    public void setSep(String sep)
    {
        this.sep = sep;
    }
    
    private void tryAction(ReadAction action) throws CorruptedStreamException
    {
        try {
            action.read();
            lineNr += action.linesRead();
        } catch (Exception ex) {
            throw new CorruptedStreamException("Error in line " +
                    (lineNr + action.linesRead()), ex);
        }
    }    
    
    @Override
    public void readAndCalc(InputStream in) throws CorruptedStreamException
    {
        try {
            reset();
            br = new BufferedReader( new InputStreamReader(in) );
            tryAction( readFirstLine() );
            tryAction( readPrices() );
            calcLogarithms();
            calcMeanAndVar();
            calcBasicParams();
            calcCorrelation();
        } catch (IOException ex) {
            throw new CorruptedStreamException(ex);
        }
    }

    private void reset()
    {
        correlation = null;
        oneAssetParams = null;
        lineNr = 1;
        if (isDateInFirstColumn())
            wordsOffset = 1;
        else wordsOffset = 0;
    }
    
    private ReadAction readFirstLine() throws IOException
    {
        return new ReadAction()
        {
            int linesRead = 0;
            @Override void read() throws Exception
            {
                String line = br.readLine();
                String[] words = line.trim().split(sep);
                firstLineWordsToAssetNames(words);
                preparePrices();
                linesRead = 1;
            }
            @Override int linesRead() { return linesRead; }
        };
    }
    
    private void firstLineWordsToAssetNames(String[] words)
    {
        int n = words.length;
        if (isDateInFirstColumn())
            n--;
        assetNames = new String[n];
        for (int i = 0; i < n; ++i)
            assetNames[i] = words[i + wordsOffset].trim();
    }
    
    private void preparePrices()
    {
        prices = new ArrayList<  >();
        for (int i = 0; i < getNumberOfAssets(); ++i)
            prices.add( new ArrayList<Double>() );
    }

    private ReadAction readPrices() throws IOException
    {
        return new ReadAction()
        {
            int linesRead = 0;
            @Override void read() throws Exception
            {                
                String line;
                while ( (line = br.readLine()) != null )
                {
                    readPricesFromLine(line);
                    linesRead++;
                }
            }
            @Override int linesRead() { return linesRead; }
        };
    }
    
    private void readPricesFromLine(String line)
            throws CorruptedStreamException, NumberFormatException
    {
        String[] words = line.trim().split(sep);
        if (words.length != properNumberOfWords())
            throw new CorruptedStreamException("Wrong number of values");
        for (int i = 0; i < getNumberOfAssets(); ++i)
            prices.get(i).add(toDouble(words[i + wordsOffset]));
    }
    
    private double toDouble(String str) throws NumberFormatException
    {
        return Double.parseDouble(str.trim());
    }
    
    private int properNumberOfWords()
    {
        if (isDateInFirstColumn())
            return getNumberOfAssets() + 1;
        else return getNumberOfAssets();
    }
    
    private int getNumberOfAssets()
    {
        return assetNames.length;
    }
    
    private int getLengthOfPrices()
    {
        return prices.get(0).size();
    }
    
    private int getLengthOfLogs()
    {
        return getLengthOfPrices() - 1;
    }
    
    private void calcLogarithms()
    {
        logs = new Vector[getNumberOfAssets()];
        for (int i = 0; i < getNumberOfAssets(); ++i)
            calcLogarithms(i);
        data = new Matrix(logs);
    }
    
    private void calcLogarithms(int assetNr)
    {
        Vector v = new Vector( getLengthOfLogs() );
        ArrayList<Double> p = prices.get(assetNr);
        for (int row = 1; row <= v.getRows(); ++row)
            v.set(row, Math.log(p.get(row) / p.get(row-1)));
        logs[assetNr] = v;
    }
    
    private void calcMeanAndVar()
    {
        alfas = Statistics.mean(data);
        betas = Statistics.var(data);
    }
    
    private void calcBasicParams()
    {
        oneAssetParams = new OneAssetParams[getNumberOfAssets()];
        for (int i = 0; i < getNumberOfAssets(); ++i)
            calcBasicParams(i);
    }
    
    private void calcBasicParams(int assetNr)
    {
        double S = calcSpot(assetNr);
        double vol = calcVol(assetNr);
        double mu = calcDrift(assetNr);
        oneAssetParams[assetNr] = new OneAssetParams(assetNames[assetNr], S, vol, mu);
    }
    
    private double calcSpot(int assetNr)
    {
        return prices.get(assetNr).get(getLengthOfPrices()-1);
    }
    
    private double calcVol(int assetNr)
    {
        return Math.sqrt( betas.get(assetNr+1) / dt);
    }
    
    private double calcDrift(int assetNr)
    {
        return (alfas.get(assetNr+1) + betas.get(assetNr+1)/2) / dt;
    }
    
    private void calcCorrelation()
    {
        correlation = Statistics.corr(data);
    }
    
    @Override
    public OneAssetParams[] getOneAssetParams()
    {
        return oneAssetParams;
    }

    @Override
    public Matrix getCorrelation()
    {
        return correlation;
    }
    
    // parameters of calibrator
    private String sep = ",";
    private boolean dateInFirstColumn;
    private double dt;
    
    // intermediate calculations
    BufferedReader br;
    int lineNr;
    int wordsOffset;
    private String[] assetNames;
    private ArrayList< ArrayList<Double> > prices;
    private Vector[] logs;
    private Matrix data;
    private Vector alfas; // means of logarithms
    private Vector betas; // variances of logarithms
    
    // results of calibration
    private Matrix correlation;
    private OneAssetParams[] oneAssetParams;
}
