package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.ServerConnectionException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Collection;

/**
 * Created by Madhawa on 13/04/2016.
 */
@WebService
public interface PresenterConsole {
    /**
     * Confirmation from presenter to facilitator
     */
    @WebMethod
    public void acknowledgeConnection() throws ServerConnectionException;

    /**
     * Disconnect from Facilitator
     */
    @WebMethod
    public void disconnect() throws ServerConnectionException;

    /**
     * Return console id
     * @return
     */
    @WebMethod
    public String getConsoleId();

    /**
     * Return name of Presenter as set in Facilitator
     * @return Name of Presenter
     */
    @WebMethod
    public String getName();

    /**
     * Set the name of presenter as recognized by facilitator
     * @param newName
     */
    @WebMethod
    public void setName(@WebParam(name = "NewName") String newName) throws DisconnectedException;

    /**
     * Return snapshot of current status of virtual meeting
     * @return Snapshot of Virtual Meeting
     */
    @WebMethod
    public VirtualMeetingSnapshot getVMSnapshot() throws ServerConnectionException;

    /**
     * Retrieve ID for ScreenShareConsole
     * @return
     */
    @WebMethod
    public String getOutScreenShareConsoleId() throws DisconnectedException;

    /**
     * Retrieve ID for AudioRelayConsole
     * @return
     */
    @WebMethod
    public String getOutAudioRelayConsoleId() throws DisconnectedException;

    /**
     * Request permission to share screen
     * @param includeAudio True to request audio share as well
     * @return true if request is considered. false if request is immediately rejected.
     */
    @WebMethod
    public boolean requestScreenAccess(@WebParam(name = "IncludeAudio") boolean includeAudio) throws DisconnectedException;

    /**
     * Request permission to speak
     * @return True if request is being considered. False if request is immediately rejected.
     */
    @WebMethod
    public boolean requestAudioRelayAccess() throws DisconnectedException;

    /**
     * Retrieve assigned tasks of presenter
     * @return
     */
    @WebMethod
    public Collection<AssignedTask> getAssignedTasks() throws DisconnectedException;

    /**
     * Notify the Facilitator that presenter is alive
     * @throws DisconnectedException
     */
    @WebMethod
    public void notifyAlive() throws DisconnectedException;


}
