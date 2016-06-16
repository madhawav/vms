package eduze.vms.server.logic.webservices;

import eduze.vms.server.logic.PasswordUtil;
import eduze.vms.foundation.logic.webservices.AudioRelayConsoleImpl;
import eduze.vms.foundation.logic.webservices.ScreenShareConsoleImpl;
import eduze.vms.server.logic.URLGenerator;
import org.apache.log4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Madhawa on 12/04/2016.
 */
/**
 * Implementation of WebService that provides core information about virtual meeting to presenters
 */
@WebService(endpointInterface = "eduze.vms.server.logic.webservices.VirtualMeeting")
public class VirtualMeetingImpl implements VirtualMeeting {
    private String virtualMeetingId; //unique id of virtual meeting
    private VMSessionManagerImpl sessionManager; //session manager of virtual meeting

    //active entities
    private String activeScreenFacilitatorId = null;
    private String activeScreenPresenterId = null;
    private String activeSpeechFacilitatorId = null;
    private String activeSpeechPresenterId = null;

    //consoles of connected facilitators
    private  FacilitatorConsoleImpl[] facilitatorConsoles = new FacilitatorConsoleImpl[2];

    //consoles used for screen share
    private ScreenShareConsoleImpl ascendingScreenShareConsole = null; //from index 0 facilitator to index 1 facilitator
    private ScreenShareConsoleImpl descendingScreenShareConsole = null; //from index 1 facilitator to index 0 facilitator

    //consoles used for audio share
    private AudioRelayConsoleImpl ascendingAudioRelayConsole = null; //from index 0 facilitator to index 1 facilitator
    private AudioRelayConsoleImpl descendingAudioRelayConsole = null; //from index 1 facilitator to index 0 facilitator

    //status of virtual meeting
    private SessionStatus status = SessionStatus.NotReady;

    //shared tasks in virtual meeting
    private HashMap<String,SharedTask> sharedTasks = new HashMap();

    //endpoint of service
    private Endpoint endpoint = null;

    /**
     * Update the presenter name of shared task to friendly name of presenter
     * @param task
     */
    private void updateSharedTaskPresenterName(SharedTask task)
    {
        if(task.getAssignedPresenterId() == null)
        {
            task.setAssignedPresenterName(null);
            return;
        }
        VMParticipant participant = getParticipant(task.getAssignedPresenterId());
        if(participant != null)
        {
            task.setAssignedPresenterName(participant.getName());
        }
    }

    /**
     * update presenter names of shared tasks, assigned to presenters connected to current facilitator
     */
    private void updateSharedTaskPresenterNames()
    {
        for(SharedTask task : sharedTasks.values())
        {
            updateSharedTaskPresenterName(task);
        }
    }

    public VirtualMeetingImpl()
    {

    }

    /**
     * Set the active presenter for screen share
     * @param activeScreenPresenterId Presenter ID of active presenter to screen share
     */
    public void setActiveScreenPresenterId(String activeScreenPresenterId) {
        this.activeScreenPresenterId = activeScreenPresenterId;
    }

    /**
     * Set the active presenter to screen share
     * @param activeScreenFacilitatorId Facilitator Id of active presenter to screen share
     */
    public void setActiveScreenFacilitatorId(String activeScreenFacilitatorId) {
        //update enable status of communication channel
        getFacilitatorConsole(activeScreenFacilitatorId).getOutputScreenShareConsole().setEnabled(true);
        getFacilitatorConsole(activeScreenFacilitatorId).getInputScreenShareConsole().setEnabled(false);
        this.activeScreenFacilitatorId = activeScreenFacilitatorId;
    }


    /**
     * Set the active presenter for speech share
     * @param activeSpeechPresenterId Presenter ID of active presenter to speech share
     */
    public void setActiveSpeechPresenterId(String activeSpeechPresenterId) {
        this.activeSpeechPresenterId = activeSpeechPresenterId;
    }

    /**
     * Set the active presenter to speech share
     * @param activeSpeechFacilitatorId Facilitator Id of active presenter to audio share
     */
    public void setActiveSpeechFacilitatorId(String activeSpeechFacilitatorId) {
        //update enable status of communication channel
        getFacilitatorConsole(activeSpeechFacilitatorId).getOutputAudioRelayConsole().setEnabled(true);
        getFacilitatorConsole(activeSpeechFacilitatorId).getInputAudioRelayConsole().setEnabled(false);
        this.activeSpeechFacilitatorId = activeSpeechFacilitatorId;
    }

