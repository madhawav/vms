import eduze.vms.facilitator.gui.FacilitatorApp;
import eduze.vms.presenter.gui.PresenterApp;
import eduze.vms.server.ui.ServerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Admin on 6/16/2016.
 */
public class Startup {
    private JPanel mainPanel;
    private JButton testServerButton;
    private JButton testFacilitatorButton;
    private JButton testPresenterButton;

    public Startup()
    {
        testServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ServerApp serverApp = new ServerApp(null);
                try {
                    serverApp.run();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        });

        testFacilitatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FacilitatorApp facilitatorApp = new FacilitatorApp(null);
                try {
                    facilitatorApp.run();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        });
        testPresenterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PresenterApp presenterApp = PresenterApp.getInstance();
                try {
                    presenterApp.run(null);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }

            }
        });
    }
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        Startup startup = new Startup();
        startup.run();
    }

    private void run() {
        JFrame mainFrame = new JFrame();
        mainFrame.setContentPane(mainPanel);
        mainFrame.pack();
        mainFrame.setSize(200,200);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setTitle("UI Test");
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setVisible(true);
    }
}
