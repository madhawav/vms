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
import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;
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
    private JButton btnSetActiveSpeechPresenter;
    private JButton setActiveButton;
    private JTextField txtTaskTitle;
    private JTextField txtTaskDescription;
    private JButton newTaskButton;
    private JTextField txtId;
    private JButton assignButton;
    private JButton clearAssignButton;
    private JButton deleteTaskButton;
    private JButton disconnectButton;
    private JButton adjournMeetingButton;
    private JButton btnStartListener;
    private JFrame mainFrame;


    FacilitatorController facilitatorController = null;

    private ScreenShow screenShow = null;
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
        setActiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSetAllActiveClicked();
            }
        });
        btnSetActiveSpeechPresenter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSetActiveSpeech();
            }
        });

        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDeleteTask();
            }
        });
        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAssignTask();
            }
        });
        clearAssignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClearAssign();
            }
        });
        newTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAddNewSharedTask();
            }


        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDisconnectClicked();
            }
        });
        adjournMeetingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    onAdjournClicked();
                } catch (ServerConnectionException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void onAdjournClicked() throws ServerConnectionException {
        facilitatorController.adjournMeeting();
    }

    private void onDisconnectClicked() {
        try {
            facilitatorController.getServerConnectionController().disconnect();
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        }
    }

    private void onDeleteTask() {
        try {
            facilitatorController.getSharedTaskManager().removeSharedTask(txtId.getText());
        } catch (SharedTaskNotFoundException e) {
            e.printStackTrace();
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
        }
    }

    private void onClearAssign() {
        try {
            facilitatorController.getSharedTaskManager().unAssignTaskFromPresenter(txtId.getText());
        } catch (SharedTaskNotFoundException e) {
            e.printStackTrace();
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
        }
    }

    private void onAssignTask() {
        try {
            facilitatorController.getSharedTaskManager().assignTaskToPresenter(txtId.getText(),facilitatorController.getFacilitatorId(),txtActivePresenterConsoleId.getText());
        } catch (SharedTaskNotFoundException e) {
            e.printStackTrace();
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
        }
    }

    private void onAddNewSharedTask() {
        try {
            txtId.setText(facilitatorController.getSharedTaskManager().createNewSharedTask(txtTaskTitle.getText(),txtTaskDescription.getText()));
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
        }
    }

    private void onSetActiveSpeech() {
        try {
            facilitatorController.setAudioRelayAccessPresenter(txtActivePresenterConsoleId.getText());
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        } catch (InvalidIdException e) {
            JOptionPane.showMessageDialog(mainFrame,"Invalid Presenter Console Id");
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame,"Server not ready");
        }
    }

    private void onSetAllActiveClicked() {
        try {
            facilitatorController.setScreenAccessPresenter(txtActivePresenterConsoleId.getText(),true);
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        } catch (InvalidIdException e) {
            JOptionPane.showMessageDialog(mainFrame,"Invalid Presenter Console Id");
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame,"Server not ready");
        }
    }

    private void onSetActiveClicked() {
        try {
            facilitatorController.setScreenAccessPresenter(txtActivePresenterConsoleId.getText(),false);
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        } catch (InvalidIdException e) {
            JOptionPane.showMessageDialog(mainFrame,"Invalid Presenter Console Id");
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame,"Server not ready");
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

            facilitatorController.getSharedTaskManager().addSharedTasksListener(new SharedTaskManager.SharedTasksListener() {
                @Override
                public void onInitiated() {
                    JOptionPane.showMessageDialog(mainFrame,"Shared Tasks initiated");
                }

                @Override
                public void onNewSharedTask(SharedTaskInfo newTask) {
                    JOptionPane.showMessageDialog(mainFrame,"New Shared Task: " + newTask.getTitle());
                }

                @Override
                public void onSharedTaskRemoved(SharedTaskInfo removedTask) {
                    JOptionPane.showMessageDialog(mainFrame,"Shared Task Removed: " + removedTask.getTitle());
                }

                @Override
                public void onSharedTaskModified(SharedTaskInfo oldTask, SharedTaskInfo newTask) {
                    JOptionPane.showMessageDialog(mainFrame,"Shared Task Modifier: " + oldTask.getTitle() + " >> " + newTask.getTitle() + " :: " + oldTask.getDescription() + " >> " + newTask.getDescription());
                }

                @Override
                public void onSharedTaskAssignmentChanged(SharedTaskInfo oldTask, SharedTaskInfo newTask) {
                    if(oldTask == null)
                    {
                        JOptionPane.showMessageDialog(mainFrame,"New Task " + newTask.getTitle()  + " assigned to " + newTask.getAssignedPresenterName());
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(mainFrame,"Task " + newTask.getTitle()  + " assigned to " + String.valueOf(newTask.getAssignedPresenterName()));
                    }
                }
            });

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
        } catch (MeetingAdjournedException e) {
            JOptionPane.showMessageDialog(mainFrame,"Error: Meeting Adjourned");
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

            @Override
            public void onDisconnected(String consoleId) {

            }
        });

        facilitatorController.addControlLoopListener(new ControlLoopListener() {
            @Override
            public void updateReceived(VirtualMeetingSnapshot vm) {
                vmUpdateReceived(vm);
            }

            @Override
            public void onMeetingAdjourned() throws ServerConnectionException {
                JOptionPane.showMessageDialog(mainFrame,"Meeting Adjourned");
            }
        });
        facilitatorController.addCaptureReceivedListener(new CaptureReceivedListener() {
            @Override
            public void onScreenCaptureReceived(byte[] rawData, BufferedImage image, String facilitatorConsoleId, String presenterConsoleId) {
                System.out.println("Screen Capture Received " + String.valueOf(rawData.length) + " bytes");
                screenShow.setImage(image);
            }

            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onAudioDataReceived(byte[] bytes, String activeScreenFacilitatorId, String activeScreenPresenterId) {
                System.out.println("Audio Data Received " + String.valueOf(bytes.length) + " bytes");
            }
        });

        facilitatorController.setShareRequestListener(new ShareRequestListener() {
            @Override
            public boolean onShareRequest(final AbstractShareRequest shareRequest) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if(shareRequest instanceof ScreenShareRequest)
                        {
                            if(JOptionPane.showConfirmDialog(mainFrame,shareRequest.getPresenterName() + " is requesting to share screen. Accept?","Request Screen",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                            {
                                try {
                                    shareRequest.honour();
                                } catch (RequestAlreadyProcessedException e) {
                                    e.printStackTrace();
                                } catch (ServerConnectionException e) {
                                    e.printStackTrace();
                                } catch (InvalidIdException e) {
                                    e.printStackTrace();
                                } catch (ServerNotReadyException e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(mainFrame,"Server not connected yet");
                                }
                            }
                            else
                            {
                                try {
                                    shareRequest.dismiss();
                                } catch (RequestAlreadyProcessedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if(shareRequest instanceof AudioRelayRequest)
                        {
                            if(JOptionPane.showConfirmDialog(mainFrame,shareRequest.getPresenterName() + " is requesting to share audio. Accept?","Request Screen",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                            {
                                try {
                                    shareRequest.honour();
                                } catch (RequestAlreadyProcessedException e) {
                                    e.printStackTrace();
                                } catch (ServerConnectionException e) {
                                    e.printStackTrace();
                                } catch (InvalidIdException e) {
                                    e.printStackTrace();
                                } catch (ServerNotReadyException e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(mainFrame,"Server not connected yet");
                                }
                            }
                            else
                            {
                                try {
                                    shareRequest.dismiss();
                                } catch (RequestAlreadyProcessedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if(shareRequest instanceof ScreenAudioShareRequest)
                        {
                            if(JOptionPane.showConfirmDialog(mainFrame,shareRequest.getPresenterName() + " is requesting to share screen and audio. Accept?","Request Screen",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                            {
                                try {
                                    shareRequest.honour();
                                } catch (RequestAlreadyProcessedException e) {
                                    e.printStackTrace();
                                } catch (ServerConnectionException e) {
                                    e.printStackTrace();
                                } catch (InvalidIdException e) {
                                    e.printStackTrace();
                                } catch (ServerNotReadyException e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(mainFrame,"Server not connected yet");
                                }
                            }
                            else
                            {
                                try {
                                    shareRequest.dismiss();
                                } catch (RequestAlreadyProcessedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                    }
                });
                return true;
            }
        });
        updatePairedList();

        screenShow = new ScreenShow();
        screenShow.run();

    }

    private void vmUpdateReceived(VirtualMeetingSnapshot vm) {
        lblVMStatus.setText(vm.getStatus().toString());
        VirtualMeetingParticipantInfo[] presenters = facilitatorController.getVMParticipants();
        String presenterString = "";
        for(VirtualMeetingParticipantInfo p : presenters)
        {
            String info = p.getName();
            info+= "<" + p.getPresenterId() + ">";
            if(p.getPresenterId().equals(vm.getActiveScreenPresenterId()))
            {
                info = info + "(Screen Active)";
            }
            if(p.getPresenterId().equals(vm.getActiveSpeechPresenterId()))
            {
                info = info + "(Speech Active)";
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
