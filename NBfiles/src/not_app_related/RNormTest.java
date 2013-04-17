
package not_app_related;

import static java.lang.Math.*;
import java.util.Random;

public class RNormTest
{    
    public static void main(String[] args)
    {
        int n = 10000000;
        testMethod(n, new Method() {
            void run() {
                boxMuller();
            }
            String name() {
                return "Box-Muller";
            }
        });
        
        testMethod(n, new Method() {
            void run() {
                boxMuller();
            }
            String name() {
                return "Polar rejection";
            }
        });
    }
    
    static class Pair
    {
        public Pair(double x, double y)
        {
            this.x = x;
            this.y = y;
        }
        public double x, y;
    }
    
    static abstract class Method
    {
        abstract void run();
        abstract String name();
    }

    
    static Pair boxMuller()
    {
        double U1 = r.nextDouble();
        double U2 = r.nextDouble();
        double root = sqrt( -2 * log(U1) );
        double N1 = root * cos(2 * PI * U2);
        double N2 = root * sin(2 * PI * U2);
        return new Pair(N1, N2);
    }
    
    static Pair polarRejection()
    {
        double V1 = 0, V2 = 0, W = 5;
        while (W > 1)
        {
            V1 = r.nextDouble();
            V2 = r.nextDouble();
            V1 = 2*V1 - 1;
            V2 = 2*V2 - 1;
            W = V1*V1 + V2*V2;
        }        
        double root = sqrt( -2 * log(W) / W);
        double N1 = root * V1;
        double N2 = root * V2;
        return new Pair(N1, N2);
    }
    
    static void testMethod(int n, Method m)
    {
        long start, end;
        start = System.currentTimeMillis();
        for (int i = 0; i < n; ++i)
            m.run();
        end = System.currentTimeMillis();
        System.out.println(m.name() + ": " +
                (double)(end-start)/1000 + " s.");
    }
    
    static Random r = new Random(1);
}
