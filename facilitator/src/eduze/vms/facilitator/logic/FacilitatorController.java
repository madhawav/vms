package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.server.Server;
import eduze.vms.facilitator.logic.mpi.server.ServerImplServiceLocator;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Madhawa on 12/04/2016.
 */

public class FacilitatorController {
    private FacilitatorImpl facilitatorService = null;
    private FacilitatorController.Configuration configuration = null;
    private boolean running = false;
    private  FacilitatorController(FacilitatorController.Configuration config)
    {
        this.configuration = config;
    }

    private ServerConnectionController serverConnectionController = null;

    private ArrayList<ConnectionListener> connectionListeners = new ArrayList<>();


    public boolean isServerConnected()
    {
        if(serverConnectionController == null)
            return false;
        return serverConnectionController.isConnected();
    }

    public ServerConnectionController getServerConnectionController()
    {
        return serverConnectionController;
    }

    private void start()
    {
        facilitatorService = new FacilitatorImpl(configuration);
        facilitatorService.setConnectionListener(new ConnectionListener() {
            @Override
            public void onConnectionRequested(ConnectionRequest connectionRequest) {
                notifyConnectionRequested(connectionRequest);
            }
        });
        facilitatorService.start();
        running = true;
    }



//    public ServerConnectionController bindServer(String serverURL) throws MalformedURLException, ServerConnectionException {
//        serverURL = UrlGenerator.extractURL(serverURL);
//        String serverMPIUrl = UrlGenerator.generateServerAccessURL(serverURL);
//
//        ServerImplServiceLocator serverImplServiceLocator = new ServerImplServiceLocator();
//        Server server;
//        try {
//            server = serverImplServiceLocator.getServerImplPort(new URL(serverMPIUrl));
//            //test for connectivity by requesting server name
//            String serverName = server.getServerName();
//            serverMPI = server;
//            serverConnectionController = new ServerConnectionController(this);
//            return serverConnectionController;
//
//        } catch (ServiceException e) {
//            e.printStackTrace();
//            throw new ServerConnectionException(e);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            throw new ServerConnectionException(e);
//        }
//    }



    public void acceptConnectionRequest(ConnectionRequest connectionRequest)
    {
        facilitatorService.acceptConnectionRequest(connectionRequest.getConnectionRequestId());
    }

    public  Configuration getConfiguration()
    {
        return configuration;
    }
    public static FacilitatorController start(FacilitatorController.Configuration configuration)
    {
        FacilitatorController controller = new FacilitatorController(configuration);
        controller.start();
        return controller;
    }

    public void addConnectionListener(ConnectionListener listener)
    {
        connectionListeners.add(listener);
    }

    public void removeConnectionListener(ConnectionListener listener)
    {
        connectionListeners.remove(listener);
    }

    private void notifyConnectionRequested(ConnectionRequest cr)
    {
        for(ConnectionListener listener : connectionListeners)
            listener.onConnectionRequested(cr);
    }



    public static class Configuration
    {
        private String name = "Facilitator";
        private int listenerPort = 7000;
        private String password = "password";

        public int getListenerPort() {
            return listenerPort;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }

        public void setListenerPort(int listenerPort) {
            this.listenerPort = listenerPort;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
