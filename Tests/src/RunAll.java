import eduze.vms.server.ui.ServerApp;

import javax.swing.*;

/**
 * Created by Admin on 6/14/2016.
 */
public class RunAll {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        ServerApp serverApp = new ServerApp(null);
        FacilitatorApp facilitatorApp = new FacilitatorApp(null);

        serverApp.run();
        facilitatorApp.run();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PresenterApp presenterApp = PresenterApp.getInstance();
                try {
                    presenterApp.run(null);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
