
package lsmapp.taskPanels;

import finance.instruments.Instr;
import finance.instruments.InvalidInstrParametersException;
import finance.methods.common.Method;
import finance.parameters.ModelParams;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import lsmapp.frame.Pricer;
import lsmapp.resultPanels.ResultHandler;
import math.matrices.NotPositiveDefiniteMatrixException;

/**
 *
 * @author Grzegorz Los
 */
public class TaskScheduler implements TaskInfo
{
    @Override
    public void addTaskObserver(TaskObserver observer)
    {
        observers.add(observer);
    }

    @Override
    public void removeTaskObserver(TaskObserver observer)
    {
        observers.remove(observer);
    }

    @Override
    public void fireNewTask(String methodName, String instrName, PricingTask task)
    {
        for (TaskObserver ob: observers)
            ob.newTask(methodName, instrName, task);
    }

    void setNewTaskPanel(NewTaskPanel newTaskPanel)
    {
        if (this.newTaskPanel != null)
            throw new RuntimeException("TaskSceduler already has corresponding NewTaskPanel.");
        this.newTaskPanel = newTaskPanel;
    }
    
    public void startNewTask(String methodName, String instrName)
    {
        this.methodName = methodName;
        this.instrName = instrName;
        try {
            preparePricingTask();
            if (checkIfPricingMayBeDone())
            {
                fireNewTask(methodName, instrName, task);
                Pricer.getApp().setStatus("New task started: " + task.getDesc());
                task.execute();
            }
        } catch (MethodInstantiationException ex) {
            informAboutMethodInstantiationProblem(ex.getMessage());
        } catch (InvalidInstrParametersException ex) {
            informAboutBadParams(instrName, ex.getMessage());
        } catch (NotPositiveDefiniteMatrixException ex) {
            informAboutWrongCorrelation();
        }
        
    }
        
    private void preparePricingTask() throws NotPositiveDefiniteMatrixException,
            InvalidInstrParametersException, MethodInstantiationException
    {
        makeInstr();
        makeMethod(methodName);
        makeModelParams();
        makeResultHandler();
        task = new PricingTask(method, modelParams, instr, resultHandler);
    }
    
    private void makeMethod(String methodName) throws MethodInstantiationException
    {
        method = newTaskPanel.makeMethod(methodName, instr);
    }
    
    private void makeModelParams() throws NotPositiveDefiniteMatrixException
    {
        modelParams = Pricer.getApp().getModelManager().toParams(instr);
    }

    private void makeInstr() throws InvalidInstrParametersException
    {
        instr = Pricer.getApp().getInstrManager().makeInstr(instrName);
    }
    
    private void makeResultHandler()
    {
        resultHandler =  newTaskPanel.makeResultHandler(methodName);
        resultHandler.setAll(method, modelParams, instr);
    }

    private boolean checkIfPricingMayBeDone()
    {
        boolean res = method.isPriceable(instr);
        if (!res)
        {
            JOptionPane.showMessageDialog(Pricer.getApp(), "Chosen instrument cannot be priced "
                + "with selected method.\n" + newTaskPanel.getPriceableDesc(methodName),
                "Pricing impossible", JOptionPane.ERROR_MESSAGE);
        }
        return res;
    }

    private void informAboutWrongCorrelation()
    {
        JOptionPane.showMessageDialog(Pricer.getApp(),
            "Could no create model parameters.\n" +
            "Provided correlations are invalid.\n" + 
            "Correlation matrix is not positive definite.",
            "Pricing can not be done", JOptionPane.ERROR_MESSAGE);
    }

    private void informAboutBadParams(String instrName, String message)
    {
        JOptionPane.showMessageDialog(Pricer.getApp(),
                "Could not create instrument \"" + instrName + "\".\n" + message,
                "Pricing can not be done", JOptionPane.ERROR_MESSAGE);
    }

    private void informAboutMethodInstantiationProblem(String message)
    {
        JOptionPane.showMessageDialog(Pricer.getApp(),
                "Could not use method: \"" + methodName + "\".\n" + message,
                "Pricing can not be done", JOptionPane.ERROR_MESSAGE);
    }
    
    private String methodName;
    private String instrName;
    private Method method;
    private ModelParams modelParams;
    private Instr instr;
    private ResultHandler resultHandler;
    private PricingTask task;
    private NewTaskPanel newTaskPanel;
    private ArrayList<TaskObserver> observers = new ArrayList<>();
}
