package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.*;
import eduze.vms.foundation.logic.webservices.AudioRelayConsoleImpl;
import eduze.vms.foundation.logic.webservices.ScreenShareConsoleImpl;
import javafx.stage.Screen;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Presenter Console WebService provided to Presenter Subsystem. This is the main service used by presenter subsystem to connect with facilitator.
 * Each presenter is provided with a unique instance of PresenterConsole.
 */
@WebService(endpointInterface = "eduze.vms.facilitator.logic.webservices.PresenterConsole")
public class PresenterConsoleImpl implements PresenterConsole {
    private FacilitatorImpl facilitator = null; //Facilitator WebService
    private String name = null; //Name of Presenter
    private String consoleId = null; //Presenter Console Id
    private boolean connectionAcknowledged = false; //Indicate whether connection is acknowledged by server
    private Endpoint endpoint = null; //The WebService Endpoint for Presenter Console Web Service
    private String connectionRequestId; //Connection request id used to generate this presenter-facilitator connection
    private ArrayList<AssignedTask> assignedTasks = new ArrayList<>(); //List of assigned tasks to presenter

    private AudioRelayConsoleImpl audioRelayConsole = null; //Audio relay console of presenter
    private ScreenShareConsoleImpl screenShareConsole = null; //Screen share console of presenter

    public PresenterConsoleImpl()
    {

    }

    /**
     * Initiate a WebService interface to communicate with presenter
     * @param facilitator Facilitator used to host the console
     * @param name Name of presenter
     */
    public PresenterConsoleImpl(FacilitatorImpl facilitator, String name)
    {
        this.facilitator = facilitator;
        this.name = name;
        consoleId = PasswordUtil.generatePresenterConsoleId(); //generate console id
        //Setup screen share console to presenter
        screenShareConsole = new ScreenShareConsoleImpl(getFacilitator().getConfiguration().getListenerPort(),getFacilitator().getConfiguration().getScreenShareBufferSize());
        //Setup audio relay console to presenter
        audioRelayConsole = new AudioRelayConsoleImpl(getFacilitator().getConfiguration().getListenerPort(),getFacilitator().getConfiguration().getAudioRelayBufferSize());

    }

    /**
     * Start the Presenter Console Web Service
     */
    public void start()
    {
        //Start the web services
        screenShareConsole.start();
        audioRelayConsole.start();
        endpoint = Endpoint.publish(UrlGenerator.generatePresenterConsolePublishUrl(facilitator.getConfiguration().getListenerPort(),consoleId),this);
        Logger.getLogger("DEBUG").log(Level.INFO,"Presenter Console Started " + UrlGenerator.generatePresenterConsolePublishUrl(facilitator.getConfiguration().getListenerPort(),consoleId));
    }

    /**
     * Stop the Web Service
     */
    void stop()
    {
        screenShareConsole.stop();
        audioRelayConsole.stop();
        //TODO: put stop logic here
        //endpoint.stop();
    }

    /**
     * Return Facilitator WebService
     * @return
     */
    FacilitatorImpl getFacilitator()
    {
        return facilitator;
    }

    /**
     * Has the presenter acknowledged the connection
     * @return
     */
    public boolean isConnectionAcknowledged()
    {
        return connectionAcknowledged;
    }


    /**
     * Is the presenter connected to Facilitator
     * @return
     */
    public boolean isConnected()
    {
       if(!connectionAcknowledged)
           return false;
        if(getFacilitator().getPresenterConsole(consoleId) == null)
            return false;
        return true;
        //TODO: Improve logic here
    }

    /**
     * Acknowledge the connection with Facilitator. This should be called by Presenter just after connecting with Presenter console
     * @throws ServerConnectionException
     */
    @Override
    public void acknowledgeConnection() throws ServerConnectionException {

        if(!connectionAcknowledged)
        {
            //if not acknowledged
            connectionAcknowledged = true;
            //report the updated list of participants to server since now we have a new participant
            getFacilitator().updateVMParticipants();
            //notify listeners
            if(getFacilitator().getPresenterConnectionListener() != null)
                getFacilitator().getPresenterConnectionListener().onConnected(connectionRequestId,consoleId);
        }

    }

    /**
     * Disconnect Presenter from Facilitator
     */
    @Override
    public void disconnect() throws ServerConnectionException {
        getFacilitator().disconnectPresenter(this);
        this.stop();
    }

    /**
     * Get Presenter Console Id
     * @return
     */
    @Override
    public String getConsoleId()  {

            return consoleId;
    }

    /**
     * Get presenters name as identified by facilitator
     * @return
     */
    @Override
    public String getName(){
        return name;
    }

