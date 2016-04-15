package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManager;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.server.Server;
import eduze.vms.facilitator.logic.mpi.server.ServerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.VMSessionManager;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.VMSessionManagerImplServiceLocator;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by Madhawa on 13/04/2016.
 */
public class ServerConnectionController {
    private Server server;
    private FacilitatorController facilitatorController;
    private FacilitatorManager facilitatorManager;
    private VMSessionManager vmSessionManager;
    private String serverURL;

    private String virtualMeetingConsoleId = null;
    private String facilitatorConsoleId = null;



    private boolean isConnected = false;

    ServerConnectionController(FacilitatorController facilitatorController, String serverURL, ConnectionResult connectionResult) throws MalformedURLException, ServiceException {
        String url = UrlGenerator.extractURL(serverURL);
        this.serverURL = url;
        this.facilitatorController = facilitatorController;
        ServerImplServiceLocator serverLocator = new ServerImplServiceLocator();
        FacilitatorManagerImplServiceLocator facilitatorManagerImplServiceLocator = new FacilitatorManagerImplServiceLocator();
        VMSessionManagerImplServiceLocator vmSesssionManagerImplServiceLocator = new VMSessionManagerImplServiceLocator();
        server = serverLocator.getServerImplPort(new URL(UrlGenerator.generateServerAccessURL(url)));
        facilitatorManager = facilitatorManagerImplServiceLocator.getFacilitatorManagerImplPort(new URL(UrlGenerator.generateFacilitatorManagerAccessURL(url)));
        vmSessionManager = vmSesssionManagerImplServiceLocator.getVMSessionManagerImplPort(new URL(UrlGenerator.generateVMSessionManagerAccessURL(url)));

        virtualMeetingConsoleId = connectionResult.getVirtualMeetingConsoleId();
        facilitatorConsoleId = connectionResult.getFacilitatorConsoleId();

        isConnected = connectionResult.isSuccessful();


    }

    public String getServerName() throws ServerConnectionException {
        try {
            return server.getServerName();
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }



    public void disconnect()
    {
        //TODO: Setup server disconnection logic here
    }

    public String getServerURL()
    {
        return serverURL;
    }

    Server getServerMPI()
    {
        return server;
    }

    public FacilitatorController getFacilitatorController() {
        return facilitatorController;
    }

    FacilitatorManager getFacilitatorManager() {
        return facilitatorManager;
    }

    VMSessionManager getVmSessionManager() {
        return vmSessionManager;
    }


    public boolean isConnected() {
        return isConnected;
    }


    public String getVirtualMeetingConsoleId() {
        return virtualMeetingConsoleId;
    }

    public String getFacilitatorConsoleId() {
        return facilitatorConsoleId;
    }
}
