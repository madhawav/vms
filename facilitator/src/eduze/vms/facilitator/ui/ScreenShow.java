package eduze.vms.facilitator.ui;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by Madhawa on 14/04/2016.
 */
public class ScreenShow {
    private JPanel mainPanel;
    private JLabel lblScreen;
    private JFrame mainFrame;
    public ScreenShow()
    {
        mainFrame = new JFrame("VMS ScreenShow");
        mainFrame.setContentPane(this.mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // mainFrame.pack();
        mainFrame.setSize(800,600);
    }

    public void run()
    {
        mainFrame.setVisible(true);
    }

    public void setImage(BufferedImage image)
    {
        lblScreen.setIcon(new ImageIcon(image));
    }
}
