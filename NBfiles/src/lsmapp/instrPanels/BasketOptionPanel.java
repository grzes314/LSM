
package lsmapp.instrPanels;

import finance.instruments.BasketOption;
import finance.instruments.EuExercise;
import finance.instruments.Instr;
import finance.instruments.InvalidInstrParametersException;
import finance.parameters.VanillaOptionParams.CallOrPut;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static finance.parameters.VanillaOptionParams.CallOrPut.PUT;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import lsmapp.frame.Pricer;

/**
 *
 * @author Grzegorz Los
 */
public class BasketOptionPanel extends SpecificInstrPanel
{

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonExerciseGroup = new javax.swing.ButtonGroup();
        buttonTypeGroup = new javax.swing.ButtonGroup();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        european = new javax.swing.JRadioButton();
        american = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        call = new javax.swing.JRadioButton();
        put = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        expiry = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        assets1 = new javax.swing.JComboBox();
        strike = new javax.swing.JSpinner();
        assets3 = new javax.swing.JComboBox();
        assets4 = new javax.swing.JComboBox();
        assets2 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        quantity1 = new javax.swing.JSpinner();
        quantity2 = new javax.swing.JSpinner();
        quantity3 = new javax.swing.JSpinner();
        quantity4 = new javax.swing.JSpinner();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Basket Option");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Exercise:");

        buttonExerciseGroup.add(european);
        european.setSelected(true);
        european.setText("european");

        buttonExerciseGroup.add(american);
        american.setText("american");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Type:");

        buttonTypeGroup.add(call);
        call.setSelected(true);
        call.setText("call");

        buttonTypeGroup.add(put);
        put.setText("put");

        jLabel4.setText("<html>Time to<br> expiry:");

        expiry.setModel(new javax.swing.SpinnerNumberModel(1.0d, 0.0d, 100.0d, 0.1d));

        jLabel5.setText("Strike:");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Assets in basket:");

        strike.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Quantity:");
        jLabel7.setToolTipText("");

        quantity1.setModel(new javax.swing.SpinnerNumberModel(0.0d, -10000.0d, 10000.0d, 1.0d));

        quantity2.setModel(new javax.swing.SpinnerNumberModel(0.0d, -10000.0d, 10000.0d, 1.0d));

        quantity3.setModel(new javax.swing.SpinnerNumberModel(0.0d, -10000.0d, 10000.0d, 1.0d));

        quantity4.setModel(new javax.swing.SpinnerNumberModel(0.0d, -10000.0d, 10000.0d, 1.0d));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(expiry, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(european, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(call, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(put)
                            .addComponent(american)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(strike, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(assets2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(assets3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(quantity3, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(quantity2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(assets4, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(quantity4, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(assets1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(quantity1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quantity1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(assets1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(assets2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantity2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(assets3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantity3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(assets4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantity4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(european)
                    .addComponent(american))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(call)
                    .addComponent(put))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(expiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(strike, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton american;
    private javax.swing.JComboBox assets1;
    private javax.swing.JComboBox assets2;
    private javax.swing.JComboBox assets3;
    private javax.swing.JComboBox assets4;
    private javax.swing.ButtonGroup buttonExerciseGroup;
    private javax.swing.ButtonGroup buttonTypeGroup;
    private javax.swing.JRadioButton call;
    private javax.swing.JRadioButton european;
    private javax.swing.JSpinner expiry;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JRadioButton put;
    private javax.swing.JSpinner quantity1;
    private javax.swing.JSpinner quantity2;
    private javax.swing.JSpinner quantity3;
    private javax.swing.JSpinner quantity4;
    private javax.swing.JSpinner strike;
    // End of variables declaration//GEN-END:variables

    /** Creates new form OptionPanel */
    public BasketOptionPanel()
    {
        initComponents();
        putAssetsCombosToArray();
        putQuantitiesToArray();
        fillAssetsCombos();
    }
    
    private void putAssetsCombosToArray()
    {
        assets = new JComboBox[4];
        assets[0] = assets1;
        assets[1] = assets2;
        assets[2] = assets3;
        assets[3] = assets4;
    }
    
    private void putQuantitiesToArray()
    {
        quantity = new JSpinner[4];
        quantity[0] = quantity1;
        quantity[1] = quantity2;
        quantity[2] = quantity3;
        quantity[3] = quantity4;
    }
    
    @Override
    public Instr makeInstr() throws InvalidInstrParametersException
    {
        String[] underlyings = getAssetsNamesInBasket();
        double[] quantities = getAssetsQuantitiesInBasket();
        CallOrPut callOrPut = call.isSelected() ? CALL : PUT;
        double E = (Double) strike.getValue();
        double T = (Double) expiry.getValue();
        BasketOption opt = new BasketOption(T, underlyings, quantities, callOrPut, E);
        if (american.isSelected())
            return opt;
        else
            return new EuExercise( opt );
    }

    private void fillAssetsCombos()
    {
        for (int i = 0; i < 4; ++i)
            fillAssetsCombo(assets[i]);
    }

    private void fillAssetsCombo(JComboBox combo)
    {
        combo.removeAllItems();
        Collection<String> assetNames = Pricer.getApp().getModelManager().getAssets();
        combo.addItem("");
        for (String name: assetNames)
            combo.addItem(name);
    }

    @Override
    boolean isUsing(String asset)
    {
        for (int i = 0; i < 4; ++i)
            if (assets[i].getSelectedItem().equals(asset))
                return true;
        return false;
    }
       
    @Override
    public Set<String> getUnderlyings()
    {
        Set<String> res = new HashSet<>();
        for (int i = 0; i < 4; ++i)
        {
            String sel = (String) assets[i].getSelectedItem();
            if (!"".equals(sel))
                res.add(sel);
        }
        return res;
    }
    
    @Override
    void assetAdded(String assetName)
    {
        for (int i = 0; i < 4; ++i)
            assets[i].addItem(assetName);
    }

    @Override
    void assetDeleted(String assetName)
    {
        for (int i = 0; i < 4; ++i)
            assets[i].removeItem(assetName);
    }

    private int getNumberOfAssetsInBasket()
    {
        int res = 0;
        for (int i = 0; i < 4; ++i)
        {
            String sel = (String) assets[i].getSelectedItem();
            if (!"".equals(sel))
                res++;
        }
        return res;
    }

    private String[] getAssetsNamesInBasket()
    {
        int n = getNumberOfAssetsInBasket();
        String[] names = new String[n];
        int j = 0;
        for (int i = 0; i < 4; ++i)
        {
            String sel = (String) assets[i].getSelectedItem();
            if (!"".equals(sel))
                names[j++] = sel;
        }
        return names;
    }

    private double[] getAssetsQuantitiesInBasket()
    {
        int n = getNumberOfAssetsInBasket();
        double[] quant = new double[n];
        int j = 0;
        for (int i = 0; i < 4; ++i)
        {
            String sel = (String) assets[i].getSelectedItem();
            double val = (Double) quantity[i].getValue();
            if (!"".equals(sel))
                quant[j++] = val;
        }
        return quant;
    }
    
    private JComboBox[] assets;
    private JSpinner[] quantity;
}
