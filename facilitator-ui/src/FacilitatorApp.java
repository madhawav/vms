import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;

/**
 * Created by Admin on 6/14/2016.
 */
public class FacilitatorApp {
    public FacilitatorApp(String[] args)
    {

    }
    public void run() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        //PropertyConfigurator.configure("log4j.properties");
       // BasicConfigurator.configure();

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        FacilitatorConnection dialog = new FacilitatorConnection();
        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        FacilitatorApp facilitatorApp = new FacilitatorApp(null);
        facilitatorApp.run();
    }
}
