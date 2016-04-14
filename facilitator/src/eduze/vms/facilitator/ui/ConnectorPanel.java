package eduze.vms.facilitator.ui;

import eduze.vms.facilitator.logic.*;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsole;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsoleImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.AlreadyPairedException;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManager;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.InvalidServerPasswordException;
import eduze.vms.facilitator.logic.mpi.server.Server;
import eduze.vms.facilitator.logic.mpi.server.ServerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.*;

import javax.swing.*;
import javax.xml.rpc.ServiceException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
    private JPanel facilitatorServicePanel;
    private JPanel serverConnectionPanel;
    private JTextField txtServerHost;
    private JPasswordField txtServerPassword;
    private JButton pairButton;
    private JComboBox cmbPairedDevices;
    private JButton unpairButton;
    private JButton connectButton;
    private JLabel lblVMStatus;
    private JLabel lblPresenters;
    private JTextField txtActivePresenterConsoleId;
    private JButton btnSetActivePresenter;
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
        pairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPairClicked();
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onConnectClicked();
            }
        });

        unpairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onUnPairClicked();
            }
        });
        btnSetActivePresenter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSetActiveClicked();
            }
        });
    }

    private void onSetActiveClicked() {
        try {
            facilitatorController.setScreenAccessPresenter(txtActivePresenterConsoleId.getText(),false);
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        }
    }

    private void onUnPairClicked() {
        String name = (String)cmbPairedDevices.getSelectedItem();
        try {
            ServerManager.PairedServer server= facilitatorController.getServerManager().getPairedServerFromName(name);
            facilitatorController.getServerManager().unPair(server);
            JOptionPane.showMessageDialog(mainFrame,"Successfully Unpaired");
            updatePairedList();
        }
        catch (ServiceNotStartedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServerConnectionException e) {
            JOptionPane.showMessageDialog(mainFrame,"Server Connection Error");
            e.printStackTrace();
        }
    }

    private void onConnectClicked() {
        String name = (String)cmbPairedDevices.getSelectedItem();
        try {
            ServerManager.PairedServer server= facilitatorController.getServerManager().getPairedServerFromName(name);
            facilitatorController.getServerManager().connect(server);
            JOptionPane.showMessageDialog(mainFrame,"Successfully Connected");
        }
        catch (FacilitatorAlreadyConnectedException e)
        {
            JOptionPane.showMessageDialog(mainFrame,"Already Connected");
            e.printStackTrace();
        }
        catch (MeetingAlreadyStartedException e)
        {
            JOptionPane.showMessageDialog(mainFrame,"Meeting Already Started");
            e.printStackTrace();;
        }
        catch (UnknownFacilitatorException e)
        {
            JOptionPane.showMessageDialog(mainFrame,"Facilitator is not known. Please pair before connecting.");
            e.printStackTrace();;
        }
        catch (ServerNotReadyException e) {
            JOptionPane.showMessageDialog(mainFrame,"Server busy.");
            e.printStackTrace();
        }
        catch (ServiceNotStartedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServerConnectionException e) {
            JOptionPane.showMessageDialog(mainFrame,"Server Connection Error");
            e.printStackTrace();
        }
    }

    private void updatePairedList()
    {
        cmbPairedDevices.removeAllItems();
        if(facilitatorController.isRunning())
        {
            try {
                for(ServerManager.PairedServer p : facilitatorController.getServerManager().getPairedServers())
                {
                    cmbPairedDevices.addItem(p.getServerName());
                }
            } catch (ServiceNotStartedException e) {
                e.printStackTrace();
            }
        }
    }
    private void onPairClicked() {
        try {
            ServerManager serverManager = facilitatorController.getServerManager();
            ServerManager.PairedServer pairedServer = serverManager.pair(txtServerHost.getText(),PasswordUtil.hashServerPassword(txtServerPassword.getPassword()));
            JOptionPane.showMessageDialog(mainFrame,"Successfully Paired");

        } catch (ServiceNotStartedException e) {
            e.printStackTrace();
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        } catch (InvalidServerPasswordException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame,"Invalid Server Password");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyPairedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame,"Already Paired");
        }
        updatePairedList();
    }

    private void onStartListener() {
       FacilitatorController.Configuration configuration = new FacilitatorController.Configuration();
       configuration.setName(txtFacilitatorName.getText());
       configuration.setPassword(PasswordUtil.hashPasskey(txtPassKey.getPassword()));
       configuration.setListenerPort(Integer.valueOf(txtPort.getText()));
       facilitatorController = FacilitatorController.start(configuration);
        facilitatorController.addConnectionListener(new PresenterConnectionListener() {
            @Override
            public void onConnectionRequested(ConnectionRequest connectionRequest) {
                int result = JOptionPane.showConfirmDialog(mainFrame,"Connection Request from " + connectionRequest.getPresenterName() + ". Accept?","Connection Request", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION)
                {
                    try {
                        connectionRequest.accept();
                    } catch (RequestAlreadyProcessedException e) {
                        e.printStackTrace();
                    }
                }
                else if(result == JOptionPane.NO_OPTION)
                {
                    try {
                        connectionRequest.reject();
                    } catch (RequestAlreadyProcessedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onConnected(String connectionRequestId, String consoleId) {

            }
        });

        facilitatorController.addControlLoopListener(new ControlLoopListener() {
            @Override
            public void updateReceived(VirtualMeetingSnapshot vm) {
                vmUpdateReceived(vm);
            }
        });
        facilitatorController.addCaptureReceivedListener(new CaptureReceivedListener() {
            @Override
            public void onScreenCaptureReceived(byte[] rawData, BufferedImage image, String facilitatorConsoleId, String presenterConsoleId) {
                System.out.println("Screen Capture Received " + String.valueOf(rawData) + " bytes");
            }

            @Override
            public void onException(Exception e) {

            }
        });
        updatePairedList();
    }

    private void vmUpdateReceived(VirtualMeetingSnapshot vm) {
        lblVMStatus.setText(vm.getStatus().toString());
        Presenter[] presenters = facilitatorController.getPresenters();
        String presenterString = "";
        for(Presenter p : presenters)
        {
            String info = p.getPresenterName();
            info+= "<" + p.getPresenterConsoleId() + ">";
            if(p.isScreenActive())
            {
                info = info + "(Active)";
            }
            info = info + ", ";
            presenterString += info;
        }
        lblPresenters.setText(presenterString);
    }

    public void run()
    {
        mainFrame.setVisible(true);


    }



    public static void main(String[] args) {
        ConnectorPanel con = new ConnectorPanel();
        //con.testSequence();
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
