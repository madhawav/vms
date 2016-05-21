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

/**
 * Controller for Managing Facilitator Server Connection
 */
public class ServerConnectionController {
    private Server server; //MPI from Server
    private FacilitatorController facilitatorController; //Facilitator Controller
    private FacilitatorManager facilitatorManager; //Facilitator Manager MPI from Server
    private VMSessionManager vmSessionManager; //VMSessionManager MPI from server
    private String serverURL; //Server URL

    private String virtualMeetingConsoleId = null; //Virtual Meeting Console ID
    private String facilitatorConsoleId = null; //Facilitator Console Id


    private boolean isConnected = false; //Is the Facilitator Server Connection Active

    /**
     * Establish Controller
     * @param facilitatorController The containing facilitator controller
     * @param serverURL Server URL
     * @param connectionResult Connection result from Server
     * @throws MalformedURLException
     * @throws ServiceException
     */
    ServerConnectionController(FacilitatorController facilitatorController, String serverURL, ConnectionResult connectionResult) throws MalformedURLException, ServiceException {
        String url = UrlGenerator.extractURL(serverURL);
        this.serverURL = url;
        this.facilitatorController = facilitatorController;

        //Setup Service Locators
        ServerImplServiceLocator serverLocator = new ServerImplServiceLocator();
        FacilitatorManagerImplServiceLocator facilitatorManagerImplServiceLocator = new FacilitatorManagerImplServiceLocator();
        VMSessionManagerImplServiceLocator vmSesssionManagerImplServiceLocator = new VMSessionManagerImplServiceLocator();

        //Setup Service MPIs
        server = serverLocator.getServerImplPort(new URL(UrlGenerator.generateServerAccessURL(url)));
        facilitatorManager = facilitatorManagerImplServiceLocator.getFacilitatorManagerImplPort(new URL(UrlGenerator.generateFacilitatorManagerAccessURL(url)));
        vmSessionManager = vmSesssionManagerImplServiceLocator.getVMSessionManagerImplPort(new URL(UrlGenerator.generateVMSessionManagerAccessURL(url)));


        //Obtain ids
        virtualMeetingConsoleId = connectionResult.getVirtualMeetingConsoleId();
        facilitatorConsoleId = connectionResult.getFacilitatorConsoleId();

        //Set connection status
        isConnected = connectionResult.isSuccessful();


    }

    /**
     * Retrieve Server Name
     * @return Server Name
     * @throws ServerConnectionException
     */
    public String getServerName() throws ServerConnectionException {
        try {
            return server.getServerName();
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }


    /**
     * Disconnect Facilitator from Server
     */
    public void disconnect() throws ServerConnectionException {
        facilitatorController.notifyServerDisconnected();
        this.vmSessionManager = null;
        this.facilitatorController = null;
        this.server = null;
        isConnected = false;
    }

    /**
     * Retrieve Server URL
     * @return
     */
    public String getServerURL()
    {
        return serverURL;
    }

    /**
     * Retrieve Server MPI
     * @return
     */
    Server getServerMPI()
    {
        return server;
    }

    /**
     * Retrieve Facilitator Controller
     * @return
     */
    public FacilitatorController getFacilitatorController() {
        return facilitatorController;
    }



    /**
     * Retrieve FaciltiatorManager MPIf from Server
     * @return
     */
    FacilitatorManager getFacilitatorManager() {
        return facilitatorManager;
    }

    /**
     * Retrieve VMSessionManager MPI from Server
     * @return
     */
    VMSessionManager getVmSessionManager() {
        return vmSessionManager;
    }


    /**
     * Return whether Facilitator is connected to server
     * @return
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Retrieve Virtual Meeting Console Id
     * @return
     */
    public String getVirtualMeetingConsoleId() {
        return virtualMeetingConsoleId;
    }

    /**
     * Retrieve Facilitator Console Id
     * @return
     */
    public String getFacilitatorConsoleId() {
        return facilitatorConsoleId;
    }


}
