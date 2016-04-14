package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.ConnectionRequest;
import eduze.vms.facilitator.logic.PasswordUtil;
import eduze.vms.facilitator.logic.ServerConnectionException;
import eduze.vms.facilitator.logic.UrlGenerator;
import javafx.stage.Screen;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

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
    }

    public void start()
    {
        screenShareConsole.start();
        endpoint = Endpoint.publish(UrlGenerator.generatePresenterConsolePublishUrl(facilitator.getConfiguration().getListenerPort(),consoleId),this);
        System.out.println("Presenter Console Started " + UrlGenerator.generatePresenterConsolePublishUrl(facilitator.getConfiguration().getListenerPort(),consoleId));
    }

    void stop()
    {
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
    public void acknowledgeConnection() {

        if(!connectionAcknowledged)
        {
            connectionAcknowledged = true;
            //find connection request id
            if(getFacilitator().getPresenterConnectionListener() != null)
                getFacilitator().getPresenterConnectionListener().onConnected(connectionRequestId,consoleId);
        }

    }

    @Override
    public void disconnect() {
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

    public ScreenShareConsoleImpl getScreenShareConsole()
    {
        return screenShareConsole;
    }

    void setConnectionRequestId(String connectionRequestId) {
        this.connectionRequestId = connectionRequestId;
    }

    String getConnectionRequestId() {
        return connectionRequestId;
    }
}