    /**
     * Retrieve screen share console used to share screen from facilitator 0 to facilitator 1
     * @return Screen Share Console used to share screen from facilitator 0 to facilitator 1
     */
    public ScreenShareConsoleImpl getAscendingScreenShareConsole() {
        return ascendingScreenShareConsole;
    }

    /**
     * Retrieve screen share console used to share screen from facilitator 1 to facilitator 0
     * @return Screen Share Console used to share screen from facilitator 1 to facilitator 0
     */
    public ScreenShareConsoleImpl getDescendingScreenShareConsole() {
        return descendingScreenShareConsole;
    }

    /**
     * Retrieve speech share console used to share speech from facilitator 0 to facilitator 1
     * @return speech Share Console used to share speech from facilitator 0 to facilitator 1
     */
    public AudioRelayConsoleImpl getAscendingAudioRelayConsole() {
        return ascendingAudioRelayConsole;
    }

    /**
     * Retrieve speech share console used to share speech from facilitator 1 to facilitator 0
     * @return speech Share Console used to share speech from facilitator 1 to facilitator 0
     */
    public AudioRelayConsoleImpl getDescendingAudioRelayConsole() {
        return descendingAudioRelayConsole;
    }

    /**
     * Construction of Virtual Meeting
     * @param sessionManager Session Manager used to generate Virtual Meeting
     */
    public VirtualMeetingImpl(VMSessionManagerImpl sessionManager)
    {
        this.sessionManager = sessionManager;
        this.virtualMeetingId = PasswordUtil.generateVMId();

        ServerImpl.Configuration configuration = getSessionManager().getServer().getConfiguration();
        this.ascendingScreenShareConsole = new ScreenShareConsoleImpl(configuration.getPort(),configuration.getScreenShareBufferSize());
        this.descendingScreenShareConsole = new ScreenShareConsoleImpl(configuration.getPort(),configuration.getScreenShareBufferSize());
        this.ascendingAudioRelayConsole = new AudioRelayConsoleImpl(configuration.getPort(),configuration.getAudioRelayBufferSize());
        this.descendingAudioRelayConsole = new AudioRelayConsoleImpl(configuration.getPort(),configuration.getAudioRelayBufferSize());
    }

    /**
     * Retrieve Session Manager assigned to Virtual Meeting
     * @return
     */
    public VMSessionManagerImpl getSessionManager() {
        return sessionManager;
    }

    /**
     * Retrieve unique id of Virtual Meeting
     * @return Virtual Meeting Id
     */
    @Override
    public String getVMId() {
        return virtualMeetingId;
    }

    /**
     * Retrieve Facilitator Id of Active Screen Share Presenter
     * @return Facilitator Id of Active Screen Share Presenter
     */
    @Override
    public String getActiveScreenFacilitatorId() {
        return activeScreenFacilitatorId;
    }

    /**
     * Retrieve Presenter Id of Active Screen Share Presenter
     * @return Presenter Id of Active Screen Share Presenter
     */
    @Override
    public String getActiveScreenPresenterId() {
        return activeScreenPresenterId;
    }

    /**
     * Retrieve Facilitator Id of Active Speech Share Presenter
     * @return Facilitator Id of Active Speech Share Presenter
     */
    @Override
    public String getActiveSpeechFacilitatorId() {
        return activeSpeechFacilitatorId;
    }

    /**
     * Retrieve Presenter Id of Active Speech Share Presenter
     * @return Presenter Id of Active Speech Share Presenter
     */
    @Override
    public String getActiveSpeechPresenterId() {
        return activeSpeechPresenterId;
    }

    /**
     * Retrieve Status of VirtualMeeting
     * @return Status of Virtual Meeting
     */
    @Override
    public SessionStatus getStatus() {
        return status;
    }

