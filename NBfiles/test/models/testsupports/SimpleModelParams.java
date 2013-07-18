
package models.testsupports;

/**
 * Parameters required by the most of simple models.
 * @author Grzegorz Los
 */
public class SimpleModelParams
{

    public SimpleModelParams(double S, double vol, double r)
    {
        this.S = S;
        this.vol = vol;
        this.r = r;
    }
    public final double S;
    public final double vol;
    public final double r;
}
