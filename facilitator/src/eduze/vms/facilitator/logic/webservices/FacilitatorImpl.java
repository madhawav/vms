package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.*;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsole;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsoleImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.VMParticipant;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeeting;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.swing.*;
import javax.xml.rpc.ServiceException;
import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Main WebService to Presenters.
 */
@WebService(endpointInterface = "eduze.vms.facilitator.logic.webservices.Facilitator")
public class FacilitatorImpl implements Facilitator  {
    private FacilitatorController.Configuration configuration = null; //Configuration set by Facilitator at Startup
    private HashMap<String,ConnectionRequestState> connectionRequests = new HashMap<>(); //List of connection requests received from presenter subsystems
    private PresenterConnectionListener presenterConnectionListener = null; //Listener to subscribe for presenter connection events
    private PresenterModifiedListener presenterModifiedListener = null; //Listener to subscribe for presenter modification events
    private HashMap<String,PresenterConsoleImpl> presenterConsoles = new HashMap<>(); //List of consoles for presenters currently connected to facilitator

    private String serverURL = null; //The URL of Server
    private String facilitatorConsoleId = null; //unique id of this facilitator webService
    private String virtualMeetingId = null; //unique id of virtual meeting currently in progress (if any)

    private FacilitatorConsole facilitatorConsole = null; //MPI for facilitator console exposed from server
    private VirtualMeeting virtualMeeting = null; //MPI for virtual meeting WebService exposed from server

    private ShareRequestListener shareRequestListener = null; //The listener to subscribe for scree/audio share requests

    /**
     * Method call to request Facilitator WebService to connect with Server Subsystem
     * @param serverURL URL of Server
     * @param facilitatorConsoleId Facilitator console ID provided by Server upon connection request
     * @param virtualMeetingId Virtual Meeting ID provided by Server upon connection request
     * @throws MalformedURLException Error in Server URL
     * @throws ServerConnectionException Error in connection to server
     */
    public void establishServerConnection(String serverURL, String facilitatorConsoleId, String virtualMeetingId) throws MalformedURLException, ServerConnectionException {

        //Establish service locators to Virtual Meeting and Facilitator Console WebServices offered by Server
        VirtualMeetingImplServiceLocator vmLocator = new VirtualMeetingImplServiceLocator();
        FacilitatorConsoleImplServiceLocator facilitatorLocator = new FacilitatorConsoleImplServiceLocator();

        this.facilitatorConsoleId = facilitatorConsoleId;
        this.virtualMeetingId = virtualMeetingId;

        try{
            //Establish Server Connection
            virtualMeeting = vmLocator.getVirtualMeetingImplPort(new URL(UrlGenerator.generateVMAccessUrl(serverURL,virtualMeetingId)));
            facilitatorConsole = facilitatorLocator.getFacilitatorConsoleImplPort(new URL(UrlGenerator.generateFacilitatorConsoleAccessUrl(serverURL,facilitatorConsoleId)));


            //Update the list of Virtual Meeting participants in Server to include participants connected to this facilitator
            updateVMParticipants();
        }
        catch (ServiceException e)
        {
            //Error in connection with Server
            throw new ServerConnectionException(e);
        }

    }

    /**
     * Disconnect from Server
     */
    public void disconnectServerConnection() throws ServerConnectionException {
        try {
            getFacilitatorConsole().disconnect();
            this.virtualMeeting = null;
            this.facilitatorConsole = null;
            this.facilitatorConsoleId = null;
            this.virtualMeetingId = null;

        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }

    }

