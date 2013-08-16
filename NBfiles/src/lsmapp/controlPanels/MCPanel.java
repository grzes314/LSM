
package lsmapp.controlPanels;

import finance.instruments.*;
import finance.methods.common.Progress;
import finance.methods.montecarlo.AV;
import finance.methods.montecarlo.CMC;
import finance.methods.montecarlo.MonteCarlo;
import finance.parameters.BarrierParams;
import finance.parameters.BarrierParams.Type;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import finance.parameters.VanillaOptionParams.CallOrPut;
import static finance.parameters.VanillaOptionParams.CallOrPut.CALL;
import static finance.parameters.VanillaOptionParams.CallOrPut.PUT;
import java.awt.Component;
import javax.swing.JPanel;
import lsmapp.resultPanels.MC2GUI;

/**
 *
 * @author grzes
 */
public class MCPanel extends ModelPanel
{    
    public MCPanel(MC2GUI toGui)
    {
        super(toGui);
        initComponents();
        this.toGui = toGui;
        progressDesc.setVisible(false);    
        progressBar.setVisible(false);
        panelEnabled(barrierPanel, false);
    }
    
    @Override
    public MonteCarlo getMethod()
    {
        return method;
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
    protected MonteCarlo createModel()
    {
        double v = (Double) volatility.getValue();
        double r = (Double) rate.getValue();
        double S = (Double) spot.getValue();
        int N = (Integer) simulations.getValue();
        int K = (Integer) timeSteps.getValue();
        SimpleModelParams smp = new SimpleModelParams(S, v, r);
        if (useAnthi.isSelected())
            method = new AV(smp);
        else
            method = new CMC(smp);
        method.setAllParams(smp, N, K);
        toGui.setMethod(method);
        return method;
    }
    
    @Override
    protected Instr createInstr()
    {
        double T = (Double) years.getValue();
        CallOrPut type = (put.isSelected() ? PUT : CALL);
        double E = (Double) strike.getValue();
        VanillaOptionParams vop = new VanillaOptionParams(E, T, type);
        if (option.isSelected())
            instr = new EuExercise( new Option(vop, SimpleModelParams.onlyAsset) );
        else instr = new Bond(T);
        if (isBarrier.isSelected())
            instr = addBarrier(instr);
        toGui.setInstr(instr);
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
        try {
            return method.price(instr);
        } catch (InterruptedException ex) {
            return -1;
        }
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
    
    private Instr addBarrier(Instr instr)
    {
        double level = (Double) barrierPrice.getValue();
        BarrierParams bp;
        if (upAndIn.isSelected()) 
            bp = new BarrierParams(Type.UAI, level);
         else if (upAndOut.isSelected()) 
            bp = new BarrierParams(Type.UAO, level);
         else if (downAndIn.isSelected()) 
            bp = new BarrierParams(Type.DAI, level);
         else 
            bp = new BarrierParams(Type.DAO, level);
        
        return new Barrier(bp, SimpleModelParams.onlyAsset, instr);
    }
    
    private void panelEnabled(JPanel panel, boolean enabled)
    {
        panel.setEnabled(enabled);
        for (Component c: panel.getComponents())
            c.setEnabled(enabled);
    }
    
    private MC2GUI toGui;
    private MonteCarlo method;
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
        option = new javax.swing.JRadioButton();
        obligation = new javax.swing.JRadioButton();
        isBarrier = new javax.swing.JCheckBox();
        optionPanel = new javax.swing.JPanel();
        put = new javax.swing.JRadioButton();
        call = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        strike = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        years = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        timeSteps = new javax.swing.JSpinner();
        simulations = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        useAnthi = new javax.swing.JCheckBox();
        priceBttn = new javax.swing.JButton();
        progressDesc = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel9 = new javax.swing.JLabel();
        barrierPanel = new javax.swing.JPanel();
        barrierPrice = new javax.swing.JSpinner();
        upAndIn = new javax.swing.JRadioButton();
        upAndOut = new javax.swing.JRadioButton();
        downAndIn = new javax.swing.JRadioButton();
        downAndOut = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();

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
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(volatility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Instrument"));

        buttonGroup1.add(option);
        option.setSelected(true);
        option.setText("Option");
        option.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionActionPerformed(evt);
            }
        });

