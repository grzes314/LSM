
package lsmprototype;

import models.WrongInstrException;

public class LSMPrototype
{

    public static void main(String[] args) throws WrongInstrException
    {
       /* RandomTools r = new RandomTools();
        double[] t = new double[11];
        for (int i = 0; i <= 10; ++i)
            t[i] = (i-5)/2.5;
        for (int i = 0; i <= 10; ++i)
            System.out.println(t[i] + "  " + r.cndf(t[i])); 
        int type = Option.CALL;
        double K = 100;
        double S = 100;
        double T = 1.0;
        double vol = 0.2;
        double r = 0.05;
        
        Obligation obl = new Obligation(1.0);
        EuOption opt = new EuOption(type, K, T);
        AmOption opt_am = new AmOption(type, K, T);
        BSModel bs = new BSModel(S, vol, r);
        LSModel ls = new LSModel(S, vol, r, 1000000, 10, 2);
        System.out.println("BSModel: " + bs.price(opt).getPrice());
        //System.out.println("LSModel: " + ls.price(opt).getPrice());
        System.out.println("amer: " + ls.price(opt_am).getPrice());
        //System.out.println("Obl: " + ls.price(obl).getPrice());*/
        
        new MainFrame().setVisible(true);
    }
}
