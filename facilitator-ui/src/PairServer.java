import eduze.vms.facilitator.logic.ServerConnectionException;
import eduze.vms.facilitator.logic.ServerManager;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.AlreadyPairedException;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.InvalidServerPasswordException;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PairServer extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtServerURL;
    private JPasswordField txtServerPassword;

    private ServerManager serverManager = null;
    public PairServer(ServerManager serverManager) {
        this.serverManager = serverManager;
        setContentPane(contentPane);
        setModal(true);
        setSize(new Dimension(400,200));
        setTitle("Pair New Server");
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            serverManager.pair(txtServerURL.getText(),new String(txtServerPassword.getPassword()));
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(this,"Invalid Server URL","Error",JOptionPane.OK_OPTION);
            e.printStackTrace();
            return;
        } catch (ServerConnectionException e) {
            JOptionPane.showMessageDialog(this,"Server Connection Exception","Error",JOptionPane.OK_OPTION);
            e.printStackTrace();
            return;
        } catch (AlreadyPairedException e) {
            JOptionPane.showMessageDialog(this,"Server is already paired","Error",JOptionPane.OK_OPTION);
            Logger.getLogger("Error").log(Level.SEVERE,"Already Paired");
            return;
        } catch (InvalidServerPasswordException e) {
            JOptionPane.showMessageDialog(this,"Invalid Password","Error",JOptionPane.OK_OPTION);
            Logger.getLogger("Error").log(Level.SEVERE,"Invalid Password");
            return;
        }
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }


}
