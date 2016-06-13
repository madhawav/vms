import eduze.vms.foundation.ui.CryptoUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class FacilitatorConnection extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField txtPort;
    private JTextField txtConfigPath;
    private JButton loadButton;
    private JButton createButton;
    private JPasswordField txtPassword;
    private JTextField txtFacilitatorName;

    public FacilitatorConnection() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Facilitator Startup");
        pack();
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
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCreateConfigFile();
            }


        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoadConfigFile();
            }
        });
    }

    private FileFilter getFileFilter()
    {
        return  new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.getAbsolutePath().endsWith(".bin"))
                    return true;
                else return false;
            }

            @Override
            public String getDescription() {
                return "Binary File";
            }};
    }

    private void onLoadConfigFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(getFileFilter());
        int returnVal = fileChooser.showOpenDialog(contentPane);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            txtConfigPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
            StorageManager storageManager =  StorageManager.getInstance();
            File yourFile = new File(txtConfigPath.getText());
            storageManager.init(yourFile);
            storageManager.setEncryptionKey(new String(txtPassword.getPassword()));

            try {
                StorageManager.DataNode dataNode =  storageManager.readStorage();
                txtFacilitatorName.setText(dataNode.getConfiguration().getName());
                txtPort.setText(String.valueOf(dataNode.getConfiguration().getListenerPort()));

            } catch (StorageManager.StorageException e) {
                e.printStackTrace();
            } catch (CryptoUtil.CryptoException e) {
                e.printStackTrace();
            }

        }

    }

    private void onCreateConfigFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(getFileFilter());
        int returnVal = fileChooser.showSaveDialog(contentPane);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            txtConfigPath.setText(fileChooser.getSelectedFile().getAbsolutePath());

        }
    }

    public boolean validateInput()
    {
        if(!InputUtil.validatePort(txtPort.getText()))
        {
            JOptionPane.showMessageDialog(contentPane,"Invalid Port Specified. The port should be a number between 1025 and 65535.","Error",JOptionPane.OK_OPTION);
            return false;
        }
        if(!InputUtil.validateFacilitatorName(txtFacilitatorName.getText()))
        {
            JOptionPane.showMessageDialog(contentPane,"Invalid Facilitator Name Specified. The facilitator name should only include alphanumeric characters and underscore.","Error",JOptionPane.OK_OPTION);
            return false;
        }

        return true;
    }
    private void onOK() {
// add your code here
        if(validateInput())
        {
            StorageManager storageManager =  StorageManager.getInstance();
            File yourFile = new File(txtConfigPath.getText());
            boolean newMade = false;
            if(!yourFile.exists()) {
                try {
                    yourFile.createNewFile();
                    newMade = true;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(contentPane,"Unable to Create File","Error",JOptionPane.OK_OPTION);
                    e.printStackTrace();
                    return;
                }
            }
            storageManager.init(yourFile);
            storageManager.setEncryptionKey(new String(txtPassword.getPassword()));
            if(newMade)
            {
                try {
                    storageManager.saveDefaultConfiguration(txtFacilitatorName.getText(),Integer.parseInt(txtPort.getText()));
                }  catch (StorageManager.StorageException e) {
                    JOptionPane.showMessageDialog(contentPane,"Storage Error","Error",JOptionPane.OK_OPTION);
                    e.printStackTrace();
                } catch (CryptoUtil.CryptoException e) {
                    JOptionPane.showMessageDialog(contentPane,"Encryption Error","Error",JOptionPane.OK_OPTION);
                    e.printStackTrace();
                }
            }

            try {
                //Load and Configure Storage Data
                StorageManager.DataNode dataNode = storageManager.readStorage();
                dataNode.getConfiguration().setListenerPort(Integer.parseInt(txtPort.getText()));
                dataNode.getConfiguration().setName(txtFacilitatorName.getText());
                dataNode.getConfiguration().setPassword(new String(txtPassword.getPassword()));
                storageManager.updateStorage(dataNode);

                //Load the new window
                FacilitatorWindow window = new FacilitatorWindow();
                window.run();
                dispose();
            }catch (StorageManager.StorageException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(contentPane,"Unable to Read Storage File","Error",JOptionPane.OK_OPTION);
            } catch (CryptoUtil.CryptoException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(contentPane,"Encryption Error","Error",JOptionPane.OK_OPTION);
            }


        }

    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
      //  UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        FacilitatorConnection dialog = new FacilitatorConnection();
        dialog.pack();
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        //System.exit(0);
    }
}
