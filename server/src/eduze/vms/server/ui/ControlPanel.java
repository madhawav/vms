package eduze.vms.server.ui;

import eduze.vms.PasswordUtil;
import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.PairListener;
import eduze.vms.server.logic.ServerController;
import eduze.vms.server.logic.URLGenerator;
import eduze.vms.server.logic.webservices.Facilitator;
import eduze.vms.server.logic.webservices.FacilitatorConsoleImpl;
import eduze.vms.server.logic.webservices.VirtualMeeting;
import eduze.vms.server.logic.webservices.VirtualMeetingImpl;

import javax.swing.*;
import javax.xml.ws.Endpoint;
import java.awt.event.ActionEvent;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class ControlPanel extends JFrame {
    private JPanel jControlPanel;
    private JTextField txtPort;
    private JTextField txtServerName;
    private JPasswordField txtPassword;
    private JButton btnStart;
    private JButton exitButton;
    private JPanel pnlStart;
    private JTextPane txtStatus;
    private JFrame mainFrame;
    public ControlPanel()
    {
        mainFrame = new JFrame("VMS ServerController");
        mainFrame.setContentPane(this.jControlPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setSize(800,600);

        btnStart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStartServerClicked();
            }
        });

    }

    private void onStartServerClicked()
    {
        ServerController controller = new ServerController();
        try {
            controller.setName(txtServerName.getText());
            controller.setPort(Integer.valueOf(txtPort.getText()));
            controller.setPassword(PasswordUtil.hashPassword(txtPassword.getPassword()));
            controller.addPairListener(new PairListener() {
                @Override
                public void onPair(Facilitator pairedFacilitator) {
                    txtStatus.setText(txtStatus.getText()+"\n" + "Pair Added " + pairedFacilitator.getName() + " " + pairedFacilitator.getPairKey());
                }

                @Override
                public void onUnPair(Facilitator pairedFacilitator) {
                    txtStatus.setText(txtStatus.getText()+"\n" + "Pair Removed " + pairedFacilitator.getName() + " " + pairedFacilitator.getPairKey());
                }

                @Override
                public void onPairNameChanged(String pairKey, String oldName, String newName) {
                    txtStatus.setText(txtStatus.getText() + "\n" + "Device Name Changed " + oldName + " >> " + newName + " :: " + pairKey);
                }
            });

            controller.addFacilitatorSessionListener(new FacilitatorSessionListener() {
                @Override
                public void onConnected(FacilitatorConsoleImpl console) {
                    txtStatus.setText(txtStatus.getText()+"\n" + "Connected " + console.getFacilitatorName() + " " + console.getConsoleId());
                }

                @Override
                public void onDisconnected(Facilitator facilitator, String consoleId) {
                    txtStatus.setText(txtStatus.getText()+"\n" + "Disconnected " + facilitator.getName() + " " + consoleId);
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
                txtStatus.setText(txtStatus.getText() + "\nServer Started");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        mainFrame.setVisible(true);
    }
    public static void main(String[] args) {
        ControlPanel cp = new ControlPanel();
        cp.run();

        VirtualMeetingImpl virtualMeeting = new VirtualMeetingImpl();
        String url = URLGenerator.generateVMPublishURL(8000,"12");
        System.out.println(url);
        Endpoint.publish(url,virtualMeeting);

        FacilitatorConsoleImpl consolePanel = new FacilitatorConsoleImpl();
        url = URLGenerator.generateFacilitatorConsolePublishURL(8000,"13");
        System.out.println(url);
        Endpoint.publish(url,consolePanel);
    }

}
