import eduze.vms.facilitator.logic.FacilitatorController;
import eduze.vms.facilitator.logic.VirtualMeetingParticipantInfo;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

    public ScreenFrame(FacilitatorController controller)
    {
        this.controller = controller;
        frame.setContentPane(this.screenPanel);
        frame.setSize(800,600);
        frame.setTitle("Projector Output");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Image scaledImage = originalImage.getScaledInstance(frame.getWidth(),frame.getHeight() - 50,Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(scaledImage));
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
        originalImage = image;
        Image scaledImage = originalImage.getScaledInstance(frame.getWidth(),frame.getHeight() - 50,Image.SCALE_SMOOTH);
        lblImage.setIcon(new ImageIcon(scaledImage));
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
