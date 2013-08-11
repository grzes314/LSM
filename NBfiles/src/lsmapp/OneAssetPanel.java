
package lsmapp;

import finance.parameters.OneAssetParams;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;






/**
 *
 * @author Grzegorz Los
 */
public class OneAssetPanel extends javax.swing.JPanel
{
    public OneAssetPanel(String asset)
    {
        initComponents();
        assetName.setText(asset);
        tableModel = (MyTableModel) table.getModel();
        tableModel.setAssetName(asset);
    }

    OneAssetPanel(OneAssetParams params)
    {
        initComponents();
        assetName.setText(params.name);
        tableModel = (MyTableModel) table.getModel();
        tableModel.setAssetName(params.name);
        spot.setValue( (Double) params.S );
        volatility.setValue( (Double) params.vol );
        drift.setValue( (Double) params.mu );
    }

    void zeroCorrelations(Collection<String> assetsNames)
    {
        tableModel.zeroCorrelations(assetsNames);
    }

    void resetCorrelations(Collection<Pair<String, Double>> corrs)
    {
        tableModel.resetCorrelations(corrs);
    }
    
    Collection<Pair<String, Double>> getCorrelations()
    {
        return tableModel.getCorrelations();
    }

    void addNewAsset(String assetName)
    {
        tableModel.addNewAsset(assetName);
    }
    
    void assetDeleted(String assetName)
    {
        tableModel.assetDeleted(assetName);
    }
    
    OneAssetParams makeOneAssetParams()
    {
        return new OneAssetParams(
                assetName.getText(),
                (Double) spot.getValue(),
                (Double) volatility.getValue(),
                (Double) drift.getValue() );
    }

    public void removeTableModelListener(TableModelListener l)
    {
        tableModel.removeTableModelListener(l);
    }

    public void addTableModelListener(TableModelListener l)
    {
        tableModel.addTableModelListener(l);
    }
    
    void showIllegalCorrValue(double val)
    {
        JOptionPane.showMessageDialog(this, "Incorrect correlation value: " + val,
                "Information", JOptionPane.WARNING_MESSAGE);
    }
    
    void changeCorrelation(String asset, double value)
    {
        tableModel.changeCorrelation(asset, value);
    }
    
