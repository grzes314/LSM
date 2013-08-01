
package lsmapp.controlPanels;

import finance.instruments.EuExercise;
import finance.instruments.Instr;
import finance.instruments.Option;
import finance.methods.FiniteDifference;
import finance.methods.Progress;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import finance.parameters.VanillaOptionParams.AmOrEu;
import static finance.parameters.VanillaOptionParams.AmOrEu.AM;
import static finance.parameters.VanillaOptionParams.AmOrEu.EU;
import finance.parameters.VanillaOptionParams.CallOrPut;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static finance.parameters.VanillaOptionParams.CallOrPut.PUT;
import finance.trajectories.TimeSupport;

/**
 *
 * @author grzes
 */
public class FDPanel extends ModelPanel
{    
    public FDPanel()
    {
        initComponents();
        progressDesc.setVisible(false);    
        progressBar.setVisible(false);
    }
    
    @Override
    public FiniteDifference getModel()
    {
        return model;
    }

    @Override
    public Instr getInstr()
    {
        return instr;
    }
 
    public void setPriceBttnEnabled(boolean on)
    {
        priceBttn.setEnabled(on);
        progressDesc.setVisible(!on);
    }


    @Override
    protected FiniteDifference createModel()
    {
        double v = (Double) volatility.getValue();
        double r = (Double) rate.getValue();
        double S = (Double) spot.getValue();
        model = new FiniteDifference( new SimpleModelParams(S, v, r) );
        return model;
    }
    
    @Override
    protected Instr createInstr()
    {
        double T = (Double) years.getValue();
        int K = (Integer) timeSteps.getValue();
        int type = (put.isSelected() ? Option.PUT : Option.CALL);
        double E = (Double) strike.getValue();
        if (euoption.isSelected())
            instr = new EuExercise( new Option(type, E, "noname", T) );
        else instr = new Option(type, E, "noname", T);
        return instr;
    }
    
    @Override
    protected void prepareForTask()
    {
        progressDesc.setText("Preparing...");
        progressDesc.setVisible(true);
        progressBar.setValue(0);
        progressBar.setVisible(true);
        priceBttn.setEnabled(false);
    }

    @Override
    protected double calculate()
    {
        double E = (Double) strike.getValue();
        double T = (Double) years.getValue();
        CallOrPut CorP = call.isSelected() ? CALL : PUT;
        AmOrEu AorE = amoption.isSelected() ? AM : EU;
        int I = (Integer) priceSteps.getValue();
        int K = (Integer) timeSteps.getValue();
        return model.price(new VanillaOptionParams(E, T, CorP, AorE), I, K);
    }

    @Override
    protected void progressUpdate(Progress pr)
    {
        progressDesc.setText(pr.desc);
        progressBar.setValue(pr.percent);
    }

    @Override
    protected void cleanAfterTask()
    {
        progressDesc.setVisible(false);
        progressBar.setVisible(false);
        priceBttn.setEnabled(true);
    }
    
    private FiniteDifference model;
    private Instr instr;
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        volatility = new javax.swing.JSpinner();
        spot = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        rate = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        euoption = new javax.swing.JRadioButton();
        amoption = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        put = new javax.swing.JRadioButton();
        call = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        strike = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        years = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        timeSteps = new javax.swing.JSpinner();
        priceSteps = new javax.swing.JSpinner();
        priceBttn = new javax.swing.JButton();
        progressDesc = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel9 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Market and asset"));

        jLabel2.setText("volatility");

        jLabel3.setText("spot price");

        volatility.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.2d), Double.valueOf(0.0d), null, Double.valueOf(0.01d)));

        spot.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(100.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        jLabel4.setText("interest rate");

        rate.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.05d), null, null, Double.valueOf(0.01d)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(volatility)
                    .addComponent(spot)
                    .addComponent(rate, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(volatility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Instrument"));

        buttonGroup1.add(euoption);
        euoption.setText("European Option");

        buttonGroup1.add(amoption);
        amoption.setSelected(true);
        amoption.setText("American Option");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(euoption)
                    .addComponent(amoption))
                .addGap(0, 136, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(euoption)
                .addGap(18, 18, 18)
                .addComponent(amoption)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Option Parameters"));

        buttonGroup2.add(put);
        put.setSelected(true);
        put.setText("put");

        buttonGroup2.add(call);
        call.setText("call");

        jLabel1.setText("Strike");

        strike.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(100.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        jLabel5.setText("Years to exp.");

        years.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), null, null, Double.valueOf(1.0d)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(put)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(call))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(strike, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(years, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(put)
                    .addComponent(call))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(strike, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(years, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Model"));

        jLabel6.setText("Price steps");

        jLabel7.setText("Time steps");

        timeSteps.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10000), Integer.valueOf(100), null, Integer.valueOf(1000)));

        priceSteps.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(200), Integer.valueOf(10), null, Integer.valueOf(100)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(59, 59, 59)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(timeSteps, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(priceSteps))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(priceSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(timeSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        priceBttn.setText("Price!");
        priceBttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceBttnActionPerformed(evt);
            }
        });

        progressDesc.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        progressDesc.setForeground(new java.awt.Color(255, 0, 0));
        progressDesc.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        progressDesc.setText("Description");

        progressBar.setValue(30);
        progressBar.setStringPainted(true);

        jLabel9.setFont(new java.awt.Font("Cantarell", 1, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Finite Difference");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                .addGap(228, 228, 228)
                .addComponent(priceBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(progressDesc)
                        .addGap(18, 18, 18)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(priceBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void priceBttnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_priceBttnActionPerformed
    {//GEN-HEADEREND:event_priceBttnActionPerformed
        execute();
    }//GEN-LAST:event_priceBttnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton amoption;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JRadioButton call;
    private javax.swing.JRadioButton euoption;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton priceBttn;
    private javax.swing.JSpinner priceSteps;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressDesc;
    private javax.swing.JRadioButton put;
    private javax.swing.JSpinner rate;
    private javax.swing.JSpinner spot;
    private javax.swing.JSpinner strike;
    private javax.swing.JSpinner timeSteps;
    private javax.swing.JSpinner volatility;
    private javax.swing.JSpinner years;
    // End of variables declaration//GEN-END:variables
}