        buttonGroup1.add(obligation);
        obligation.setText("Obligation");
        obligation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                obligationActionPerformed(evt);
            }
        });

        isBarrier.setText("Barrier on");
        isBarrier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isBarrierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(option)
                    .addComponent(obligation)
                    .addComponent(isBarrier))
                .addGap(0, 43, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(option)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(obligation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isBarrier)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        optionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Option Parameters"));

        buttonGroup2.add(put);
        put.setSelected(true);
        put.setText("put");

        buttonGroup2.add(call);
        call.setText("call");

        jLabel1.setText("Strike");

        strike.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(100.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        jLabel5.setText("Years to exp.");

        years.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), null, null, Double.valueOf(1.0d)));

        javax.swing.GroupLayout optionPanelLayout = new javax.swing.GroupLayout(optionPanel);
        optionPanel.setLayout(optionPanelLayout);
        optionPanelLayout.setHorizontalGroup(
            optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionPanelLayout.createSequentialGroup()
                .addGroup(optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(optionPanelLayout.createSequentialGroup()
                        .addGroup(optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(put)
                            .addGroup(optionPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)))
                        .addGroup(optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(optionPanelLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(call))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(strike, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(optionPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(years, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(169, Short.MAX_VALUE))
        );
        optionPanelLayout.setVerticalGroup(
            optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionPanelLayout.createSequentialGroup()
                .addGroup(optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(put)
                    .addComponent(call))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(strike, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(optionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(years, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Model"));

        jLabel7.setText("Time steps");

        timeSteps.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        simulations.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10000), Integer.valueOf(10), null, Integer.valueOf(10000)));

        jLabel8.setText("Simulations");

        useAnthi.setText("Use anthitetic paths");
        useAnthi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useAnthiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(simulations, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                            .addComponent(timeSteps)))
                    .addComponent(useAnthi))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(timeSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(simulations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(useAnthi)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jLabel9.setText("Classic Monte Carlo");

        barrierPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Barrier"));
        barrierPanel.setEnabled(false);

        barrierPrice.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(100.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        buttonGroup3.add(upAndIn);
        upAndIn.setSelected(true);
        upAndIn.setText("Up-and-in");

        buttonGroup3.add(upAndOut);
        upAndOut.setText("Up-and-out");

        buttonGroup3.add(downAndIn);
        downAndIn.setText("Down-and-in");

        buttonGroup3.add(downAndOut);
        downAndOut.setText("Down-and-out");

        jLabel6.setText("Barrier price");

        javax.swing.GroupLayout barrierPanelLayout = new javax.swing.GroupLayout(barrierPanel);
        barrierPanel.setLayout(barrierPanelLayout);
        barrierPanelLayout.setHorizontalGroup(
            barrierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barrierPanelLayout.createSequentialGroup()
                .addGroup(barrierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(barrierPanelLayout.createSequentialGroup()
                        .addGap(0, 12, 12)
                        .addComponent(jLabel6)
                        .addGap(2, 2, 2)
                        .addComponent(barrierPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(barrierPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(barrierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(upAndIn)
                            .addComponent(upAndOut)
                            .addComponent(downAndIn)
                            .addComponent(downAndOut))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        barrierPanelLayout.setVerticalGroup(
            barrierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barrierPanelLayout.createSequentialGroup()
                .addGroup(barrierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(barrierPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(4, 4, 4)
                .addComponent(upAndIn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(upAndOut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downAndIn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downAndOut)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                .addGap(228, 228, 228)
                .addComponent(priceBttn, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barrierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(optionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(optionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(barrierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void useAnthiActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_useAnthiActionPerformed
    {//GEN-HEADEREND:event_useAnthiActionPerformed
        if (useAnthi.isSelected())
            simulations.setValue( (Integer) simulations.getValue() / 2);
        else 
            simulations.setValue( (Integer) simulations.getValue() * 2);
    }//GEN-LAST:event_useAnthiActionPerformed

    private void isBarrierActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_isBarrierActionPerformed
    {//GEN-HEADEREND:event_isBarrierActionPerformed
        panelEnabled(barrierPanel, isBarrier.isSelected());
        if (isBarrier.isSelected())
        {
            if ((Integer) timeSteps.getValue() < 250)
                timeSteps.setValue(250);
        }
        else {
            timeSteps.setValue(1);            
        }            
    }//GEN-LAST:event_isBarrierActionPerformed

    private void optionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_optionActionPerformed
    {//GEN-HEADEREND:event_optionActionPerformed
        panelEnabled(optionPanel, true);
    }//GEN-LAST:event_optionActionPerformed

    private void obligationActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_obligationActionPerformed
    {//GEN-HEADEREND:event_obligationActionPerformed
        panelEnabled(optionPanel, false);
    }//GEN-LAST:event_obligationActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barrierPanel;
    private javax.swing.JSpinner barrierPrice;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JRadioButton call;
    private javax.swing.JRadioButton downAndIn;
    private javax.swing.JRadioButton downAndOut;
    private javax.swing.JCheckBox isBarrier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton obligation;
    private javax.swing.JRadioButton option;
    private javax.swing.JPanel optionPanel;
    private javax.swing.JButton priceBttn;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressDesc;
    private javax.swing.JRadioButton put;
    private javax.swing.JSpinner rate;
    private javax.swing.JSpinner simulations;
    private javax.swing.JSpinner spot;
    private javax.swing.JSpinner strike;
    private javax.swing.JSpinner timeSteps;
    private javax.swing.JRadioButton upAndIn;
    private javax.swing.JRadioButton upAndOut;
    private javax.swing.JCheckBox useAnthi;
    private javax.swing.JSpinner volatility;
    private javax.swing.JSpinner years;
    // End of variables declaration//GEN-END:variables
}
