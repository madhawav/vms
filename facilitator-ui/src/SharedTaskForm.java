import eduze.vms.facilitator.logic.*;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Admin on 6/2/2016.
 */
public class SharedTaskForm {
    private JTextArea txtDescription;
    private JPanel pnlContainer;
    private JLabel lblTitle;
    private JButton btnDelete;
    private JButton btnModify;
    private JButton btnAssignment;


    private FacilitatorController controller;
    private SharedTaskInfo sharedTask = null;
    private SharedTaskManager sharedTaskManager = null;

    private SharedTaskManager.SharedTasksListener sharedTasksListener = new SharedTaskManager.SharedTasksListener() {
        @Override
        public void onInitiated() {

        }

        @Override
        public void onNewSharedTask(SharedTaskInfo newTask) {

        }

        @Override
        public void onSharedTaskRemoved(SharedTaskInfo removedTask) {
            if(removedTask.getId().equals(sharedTask.getId())) {
                pnlContainer.setVisible(false);
                pnlContainer.getParent().remove(pnlContainer);
                sharedTaskManager.removeSharedTasksListener(sharedTasksListener);
            }


        }

        @Override
        public void onSharedTaskModified(SharedTaskInfo oldTask, SharedTaskInfo newTask) {
            if(oldTask.getId().equals(sharedTask.getId()))
            {
                sharedTask = newTask;
                txtDescription.setText(newTask.getDescription());
                lblTitle.setText(newTask.getTitle());

            }
        }

        @Override
        public void onSharedTaskAssignmentChanged(SharedTaskInfo oldTask, SharedTaskInfo newTask) {
            if(oldTask.getId().equals(sharedTask.getId()))
            {
                sharedTask = newTask;
                if(newTask.isAssigned())
                    btnAssignment.setText(newTask.getAssignedPresenterName());
                else
                    btnAssignment.setText("Unassigned");
            }
        }
    };
    public SharedTaskForm(final FacilitatorController controller, final SharedTaskInfo sharedTask)
    {

        this.controller = controller;
        this.sharedTask = sharedTask;

        this.lblTitle.setText(sharedTask.getTitle());
        this.txtDescription.setText(sharedTask.getDescription());
        if(sharedTask.isAssigned())
        {
            this.btnAssignment.setText(sharedTask.getAssignedPresenterName());
        }
        else
        {
            this.btnAssignment.setText("Unassigned");
        }

        try {
            this.sharedTaskManager = controller.getSharedTaskManager();
        } catch (ServerNotReadyException e) {
            e.printStackTrace();
        }
        sharedTaskManager.addSharedTasksListener(sharedTasksListener);

        btnAssignment.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu menu = getParticipantsPopup();
                menu.show(btnAssignment,0,btnAssignment.getHeight());
            }
        });
        btnDelete.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sharedTaskManager.removeSharedTask(sharedTask.getId());
                } catch (SharedTaskNotFoundException e1) {
                    e1.printStackTrace();
                } catch (ServerConnectionException e1) {
                    JOptionPane.showMessageDialog(pnlContainer, "Server Connection Error", "Error", JOptionPane.OK_OPTION);
                    e1.printStackTrace();
                }
            }
        });


        btnModify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModifySharedTask dlg = null;
                try {
                    dlg = new ModifySharedTask(controller.getSharedTaskManager(),sharedTaskManager.getSharedTask(sharedTask.getId()));
                } catch (ServerNotReadyException de) {
                    de.printStackTrace();
                }
                dlg.setVisible(true);
            }
        });
    }

    private JPopupMenu getParticipantsPopup()
    {
        JPopupMenu result = new JPopupMenu();
        JMenuItem notSetMenu = new JMenuItem("Unassigned");
        notSetMenu.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sharedTaskManager.unAssignTaskFromPresenter(sharedTask.getId());
                } catch (SharedTaskNotFoundException e1) {
                    JOptionPane.showMessageDialog(pnlContainer,"Task Not Found","Error",JOptionPane.OK_OPTION);
                    e1.printStackTrace();
                } catch (ServerConnectionException e1) {
                    JOptionPane.showMessageDialog(pnlContainer,"Server Connection Error","Error",JOptionPane.OK_OPTION);
                    e1.printStackTrace();
                }
            }
        });
        result.add(notSetMenu);

        VMParticipant[] participants =  controller.getVmStatus().getParticipants();
        if(participants == null)
            participants = new VMParticipant[0];
        for (final VMParticipant participant: participants)
        {
            JMenuItem participantMenuItem = new JMenuItem(participant.getName());
            participantMenuItem.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        sharedTaskManager.assignTaskToPresenter(sharedTask.getId(),participant.getFacilitatorId(),participant.getPresenterId());
                    } catch (SharedTaskNotFoundException e1) {
                        JOptionPane.showMessageDialog(pnlContainer,"Task Not Found","Error",JOptionPane.OK_OPTION);
                        e1.printStackTrace();
                    } catch (ServerConnectionException e1) {
                        JOptionPane.showMessageDialog(pnlContainer,"Server Connection Error","Error",JOptionPane.OK_OPTION);
                        e1.printStackTrace();
                    }
                }
            });
            result.add(participantMenuItem);
        }
        return result;
    }



    public JTextArea getTxtDescription() {
        return txtDescription;

    }


    public JPanel getPnlContainer() {
        return pnlContainer;
    }

    public JLabel getLblTitle() {
        return lblTitle;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnModify() {
        return btnModify;
    }

    public JButton getBtnAssignment() {
        return btnAssignment;
    }
}
