import eduze.vms.facilitator.logic.FacilitatorController;
import eduze.vms.facilitator.logic.Presenter;
import eduze.vms.facilitator.logic.ServerConnectionException;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Admin on 6/2/2016.
 */
public class ConnectionStatusForm {
    private final FacilitatorController controller;
    private final String connectionRequestId;
    private final String consoleId;
    private JPanel pnlContainer;
    private JLabel lblName;
    private JButton btnDisconnect;
    private JLabel lblConnectionStatus;

    private Presenter presenter = null;

    public ConnectionStatusForm(FacilitatorController controller, String connectionRequestId, String consoleId) {
        this.controller = controller;
        this.connectionRequestId = connectionRequestId;
        this.consoleId = consoleId;
        this.presenter = controller.getPresenter(consoleId);

        lblName.setText(presenter.getPresenterName());
        btnDisconnect.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    presenter.disconnect();
                } catch (ServerConnectionException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    public JPanel getPnlContainer() {
        return pnlContainer;
    }
}
