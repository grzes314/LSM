
package finance.parameters;

import java.io.*;
import math.matrices.Matrix;

/**
 * Reads and writes {@code ConcreteParams} in the following format:
 * 1. First line contains single number {@code n} denoting the number of 
 * assets in the model.
 * 2. Each of next {@code n} lines contain 4 values separated by a single space:
 * asset's name, spot price, volatility and drift.
 * 3. Next {@code n} lines describes a correlation between assets. {@code i}'th of these lines
 * contains {@code n} numbers, separated by a single space, where {@code j}'th number denoted
 * the correlation between assets {@code i} and {@code j}.
 * 4. After that comes the last line containing one number, value of the riskless interest
 * rate.
 * @author Grzegorz Los
 */
public class ConcreteParamsIO implements ParamsIO
{
    private abstract class ReadAction
    {
        abstract void read() throws Exception;
        abstract int linesRead();
    }
    
    @Override
    public ModelParams read(InputStream in) throws CorruptedStreamException
    {
        br = new BufferedReader(new InputStreamReader(in));
        lineNr = 1;
        tryAction( readNumberOfAssets() );
        tryAction( readAssets() );
        tryAction( readCorrelation() );
        tryAction( readInterestRate() );
        return makeResult();
    }
    
    private void tryAction(ReadAction action) throws CorruptedStreamException
    {
        try {
            action.read();
            lineNr += action.linesRead();
        } catch (Exception ex) {
            throw new CorruptedStreamException("Error in line " +
                    (lineNr+ action.linesRead()), ex);
        }
    }

    private ReadAction readNumberOfAssets() throws CorruptedStreamException
    {
        return new ReadAction()
        {
            int linesRead = 0;
            @Override void read() throws Exception
            {
                String line = br.readLine();
                n = Integer.parseInt(line.trim());
                if (n < 1)
                    throw new IllegalArgumentException("The number of assets must "
                            + "be at least 1");
                linesRead = 1;
            } 
            @Override int linesRead() { return linesRead; }
        };
    }
    
    private ReadAction readAssets()
    {
        return new ReadAction()
        {
            int i = 0;
            @Override void read() throws Exception
            {   
                basicParams = new OneAssetParams[n];
                for (i = 0; i < n; ++i)
                    basicParams[i] = readOneAsset();
            }
            @Override int linesRead() { return i; }
        };
    }

    private OneAssetParams readOneAsset() throws IOException, NumberFormatException
    {
        String line = br.readLine();
        String[] words = line.trim().split("\\s+");
        if (words.length != 4)
            throw new IllegalArgumentException("Asset should be described by four values");
        double S = Double.parseDouble(words[1].trim());
        double vol = Double.parseDouble(words[2].trim());
        double mu = Double.parseDouble(words[3].trim());
        return new OneAssetParams(words[0], S, vol, mu);
    }

    private ReadAction readCorrelation()
    {
        corr = new Matrix(n, n);
        return new ReadAction()
        {
            int i = 0;
            @Override void read() throws Exception
            {   
                for (i = 0; i < n; ++i)
                    readCorrelationLine(i+1);
            }
            @Override int linesRead() { return i; }
        };
    }
    
    private void readCorrelationLine(int assetNr) throws IOException
    {
        String line = br.readLine();
        String[] words = line.trim().split("\\s+");
        if (words.length != n)
            throw new IllegalArgumentException("Correlation matrix should have "
                    + n + " columns");
        for (int i = 0; i < n; ++i)
            corr.set(assetNr, i+1, Double.parseDouble(words[i].trim()));
    }

    private ReadAction readInterestRate()
    {
        return new ReadAction()
        {
            int linesRead = 0;
            @Override void read() throws Exception
            {
                String line = br.readLine();
                r = Double.parseDouble(line.trim());
                linesRead = 1;
            } 
            @Override int linesRead() { return linesRead; }
        };
    }

    private ConcreteParams makeResult() throws CorruptedStreamException
    {
        try {
            ConcreteParams params = new ConcreteParams(basicParams, corr, r);
            return params;
        } catch (IllegalArgumentException ex) {
            throw new CorruptedStreamException("Data in file is invalid", ex);
        }
    }
    
    @Override
    public void write(ModelParams params, OutputStream out) throws IOException
    {
        bw = new BufferedWriter(new OutputStreamWriter(out));
        paramsToWrite = params;
        writeNumberOfAssets();
        writeAssets();
        writeCorrelation();
        writeInterestRate();
        bw.flush();
    }

    private void writeNumberOfAssets() throws IOException
    {
        bw.write(paramsToWrite.getNumberOfAssets() + "\n");
    }

    private void writeAssets() throws IOException
    {
        for (int i = 1; i <= paramsToWrite.getNumberOfAssets(); ++i)
        {
            OneAssetParams oap = paramsToWrite.getParams(i);
            bw.write(oap.name);
            bw.write(" " + oap.S);
            bw.write(" " + oap.vol);
            bw.write(" " + oap.mu);
            bw.write("\n");
        }
    }

    private void writeCorrelation() throws IOException
    {
        Matrix m = paramsToWrite.getCorrelation();
        for (int row = 1; row <= m.getRows(); ++row)
        {
            for (int col = 1; col < m.getCols(); ++col)
                bw.write(m.get(row, col) + " ");
            bw.write(m.get(row, m.getCols()) + "\n");
        }
    }

    private void writeInterestRate() throws IOException
    {
        bw.write(paramsToWrite.getR() + "\n");
    }
    
    // Parameters for reading
    private BufferedReader br;
    private int lineNr;
    private int n;
    private OneAssetParams[] basicParams;
    private double r;
    private Matrix corr;
    
    // parameters for writing
    private BufferedWriter bw;
    private ModelParams paramsToWrite;
}
