import eduze.vms.facilitator.logic.ServerConnectionException;
import eduze.vms.facilitator.logic.SharedTaskManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewSharedTask extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JPanel mainPanel;

    private SharedTaskManager sharedTaskManager = null;
    public NewSharedTask(SharedTaskManager sharedTaskManager) {
        this.sharedTaskManager = sharedTaskManager;
        setContentPane(contentPane);
        setModal(true);
        setSize(new Dimension(300,200));
        setTitle("Add New Shared Task");
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
// add your code here
        try {
            sharedTaskManager.createNewSharedTask(txtTitle.getText(),txtDescription.getText());
        } catch (ServerConnectionException e) {
            JOptionPane.showMessageDialog(mainPanel,"Server Connection Error","Error", JOptionPane.OK_OPTION);
            e.printStackTrace();
            return;
        }
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }


}
