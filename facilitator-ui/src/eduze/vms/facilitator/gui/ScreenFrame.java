package eduze.vms.facilitator.gui;

import eduze.vms.facilitator.logic.FacilitatorController;
import eduze.vms.facilitator.logic.VirtualMeetingParticipantInfo;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Admin on 6/2/2016.
 */
public class ScreenFrame {
    JFrame frame = new JFrame();
    private JPanel screenPanel;
    private JLabel lblImage;
    private JLabel lblSource;
    private JPanel pnlContainer;

    private FacilitatorController controller;

    public ScreenFrame(FacilitatorController controller, final JFrame parent)
    {
        this.controller = controller;
        frame.setContentPane(this.screenPanel);
        frame.setMinimumSize(new Dimension(640,480));
        frame.setSize(800,600);
        frame.setTitle("Projector Output");
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if(originalImage == null)
                    return;
                Image scaledImage = originalImage.getScaledInstance(frame.getWidth(),frame.getHeight() - 100,Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(scaledImage));


            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
            }
        });
    }
    public void show()
    {
        frame.setVisible(true);
    }

    private Image originalImage = null;
    public void setImage(Image image, String facilitatorConsoleId, String presenterConsoleId)
    {
        if(image != null)
        {
            originalImage = image;
            Image scaledImage = originalImage.getScaledInstance(frame.getWidth(),frame.getHeight() - 100,Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));

        }
        else
        {
            originalImage = null;
            lblImage.setText("No Preview");
            lblImage.setIcon(null);
        }

        if(presenterConsoleId == null)
        {
            lblSource.setText("");
        }
        else
        {
            VirtualMeetingParticipantInfo participant =  controller.getVMParticipant(presenterConsoleId);
            if(participant == null)
            {
                lblSource.setText("Unknown Source: <" + presenterConsoleId + ">");
            }
            else
            {
                lblSource.setText(participant.getName());
            }

        }

    }

}
