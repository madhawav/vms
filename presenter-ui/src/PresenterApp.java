import eduze.vms.presenter.logic.PresenterController;

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

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        PresenterApp presenterApp = PresenterApp.getInstance();
        ConnectionWindow dialog = new ConnectionWindow();
        dialog.setVisible(true);
        if(getInstance().getController() == null)
        {
            return;
        }
        else
        {
            PresenterWindow presenterWindow = new PresenterWindow();
            presenterWindow.run();
        }
    }
}