    /**
     * Retrieve snapshot of Virtual Meeting. If many getters of Virtual Meeting are to be called, its recommended to obtain
     * snapshot of VM once and re-use it
     * @return Snapshot of VirtualMeeting
     */
    @Override
    public VirtualMeetingSnapshot getSnapshot() {
        return VirtualMeetingSnapshot.fromVirtualMeeting(this);
    }

    /**
     * Retrieve list of participants (presenters) of Virtual Meeting
     * @return List of participants of virtual meeting
     */
    @Override
    public Collection<VMParticipant> getParticipants() {
        int count = 0;
        for(FacilitatorConsole console  : getFacilitatorConsoles())
        {
            if(console == null)
                continue;
            count += console.getParticipants().size();
        }
        //identify count
        int i = 0;

        //create participant objects
        VMParticipant[] results = new VMParticipant[count];
        for(FacilitatorConsole console  : getFacilitatorConsoles())
        {
            //fill
            if(console== null)
                continue;
            for(VMParticipant participant : console.getParticipants())
            {
                results[i++] = participant;
            }
        }
        //return result
        return Arrays.asList(results);
    }

    /**
     * Obtain details of a Virtual Meeting Participant
     * @param participantId presenter console id of participant of which information is queried
     * @return Participant Information of participant with given presenterConsoleId
     */
    @Override
    public VMParticipant getParticipant(String participantId) {
        for(FacilitatorConsole console  : getFacilitatorConsoles())
        {
            if(console == null)
                continue;
            VMParticipant result = console.getParticipant(participantId);
            if(result!= null)
                return result;
        }
        return null;
    }

    /**
     * Retrieve list of shared tasks of Virtual Meeting
     * @return List of shared tasks in virtual meeting
     */
    @Override
    public Collection<SharedTask> getSharedTasks() {
        updateSharedTaskPresenterNames();
        return sharedTasks.values();
    }

    /**
     * Retrieve shared task with given SharedTaskId
     * @param sharedTaskId Id of shared task to be retrieved
     * @return Shared Task with given SharedTaskId
     */
    @Override
    public SharedTask getSharedTask(String sharedTaskId) {
        updateSharedTaskPresenterNames();
        return sharedTasks.get(sharedTaskId);
    }

    /**
     * Adds a new Shared Task to Virtual Meeting
     * @param title Title of Shared Task
     * @param description Description of Shared Task. Optional. can be null.
     * @return Shared Task Id
     */
    @Override
    public String addSharedTask(String title, String description) {
        SharedTask task = new SharedTask();
        task.setTitle(title);
        task.setDescription(description);
        sharedTasks.put(task.getTaskId(),task);
        return task.getTaskId();
    }

    /**
     * Remove shared task with given sharedTaskId
     * @param sharedTaskId SharedTaskID of shared task to be removed
     * @throws SharedTaskNotFoundException Invalid shared task id or shared task is not found
     */
    @Override
    public void removeSharedTask(String sharedTaskId) throws SharedTaskNotFoundException {
        SharedTask task = sharedTasks.get(sharedTaskId);
        if(task == null)
            throw new SharedTaskNotFoundException();

        sharedTasks.remove(sharedTaskId);
    }

    /**
     * Modify shared task to have new title and description. If a field is not to be changed, submit the existing value.
     * @param sharedTaskId SharedTaskId of shared task to be modified
     * @param newTitle new title of shared task
     * @param newDescription new description of shared task. Can be null to clear the description.
     */
    @Override
    public void modifySharedTask(String sharedTaskId, String newTitle, String newDescription) throws SharedTaskNotFoundException {
        SharedTask task = sharedTasks.get(sharedTaskId);
        if(task == null)
            throw new SharedTaskNotFoundException();
        task.setTitle(newTitle);
        task.setDescription(newDescription);
    }

    /**
     * Assign a shared task to a presenter
     * @param sharedTaskId SharedTaskId of shared task to be assigned
     * @param facilitatorId Facilitator ID of presenter to which shared task should be assigned
     * @param presenterId PresenterID of presenter to which shared task should be assigned
     */
    @Override
    public void assignSharedTask(String sharedTaskId, String facilitatorId, String presenterId) throws SharedTaskNotFoundException {
        SharedTask task = sharedTasks.get(sharedTaskId);
        if(task == null)
            throw new SharedTaskNotFoundException();
        task.setAssignedFacilitatorId(facilitatorId);
        task.setAssignedPresenterId(presenterId);
        updateSharedTaskPresenterName(task);
    }

