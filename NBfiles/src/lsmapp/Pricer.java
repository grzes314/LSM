
package lsmapp;

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
            }
        });
    }

    public static Pricer getApp()
    {
        return application;
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
        statusBar = createStatusBar();
        content.add(statusBar, BorderLayout.PAGE_END);
        return content;
    }

    private Container createMain()
    {
        JTabbedPane pane = new JTabbedPane();
        pane.add("Model", createModelPanel());
        return pane;
    }

    private Component createModelPanel()
    {
        modelManager = new ModelManager();
        ModelPanel mp = new ModelPanel(modelManager);
        modelManager.setModelPanel(mp);
        return mp;
    }

    private JLabel createStatusBar()
    {
        JLabel label = new StatusBar();
        label.setText("Hello!");
        return label;
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
    }
    
    private void openModelClicked()
    {
        File file = getFileToOpen(new String[]{"txt"});
        if (file != null)
            openModel(file);
    }
    
    private void readModelClicked()
    {
        File file = getFileToOpen(new String[]{"csv"});
        if (file != null)
            readModel(file);
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
            InputStream in = new FileInputStream(file);
            cc.readAndCalc(in);
            ModelParams mp = new ConcreteParams(cc.getOneAssetParams(),
                    cc.getCorrelation(), 0.0);
            modelManager.readModelParams(mp);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Could not read the file",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (CorruptedStreamException ex) {
            JOptionPane.showMessageDialog(rootPane, "The file is corrupted",
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Pricer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveModel(File file)
    {
        ConcreteParamsIO io = new ConcreteParamsIO();
        try {
            OutputStream out = new FileOutputStream(file);
            io.write(modelManager.toParams(), out);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Could not write to the file",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void openModel(File file)
    {
        ConcreteParamsIO io = new ConcreteParamsIO();
        try {
            InputStream in = new FileInputStream(file);
            ModelParams mp = io.read(in);
            modelManager.readModelParams(mp);
        } catch (CorruptedStreamException ex) {
            JOptionPane.showMessageDialog(rootPane, "The file is corrupted",
                    "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Pricer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Could not read the file",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    JLabel statusBar;
    ModelManager modelManager;
    static Pricer application;
}