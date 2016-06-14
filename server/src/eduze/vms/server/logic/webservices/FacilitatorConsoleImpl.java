package eduze.vms.server.logic.webservices;

import eduze.vms.server.logic.PasswordUtil;
import eduze.vms.foundation.logic.webservices.AudioRelayConsoleImpl;
import eduze.vms.foundation.logic.webservices.ScreenShareConsoleImpl;
import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.URLGenerator;
import org.apache.log4j.Logger;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.Collection;
import java.util.HashMap;

/**
 * Implementation of FacilitatorConsole
 * The Main WebService offered for Facilitator Subsystem to interact with the Server.
 * Each Facilitator is provided with a unique Facilitator Console.
 */
@WebService(endpointInterface = "eduze.vms.server.logic.webservices.FacilitatorConsole")
public class FacilitatorConsoleImpl implements FacilitatorConsole {

    //Think from perspective of user (perspective of facilitator)
    private ScreenShareConsoleImpl inputScreenShareConsole = null; //screen input to facilitator
    private ScreenShareConsoleImpl outputScreenShareConsole = null; //screen output from facilitator

    private AudioRelayConsoleImpl inputAudioRelayConsole = null; //audio input to facilitator
    private AudioRelayConsoleImpl outputAudioRelayConsole = null; //audio output to facilitator

    //Map of Presenters connected to Facilitator
    private HashMap<String,VMParticipant> participants = null;

    //Endpoint used in WebService
    private Endpoint endPoint = null;
    //Facilitator Information including pair information
    private Facilitator facilitator = null;
    //Virtual Meeting
    private VirtualMeetingImpl virtualMeeting = null;
    private String consoleId = null; //Facilitator Console Id

    //Most Recent time at which the Facilitator reported that its connected
    private long lastAliveNotifyTime = -1;

    //Index of Facilitator in array of Facilitators
    private int slotId = -1;

    public FacilitatorConsoleImpl()
    {

    }

    /**
     * Constructor for Facilitator Console
     * @param vm Virtual Meeting Service
     * @param facilitator Facilitator Information
     */
    public FacilitatorConsoleImpl(VirtualMeetingImpl vm, Facilitator facilitator)
    {
        this.virtualMeeting = vm;
        consoleId = PasswordUtil.generateFacilitatorConsoleId();
        this.facilitator = facilitator;
        this.participants = new HashMap<>();
        lastAliveNotifyTime = System.currentTimeMillis();
    }

    /**
     * Initialise the Facilitator. Should be called after Facilitator Console is added to Array of Consoles in Virtual Meeting
     * @param slotId Index to which Facilitator Console is added
     */
    void initialize(int slotId)
    {
        this.slotId = slotId;
        if(slotId == 0)
        {
            //Setup transfer route between Facilitator at slot 0 to Facilitator to slot 1
            inputScreenShareConsole = virtualMeeting.getDescendingScreenShareConsole();
            inputAudioRelayConsole = virtualMeeting.getDescendingAudioRelayConsole();
            outputScreenShareConsole = virtualMeeting.getAscendingScreenShareConsole();
            outputAudioRelayConsole =virtualMeeting.getAscendingAudioRelayConsole();
        }
        else if(slotId == 1)
        {
            //Setup transfer route between Facilitator at slot 1 and Facilitator at slot 0
            inputScreenShareConsole = virtualMeeting.getAscendingScreenShareConsole();
            inputAudioRelayConsole = virtualMeeting.getAscendingAudioRelayConsole();
            outputScreenShareConsole = virtualMeeting.getDescendingScreenShareConsole();
            outputAudioRelayConsole = virtualMeeting.getDescendingAudioRelayConsole();
        }

    }

    /**
     * Start the Facilitator Console
     */
    public void start()
    {
        Logger.getLogger(this.getClass()).info("Facilitator Console Started " + URLGenerator.generateFacilitatorConsolePublishURL(virtualMeeting.getSessionManager().getServer().getPort(),consoleId));
        endPoint = Endpoint.publish(URLGenerator.generateFacilitatorConsolePublishURL(virtualMeeting.getSessionManager().getServer().getPort(),consoleId),this);
    }

    /**
     * Disconnect Facilitator from Server
     */
    @Override
    public void disconnect() {
        virtualMeeting.removeFacilitatorConsole(this);

        //Stop Related Web Services
        getOutputAudioRelayConsole().stop();
        getOutputScreenShareConsole().stop();
        getInputAudioRelayConsole().stop();
        getInputScreenShareConsole().stop();

        //report listeners
        FacilitatorSessionListener listener =  virtualMeeting.getSessionManager().getServer().getFacilitatorSessionListener();

        if(listener != null)
            listener.onDisconnected(facilitator,consoleId);
        //endPoint.stop();
    }

    /**
     * Retrieve Facilitator Console Id
     * @return FacilitatorConsoleId
     */
    @Override
    public String getConsoleId() {
        return consoleId;
    }

    /**
     * Retrieve Facilitator Name
     * @return Facilitators Name
     */
    @Override
    public String getFacilitatorName() {
        return facilitator.getName();
    }

