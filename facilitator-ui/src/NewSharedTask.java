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

        setTitle("Add New Shared Task");
        pack();
        setSize(new Dimension(400,300));
        setLocationRelativeTo(null);
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

        if(validateInput())
        {

            try {
                sharedTaskManager.createNewSharedTask(txtTitle.getText(),txtDescription.getText());
                dispose();
            } catch (ServerConnectionException e) {
                JOptionPane.showMessageDialog(mainPanel,"Server Connection Error","Error", JOptionPane.OK_OPTION);
                e.printStackTrace();
                return;
            }
        }


    }

    private boolean validateInput() {
        if(!InputUtil.validateSharedTaskTitle(txtTitle.getText()))
        {
            JOptionPane.showMessageDialog(mainPanel,"The title should not be empty.","Input Error",JOptionPane.OK_OPTION);
            return false;
        }
        if(!InputUtil.validateSharedTaskDescription(txtDescription.getText()))
        {
            JOptionPane.showMessageDialog(mainPanel,"The description should not be empty.","Input Error",JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }


}