    /**
     * Set presenters name as identified by facilitator
     * @param newName
     */
    @Override
    public void setName(String newName) throws DisconnectedException {
        if(!isConnected())
            throw new DisconnectedException();
        if(!name.equals(newName))
        {
            name = newName;
            //Notify name change to listeners
            if(getFacilitator().getPresenterModifiedListener() != null)
                getFacilitator().getPresenterModifiedListener().presenterNameChanged(consoleId,newName);
        }

    }

    /**
     * Retrieve last known Virtual Meeting Status from Facilitator
     * @return Last Known Virtual Meeting Status
     * @throws ServerConnectionException
     */
    @Override
    public VirtualMeetingSnapshot getVMSnapshot() throws ServerConnectionException{

        //Generate Virtual Meeting Snapshot from Virtual Meeting provided by Facilitator
        VirtualMeetingSnapshot snap = VirtualMeetingSnapshot.fromVirtualMeeting(getFacilitator().getVirtualMeeting());
        return snap;
    }

    /**
     * Retrieve ID of Screen Share console used by presenter
     * @return
     */
    @Override
    public String getOutScreenShareConsoleId()  {
        return screenShareConsole.getConsoleId();
    }

    /**
     * Retrieve ID of Audio Relay Console used by presenter
     * @return
     */
    @Override
    public String getOutAudioRelayConsoleId() {
        return audioRelayConsole.getConsoleId();
    }

    /**
     * Request facilitator to allow screen share from presenter
     * @param includeAudio True to request audio share as well
     * @return
     */
    @Override
    public boolean requestScreenAccess(boolean includeAudio) throws DisconnectedException {
        if(!isConnected())
            throw new DisconnectedException();
        if(includeAudio)
        {
            //Generate share request
            ScreenAudioShareRequest shareRequest = new ScreenAudioShareRequest(getConsoleId(),getFacilitator());
            //Notify listeners
            if(getFacilitator().getShareRequestListener() == null)
                return false;
            return getFacilitator().getShareRequestListener().onShareRequest(shareRequest);
        }
        else
        {
            //Generate Share Request
            ScreenShareRequest shareRequest = new ScreenShareRequest(getConsoleId(),getFacilitator());
            //Notify listeners
            if(getFacilitator().getShareRequestListener() == null)
                return false;
            return getFacilitator().getShareRequestListener().onShareRequest(shareRequest);
        }

    }

    /**
     * Request facilitator to allow share audio from presenter
     * @return
     */
    @Override
    public boolean requestAudioRelayAccess() throws DisconnectedException {
        if(!isConnected())
            throw new DisconnectedException();
        //Generate Share Request
        AudioRelayRequest shareRequest = new AudioRelayRequest(getConsoleId(),getFacilitator());
        //Notify listeners
        if(getFacilitator().getShareRequestListener() == null)
            return false;
        return getFacilitator().getShareRequestListener().onShareRequest(shareRequest);
    }

    /**
     * Retrieve list of assigned tasks to presenter
     * @return Array of AssignedTasks
     */
    @Override
    public ArrayList<AssignedTask> getAssignedTasks() throws DisconnectedException {

        if(!isConnected())
            throw new DisconnectedException();
        return assignedTasks;
    }

    /**
     * Clear list of assigned tasks of presenter
     */
    public void clearAssignedTasks()
    {
        assignedTasks.clear();
    }

    /**
     * Add an assigned task to list of tasks for presenter
     * @param assignedTask
     */
    public void addAssignedTask(AssignedTask assignedTask)
    {
        assignedTasks.add(assignedTask);
    }

    /**
     * Remove an assigned task from list of assigned tasks of presenter
     * @param assignedTask
     */
    public void removeAssignedTask(AssignedTask assignedTask)
    {
        assignedTasks.remove(assignedTask);
    }

    /**
     * Retrieve Screen share console of presenter
     * @return
     */
    public ScreenShareConsoleImpl getScreenShareConsole()
    {
        return screenShareConsole;
    }

    /**
     * Retrieve audio relay console of presenter
     * @return
     */
    public AudioRelayConsoleImpl getAudioRelayConsole()
    {
        return audioRelayConsole;
    }

    /**
     * Set the connection request id of presenter-facilitator connection
     * @param connectionRequestId
     */
    void setConnectionRequestId(String connectionRequestId) {
        this.connectionRequestId = connectionRequestId;
    }

    /**
     * Retrieve connection request if of presenter-facilitator connection
     * @return
     */
    String getConnectionRequestId() {
        return connectionRequestId;
    }


}