    private MyTableModel tableModel;
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        assetName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        spot = new javax.swing.JSpinner();
        volatility = new javax.swing.JSpinner();
        drift = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        assetName.setFont(new java.awt.Font("Cantarell", 1, 18)); // NOI18N
        assetName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        assetName.setText("Asset name");
        assetName.setToolTipText("");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("spot");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, spot, org.jdesktop.beansbinding.ObjectProperty.create(), jLabel1, org.jdesktop.beansbinding.BeanProperty.create("labelFor"));
        bindingGroup.addBinding(binding);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("volatility");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, volatility, org.jdesktop.beansbinding.ObjectProperty.create(), jLabel2, org.jdesktop.beansbinding.BeanProperty.create("labelFor"));
        bindingGroup.addBinding(binding);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("drift");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, drift, org.jdesktop.beansbinding.ObjectProperty.create(), jLabel3, org.jdesktop.beansbinding.BeanProperty.create("labelFor"));
        bindingGroup.addBinding(binding);

        spot.setModel(new javax.swing.SpinnerNumberModel(100.0d, 0.0d, 1000000.0d, 1.0d));

        volatility.setModel(new javax.swing.SpinnerNumberModel(0.2d, 0.0d, 2.0d, 0.01d));

        drift.setModel(new javax.swing.SpinnerNumberModel(0.0d, -10.0d, 10.0d, 0.01d));

        table.setModel(new MyTableModel(this));
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(assetName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(volatility, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spot, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(drift, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(assetName, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(volatility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(drift, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel assetName;
    private javax.swing.JSpinner drift;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spot;
    private javax.swing.JTable table;
    private javax.swing.JSpinner volatility;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}


class MyTableModel implements TableModel
{
    public MyTableModel(OneAssetPanel oap)
    {
        this.oap = oap;
    }

    public String getAssetName()
    {
        return assetName;
    }

    public void setAssetName(String assetName)
    {
        this.assetName = assetName;
    }

    @Override
    public int getRowCount()
    {
        return otherAssets.size();
    }

    @Override
    public int getColumnCount()
    {
        return 2;
    }
    
    private void ensureColumnOK(int col)
    {
        if (col < 0 || col > 1)
            throw new IllegalArgumentException("Invalid column index: " + col);
    }
    
    private void ensureRowOK(int row)
    {
        if (row < 0 || row >= getRowCount())
            throw new IllegalArgumentException("Invalid row index: " + row);
    }
    
    private void ensureIndicesOK(int row, int col)
    {
        ensureColumnOK(col);
        ensureRowOK(row);
    }

    @Override
    public String getColumnName(int columnIndex)
    {
        ensureColumnOK(columnIndex);
        if (columnIndex == 0)
            return "Other asset";
        else
            return "Correlation";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        ensureColumnOK(columnIndex);
        if (columnIndex == 0)
            return String.class;
        else
            return Double.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex == 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        ensureIndicesOK(rowIndex, columnIndex);
        if (columnIndex == 0)
            return otherAssets.get(rowIndex);
        else 
            return correlations.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        ensureIndicesOK(rowIndex, columnIndex);
        if (columnIndex == 0)
            throw new IllegalArgumentException("Column \"Other asset\"" + "is not editable");
        double val =  Double.valueOf(aValue + "");
        trySettingValue(val, rowIndex, columnIndex);
    }

    private void trySettingValue(double val, int rowIndex, int columnIndex)
    {
        if (val > 1 || val < -1)
            oap.showIllegalCorrValue(val);
        else 
        {
            correlations.set(rowIndex, val);
            fireChange( new CorrChangeEvent(this, rowIndex, rowIndex, columnIndex,
                    assetName, otherAssets.get(rowIndex), correlations.get(rowIndex)) );
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l)
    {
        tableModelListeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l)
    {
        tableModelListeners.remove(l);
    }
    
    private void fireChange(TableModelEvent ev)
    {
        for (TableModelListener tml: tableModelListeners)
            tml.tableChanged(ev);
    }

    void zeroCorrelations(Collection<String> assetsNames)
    {
        otherAssets.clear();
        correlations.clear();
        for (String asset: assetsNames)
        {
            otherAssets.add(asset);
            correlations.add(0.0);
        }
    }
    
    void resetCorrelations(Collection<Pair<String, Double>> corrs)
    {
        otherAssets.clear();
        correlations.clear();
        for (Pair<String, Double> p: corrs)
        {
            otherAssets.add(p.fst);
            correlations.add(p.snd);
        }
    }

    void changeCorrelation(String asset, double value)
    {
        for (int i = 0; i < otherAssets.size(); ++i)
            if (otherAssets.get(i).equals(asset))
            {
                correlations.set(i, value);
                return ;
            }
        throw new RuntimeException("Changing correlation of not existing asset");
    }
    
    void addNewAsset(String assetName)
    {
        otherAssets.add(assetName);
        correlations.add(0.0);
    }

    void assetDeleted(String assetName)
    {
        for (int i = 0; i < otherAssets.size(); ++i)
        {
            if (otherAssets.get(i).equals(assetName))
            {
                otherAssets.remove(i);
                correlations.remove(i);
                return ;
            }
        }
    }

    Collection<Pair<String, Double>> getCorrelations()
    {
        ArrayList<Pair<String, Double>> arr = new ArrayList<>();
        for (int i = 0; i < getRowCount(); ++i)
            arr.add( new Pair<>(otherAssets.get(i), correlations.get(i)) );
        return arr;
    }
    
    private ArrayList<TableModelListener> tableModelListeners = new ArrayList<>();
    private ArrayList<String> otherAssets = new ArrayList<>();
    private ArrayList<Double> correlations = new ArrayList<>();
    private String assetName;
    private final OneAssetPanel oap;
}

class CorrChangeEvent extends TableModelEvent
{

    public CorrChangeEvent(TableModel source, int firstRow, int lastRow, int column,
            String assetSource, String assetChanged, double value)
    {
        super(source, firstRow, lastRow, column);
        this.assetSource = assetSource;
        this.assetChanged = assetChanged;
        this.value = value;
    }   
    
    final String assetSource;
    final String assetChanged;
    final double value;
}