import eduze.vms.facilitator.logic.*;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;

/**
 * Created by Admin on 6/2/2016.
 */
public class ShareRequestForm {
    private JPanel pnlContainer;
    private JLabel lblType;
    private JButton btnAccept;
    private JButton btnReject;

    private AbstractShareRequest request;

    public ShareRequestForm(final AbstractShareRequest request)
    {
        this.request = request;
        TitledBorder border = (TitledBorder) this.pnlContainer.getBorder();
        border.setTitle(request.getPresenterName());
        if(request instanceof ScreenShareRequest)
        {
            lblType.setText("Screen Share");
        }
        else if(request instanceof ScreenAudioShareRequest)
        {
            lblType.setText("Share Screen/Speech");
        }
        else if(request instanceof AudioRelayRequest)
        {
            lblType.setText("Speak");
        }
        btnAccept.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    request.honour();
                } catch (RequestAlreadyProcessedException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(btnAccept,"Request has been processed already","Error",JOptionPane.OK_OPTION);
                } catch (ServerConnectionException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(btnAccept,"Server Connection Error","Error",JOptionPane.OK_OPTION);
                    return;
                } catch (InvalidIdException e1) {
                    e1.printStackTrace();
                } catch (ServerNotReadyException e1) {
                    JOptionPane.showMessageDialog(btnAccept,"Server is not ready","Error",JOptionPane.OK_OPTION);
                    e1.printStackTrace();
                    return;
                }

                pnlContainer.setVisible(false);
                pnlContainer.getParent().remove(pnlContainer);

            }
        });

        btnReject.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    request.dismiss();
                } catch (RequestAlreadyProcessedException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(btnAccept,"Request has been processed already","Error",JOptionPane.OK_OPTION);
                }
                pnlContainer.setVisible(false);
                pnlContainer.getParent().remove(pnlContainer);

            }
        });
    }

    public JPanel getPnlContainer() {
        return pnlContainer;
    }

    public JLabel getLblType() {
        return lblType;
    }

    public JButton getBtnAccept() {
        return btnAccept;
    }

    public JButton getBtnReject() {
        return btnReject;
    }
}
