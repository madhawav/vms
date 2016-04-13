package eduze.vms.facilitator.ui;

import com.sun.deploy.panel.ControlPanel;
import eduze.vms.facilitator.logic.ConnectionListener;
import eduze.vms.facilitator.logic.ConnectionRequest;
import eduze.vms.facilitator.logic.FacilitatorController;
import eduze.vms.facilitator.logic.PasswordUtil;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsole;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsoleImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.AlreadyPairedException;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManager;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.server.Server;
import eduze.vms.facilitator.logic.mpi.server.ServerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.VMSessionManager;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.VMSessionManagerImplServiceLocator;

import javax.swing.*;
import javax.xml.rpc.ServiceException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.OpenType;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class ConnectorPanel {
    private JPanel mainPanel;
    private JTextField txtFacilitatorName;
    private JPasswordField txtPassKey;
    private JTextField txtPort;
    private JButton startButton1;
    private JButton btnStartListener;
    private JFrame mainFrame;
    FacilitatorController facilitatorController = null;
    public ConnectorPanel()
    {

        mainFrame = new JFrame("VMS Facilitator Control Panel");
        mainFrame.setContentPane(this.mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // mainFrame.pack();
        mainFrame.setSize(800,600);


        startButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStartListener();
            }
        });
    }

    private void onStartListener() {
       FacilitatorController.Configuration configuration = new FacilitatorController.Configuration();
       configuration.setName(txtFacilitatorName.getText());
       configuration.setPassword(PasswordUtil.hashPasskey(txtPassKey.getPassword()));
       configuration.setListenerPort(Integer.valueOf(txtPort.getText()));
       facilitatorController = FacilitatorController.start(configuration);
        facilitatorController.addConnectionListener(new ConnectionListener() {
            @Override
            public void onConnectionRequested(ConnectionRequest connectionRequest) {
                int result = JOptionPane.showConfirmDialog(mainFrame,"Connection Request from " + connectionRequest.getPresenterName() + ". Accept?","Connection Request", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION)
                {
                    connectionRequest.accept();
                }
                else if(result == JOptionPane.NO_OPTION)
                {
                    connectionRequest.reject();
                }
            }
        });
    }

    public void run()
    {
        mainFrame.setVisible(true);


    }



    public static void main(String[] args) {
        ConnectorPanel con = new ConnectorPanel();
        con.run();

    }

    private void testSequence()
    {
        eduze.vms.facilitator.logic.mpi.server.ServerImplServiceLocator locator = new ServerImplServiceLocator();
        eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplServiceLocator loc = new FacilitatorManagerImplServiceLocator();
        eduze.vms.facilitator.logic.mpi.vmsessionmanager.VMSessionManagerImplServiceLocator vmsloc = new VMSessionManagerImplServiceLocator();

        eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsoleImplServiceLocator faciloc = new FacilitatorConsoleImplServiceLocator();

        try {
            Server serve =  locator.getServerImplPort(new URL("http://localhost:8000/server?wsdl"));
            FacilitatorManager facim = loc.getFacilitatorManagerImplPort(new URL("http://localhost:8000/facilitator-manager?wsdl"));
            VMSessionManager vmSessionManager = vmsloc.getVMSessionManagerImplPort(new URL("http://localhost:8000/vm-session-manager?wsdl"));

            mainFrame.setTitle(serve.getServerName());


            try{
                String pid1 = facim.pair("Madhawa","qwerty");
                System.out.println("PID1 " + pid1);

                String pid2 = facim.pair("Razer","qwerty");
                System.out.println("PID2 " + pid2);

                ConnectionResult cr1=vmSessionManager.connect("Madhawa2",pid1);
                System.out.println("Console1 " + cr1.getFacilitatorConsoleId());
                ConnectionResult cr2=vmSessionManager.connect("Razer2",pid2);
                System.out.println("Console2 " + cr2.getFacilitatorConsoleId());
                System.out.println("VMid" + cr1.getVirtualMeetingConsoleId());

                FacilitatorConsole facicon1 = faciloc.getFacilitatorConsoleImplPort(new URL("http://localhost:8000/facilitator/"+cr1.getFacilitatorConsoleId()+"?wsdl"));
                facicon1.disconnect();
            }
            catch (AlreadyPairedException e)
            {
                System.out.println("Already paired");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
