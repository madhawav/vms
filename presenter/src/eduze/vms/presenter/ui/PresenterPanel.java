package eduze.vms.presenter.ui;

import eduze.vms.presenter.logic.mpi.facilitator.Facilitator;
import eduze.vms.presenter.logic.mpi.facilitator.FacilitatorImplServiceLocator;

import javax.swing.*;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by Madhawa on 13/04/2016.
 */
public class PresenterPanel {

    private JPanel mainPanel;
    private JFrame mainFrame;
    public PresenterPanel()  {
        mainFrame = new JFrame("VMS Presenter Panel");
        mainFrame.setContentPane(this.mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // mainFrame.pack();
        mainFrame.setSize(800,600);

        FacilitatorImplServiceLocator locator = new FacilitatorImplServiceLocator();
        try {
            Facilitator facilitator =  locator.getFacilitatorImplPort(new URL("http://localhost:7000/facilitator"));
            System.out.println("Facilitator Name " + facilitator.getFacilitatorName());

            String result = facilitator.requestConnection("My Presenter", "password");
            System.out.println("Connection Request Id " + result);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    public void run()
    {
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        PresenterPanel panel = new PresenterPanel();
        panel.run();
    }
}
