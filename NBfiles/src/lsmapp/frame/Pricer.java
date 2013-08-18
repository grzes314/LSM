
package lsmapp.frame;

import finance.parameters.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import lsmapp.instrPanels.InstrManager;
import lsmapp.instrPanels.InstrTab;
import lsmapp.instrPanels.NewInstrInfo;
import lsmapp.instrPanels.NoAssetsException;
import lsmapp.modelTab.ModelManager;
import lsmapp.modelTab.ModelPanel;
import lsmapp.resultPanels.ResultDisplay;
import lsmapp.resultPanels.ResultPanel;
import lsmapp.taskPanels.NewTaskTab;
import lsmapp.taskPanels.QuickPricePanel;
import lsmapp.taskPanels.TaskScheduler;
import math.matrices.NotPositiveDefiniteMatrixException;

/**
 *
 * @author Grzegorz Los
 */
public class Pricer extends JFrame
{
    public static void main(String[] args)
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                application = new Pricer();
                application.setVisible(true);
                addAssetsAndInstrs();
            }
        });
    }

    public static Pricer getApp()
    {
        return application;
    }
    
    private static void addAssetsAndInstrs() //TODO remove
    {
        try {
            ModelManager mm = application.getModelManager();
            InstrManager im = application.getInstrManager();
            mm.addAsset("aktywo");
            im.addInstr( new NewInstrInfo(
                NewInstrInfo.InstrType.Bond, "obligacja"));
            im.addInstr( new NewInstrInfo(
                NewInstrInfo.InstrType.Vanilla, "wanilia"));
        } catch (IllegalArgumentException | NoAssetsException | UnsupportedOperationException ex) {
            Logger.getLogger(Pricer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Pricer()
    {
        setSize(800,600);
        setTitle("The Pricer");
        setContentPane( createContent() );
        setJMenuBar( createMenuBar() );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Container createContent()
    {
        JPanel content = new JPanel();
        content.setLayout( new BorderLayout() );
        content.add(createMain(), BorderLayout.CENTER);
        content.add(createSouth(), BorderLayout.SOUTH);
        return content;
    }

    private Container createMain()
    {
        JTabbedPane pane = new JTabbedPane();
        pane.add("Model", createModelTab());
        pane.add("Instruments", createInstrTab());
        pane.add("Tasks", createTaskTab());
        pane.add("Results", createResultsPanel());
        return pane;
    }
    
    private Component createModelTab()
    {
        modelManager = new ModelManager();
        ModelPanel mp = new ModelPanel(modelManager);
        modelManager.setModelPanel(mp);
        return mp;
    }

    private Component createInstrTab()
    {
        instrManager = new InstrManager();
        modelManager.addAssetCountObserver(instrManager);
        InstrTab instrTab = new InstrTab(instrManager);
        instrManager.addInstrCountObserver(instrTab);
        return instrTab;
    }

    private Component createTaskTab()
    {
        assert instrManager != null;
        taskScheduler = new TaskScheduler();
        return new NewTaskTab(instrManager, taskScheduler);
    }

    private Component createResultsPanel()
    {
        ResultPanel resultPanel = new ResultPanel();
        resultDisplay = resultPanel;
        return resultPanel;
    }
    
    private JComponent createSouth()
    {
        JPanel south = new JPanel(new BorderLayout());
        quickPricer = createQuickPricer();
        south.add(quickPricer, BorderLayout.CENTER);
        statusBar = createStatusBar();
        south.add(statusBar, BorderLayout.SOUTH);
        return south;
    }

    private JLabel createStatusBar()
    {
        JLabel label = new StatusBar();
        label.setText("Hello!");
        return label;
    }
    
    QuickPricePanel createQuickPricer()
    {
        return new QuickPricePanel();
    }
    
    public void setStatus(String status)
    {
        statusBar.setText(status);
    }
    
    private JMenuBar createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenuFile());
        return menuBar;
    }
    
    private JMenu createMenuFile()
    {
        JMenu file = new JMenu("File");
        file.add(createMenuItemNewModel());
        file.add(createMenuItemOpenModel());
        file.add(createMenuItemReadModel());
        file.add(createMenuItemSaveModel());
        file.add(createMenuItemQuit());
        return file;
    }
    
    private JMenuItem createMenuItemNewModel()
    {
        JMenuItem newModel = new JMenuItem("New model");
        newModel.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                newModelClicked();
            }
        });        
        return newModel;
    }    
    
    private JMenuItem createMenuItemOpenModel()
    {
        JMenuItem openModel = new JMenuItem("Open model");
        openModel.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                openModelClicked();
            }
        });        
        return openModel;
    }    
    
    private JMenuItem createMenuItemReadModel()
    {
        JMenuItem readModel = new JMenuItem("Read model from CSV");
        readModel.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                readModelClicked();
            }
        });        
        return readModel;
    }    
    
    private JMenuItem createMenuItemSaveModel()
    {
        JMenuItem saveModel = new JMenuItem("Save model");
        saveModel.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                saveModelClicked();
            }
        });        
        return saveModel;
    }    
    
    private JMenuItem createMenuItemQuit()
    {
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });        
        return quit;
    }
    
    private void newModelClicked()
    {
        modelManager.clear();
        setStatus("Model cleared.");
    }
    
    private void openModelClicked()
    {
        if (!confirmClosingInstruments())
            return ;
        File file = getFileToOpen(new String[]{"txt"});
        if (file != null)
        {
            clear();
            openModel(file);
        }
    }
    
    private void readModelClicked()
    {
        if (!confirmClosingInstruments())
            return ;
        File file = getFileToOpen(new String[]{"csv"});
        if (file != null)
        {
            clear();
            readModel(file);
        }
    }

    private boolean confirmClosingInstruments()
    {
        if (instrManager.getNumberOfInstrs() == 0)
            return true;
        int res = JOptionPane.showConfirmDialog(
            this, "Opening a new model implies closing all instruments.",
            "Confirm", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION)
            return true;
        else return false;
    }
    
    private void saveModelClicked()
    {
        File file = getFileToSave(new String[]{"txt"});
        if (file != null)
            saveModel(file);
    }
    
    private File getFileToSave(String[] extentions)
    {
        //JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter( new ExtensionFilter(extentions) );
        int res = chooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile();
        else
            return null;
    }
    
    private File getFileToOpen(String[] extentions)
    {
        //JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter( new ExtensionFilter(extentions) );
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile();
        else
            return null;
    }

    private void readModel(File file)
    {
        ConcreteCalibrator cc = new ConcreteCalibrator(true);
        try {
            tryToReadModel(file, cc);
        } catch (NotPositiveDefiniteMatrixException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Pricer.class.getName()).log(Level.SEVERE, null, ex);
            setStatus("Model was not calibrated.");
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Could not read the file",
                    "Error", JOptionPane.ERROR_MESSAGE);
            setStatus("Model was not calibrated.");
        } catch (CorruptedStreamException ex) {
            JOptionPane.showMessageDialog(rootPane, "The file is corrupted",
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Pricer.class.getName()).log(Level.SEVERE, null, ex);
            setStatus("Model was not calibrated.");
        }
    }

    private void tryToOpenModel(File file, ConcreteParamsIO io) throws FileNotFoundException, CorruptedStreamException
    {
        InputStream in = new FileInputStream(file);
        ModelParams mp = io.read(in);
        modelManager.fromModelParams(mp);
        setStatus("Model from file " + file.getName() + " opened.");
    }

    private void tryToReadModel(File file, ConcreteCalibrator cc)
            throws NotPositiveDefiniteMatrixException, CorruptedStreamException,
                    FileNotFoundException
    {
        InputStream in = new FileInputStream(file);
        cc.readAndCalc(in);
        ModelParams mp = new ConcreteParams(cc.getOneAssetParams(),
                cc.getCorrelation(), 0.0);
        modelManager.fromModelParams(mp);
        setStatus("Model calibrated basing on data from file " + file.getName() + ".");
    }

    private void saveModel(File file)
    {
        ConcreteParamsIO io = new ConcreteParamsIO();
        try {
            tryToSaveModel(file, io);
        } catch (NotPositiveDefiniteMatrixException ex) {
            JOptionPane.showMessageDialog(rootPane, "Can not save model: " + ex.getMessage(),
                    "Saving failed", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Pricer.class.getName()).log(Level.SEVERE, null, ex);
            setStatus("Model was not saved.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Could not write to the file",
                    "Saving failed", JOptionPane.ERROR_MESSAGE);
            setStatus("Model was not saved.");
        }
    }

    private void tryToSaveModel(File file, ConcreteParamsIO io)
            throws FileNotFoundException, IOException, NotPositiveDefiniteMatrixException
    {
        OutputStream out = new FileOutputStream(file);
        io.write(modelManager.toParams(), out);
        setStatus("Model saved to file " + file.getName() + ".");
    }
    
    private void openModel(File file)
    {
        ConcreteParamsIO io = new ConcreteParamsIO();
        try {
            tryToOpenModel(file, io);
        } catch (CorruptedStreamException ex) {
            JOptionPane.showMessageDialog(rootPane, "The file is corrupted.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Pricer.class.getName()).log(Level.SEVERE, null, ex);
            setStatus("Failed to open model from file " + file.getName() + ".");
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Could not read the file",
                    "Error", JOptionPane.ERROR_MESSAGE);
            setStatus("File could not be opened.");
        }
    }

    public ModelManager getModelManager()
    {
        return modelManager;
    }

    public InstrManager getInstrManager()
    {
        return instrManager;
    }

    public ResultDisplay getResultDisplay()
    {
        return resultDisplay;
    }
    
    private void clear()
    {
        instrManager.clear();
        modelManager.clear();
    }
    
    private JLabel statusBar;
    private QuickPricePanel quickPricer;
    private ModelManager modelManager;
    private InstrManager instrManager;
    private TaskScheduler taskScheduler;
    private ResultDisplay resultDisplay;
    private static Pricer application;
}
