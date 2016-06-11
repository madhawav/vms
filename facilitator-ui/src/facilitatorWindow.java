import eduze.vms.facilitator.logic.*;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.*;
import eduze.vms.foundation.ui.CryptoUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by Admin on 5/21/2016.
 */
public class FacilitatorWindow {
    private JTabbedPane tabHolder;
    private JPanel mainPanel;
    private JComboBox comboServerList;
    private JButton connectButton;
    private JButton btnPair;
    private JLabel lblConnectionStatus;
    private JPanel pnlConnectionRequests;
    private JPanel tabConnection;
    private JPanel tabVM;
    private JButton btnDisconnect;
    private JLabel lblMeetingStatus;
    private JLabel lblActiveScreen;
    private JButton btnChangeActiveScreen;
    private JButton btnChangeActiveVoice;
    private JLabel lblActiveVoice;
    private JPanel pnlShareRequests;
    private JButton deleteButton;
    private JButton addTaskButton;
    private JPanel pnlSharedTasksList;
    private JLabel lblServerConnectionCompleted;
    private JLabel lblServerConnectionNotCompleted;
    private JLabel lblPresenterConnectionsAcceptable;
    private JLabel lblPresenterConnectionsNotDone;
    private JButton btnFinishMeeting;
    private JPanel pnlPreview;
    private JLabel lblPreview;
    private JButton btnUnpair;

    private ScreenFrame frmScreenFrame = null;

    private HashMap<String, ConnectionStatusForm> connectionStatusFormMap = new HashMap<>();

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {


       // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        FacilitatorWindow window = new FacilitatorWindow();
        window.run();

    }

    private FacilitatorController controller = null;