    /**
     * Retrieve the ConsoleID used to send Screen Capture data to Facilitator
     * @return Screen Share Console ID of console used to transfer screen captures from Server to Facilitator
     */
    @Override
    public String getInScreenShareConsoleId() {
        return inputScreenShareConsole.getConsoleId();
    }

    /**
     * Retrieve the ConsoleID of console used to send Screen Capture data from Facilitator to Server
     * @return Screen Share Console ID of console used to transfer screen captures from Facilitator to Server
     */
    @Override
    public String getOutScreenShareConsoleId() {
        return outputScreenShareConsole.getConsoleId();
    }

    /**
     * Retrieve the ConsoleID used to send Audio Capture data to Facilitator
     * @return Audio Relay Console ID of console used to transfer audio captures from Server to Facilitator
     */
    @Override
    public String getInAudioRelayConsoleId() {
        return inputAudioRelayConsole.getConsoleId();
    }

    /**
     * Retrieve the ConsoleID of console used to send Audio Capture data from Facilitator to Server
     * @return Audio Relay Console ID of console used to transfer audio captures from Facilitator to Server
     */
    @Override
    public String getOutAudioRelayConsoleId() {
        return outputAudioRelayConsole.getConsoleId();
    }

    /**
     * Request Permission to Share Screen
     * @param presenterId Presenter ID of presenter requesting screen share
     * @param includeAudio Should audio relay be also requested?
     * @return True if request is taken to consideration. False if request is immediately rejected.
     */
    @Override
    public boolean requestScreenAccess(String presenterId, boolean includeAudio) {
        //Accommodate request by changing Virtual Meeting state variables
        virtualMeeting.setActiveScreenFacilitatorId(consoleId);
        virtualMeeting.setActiveScreenPresenterId(presenterId);

        if(includeAudio)
        {
            virtualMeeting.setActiveSpeechFacilitatorId(consoleId);
            virtualMeeting.setActiveSpeechPresenterId(presenterId);
        }
        return true;
    }

    /**
     * Request Permission to share audio
     * @param presenterId Presenter ID of presenter requesting audio share
     * @return True if request is taken to consideration. False if request is immediately rejected.
     */
    @Override
    public boolean requestAudioRelayAccess(String presenterId) {
        //Change the Virtual Meeting state variables to accommodate request
        virtualMeeting.setActiveSpeechFacilitatorId(consoleId);
        virtualMeeting.setActiveSpeechPresenterId(presenterId);
        return true;
    }

    /**
     * Retrieve the list of Presenters reported to Server as connected to self
     * @return List of Participants reported to Server as connected to self
     */
    @Override
    public Collection<VMParticipant> getParticipants() {
        return participants.values();
    }

    /**
     * Retrieve a participant from the list of presenters reported to Server as connected to Self
     * @param participantId Participant Id to retrieve Participant
     * @return Participant with given participant id
     */
    @Override
    public VMParticipant getParticipant(String participantId) {
        return participants.get(participantId);
    }

    /**
     * Update the Server on set of presenters connected to self
     * @param participants Set of Presenters
     */
    @Override
    public void setParticipants(Collection<VMParticipant> participants) {
        this.participants.clear();
        for(VMParticipant participant: participants)
        {
            this.participants.put(participant.getPresenterId(),participant);
        }
    }


    /**
     * Adjourn the meeting
     */
    @Override
    public void adjournMeeting() {
        //Request VM to adjourn the meeting
        virtualMeeting.adjournMeeting();
    }

    /**
     * Notify server that facilitator is alive
     */
    @Override
    public void notifyAlive() {
        lastAliveNotifyTime = System.currentTimeMillis();
    }

    /**
     * Retrieve last recorded time of alive
     * @return
     */
    public long getLastAliveNotifyTime() {
        return lastAliveNotifyTime;
    }


    /**
     * Retrieve the Facilitator Information
     * @return Facilitator Information
     */
    Facilitator getFacilitator()
    {
        return facilitator;
    }


    /**
     * Retrieve the Console used to send Screen Capture data to Facilitator
     * @return Screen Share Console used to transfer screen captures from Server to Facilitator
     */
    public ScreenShareConsoleImpl getInputScreenShareConsole() {
        return inputScreenShareConsole;
    }

    /**
     * Retrieve the console used to send Screen Capture data from Facilitator to Server
     * @return Screen Share Console used to transfer screen captures from Facilitator to Server
     */
    public ScreenShareConsoleImpl getOutputScreenShareConsole() {
        return outputScreenShareConsole;
    }

    /**
     * Retrieve the Console used to send Audio Capture data to Facilitator
     * @return Audio Relay Console used to transfer audio captures from Server to Facilitator
     */
    public AudioRelayConsoleImpl getInputAudioRelayConsole() {
        return inputAudioRelayConsole;
    }


    /**
     * Retrieve the Console used to send Audio Capture data from Facilitator to Server
     * @return Audio Relay Console used to transfer audio captures from Facilitator to Server
     */
    public AudioRelayConsoleImpl getOutputAudioRelayConsole() {
        return outputAudioRelayConsole;
    }

}
