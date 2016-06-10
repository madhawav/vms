import eduze.vms.facilitator.logic.ConnectionRequest;
import eduze.vms.facilitator.logic.RequestAlreadyProcessedException;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Admin on 6/2/2016.
 */
public class ConnectionRequestForm {
    private JButton btnAccept;
    private JPanel pnlContainer;
    private JLabel lblName;
    private JButton btnReject;
    private JLabel lblConnectionStatus;
    private ConnectionRequest connectionRequest = null;

    public ConnectionRequestForm(final ConnectionRequest connectionRequest)
    {
        this.connectionRequest = connectionRequest;
        lblName.setText(connectionRequest.getPresenterName());
        btnAccept.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlContainer.getParent().remove(pnlContainer);
                try {
                    connectionRequest.accept();
                } catch (RequestAlreadyProcessedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnReject.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlContainer.getParent().remove(pnlContainer);
                try {
                    connectionRequest.reject();
                } catch (RequestAlreadyProcessedException e1) {
                    e1.printStackTrace();
                }

            }
        });
    }

    public JPanel getPnlContainer() {
        return pnlContainer;
    }

    public JLabel getLblName() {
        return lblName;
    }

    public JButton getBtnReject() {
        return btnReject;
    }

    public JButton getBtnAccept() {
        return btnAccept;
    }

    public JLabel getLblConnectionStatus() { return  lblConnectionStatus;}
}
