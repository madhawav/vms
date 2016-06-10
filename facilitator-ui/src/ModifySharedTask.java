import eduze.vms.facilitator.logic.ServerConnectionException;
import eduze.vms.facilitator.logic.SharedTaskInfo;
import eduze.vms.facilitator.logic.SharedTaskManager;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModifySharedTask extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel mainPanel;
    private JTextField txtTitle;
    private JTextArea txtDescription;

    private SharedTaskInfo sharedTask;
    private SharedTaskManager sharedTaskManager = null;

    public ModifySharedTask(SharedTaskManager sharedTaskManager, SharedTaskInfo sharedTask) {
        this.sharedTask = sharedTask;
        this.sharedTaskManager = sharedTaskManager;
        setContentPane(contentPane);
        setModal(true);
        setSize(new Dimension(300,200));
        setTitle("Modify Shared Task");
        pack();
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);

        txtDescription.setText(sharedTask.getDescription());
        txtTitle.setText(sharedTask.getTitle());


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

    public SharedTaskInfo getSharedTask() {
        return sharedTask;
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



    private void onOK() {
        if(validateInput())
        {
            try {
                sharedTaskManager.modifySharedTask(sharedTask.getId(),txtTitle.getText(),txtDescription.getText());
                dispose();
            } catch (ServerConnectionException e) {
                JOptionPane.showMessageDialog(mainPanel,"Server Connection Error","Error", JOptionPane.OK_OPTION);
                e.printStackTrace();
            } catch (SharedTaskNotFoundException e) {
                JOptionPane.showMessageDialog(mainPanel,"The Shared Task has been removed","Error", JOptionPane.OK_OPTION);
                e.printStackTrace();
                dispose();
            }
        }
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

}
