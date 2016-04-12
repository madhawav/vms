package eduze.vms.server.logic.webservices;

import eduze.vms.PasswordUtil;
import eduze.vms.server.logic.URLGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService(endpointInterface = "eduze.vms.server.logic.webservices.VirtualMeeting")
public class VirtualMeetingImpl implements VirtualMeeting {
    private String virtualMeetingId;
    private VMSessionManagerImpl sessionManager;

    private String activeScreenFacilitatorId = null;
    private String activeScreenPresenterId = null;
    private String activeSpeechFacilitatorId = null;
    private String activeSpeechPresenterId = null;

    private  FacilitatorConsoleImpl[] facilitatorConsoles = new FacilitatorConsoleImpl[2];

    private ScreenShareConsoleImpl ascendingScreenShareConsole = null;
    private ScreenShareConsoleImpl descendingScreenShareConsole = null;

    private SessionStatus status = SessionStatus.NotReady;

    public VirtualMeetingImpl()
    {

    }

    public void setActiveScreenPresenterId(String activeScreenPresenterId) {
        this.activeScreenPresenterId = activeScreenPresenterId;
    }

    public void setActiveScreenFacilitatorId(String activeScreenFacilitatorId) {
        getFacilitatorConsole(activeScreenFacilitatorId).getOutputScreenShareConsole().setEnabled(true);
        getFacilitatorConsole(activeScreenFacilitatorId).getInputScreenShareConsole().setEnabled(false);
        this.activeScreenFacilitatorId = activeScreenFacilitatorId;
    }

    public ScreenShareConsoleImpl getAscendingScreenShareConsole() {
        return ascendingScreenShareConsole;
    }

    public ScreenShareConsoleImpl getDescendingScreenShareConsole() {
        return descendingScreenShareConsole;
    }

    public VirtualMeetingImpl(VMSessionManagerImpl sessionManager)
    {
        this.sessionManager = sessionManager;
        this.virtualMeetingId = PasswordUtil.generateVMId();

        ServerImpl.Configuration configuration = getSessionManager().getServer().getConfiguration();
        this.ascendingScreenShareConsole = new ScreenShareConsoleImpl(configuration.getPort(),configuration.getScreenShareBufferSize());
        this.descendingScreenShareConsole = new ScreenShareConsoleImpl(configuration.getPort(),configuration.getScreenShareBufferSize());
    }

    public VMSessionManagerImpl getSessionManager() {
        return sessionManager;
    }

    @Override
    public String getVMId() {
        return virtualMeetingId;
    }

    @Override
    public String getActiveScreenFacilitatorId() {
        return activeScreenFacilitatorId;
    }

    @Override
    public String getActiveScreenPresenterId() {
        return activeScreenPresenterId;
    }

    @Override
    public String getActiveSpeechFacilitatorId() {
        return activeSpeechFacilitatorId;
    }

    @Override
    public String getActiveSpeechPresenterId() {
        return activeSpeechPresenterId;
    }

    @Override
    public SessionStatus getStatus() {
        return status;
    }


    FacilitatorConsoleImpl getFacilitatorConsole(String consoleId)
    {
        for(FacilitatorConsoleImpl fac: facilitatorConsoles)
        {
            if(fac != null)
                if(fac.getConsoleId() == consoleId)
                {
                    return fac;
                }
        }
        return null;
    }

    int addNewFacilitatorConsole(FacilitatorConsoleImpl console)
    {

        for(int i = 0; i < facilitatorConsoles.length; i++)
        {
            if(facilitatorConsoles[i] == null)
            {
                facilitatorConsoles[i] = console;
                return i;
            }
        }

        throw new ArrayIndexOutOfBoundsException();

    }

    void removeFacilitatorConsole(FacilitatorConsoleImpl console)
    {
        for(int i = 0; i < facilitatorConsoles.length;i++)
        {
            if(facilitatorConsoles[i].getConsoleId().equals(console.getConsoleId()))
                facilitatorConsoles[i] = null;
        }

    }

    int getFacilitatorConsoleCount()
    {
        int count = 0;
        for(FacilitatorConsole con: facilitatorConsoles)
        {
            if(con != null)
                count++;
        }
        return  count;
    }

    FacilitatorConsoleImpl[] getFacilitatorConsoles()
    {
        return facilitatorConsoles;
    }

    public void start()
    {
        this.status = SessionStatus.WaitingForFirstFacilitator;

        ascendingScreenShareConsole.start();
        descendingScreenShareConsole.start();
        System.out.println("Virtual Meeting Started " + URLGenerator.generateVMPublishURL(sessionManager.getServer().getPort(),virtualMeetingId) );
        Endpoint.publish(URLGenerator.generateVMPublishURL(sessionManager.getServer().getPort(),virtualMeetingId),this);
    }

    void updateStatus() {
        if(status == SessionStatus.NotReady)
            return;
        if(getFacilitatorConsoleCount() == 0)
            status = SessionStatus.WaitingForFirstFacilitator;
        if(getFacilitatorConsoleCount() == 1)
            status = SessionStatus.WaitingForSecondFacilitator;

        if(getFacilitatorConsoleCount() == 2)
            status = SessionStatus.MeetingOnline;
    }
}