    /**
     * Update the list of Virtual Meeting participants maintained by Server
     * @throws ServerConnectionException
     */
    void updateVMParticipants() throws ServerConnectionException {
        //if not connected to a server, then we cant update. Therefore return
        if(facilitatorConsole == null)
            return;
        //Create a container for list of participants
        ArrayList<VMParticipant> participants = new ArrayList<>();

        //Populate list of participants
        for(PresenterConsoleImpl presenterConsole : getPresenterConsoles()) {
            if (presenterConsole.isConnected()) {
                VMParticipant participant = new VMParticipant();
                participant.setPresenterId(presenterConsole.getConsoleId());
                participant.setFacilitatorId(facilitatorConsoleId);
                participant.setName(presenterConsole.getName());
                participants.add(participant);

            }
        }
        try {
            //Update the List of participants of this facilitator in servers facilitator console
            VMParticipant[] participantsArray = new VMParticipant[participants.size()];
            participants.toArray(participantsArray);
            facilitatorConsole.setParticipants(participantsArray);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }

    public FacilitatorImpl()
    {

    }

    //Constructor
    public FacilitatorImpl(FacilitatorController.Configuration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Retrieve list of PresenterConsoles connected to Facilitator
     * @return
     */
    public Collection<PresenterConsoleImpl> getPresenterConsoles()
    {
        return presenterConsoles.values();
    }

    /**
     * Retrieve a Presenter Console from presenter console Id
     * @param consoleId
     * @return
     */
    @WebMethod(exclude = true)
    public PresenterConsoleImpl getPresenterConsole(String consoleId) {
        return presenterConsoles.get(consoleId);
    }

    /**
     * Check the status of a connection request raised by presenter to facilitator
     * @param connectionRequestId connectionRequestId returned from requestConnectionMethod
     * @return Connection Request State
     */
    @Override
    public ConnectionRequestState checkConnectionRequestState(String connectionRequestId) {
        return connectionRequests.get(connectionRequestId);
    }

    /**
     * Retrieve the listener that is notified on connections from presenters
     * @return Listener who is notified when a connection is requested by a presenter
     */
    public PresenterConnectionListener getPresenterConnectionListener() {
        return presenterConnectionListener;
    }

    /**
     * Set the listener that is notified on connections from preseters
     * @param presenterConnectionListener
     */
    public void setPresenterConnectionListener(PresenterConnectionListener presenterConnectionListener) {
        this.presenterConnectionListener = presenterConnectionListener;
    }


    /**
     * Internal method to verify validity of password submitted by presenter subsystem
     * @param password
     * @return
     */
    boolean verifyPassword(String password)
    {
        //TODO: use a proper hashing logic here
        if(configuration.getPassword().equals(password))
            return true;
        else
            return false;
    }

    /**
     * Start the Facilitator Web Service
     */
    public void start()
    {
        //Publish Web Service
        Endpoint.publish(UrlGenerator.generateFacilitatorPublishUrl(configuration.getListenerPort()),this);
        Logger.getLogger("DEBUG").log(Level.INFO,"Facilitator Console Started " +UrlGenerator.generateFacilitatorPublishUrl(configuration.getListenerPort()));
    }

    /**
     * Retrieve Facilitator Service Startup Configuration
     * @return
     */
    FacilitatorController.Configuration getConfiguration()
    {
        return configuration;
    }

    /**
     * Retrieve Name of Facilitator Subsystem
     * @return
     */
    @Override
    public String getFacilitatorName() {
        return configuration.getName();
    }

    /**
     * Request connection to Facilitator as a Presenter Subsystem
     * @param presenterName name of presenter
     * @param passKey facilitator passkey
     * @return Connection Request Id
     * @throws InvalidFacilitatorPasskeyException Invalid passkey
     */
    @Override
    public String requestConnection(String presenterName, String passKey) throws InvalidFacilitatorPasskeyException {
        //Verify validity of server pass-key
        if(verifyPassword(passKey))
        {
            String crId = PasswordUtil.generateConnectionRequestId(); //Generate a connection request Id
            ConnectionRequestState connectionRequestState = new ConnectionRequestState(); //Create data structure to hold connection request information for presenter facilitator connections
            //Set parameters
            connectionRequestState.setPending(true);
            connectionRequestState.setSuccessful(false);
            connectionRequestState.setPresenterConsoleId(null);
            connectionRequestState.setConnectionRequestId(crId);
            connectionRequestState.setPresenterName(presenterName);
            connectionRequests.put(crId,connectionRequestState);

            //Notify connection request to controller
            final ConnectionRequest request = new ConnectionRequest(connectionRequestState,this);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if(getPresenterConnectionListener() != null)
                        getPresenterConnectionListener().onConnectionRequested(request);
                }
            });

            //Return connection request ID to presenter
            return crId;
        }
        else throw new InvalidFacilitatorPasskeyException();
    }

    /**
     * Disconnect a presenter connected to facilitator
     * @param console PresenterConsole Id
     */
    void disconnectPresenter(PresenterConsoleImpl console) throws ServerConnectionException {

        presenterConsoles.remove(console.getConsoleId());

        updateVMParticipants();
        if(getPresenterConnectionListener()!=null)
            getPresenterConnectionListener().onDisconnected(console.getConsoleId());

    }


    /**
     * Reject Pending Presenter Connection Request
     * @param connectionRequestId Connection request id
     * @throws RequestAlreadyProcessedException The request has already been given a response previously
     */
    public void rejectConnectionRequest(String connectionRequestId) throws RequestAlreadyProcessedException {
        ConnectionRequestState connectionRequest = connectionRequests.get(connectionRequestId);
        if(connectionRequest.isPending()) //if connection request is un-answered
        {
            connectionRequest.setSuccessful(false);
            connectionRequest.setPending(false);
        }
        else
        {
            throw new RequestAlreadyProcessedException();
        }

    }

    /**
     * Accept a connection request from Presenter subsystem
     * @param connectionRequestId
     * @throws RequestAlreadyProcessedException
     */
    public void acceptConnectionRequest(String connectionRequestId) throws RequestAlreadyProcessedException {

        ConnectionRequestState connectionRequest = connectionRequests.get(connectionRequestId);

        //Check whether request has already been responded
        if(connectionRequest.isPending())
        {
            //Create presenter console to accommodate new presenter
            PresenterConsoleImpl presenterConsole = new PresenterConsoleImpl(this,connectionRequest.getPresenterName());
            presenterConsole.setConnectionRequestId(connectionRequest.getConnectionRequestId());
            presenterConsoles.put(presenterConsole.getConsoleId(),presenterConsole);
            presenterConsole.start(); //Start Service
            connectionRequest.setPresenterConsoleId(presenterConsole.getConsoleId()); //Set console id in connection requst
            connectionRequest.setSuccessful(true);
            connectionRequest.setPending(false);
        }
        else
            throw new RequestAlreadyProcessedException();


    }

    /**
     * Set the currently active presenter to share screen
     * @param presenterConsoleId Presenter Console Id. Input null to disable screen sharing
     * @param includeAudio Should the audio be shared as well
     * @throws ServerConnectionException Server Connection Lost
     * @throws InvalidIdException Presenter Console Id
     * @throws ServerNotReadyException Server is not ready
     */
    public void setScreenAccessPresenter(String presenterConsoleId, boolean includeAudio) throws ServerConnectionException, InvalidIdException, ServerNotReadyException {
        //Is connected to a server?
        if(getFacilitatorConsole() == null)
            throw new ServerNotReadyException("Not connected to a server");


        try {

            if(presenterConsoleId == null || presenterConsoleId.equals(""))
            {
                //Special case: stop sharing
                //indicator to stop sharing
                getFacilitatorConsole().requestScreenAccess(null,includeAudio);

            }
            else if(getPresenterConsole(presenterConsoleId) != null)
            {
                //General Case
                getFacilitatorConsole().requestScreenAccess(presenterConsoleId,includeAudio);
            }
            else
            {
                throw new InvalidIdException("Invalid Presenter Id");
            }

        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }


    /**
     * Set the active presenter for audio sharing
     * @param presenterConsoleId Presenter Console Id for Audio Sharing
     * @throws ServerConnectionException Server Connection Error
     * @throws InvalidIdException Invalid Presenter Console Id
     * @throws ServerNotReadyException Server is not ready
     */
    public void setAudioRelayAccessPresenter(String presenterConsoleId) throws ServerConnectionException, InvalidIdException, ServerNotReadyException {
        if(getFacilitatorConsole() == null)
            throw new ServerNotReadyException("Not connected to a server");
        try {
            if(presenterConsoleId == null || presenterConsoleId.equals(""))
            {
                //indicator to stop sharing
                getFacilitatorConsole().requestAudioRelayAccess(null);

            }
            else if(getPresenterConsole(presenterConsoleId) != null)
            {

                getFacilitatorConsole().requestAudioRelayAccess(presenterConsoleId);
            }
            else
            {
                throw new InvalidIdException("Invalid Presenter Id");
            }

        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }


    /**
     * Retrieve listener to receive updates of presenter
     * @return
     */
    public PresenterModifiedListener getPresenterModifiedListener() {
        return presenterModifiedListener;
    }

    /**
     * Set the listener to receive updates of presenters
     * @param presenterModifiedListener
     */
    public void setPresenterModifiedListener(PresenterModifiedListener presenterModifiedListener) {
        this.presenterModifiedListener = presenterModifiedListener;
    }

    /**
     * Return Virtual Meeting MPI to server
     * @return
     */
    public VirtualMeeting getVirtualMeeting() {
        return virtualMeeting;
    }

    /**
     * Return the Facilitator Console MPI to Server
     * @return
     */
    public FacilitatorConsole getFacilitatorConsole() {
        return facilitatorConsole;
    }

    /**
     * Return Facilitator Console ID from Server
     * @return
     */
    public String getFacilitatorConsoleId() {
        return facilitatorConsoleId;
    }

    /**
     * Return Virtual Meeting Id
     * @return
     */
    public String getVirtualMeetingId() {
        return virtualMeetingId;
    }


    /**
     * Return listener for Screen Share Requests
     * @return
     */
    public ShareRequestListener getShareRequestListener() {
        return shareRequestListener;
    }

    /**
     * Set the listener for Screen Share Requests
     * @param shareRequestListener
     */
    public void setShareRequestListener(ShareRequestListener shareRequestListener) {
        this.shareRequestListener = shareRequestListener;
    }
}
