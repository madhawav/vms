import eduze.vms.presenter.logic.ControlLoop;
import eduze.vms.presenter.logic.FacilitatorConnectionException;
import eduze.vms.presenter.logic.PresenterController;
import eduze.vms.presenter.logic.mpi.presenterconsole.PresenterConsole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Admin on 6/10/2016.
 */
public class PresenterWindow {
    private JFrame presenterWindowFrame = null;
    private JPanel mainFrame;
    private JList list1;
    private JButton btnScreenShare;
    private JButton btnAllShare;
    private JButton btnAudioShare;
    private JCheckBox chkAllowScreenShare;
    private JCheckBox chkAllowAudioShare;

    public PresenterWindow()
    {
        final PresenterController controller = PresenterApp.getInstance().getController();
        controller.addStateChangeListener(new ControlLoop.StateChangeListener() {
            @Override
            public void onScreenCaptureChanged(boolean newValue) {

            }

            @Override
            public void onAudioCaptureChanged(boolean newValue) {

            }

            @Override
            public void onControlLoopCycleCompleted() {
                updateUI();
            }
        });

        chkAllowAudioShare.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                controller.setAllowedAudioShare(chkAllowAudioShare.isSelected());
            }
        });
        chkAllowScreenShare.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                controller.setAllowedScreenShare(chkAllowScreenShare.isSelected());
            }
        });

        btnScreenShare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.requestScreenShare(false);
                    chkAllowScreenShare.setSelected(true);
                } catch (FacilitatorConnectionException e1) {
                    JOptionPane.showMessageDialog(mainFrame,"Error in Connection to Facilitator","Error",JOptionPane.OK_OPTION);
                    e1.printStackTrace();
                }
            }
        });
        btnAudioShare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.requestAudioShare();

                    chkAllowAudioShare.setSelected(true);
                } catch (FacilitatorConnectionException e1) {
                    JOptionPane.showMessageDialog(mainFrame,"Error in Connection to Facilitator","Error",JOptionPane.OK_OPTION);
                    e1.printStackTrace();
                }
            }
        });
        btnAllShare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.requestScreenShare(true);
                    chkAllowScreenShare.setSelected(true);
                    chkAllowAudioShare.setSelected(true);
                } catch (FacilitatorConnectionException e1) {
                    JOptionPane.showMessageDialog(mainFrame,"Error in Connection to Facilitator","Error",JOptionPane.OK_OPTION);
                    e1.printStackTrace();
                }
            }
        });
    }

    public void run()
    {
        presenterWindowFrame = new JFrame();
        presenterWindowFrame.setContentPane(mainFrame);
        presenterWindowFrame.pack();
        presenterWindowFrame.setSize(new Dimension(400,400));
        presenterWindowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        presenterWindowFrame.setLocationRelativeTo(null);
        presenterWindowFrame.setTitle("Presenter System - " + PresenterApp.getInstance().getController().getConfiguration().getPresenterName());
        presenterWindowFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    PresenterApp.getInstance().getController().disconnect();
                } catch (FacilitatorConnectionException e1) {
                    e1.printStackTrace();
                }
                presenterWindowFrame.dispose();
            }
        });
        presenterWindowFrame.setVisible(true);



    }

    private void updateUI()
    {
        PresenterController controller = PresenterApp.getInstance().getController();
        Color active = Color.GREEN;
        Color inactive = Color.RED;
        Color blocked = Color.ORANGE;
        if(controller.isAudioShared())
        {
            btnAudioShare.setBackground(active);
        }
        else if(controller.isServerAcceptsAudioShare())
        {
            btnAudioShare.setBackground(blocked);
        }
        else
        {
            btnAudioShare.setBackground(inactive);
        }

        if(controller.isScreenShared())
        {
            btnScreenShare.setBackground(active);
        }
        else if(controller.isServerAcceptsScreenShare())
        {
            btnScreenShare.setBackground(blocked);
        }
        else
        {
            btnScreenShare.setBackground(inactive);
        }




    }


}
