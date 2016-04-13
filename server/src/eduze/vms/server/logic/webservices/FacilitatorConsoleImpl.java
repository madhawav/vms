package eduze.vms.server.logic.webservices;

import eduze.vms.PasswordUtil;
import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.URLGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService(endpointInterface = "eduze.vms.server.logic.webservices.FacilitatorConsole")
public class FacilitatorConsoleImpl implements FacilitatorConsole {

    //Think from perspective of user (perspective of facilitator)
    private ScreenShareConsoleImpl inputScreenShareConsole = null; //input to facilitator
    private ScreenShareConsoleImpl outputScreenShareConsole = null; //output to facilitator

    private Endpoint endPoint = null;
    private Facilitator facilitator = null;
    private VirtualMeetingImpl virtualMeeting = null;
    private String consoleId = null;

    private int slotId = -1;

    public FacilitatorConsoleImpl()
    {

    }

    public FacilitatorConsoleImpl(VirtualMeetingImpl vm, Facilitator facilitator)
    {
        this.virtualMeeting = vm;
        consoleId = PasswordUtil.generateFacilitatorConsoleId();
        this.facilitator = facilitator;

    }

    //instalize should be called after facilitator console is added to array in virtual meeting
    void instalize(int slotId)
    {
        this.slotId = slotId;
        if(slotId == 0)
        {
            //data should travel from 0 to 1
            inputScreenShareConsole = virtualMeeting.getDescendingScreenShareConsole();
            outputScreenShareConsole = virtualMeeting.getAscendingScreenShareConsole();
        }
        else if(slotId == 1)
        {
            //data should travel from 1 to 0
            inputScreenShareConsole = virtualMeeting.getAscendingScreenShareConsole();
            outputScreenShareConsole = virtualMeeting.getDescendingScreenShareConsole();
        }

    }

    public void start()
    {
        System.out.println("Facilitator Console Started " + URLGenerator.generateFacilitatorConsolePublishURL(virtualMeeting.getSessionManager().getServer().getPort(),consoleId));
        endPoint = Endpoint.publish(URLGenerator.generateFacilitatorConsolePublishURL(virtualMeeting.getSessionManager().getServer().getPort(),consoleId),this);
    }
    @Override
    public void disconnect() {
        virtualMeeting.removeFacilitatorConsole(this);
        FacilitatorSessionListener listener =  virtualMeeting.getSessionManager().getServer().getFacilitatorSessionListener();
        if(listener != null)
            listener.onDisconnected(facilitator, consoleId);
        //TODO: Causes null pointer exception on stop. figure out why.
        //endPoint.stop();
    }

    @Override
    public String getConsoleId() {
        return consoleId;
    }

    @Override
    public String getFacilitatorName() {
        return facilitator.getName();
    }

    @Override
    public String getInScreenShareConsoleId() {
        return inputScreenShareConsole.getConsoleId();
    }

    @Override
    public String getOutScreenShareConsoleId() {
        return outputScreenShareConsole.getConsoleId();
    }

    @Override
    public boolean requestScreenAccess(String presenterId, boolean includeAudio) {
        virtualMeeting.setActiveScreenFacilitatorId(consoleId);
        virtualMeeting.setActiveScreenPresenterId(presenterId);
        return true;
    }

    Facilitator getFacilitator()
    {
        return facilitator;
    }

    public ScreenShareConsoleImpl getInputScreenShareConsole() {
        return inputScreenShareConsole;
    }

    public ScreenShareConsoleImpl getOutputScreenShareConsole() {
        return outputScreenShareConsole;
    }
}
