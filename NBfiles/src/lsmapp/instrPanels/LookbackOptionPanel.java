
package lsmapp.instrPanels;

import finance.instruments.EuExercise;
import finance.instruments.Instr;
import finance.instruments.LookbackOption;
import finance.parameters.VanillaOptionParams.CallOrPut;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static finance.parameters.VanillaOptionParams.CallOrPut.PUT;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lsmapp.frame.Pricer;

/**
 *
 * @author Grzegorz Los
 */
public class LookbackOptionPanel extends SpecificInstrPanel
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        european = new javax.swing.JRadioButton();
        american = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        call = new javax.swing.JRadioButton();
        put = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        expiry = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        assets = new javax.swing.JComboBox();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Lookback Option");

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

        jLabel4.setText("Time to expiry:");

        expiry.setModel(new javax.swing.SpinnerNumberModel(1.0d, 0.0d, 100.0d, 0.1d));

        jLabel6.setText("On asset:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(assets, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(european, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(call, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(put)
                                    .addComponent(american)))
                            .addComponent(expiry, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(assets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(expiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton american;
    private javax.swing.JComboBox assets;
    private javax.swing.ButtonGroup buttonExerciseGroup;
    private javax.swing.ButtonGroup buttonTypeGroup;
    private javax.swing.JRadioButton call;
    private javax.swing.JRadioButton european;
    private javax.swing.JSpinner expiry;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JRadioButton put;
    // End of variables declaration//GEN-END:variables

    /** Creates new form OptionPanel */
    public LookbackOptionPanel()
    {
        initComponents();
        fillAssetsCombo();
    }
    
    @Override
    public Instr makeInstr()
    {
        CallOrPut callOrPut = call.isSelected() ? CALL : PUT;
        double T = (Double) expiry.getValue();
        String assetName = (String) assets.getSelectedItem();
        LookbackOption opt = new LookbackOption(T, callOrPut, assetName);
        if (american.isSelected())
            return opt;
        else
            return new EuExercise( opt );
    }

    private void fillAssetsCombo()
    {
        assets.removeAllItems();
        Collection<String> assetNames = Pricer.getApp().getModelManager().getAssets();
        for (String name: assetNames)
            assets.addItem(name);
    }

    @Override
    boolean isUsing(String asset)
    {
        return assets.getSelectedItem().equals(asset);
    }
       
    @Override
    public Set<String> getUnderlyings()
    {
        Set<String> res = new HashSet<>();
        res.add((String) assets.getSelectedItem());
        return res;
    }
    
    @Override
    void assetAdded(String assetName)
    {
        assets.addItem(assetName);
    }

    @Override
    void assetDeleted(String assetName)
    {
        assets.removeItem(assetName);
    }
}
