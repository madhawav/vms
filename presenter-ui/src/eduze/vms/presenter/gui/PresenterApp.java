package eduze.vms.presenter.gui;

import eduze.vms.presenter.logic.PresenterController;
import org.apache.log4j.BasicConfigurator;

import javax.swing.*;

/**
 * Created by Admin on 6/10/2016.
 */
public class PresenterApp {
    private static PresenterApp instance = null;

    private PresenterController controller = null;

    public static PresenterApp getInstance()
    {
        if(instance == null)
        {
            instance = new PresenterApp();
        }
        return instance;
    }
    private PresenterApp()
    {

    }

    public PresenterController getController() {
        return controller;
    }

    public void setController(PresenterController controller) {
        this.controller = controller;
    }

    public void run(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

        BasicConfigurator.configure();

        ConnectionWindow dialog = new ConnectionWindow();
        dialog.setVisible(true);
        if(getController() == null)
        {
            return;
        }
        else
        {

        }
    }
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        PresenterApp presenterApp = PresenterApp.getInstance();
        presenterApp.run(args);

    }
}
