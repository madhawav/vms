package eduze.vms.server.ui;

import eduze.vms.foundation.ui.CryptoUtil;
import eduze.vms.server.logic.PasswordUtil;
import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.PairListener;
import eduze.vms.server.logic.ServerController;
import eduze.vms.server.logic.URLGenerator;
import eduze.vms.server.logic.webservices.Facilitator;
import eduze.vms.server.logic.webservices.FacilitatorConsoleImpl;
import eduze.vms.server.logic.webservices.ServerNotReadyException;
import eduze.vms.server.logic.webservices.VirtualMeetingImpl;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.xml.ws.Endpoint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class ControlPanel extends JFrame {
    private JPanel jControlPanel;
    private JTextField txtPort;
    private JTextField txtServerName;
    private JPasswordField txtPassword;
    private JButton btnStart;
    private JButton resetButton;
    private JPanel pnlStart;
    private JTextPane txtStatus;
    private JTextField txtConfigFile;
    private JButton btnBrowseConfig;
    private JButton btnCreateConfig;
    private JFrame mainFrame;
    public ControlPanel() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        mainFrame = new JFrame("VMS Server");
        mainFrame.setContentPane(this.jControlPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setSize(800,600);
        mainFrame.setLocationRelativeTo(null);

        resetButton.setEnabled(false);
        btnStart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStartServerClicked();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controller != null)
                {
                    try {
                        controller.adjournMeeting();
                    } catch (ServerNotReadyException e1) {
                        JOptionPane.showMessageDialog(mainFrame,"Error: Server is not ready","Error",JOptionPane.OK_OPTION);
                        e1.printStackTrace();
                    }
                }
            }
        });
        btnCreateConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCreateConfigFile();
            }
        });
        btnBrowseConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadConfigFile();
            }
        });
    }
    ServerController controller = null;
    private void onStartServerClicked()
    {
        if(!validateInput())
            return;
        StorageManager storageManager = StorageManager.getInstance();

        File yourFile = new File(txtConfigFile.getText());
        boolean newMade = false;
        if(!yourFile.exists()) {
            try {
                yourFile.createNewFile();
                newMade = true;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame,"Unable to Create Config File","Error",JOptionPane.OK_OPTION);
                e.printStackTrace();
                return;
            }
        }
        storageManager.init(yourFile);
        storageManager.setEncryptionKey(new String(txtPassword.getPassword()));
        if(newMade)
        {
            try {
                storageManager.saveDefaultConfiguration(txtServerName.getText(),Integer.parseInt(txtPort.getText()));
            }  catch (StorageManager.StorageException e) {
                JOptionPane.showMessageDialog(mainFrame,"Storage Error","Error",JOptionPane.OK_OPTION);
                e.printStackTrace();
                return;
            } catch (CryptoUtil.CryptoException e) {
                JOptionPane.showMessageDialog(mainFrame,"Encryption Error","Error",JOptionPane.OK_OPTION);
                e.printStackTrace();
                return;
            }
        }

        try {
            //Load and Configure Storage Data
            StorageManager.DataNode dataNode = storageManager.readStorage();
            dataNode.getConfiguration().setPort(Integer.parseInt(txtPort.getText()));
            dataNode.getConfiguration().setName(txtServerName.getText());
            dataNode.getConfiguration().setPassword(new String(txtPassword.getPassword()));
            storageManager.updateStorage(dataNode);

            //Load the new window
        }catch (StorageManager.StorageException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame,"Unable to Read Storage File","Error",JOptionPane.OK_OPTION);
            return;
        } catch (CryptoUtil.CryptoException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame,"Encryption Error","Error",JOptionPane.OK_OPTION);
            return;
        }


        controller = new ServerController();
        try {
            controller.setName(txtServerName.getText());
            controller.setPort(Integer.valueOf(txtPort.getText()));
            controller.setPassword(PasswordUtil.hashPassword(txtPassword.getPassword()));


            controller.addPairListener(new PairListener() {
                @Override
                public void onPair(Facilitator pairedFacilitator) {
                    txtStatus.setText(txtStatus.getText()+"\n" + "Pair Added " + pairedFacilitator.getName() + " " + pairedFacilitator.getPairKey());
                    try {
                        StorageManager.getInstance().updatePairedFacilitators(controller.getPairedFacilitators());
                    } catch (CryptoUtil.CryptoException e) {
                        e.printStackTrace();
                        writeLog("Crypto Error in Saving paired device");
                    } catch (StorageManager.StorageException e) {
                        e.printStackTrace();
                        writeLog("Storage Error in Saving paired device");
                    }
                }

                @Override
                public void onUnPair(Facilitator pairedFacilitator) {
                    txtStatus.setText(txtStatus.getText()+"\n" + "Pair Removed " + pairedFacilitator.getName() + " " + pairedFacilitator.getPairKey());
                    try {
                        StorageManager.getInstance().updatePairedFacilitators(controller.getPairedFacilitators());
                    } catch (CryptoUtil.CryptoException e) {
                        e.printStackTrace();
                        writeLog("Crypto Error in Saving paired device");
                    } catch (StorageManager.StorageException e) {
                        e.printStackTrace();
                        writeLog("Storage Error in Saving paired device");
                    }
                }

                @Override
                public void onPairNameChanged(String pairKey, String oldName, String newName) {
                    txtStatus.setText(txtStatus.getText() + "\n" + "Device Name Changed " + oldName + " >> " + newName + " :: " + pairKey);
                    try {
                        StorageManager.getInstance().updatePairedFacilitators(controller.getPairedFacilitators());
                    } catch (CryptoUtil.CryptoException e) {
                        e.printStackTrace();
                        writeLog("Crypto Error in Saving paired device");
                    } catch (StorageManager.StorageException e) {
                        e.printStackTrace();
                        writeLog("Storage Error in Saving paired device");
                    }
                }
            });

            controller.addFacilitatorSessionListener(new FacilitatorSessionListener() {
                @Override
                public void onConnected(Facilitator facilitator, String consoleId) {
                    txtStatus.setText(txtStatus.getText()+"\n" + "Connected " + facilitator.getName() + " " + consoleId);
                }

                @Override
                public void onDisconnected(Facilitator facilitator, String consoleId) {
                    txtStatus.setText(txtStatus.getText()+"\n" + "Disconnected " + facilitator.getName() + " " + consoleId);
                }

                @Override
                public void onMeetingAdjourned() {
                    txtStatus.setText(txtStatus.getText()+"\n" + "Meeting Adjourned");

                }
            });
            controller.start();

            if(controller.isRunning())
            {
                pnlStart.setEnabled(false);
                txtServerName.setEnabled(false);
                txtPassword.setEnabled(false);
                txtPort.setEnabled(false);
                btnStart.setEnabled(false);
                btnBrowseConfig.setEnabled(false);
                btnCreateConfig.setEnabled(false);
                txtConfigFile.setEnabled(false);
                resetButton.setEnabled(true);
                writeLog("Server Started");


                loadPairedDevicesFromStorage();

            }
        }
        catch (StorageManager.StorageException e)
        {
            JOptionPane.showMessageDialog(mainFrame,"Error in Configuration File","Storage Error", JOptionPane.OK_OPTION);
            e.printStackTrace();
        }
        catch (CryptoUtil.CryptoException e)
        {
            JOptionPane.showMessageDialog(mainFrame,"Error in Configuration File","Crypto Error", JOptionPane.OK_OPTION);
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        if(!InputUtil.validatePort(txtPort.getText()))
        {
            JOptionPane.showMessageDialog(mainFrame,"The port should be an integer between 1025 and 65535.","Input Error", JOptionPane.OK_OPTION);
            return false;
        }
        if(!InputUtil.validateServerName(txtServerName.getText()))
        {
            JOptionPane.showMessageDialog(mainFrame,"The Server name is invalid. It should be a non-space separated alphanumeric string. (Underscore is allowed)","Input Error",JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private void loadPairedDevicesFromStorage() throws CryptoUtil.CryptoException, StorageManager.StorageException {
        StorageManager storageManager = StorageManager.getInstance();
        Collection<Facilitator> facilitators = storageManager.readStorage().getPairedFacilitators();
        for(Facilitator facilitator : facilitators)
        {
            controller.addPairedFacilitator(facilitator.getName(),facilitator.getPairKey());
        }
    }

    public void run()
    {

        mainFrame.setVisible(true);

    }
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        ControlPanel cp = new ControlPanel();
        cp.run();

        /*VirtualMeetingImpl virtualMeeting = new VirtualMeetingImpl();
        String url = URLGenerator.generateVMPublishURL(8000,"12");
        System.out.println(url);
        Endpoint.publish(url,virtualMeeting);

        FacilitatorConsoleImpl consolePanel = new FacilitatorConsoleImpl();
        url = URLGenerator.generateFacilitatorConsolePublishURL(8000,"13");
        System.out.println(url);
        Endpoint.publish(url,consolePanel);*/
    }

    public void writeLog(String message)
    {
        txtStatus.setText(txtStatus.getText() + "\n" + message);
    }

    private FileFilter getFileFilter()
    {
        return  new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.getAbsolutePath().endsWith(".bin"))
                    return true;
                else return false;
            }

            @Override
            public String getDescription() {
                return "Binary File";
            }};
    }

    private void onCreateConfigFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(getFileFilter());
        int returnVal = fileChooser.showSaveDialog(mainFrame);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            txtConfigFile.setText(fileChooser.getSelectedFile().getAbsolutePath());

        }
    }

    private void onLoadConfigFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(getFileFilter());
        int returnVal = fileChooser.showOpenDialog(mainFrame);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            txtConfigFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
            StorageManager storageManager =  StorageManager.getInstance();
            File yourFile = new File(txtConfigFile.getText());
            storageManager.init(yourFile);
            storageManager.setEncryptionKey(new String(txtPassword.getPassword()));

            try {
                StorageManager.DataNode dataNode =  storageManager.readStorage();
                txtServerName.setText(dataNode.getConfiguration().getName());
                txtPort.setText(String.valueOf(dataNode.getConfiguration().getPort()));

            } catch (StorageManager.StorageException e) {
                e.printStackTrace();
            } catch (CryptoUtil.CryptoException e) {
                e.printStackTrace();
            }

        }

    }

}
