package eduze.vms.presenter.gui;

import eduze.vms.presenter.logic.FacilitatorConnectionException;
import eduze.vms.presenter.logic.FacilitatorConnectionNotReadyException;
import eduze.vms.presenter.logic.FacilitatorConnector;
import eduze.vms.presenter.logic.FacilitatorDisconnectedException;
import eduze.vms.presenter.logic.mpi.facilitator.InvalidFacilitatorPasskeyException;

import javax.swing.*;
import java.awt.event.*;
import java.net.MalformedURLException;

public class ConnectionWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtPresenterName;
    private JTextField txtFacilitatorURL;
    private JPasswordField txtFacilitatorPassword;
    private JLabel txtStatus;

    public ConnectionWindow() {
        setContentPane(contentPane);
        setModal(false);
        setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
        setTitle("Presenter Startup");
        pack();
        setMinimumSize(getSize());
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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
    private FacilitatorConnector connector = null;
    private void onOK() {
        if(validateInput())
        {
            FacilitatorConnector.Configuration configuration = new FacilitatorConnector.Configuration();
            configuration.setPresenterName(txtPresenterName.getText());
            configuration.setFacilitatorPasskey(txtFacilitatorPassword.getPassword());
            configuration.setFacilitatorURL(txtFacilitatorURL.getText());
            try {
                connector = FacilitatorConnector.connect(configuration);
                buttonOK.setText("Wait");
                buttonOK.setEnabled(false);
                txtStatus.setText("Pending Confirmation from Facilitator...");
                connector.setConnectionRequestStateListener(new FacilitatorConnector.ConnectionRequestStateListener() {
                    @Override
                    public void onSuccess(FacilitatorConnector sender) {
                        txtStatus.setText("Connection Successful");
                        try {
                            PresenterApp.getInstance().setController(sender.obtainController());
                            PresenterWindow presenterWindow = new PresenterWindow();
                            presenterWindow.run();
                        } catch (FacilitatorConnectionNotReadyException e) {
                            e.printStackTrace();
                        } catch (FacilitatorConnectionException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(contentPane,"Error in Connection to Facilitator","Connection Error",JOptionPane.OK_OPTION);
                            System.exit(0);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(contentPane,"Error in Connection to Facilitator","Connection Error",JOptionPane.OK_OPTION);
                            System.exit(0);
                        } catch (FacilitatorDisconnectedException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(contentPane,"Error in Connection to Facilitator","Connection Error",JOptionPane.OK_OPTION);
                            System.exit(0);
                        }
                        dispose();
                    }

                    @Override
                    public void onFailed(FacilitatorConnector sender) {
                        JOptionPane.showMessageDialog(contentPane,"Connection Request has been Rejected by Facilitator","Connection Failed",JOptionPane.OK_OPTION);
                        txtStatus.setText("Connection Attempt Rejected");
                        buttonOK.setEnabled(true);
                        buttonOK.setText("Connect");
                    }

                    @Override
                    public boolean onException(FacilitatorConnector sender, Exception e) {
                        return true;
                    }
                });
            } catch (FacilitatorConnectionException e) {
                JOptionPane.showMessageDialog(contentPane,"Error in Connection to Facilitator","Connection Error",JOptionPane.OK_OPTION);
                e.printStackTrace();
            } catch (InvalidFacilitatorPasskeyException e) {
                JOptionPane.showMessageDialog(contentPane,"Invalid Facilitator Password","Facilitator Connection Error",JOptionPane.OK_OPTION);
                e.printStackTrace();
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(contentPane,"Invalid Facilitator URL","Facilitator Connection Error",JOptionPane.OK_OPTION);
                e.printStackTrace();
            }


        }

    }

    private boolean validateInput()
    {
        if(!InputUtil.validatePresenterName(txtPresenterName.getText()))
        {
            JOptionPane.showMessageDialog(contentPane,"Presenter Name is not valid. It should a non space separated alpha numeric text.","Error",JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private void onCancel() {
// add your code here if necessary
        if(connector != null)
        {

        }
        dispose();
    }

}