    private HashMap<String,ServerManager.PairedServer> pairedServerHashMap = new HashMap<>();
    private void refreshServerList() throws ServiceNotStartedException {
        comboServerList.removeAllItems();
        pairedServerHashMap.clear();

        for (ServerManager.PairedServer server : controller.getServerManager().getPairedServers())
        {
            comboServerList.addItem(String.valueOf(server.getServerName() + " (" + server.getServerURL() + ")"));
            pairedServerHashMap.put(String.valueOf(server.getServerName() + " (" + server.getServerURL() + ")"),server);
        }

    }
    private void init() {
        btnDisconnect.setVisible(false);
        lblServerConnectionCompleted.setVisible(false);
        lblPresenterConnectionsAcceptable.setVisible(false);
        try {
            StorageManager.DataNode dataNode = StorageManager.getInstance().readStorage();
            controller = FacilitatorController.start(dataNode.getConfiguration());

            frmScreenFrame = new ScreenFrame(controller,facilitatorWindowFrame);

            //Load paired devices
            for (ServerManager.PairedServer server:dataNode.getPairedServers()) {
                controller.getServerManager().addPairedServer(server.getServerName(),server.getServerURL(),server.getServerPairKey());
            }
            refreshServerList();

            controller.setShareRequestListener(new ShareRequestListener() {
                @Override
                public boolean onShareRequest(AbstractShareRequest shareRequest) {

                    ShareRequestForm shareRequestForm = new ShareRequestForm(shareRequest);
                    JPanel pnl = shareRequestForm.getPnlContainer();
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.weightx = 1;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.gridy = pnlShareRequests.getComponentCount();
                    pnlShareRequests.add(pnl,constraints);
                    pnl.setVisible(true);
                    return true;

                }
            });

            controller.addControlLoopListener(new ControlLoopListener() {
                @Override
                public void updateReceived(VirtualMeetingSnapshot vm) {
                    lblMeetingStatus.setText(vm.getStatus().toString());
                    if (vm.getActiveScreenPresenterId() == null || vm.getActiveScreenPresenterId().equals(""))
                    {
                        lblActiveScreen.setText("Not Set");
                        setPreviewImage(null,null,null);
                        frmScreenFrame.setImage(null,null,null);
                    }
                    else
                    {
                        VirtualMeetingParticipantInfo activeScreenPresenter = controller.getVMParticipant(vm.getActiveScreenPresenterId());
                        if(activeScreenPresenter == null)
                        {
                            lblActiveScreen.setText("<" + vm.getActiveScreenPresenterId() + ">");
                        }
                        else
                        {
                            lblActiveScreen.setText(activeScreenPresenter.getName());
                        }
                    }

                    if (vm.getActiveSpeechPresenterId() == null || vm.getActiveSpeechPresenterId().equals(""))
                    {
                        lblActiveVoice.setText("Not Set");
                    }
                    else
                    {
                        VirtualMeetingParticipantInfo activeSpeechPresenter = controller.getVMParticipant(vm.getActiveSpeechPresenterId());
                        if(activeSpeechPresenter == null)
                        {
                            lblActiveVoice.setText("<" + vm.getActiveSpeechPresenterId() + ">");
                        }
                        else
                        {
                            lblActiveVoice.setText(activeSpeechPresenter.getName());
                        }
                    }


                }

                @Override
                public void onMeetingAdjourned() throws ServerConnectionException {
                    JOptionPane.showMessageDialog(mainPanel,"The meeting has been adjourned.","Meeting Adjourned",JOptionPane.OK_OPTION);
                    System.exit(0);
                }
            });

            controller.addCaptureReceivedListener(new CaptureReceivedListener() {
                @Override
                public void onScreenCaptureReceived(byte[] rawData, BufferedImage image, String facilitatorConsoleId, String presenterConsoleId) {
                    if(frmScreenFrame != null)
                    {
                        frmScreenFrame.setImage(image,facilitatorConsoleId,presenterConsoleId);
                        setPreviewImage(image,facilitatorConsoleId,presenterConsoleId);
                    }
                }

                @Override
                public void onException(Exception e) {

                }

                @Override
                public void onAudioDataReceived(byte[] rawData, String facilitatorConsoleId, String presenterConsoleId) {

                }
            });

            controller.addConnectionListener(new PresenterConnectionListener() {
                @Override
                public void onConnectionRequested(ConnectionRequest connectionRequest) {
                    ConnectionRequestForm connectionRequestForm = new ConnectionRequestForm(connectionRequest);
                    JPanel pnl = connectionRequestForm.getPnlContainer();
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.weightx = 1;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.gridy = pnlConnectionRequests.getComponentCount();
                    pnlConnectionRequests.add(pnl,constraints);

                }

                @Override
                public void onConnected(String connectionRequestId, String consoleId) {
                    ConnectionStatusForm connectionStatusForm = new ConnectionStatusForm(controller, connectionRequestId, consoleId);
                    JPanel pnl = connectionStatusForm.getPnlContainer();
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.weightx = 1;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.gridy = pnlConnectionRequests.getComponentCount();
                    pnlConnectionRequests.add(pnl,constraints);
                    connectionStatusFormMap.put(consoleId,connectionStatusForm);

                    lblPresenterConnectionsAcceptable.setVisible(true);
                    lblPresenterConnectionsNotDone.setVisible(false);
                }

                @Override
                public void onDisconnected(String consoleId) {
                    ConnectionStatusForm form = connectionStatusFormMap.get(consoleId);
                    form.getPnlContainer().getParent().remove(form.getPnlContainer());

                    if(controller.getPresenterCount() == 0)
                    {
                        lblPresenterConnectionsAcceptable.setVisible(false);
                        lblPresenterConnectionsNotDone.setVisible(true);
                    }
                }
            });

        } catch (StorageManager.StorageException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Storage Error","Error",JOptionPane.OK_OPTION);
        } catch (CryptoUtil.CryptoException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Crypto Error","Error",JOptionPane.OK_OPTION);
        } catch (ServiceNotStartedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"System Error","Error",JOptionPane.OK_OPTION);
        }



    }

    private void setupSharedTasksSystem()
    {
        try {
            sharedTaskManager = controller.getSharedTaskManager();
        } catch (ServerNotReadyException e) {
            JOptionPane.showMessageDialog(mainPanel,"Server not ready","Error",JOptionPane.OK_OPTION);
            e.printStackTrace();
        }

        sharedTaskManager.addSharedTasksListener(new SharedTaskManager.SharedTasksListener() {
            @Override
            public void onInitiated() {
                SharedTaskInfo[] sharedTasks = sharedTaskManager.getSharedTasks();
                pnlSharedTasksList.removeAll();
                for(SharedTaskInfo sharedTask : sharedTasks)
                {
                    SharedTaskForm sharedTaskForm = new SharedTaskForm(controller, sharedTask);
                    JPanel pnl = sharedTaskForm.getPnlContainer();
                    GridBagConstraints constraints = new GridBagConstraints();
                    constraints.weightx = 1;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.gridy = pnlSharedTasksList.getComponentCount();
                    pnlSharedTasksList.add(pnl,constraints);
                    pnl.setVisible(true);

                }
            }

            @Override
            public void onNewSharedTask(SharedTaskInfo newTask) {
                SharedTaskForm sharedTaskForm = new SharedTaskForm(controller, newTask);
                JPanel pnl = sharedTaskForm.getPnlContainer();
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.weightx = 1;
                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.gridy = pnlSharedTasksList.getComponentCount();
                pnlSharedTasksList.add(pnl,constraints);
                pnl.setVisible(true);
            }

            @Override
            public void onSharedTaskRemoved(SharedTaskInfo removedTask) {

            }

            @Override
            public void onSharedTaskModified(SharedTaskInfo oldTask, SharedTaskInfo newTask) {

            }

            @Override
            public void onSharedTaskAssignmentChanged(SharedTaskInfo oldTask, SharedTaskInfo newTask) {

            }
        });
    }
    private SharedTaskManager sharedTaskManager = null;
    public FacilitatorWindow()
    {
        init();
        btnPair.addActionListener(new ActionListener() {
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
        btnDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDisconnectClicked();
            }
        });
        btnChangeActiveScreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onScreenSwitchClicked();
            }
        });
        btnChangeActiveVoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAudioSwitchClicked();
            }
        });
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAddNewTask();
            }
        });
        btnFinishMeeting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFinishMeeting();
            }
        });
        btnUnpair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onUnpairClicked();
            }
        });
    }



    private void onFinishMeeting() {
        if(controller.isServerConnected())
        {
            try {
                controller.adjournMeeting();
                tabHolder.setEnabledAt(tabHolder.indexOfComponent(tabVM),false);
                tabHolder.setSelectedIndex(0);
            } catch (ServerConnectionException e) {
                JOptionPane.showMessageDialog(mainPanel,"Server Connection Error. Please try again in a while.","Error",JOptionPane.OK_OPTION);
                e.printStackTrace();
            }
        }
    }

    public void setPreviewImage(Image image, String facilitatorConsoleId, String presenterConsoleId)
    {
        if(image != null)
        {
            Image scaledImage = image.getScaledInstance(pnlPreview.getWidth(),pnlPreview.getHeight(),Image.SCALE_SMOOTH);
            lblPreview.setText("");
            lblPreview.setIcon(new ImageIcon(scaledImage));
        }
        else
        {
            lblPreview.setIcon(null);
            lblPreview.setText("No Preview");
        }


    }


    private void onAddNewTask() {
        NewSharedTask dlg = null;
        try {
            dlg = new NewSharedTask(controller.getSharedTaskManager());
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
        }
        dlg.setVisible(true);
    }

    private void onAudioSwitchClicked() {
        JPopupMenu switchMenu = getAudioSwitchPopup();
        switchMenu.show(btnChangeActiveScreen,0,btnChangeActiveScreen.getHeight());
    }

    private JPopupMenu getAudioSwitchPopup() {
        JPopupMenu menu = new JPopupMenu();
        for(Presenter presenter : controller.getPresenters())
        {
            final Presenter presenter1 = presenter;
            JMenuItem item = new JMenuItem(presenter.getPresenterName());
            item.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        presenter1.setSpeechActive();
                    } catch (ServerConnectionException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(mainPanel,"Server Connection Error","Error",JOptionPane.OK_OPTION);
                    } catch (InvalidIdException e1) {
                        e1.printStackTrace();
                    } catch (ServerNotReadyException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(mainPanel,"Server is not ready","Error",JOptionPane.OK_OPTION);
                    }
                }
            });
            menu.add(item);
        }
        JMenuItem clearItem = new JMenuItem("Not Set");
        clearItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.setAudioRelayAccessPresenter(null);
                } catch (ServerConnectionException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel,"Server Connection Error","Error",JOptionPane.OK_OPTION);
                } catch (InvalidIdException e1) {
                    e1.printStackTrace();
                } catch (ServerNotReadyException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel,"Server is not ready","Error",JOptionPane.OK_OPTION);
                }
            }
        });
        menu.add(clearItem);
        return menu;
    }

    private JPopupMenu getScreenSwitchPopup()
    {
        JPopupMenu menu = new JPopupMenu();
        for(Presenter presenter : controller.getPresenters())
        {
            final Presenter presenter1 = presenter;
            JMenuItem item = new JMenuItem(presenter.getPresenterName());
            item.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        presenter1.setScreenActive(false);
                    } catch (ServerConnectionException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(mainPanel,"Server Connection Error","Error",JOptionPane.OK_OPTION);
                    } catch (InvalidIdException e1) {
                        e1.printStackTrace();
                    } catch (ServerNotReadyException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(mainPanel,"Server is not ready","Error",JOptionPane.OK_OPTION);
                    }
                }
            });
            menu.add(item);
        }
        JMenuItem clearItem = new JMenuItem("Not Set");
        clearItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.setScreenAccessPresenter(null,false);
                } catch (ServerConnectionException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel,"Server Connection Error","Error",JOptionPane.OK_OPTION);
                } catch (InvalidIdException e1) {
                    e1.printStackTrace();
                } catch (ServerNotReadyException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(mainPanel,"Server is not ready","Error",JOptionPane.OK_OPTION);
                }
            }
        });
        menu.add(clearItem);
        return menu;
    }
    private void onScreenSwitchClicked() {
        JPopupMenu switchMenu = getScreenSwitchPopup();
        switchMenu.show(btnChangeActiveScreen,0,btnChangeActiveScreen.getHeight());
    }

    private void onDisconnectClicked() {
        try {
            controller.getServerConnectionController().disconnect();
            System.exit(0);
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        }
    }

    private void onConnectClicked() {
        if(comboServerList.getSelectedItem() == null)
            return;
        ServerManager.PairedServer server = pairedServerHashMap.get(comboServerList.getSelectedItem());
        if(server == null)
        {
            JOptionPane.showMessageDialog(mainPanel,"Please Select a Server in order to connect.","No Server",JOptionPane.OK_OPTION);
            return;
        }
        try {
            controller.getServerManager().connect(server);

            lblConnectionStatus.setText("Connected");
            connectButton.setVisible(false);
            btnPair.setEnabled(false);
            btnUnpair.setEnabled(false);
            btnDisconnect.setVisible(true);
            tabHolder.setEnabledAt(tabHolder.indexOfComponent(tabVM),true);
            frmScreenFrame.show();
            lblServerConnectionCompleted.setVisible(true);
            lblServerConnectionNotCompleted.setVisible(false);
            setupSharedTasksSystem();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServerConnectionException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Error in Server Connection","Error",JOptionPane.OK_OPTION);
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Server is not ready","Error",JOptionPane.OK_OPTION);
        } catch (MeetingAlreadyStartedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Meeting has started already","Error",JOptionPane.OK_OPTION);
        } catch (UnknownFacilitatorException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Facilitator not known by server","Error",JOptionPane.OK_OPTION);
        } catch (FacilitatorAlreadyConnectedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Already Connected","Error",JOptionPane.OK_OPTION);
        } catch (MeetingAdjournedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Meeting is adjourned","Error",JOptionPane.OK_OPTION);
        } catch (ServiceNotStartedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Service has not started","Error",JOptionPane.OK_OPTION);
        }
    }
    private void onUnpairClicked() {
        ServerManager.PairedServer pairedServer = pairedServerHashMap.get(comboServerList.getSelectedItem());
        if(pairedServer == null)
        {
            JOptionPane.showMessageDialog(mainPanel,"Please Select a Server in order to un-pair.","No Server",JOptionPane.OK_OPTION);
            return;
        }
        try {
            controller.getServerManager().unPair(pairedServer);
        } catch (ServerConnectionException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ServiceNotStartedException e) {
            e.printStackTrace();
        }
        try {
            refreshServerList();
        } catch (ServiceNotStartedException e) {
            e.printStackTrace();
        }
    }

    private void onPairClicked() {
        PairServer pairServer = null;
        try {
            pairServer = new PairServer(controller.getServerManager());
        } catch (ServiceNotStartedException e) {
            e.printStackTrace();
        }
        pairServer.setVisible(true);
        try {
            StorageManager.getInstance().updatePairedServers(controller.getServerManager().getPairedServers());
        } catch (CryptoUtil.CryptoException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Crypto Error","Error",JOptionPane.OK_OPTION);
        } catch (StorageManager.StorageException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,"Storage Error","Error",JOptionPane.OK_OPTION);
        } catch (ServiceNotStartedException e) {
            e.printStackTrace();
        }
        try {

            refreshServerList();
        } catch (ServiceNotStartedException e) {
            e.printStackTrace();
        }
    }

    JFrame facilitatorWindowFrame = new JFrame();

    protected void run() {

        facilitatorWindowFrame.setTitle("Facilitator Control Panel");
        facilitatorWindowFrame.setContentPane(this.mainPanel);

        facilitatorWindowFrame.setSize(1024,768);
       // facilitatorWindowFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        facilitatorWindowFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        facilitatorWindowFrame.pack();
        facilitatorWindowFrame.setLocationRelativeTo(null);
        facilitatorWindowFrame.setVisible(true);

        facilitatorWindowFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    controller.finish();
                } catch (ServerConnectionException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
