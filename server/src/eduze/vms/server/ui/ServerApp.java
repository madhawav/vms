package eduze.vms.server.ui;

import eduze.vms.foundation.ui.CryptoUtil;
import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.PairListener;
import eduze.vms.server.logic.ServerController;
import eduze.vms.server.logic.ServerStartedException;
import eduze.vms.server.logic.webservices.Facilitator;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


import javax.swing.*;
import java.io.File;
import java.util.Scanner;

/**
 * Created by Admin on 6/12/2016.
 */
public class ServerApp {


    private String[] args;
    public ServerApp(String[] args)
    {
        this.args = args;
    }


    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        //Setup Log4J
        BasicConfigurator.configure();

        ServerApp serverApp = new ServerApp(args);
        serverApp.run();
    }

    public void run() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        if(args == null || args.length == 0)
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            ControlPanel cp = new ControlPanel();
            cp.run();
        }
        else
        {
            //Start Headless Version
            startHeadless();
        }
    }

    private void startHeadless() {
        File file = new File(args[0]);
        Scanner sc = new Scanner(System.in);
        if(!file.exists()) {
            System.out.println("Error: Config File not Found");
            return;
        }
        StorageManager storageManager = StorageManager.getInstance();

        storageManager.init(file);
        if(args.length == 1)
        {
            System.out.print("Enter Password: ");
            String password = sc.nextLine();
            storageManager.setEncryptionKey(password);
        }
        else
        {
            storageManager.setEncryptionKey(args[1]);
        }

        System.out.println("Reading Config File...");
        try {
            StorageManager.DataNode config = storageManager.readStorage();
            final ServerController serverController = new ServerController();
            serverController.setPort(config.getConfiguration().getPort());
            serverController.setPassword(config.getConfiguration().getPassword());
            serverController.setName(config.getConfiguration().getName());
            serverController.setFacilitatorConnectivityTimeoutInterval(config.getConfiguration().getFacilitatorConnectivityTimeoutInterval());
            serverController.start();

            for(Facilitator facilitator : config.getPairedFacilitators())
            {
                serverController.addPairedFacilitator(facilitator.getName(),facilitator.getPairKey());
            }

            serverController.addFacilitatorSessionListener(new FacilitatorSessionListener() {
                @Override
                public void onConnected(Facilitator facilitator, String consoleId) {
                    System.out.println("Facilitator Connected: " + facilitator.getName());
                }

                @Override
                public void onDisconnected(Facilitator facilitator, String consoleId) {
                    System.out.println("Facilitator Disconnected: " + facilitator.getName());
                }

                @Override
                public void onMeetingAdjourned() {
                    System.out.println("Meeting Adjourned");
                }
            });

            serverController.addPairListener(new PairListener() {
                @Override
                public void onPair(Facilitator pairedFacilitator) {
                    System.out.println("Paired Device Added: " + pairedFacilitator.getName());
                    try {
                        StorageManager.getInstance().updatePairedFacilitators(serverController.getPairedFacilitators());
                    } catch (CryptoUtil.CryptoException e) {
                        e.printStackTrace();
                        System.out.println("Crypto Error in Saving paired device");
                    } catch (StorageManager.StorageException e) {
                        e.printStackTrace();
                        System.out.println("Storage Error in Saving paired device");
                    }
                }

                @Override
                public void onUnPair(Facilitator pairedFacilitator) {
                    System.out.println("Paired Device Removed: " +pairedFacilitator.getName());
                    try {
                        StorageManager.getInstance().updatePairedFacilitators(serverController.getPairedFacilitators());
                    } catch (CryptoUtil.CryptoException e) {
                        e.printStackTrace();
                        System.out.println("Crypto Error in Saving paired device");
                    } catch (StorageManager.StorageException e) {
                        e.printStackTrace();
                        System.out.println("Storage Error in Saving paired device");
                    }
                }

                @Override
                public void onPairNameChanged(String pairKey, String oldName, String newName) {
                    System.out.println("Paired Device Renamed: " + oldName + " > " + newName);
                    try {
                        StorageManager.getInstance().updatePairedFacilitators(serverController.getPairedFacilitators());
                    } catch (CryptoUtil.CryptoException e) {
                        e.printStackTrace();
                        System.out.println("Crypto Error in Saving paired device");
                    } catch (StorageManager.StorageException e) {
                        e.printStackTrace();
                        System.out.println("Storage Error in Saving paired device");
                    }
                }
            });
            System.out.println("Server has been started successfully");
        } catch (CryptoUtil.CryptoException e) {
            e.printStackTrace();
            System.out.println("Crypto Exception in reading config file");
        } catch (StorageManager.StorageException e) {
            e.printStackTrace();
            System.out.println("Storage Exception in reading config file");
        } catch (ServerStartedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