    /**
     * Clear assignment of a shared task
     * @param sharedTaskId SharedTaskID of sharedtask to be unassigned
     */
    @Override
    public void unAssignSharedTask(String sharedTaskId) throws SharedTaskNotFoundException {
        SharedTask task = sharedTasks.get(sharedTaskId);
        if(task == null)
            throw new SharedTaskNotFoundException();
        task.setAssignedPresenterName(null);
        task.setAssignedFacilitatorId(null);
        task.setAssignedPresenterId(null);
    }


    /**
     * Obtain Facilitator from console id
     * @param consoleId Console Id of Facilitator to look for
     * @return FacilitatorConsole of facilitator with given id
     */
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

    /**
     * Add a Facilitator Console to Virtual Meeting
     * @param console Facilitator Console to be added
     * @return index of location to which Facilitator Console is added
     */
    int addNewFacilitatorConsole(FacilitatorConsoleImpl console)
    {
        //Facilitator is added to index 0 or 1 depending on availability
        for(int i = 0; i < facilitatorConsoles.length; i++)
        {
            if(facilitatorConsoles[i] == null)
            {
                facilitatorConsoles[i] = console;
                updateStatus();
                return i;
            }
        }

        throw new ArrayIndexOutOfBoundsException();

    }

    /**
     * Remove a Facilitator Console from Virtual Meeting
     * @param console Facilitator Console to be Removed
     */
    void removeFacilitatorConsole(FacilitatorConsoleImpl console)
    {
        for(int i = 0; i < facilitatorConsoles.length;i++)
        {
            if(facilitatorConsoles[i] != null)
                if(facilitatorConsoles[i].getConsoleId().equals(console.getConsoleId()))
                    facilitatorConsoles[i] = null;
        }
        updateStatus();
    }

    /**
     * Retrieve number of Facilitator consoles added
     * @return Count of facilitator consoles
     */
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

    /**
     * Retrieve list of Facilitator Consoles in Virtual Meeting
     * @return List of Facilitator Consoles
     */
    @WebMethod(exclude = true)
    public FacilitatorConsoleImpl[] getFacilitatorConsoles()
    {
        return facilitatorConsoles;
    }

    /**
     * Start the Facilitator Console
     */
    public void start()
    {
        //Set the state
        this.status = SessionStatus.WaitingForFirstFacilitator;

        //Start the streaming consoles
        ascendingScreenShareConsole.start();
        descendingScreenShareConsole.start();
        ascendingAudioRelayConsole.start();
        descendingAudioRelayConsole.start();

        //Launch the WebService
        Logger.getLogger(this.getClass()).info("Virtual Meeting Started " + URLGenerator.generateVMPublishURL(sessionManager.getServer().getPort(),virtualMeetingId) );
        endpoint = Endpoint.publish(URLGenerator.generateVMPublishURL(sessionManager.getServer().getPort(),virtualMeetingId),this);
    }

    /**
     * Update Status of Virtual Meeting
     */
    void updateStatus() {
        if(status == SessionStatus.Adjourned)
        {
            if(getFacilitatorConsoleCount() == 0)
            {
                getSessionManager().resetVirtualMeeting();
                status = SessionStatus.NotReady;
            }
            return;
        }
        if(status == SessionStatus.NotReady)
            return;
        if(getFacilitatorConsoleCount() == 0)
            status = SessionStatus.WaitingForFirstFacilitator;
        if(getFacilitatorConsoleCount() == 1)
            status = SessionStatus.WaitingForSecondFacilitator;

        if(getFacilitatorConsoleCount() == 2)
            status = SessionStatus.MeetingOnline;
    }

    /**
     * Adjourn the Virtual Meeting
     */
    void adjournMeeting()
    {
        status = SessionStatus.Adjourned;

        if(getSessionManager().getServer().getFacilitatorSessionListener() != null)
            getSessionManager().getServer().getFacilitatorSessionListener().onMeetingAdjourned();
        updateStatus();
    }

    /**
     * Stop the virtual meeting
     */
    public void stop() {
        status = SessionStatus.NotReady;
        //endpoint.stop();
    }
}
