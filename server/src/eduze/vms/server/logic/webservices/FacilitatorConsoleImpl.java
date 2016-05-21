package eduze.vms.server.logic.webservices;

import eduze.vms.server.logic.PasswordUtil;
import eduze.vms.foundation.logic.webservices.AudioRelayConsoleImpl;
import eduze.vms.foundation.logic.webservices.ScreenShareConsoleImpl;
import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.URLGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService(endpointInterface = "eduze.vms.server.logic.webservices.FacilitatorConsole")
public class FacilitatorConsoleImpl implements FacilitatorConsole {

    //Think from perspective of user (perspective of facilitator)
    private ScreenShareConsoleImpl inputScreenShareConsole = null; //input to facilitator
    private ScreenShareConsoleImpl outputScreenShareConsole = null; //output to facilitator

    private AudioRelayConsoleImpl inputAudioRelayConsole = null; //input to facilitator
    private AudioRelayConsoleImpl outputAudioRelayConsole = null; //output to facilitator

    private HashMap<String,VMParticipant> participants = null;

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
        this.participants = new HashMap<>();
    }

    //initialize should be called after facilitator console is added to array in virtual meeting
    void initialize(int slotId)
    {
        this.slotId = slotId;
        if(slotId == 0)
        {
            //data should travel from 0 to 1
            inputScreenShareConsole = virtualMeeting.getDescendingScreenShareConsole();
            inputAudioRelayConsole = virtualMeeting.getDescendingAudioRelayConsole();
            outputScreenShareConsole = virtualMeeting.getAscendingScreenShareConsole();
            outputAudioRelayConsole =virtualMeeting.getAscendingAudioRelayConsole();
        }
        else if(slotId == 1)
        {
            //data should travel from 1 to 0
            inputScreenShareConsole = virtualMeeting.getAscendingScreenShareConsole();
            inputAudioRelayConsole = virtualMeeting.getAscendingAudioRelayConsole();
            outputScreenShareConsole = virtualMeeting.getDescendingScreenShareConsole();
            outputAudioRelayConsole = virtualMeeting.getDescendingAudioRelayConsole();
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
        getOutputAudioRelayConsole().stop();
        getOutputScreenShareConsole().stop();
        getInputAudioRelayConsole().stop();
        getInputScreenShareConsole().stop();
       // endPoint.stop();
        FacilitatorSessionListener listener =  virtualMeeting.getSessionManager().getServer().getFacilitatorSessionListener();

        if(listener != null)
            listener.onDisconnected(facilitator,consoleId);
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
    public String getInAudioRelayConsoleId() {
        return inputAudioRelayConsole.getConsoleId();
    }

    @Override
    public String getOutAudioRelayConsoleId() {
        return outputAudioRelayConsole.getConsoleId();
    }

    @Override
    public boolean requestScreenAccess(String presenterId, boolean includeAudio) {
        virtualMeeting.setActiveScreenFacilitatorId(consoleId);
        virtualMeeting.setActiveScreenPresenterId(presenterId);

        if(includeAudio)
        {
            virtualMeeting.setActiveSpeechFacilitatorId(consoleId);
            virtualMeeting.setActiveSpeechPresenterId(presenterId);
        }
        return true;
    }

    @Override
    public boolean requestAudioRelayAccess(String presenterId) {
        virtualMeeting.setActiveSpeechFacilitatorId(consoleId);
        virtualMeeting.setActiveSpeechPresenterId(presenterId);
        return true;
    }

    @Override
    public Collection<VMParticipant> getParticipants() {
        return participants.values();
    }

    @Override
    public VMParticipant getParticipant(String participantId) {
        return participants.get(participantId);
    }

    @Override
    public void setParticipants(Collection<VMParticipant> participants) {
        this.participants.clear();
        for(VMParticipant participant: participants)
        {
            this.participants.put(participant.getPresenterId(),participant);
        }
    }

    @Override
    public void adjournMeeting() {
        virtualMeeting.adjournMeeting();
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

    public AudioRelayConsoleImpl getInputAudioRelayConsole() {
        return inputAudioRelayConsole;
    }

    public AudioRelayConsoleImpl getOutputAudioRelayConsole() {
        return outputAudioRelayConsole;
    }

}
