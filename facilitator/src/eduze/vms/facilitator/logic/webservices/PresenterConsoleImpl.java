package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.*;
import eduze.vms.foundation.logic.webservices.AudioRelayConsoleImpl;
import javafx.stage.Screen;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Madhawa on 13/04/2016.
 */
@WebService(endpointInterface = "eduze.vms.facilitator.logic.webservices.PresenterConsole")
public class PresenterConsoleImpl implements PresenterConsole {
    private FacilitatorImpl facilitator = null;
    private String name = null;
    private String consoleId = null;
    private boolean connectionAcknowledged = false;
    private Endpoint endpoint = null;
    private String connectionRequestId;
    private ArrayList<AssignedTask> assignedTasks = new ArrayList<>();

    private AudioRelayConsoleImpl audioRelayConsole = null;
    private ScreenShareConsoleImpl screenShareConsole = null;

    public PresenterConsoleImpl()
    {

    }

    public PresenterConsoleImpl(FacilitatorImpl facilitator, String name)
    {
        this.facilitator = facilitator;
        this.name = name;
        consoleId = PasswordUtil.generatePresenterConsoleId();
        screenShareConsole = new ScreenShareConsoleImpl(getFacilitator().getConfiguration().getListenerPort(),getFacilitator().getConfiguration().getScreenShareBufferSize());
        audioRelayConsole = new AudioRelayConsoleImpl(getFacilitator().getConfiguration().getListenerPort(),getFacilitator().getConfiguration().getAudioRelayBufferSize());

    }

    public void start()
    {
        screenShareConsole.start();
        audioRelayConsole.start();
        endpoint = Endpoint.publish(UrlGenerator.generatePresenterConsolePublishUrl(facilitator.getConfiguration().getListenerPort(),consoleId),this);
        System.out.println("Presenter Console Started " + UrlGenerator.generatePresenterConsolePublishUrl(facilitator.getConfiguration().getListenerPort(),consoleId));
    }

    void stop()
    {
        //TODO: put stop logic here
        //endpoint.stop();
    }

    FacilitatorImpl getFacilitator()
    {
        return facilitator;
    }


    public boolean isConnectionAcknowledged()
    {
        return connectionAcknowledged;
    }

    //TODO: Improve logic here
    public boolean isConnected()
    {
        return connectionAcknowledged;
    }

    @Override
    public void acknowledgeConnection() throws ServerConnectionException {

        if(!connectionAcknowledged)
        {
            connectionAcknowledged = true;
            getFacilitator().updateVMParticipants();
            //find connection request id
            if(getFacilitator().getPresenterConnectionListener() != null)
                getFacilitator().getPresenterConnectionListener().onConnected(connectionRequestId,consoleId);
        }

    }

    @Override
    public void disconnect() {
        this.stop();
        getFacilitator().disconnectPresenter(this);
    }

    @Override
    public String getConsoleId() {
        return consoleId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String newName) {
        if(!name.equals(newName))
        {
            name = newName;
            if(getFacilitator().getPresenterModifiedListener() != null)
                getFacilitator().getPresenterModifiedListener().presenterNameChanged(consoleId,newName);
        }

    }

    @Override
    public VirtualMeetingSnapshot getVMSnapshot() throws ServerConnectionException {
        VirtualMeetingSnapshot snap = VirtualMeetingSnapshot.fromVirtualMeeting(getFacilitator().getVirtualMeeting());
        return snap;
    }

    @Override
    public String getOutScreenShareConsoleId() {
        return screenShareConsole.getConsoleId();
    }

    @Override
    public String getOutAudioRelayConsoleId() {
        return audioRelayConsole.getConsoleId();
    }

    @Override
    public boolean requestScreenAccess(boolean includeAudio) {
        if(includeAudio)
        {
            ScreenAudioShareRequest shareRequest = new ScreenAudioShareRequest(getConsoleId(),getFacilitator());
            if(getFacilitator().getShareRequestListener() == null)
                return false;
            return getFacilitator().getShareRequestListener().onShareRequest(shareRequest);
        }
        else
        {
            ScreenShareRequest shareRequest = new ScreenShareRequest(getConsoleId(),getFacilitator());
            if(getFacilitator().getShareRequestListener() == null)
                return false;
            return getFacilitator().getShareRequestListener().onShareRequest(shareRequest);
        }

    }

    @Override
    public boolean requestAudioRelayAccess() {
        AudioRelayRequest shareRequest = new AudioRelayRequest(getConsoleId(),getFacilitator());
        if(getFacilitator().getShareRequestListener() == null)
            return false;
        return getFacilitator().getShareRequestListener().onShareRequest(shareRequest);
    }

    @Override
    public ArrayList<AssignedTask> getAssignedTasks() {
        return assignedTasks;
    }

    public void clearAssignedTasks()
    {
        assignedTasks.clear();
    }

    public void addAssignedTask(AssignedTask assignedTask)
    {
        assignedTasks.add(assignedTask);
    }

    public void removeAssignedTask(AssignedTask assignedTask)
    {
        assignedTasks.remove(assignedTask);
    }

    public ScreenShareConsoleImpl getScreenShareConsole()
    {
        return screenShareConsole;
    }

    public AudioRelayConsoleImpl getAudioRelayConsole()
    {
        return audioRelayConsole;
    }

    void setConnectionRequestId(String connectionRequestId) {
        this.connectionRequestId = connectionRequestId;
    }

    String getConnectionRequestId() {
        return connectionRequestId;
    }


}
