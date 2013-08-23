
package finance.trajectories;

/**
 * Class constitutes a bridge between continous and discrete language.
 * Time horizon T is divided into K intervals.
 * Time points are numbered from 0 to K (inclusively). All
 * timesteps have equal length dt. Time point 0 corresponds to time 0,
 * time point 1 - to time dt, time point 2 - to time 2*dt, and so on, up to 
 * time point K which corresponds to time T.
 * @author Grzegorz Los
 */
public class TimeSupport
{
    /**
     * The only constructor. It takes T (time horizon, that is time in years)
     * and a number of timesteps between time 0 and time T. Time points
     * are numbered from 0 to K (inclusively).
     * @param T time horizon.
     * @param K number of timesteps between time 0 and time T.
     */
    public TimeSupport(double T, int K)
    {
        if (T < 0 || K < 1)
            throw new IllegalArgumentException("TimeSupport.TimeSupport: T=" +
                                               T + ", K=" + K);
        this.T = T;
        this.K = K;
        dt = T/K;
    }
    
    /**
     * Converts discrete time point to continous time.
     * @param nr number of time point.
     * @return time in years corresponding to given time point.
     */
    public double nrToTime(int nr)
    {
        if (nr < 0 || nr > K)
            throw new IllegalArgumentException("TimeSupport.nrToTime: nr=" + nr);
        if (nr == K) //to avoid numerical errors
            return T;
        else return nr*dt;
    }
    
    /**
     * Converts continous time to discrete time point.
     * @param t time in years.
     * @return number of corresponding time point.
     */
    public int timeToNr(double t)
    {
        if (t < 0 || t > T)
            throw new IllegalArgumentException("TimeSupport.timeToNr: t=" + t);
        return (int) Math.round(t/dt);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TimeSupport other = (TimeSupport) obj;
        if (Double.doubleToLongBits(this.T) != Double.doubleToLongBits(other.T)) {
            return false;
        }
        if (this.K != other.K) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return (int)(T * 1500450271) + K*18409199;
    }

    public int getK()
    {
        return K;
    }

    public void setK(int K)
    {
        this.K = K;
        dt = T/K;
    }

    public double getT()
    {
        return T;
    }

    public void setT(double T)
    {
        this.T = T;
        dt = T/K;
    }

    public double getDt()
    {
        return dt;
    }
    
    private double T;
    private int K;
    private double dt;
}
