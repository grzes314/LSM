
package models;

/**
 * Data package with all values necessary to price option in Black-Scholes
 * model.
 * @author Grzegorz Los
 */
public class BSParameters
{
    public BSParameters()
    {
    }
    
    public BSParameters(double S, double vol, double r, double K, double T)
    {
        this.S = S;
        this.vol = vol;
        this.r = r;
        this.K = K;
        this.T = T;
    }
    public double S; 
    public double vol; 
    public double r; 
    public double K; 
    public double T;
}
