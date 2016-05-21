package eduze.vms.server.logic.webservices;

import eduze.vms.server.logic.PasswordUtil;
import eduze.vms.foundation.logic.webservices.AudioRelayConsoleImpl;
import eduze.vms.foundation.logic.webservices.ScreenShareConsoleImpl;
import eduze.vms.server.logic.URLGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.Arrays;
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

    private AudioRelayConsoleImpl ascendingAudioRelayConsole = null;
    private AudioRelayConsoleImpl descendingAudioRelayConsole = null;

    private SessionStatus status = SessionStatus.NotReady;

    private HashMap<String,SharedTask> sharedTasks = new HashMap();

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

    public void setActiveScreenPresenterId(String activeScreenPresenterId) {
        this.activeScreenPresenterId = activeScreenPresenterId;
    }

    public void setActiveScreenFacilitatorId(String activeScreenFacilitatorId) {
        getFacilitatorConsole(activeScreenFacilitatorId).getOutputScreenShareConsole().setEnabled(true);
        getFacilitatorConsole(activeScreenFacilitatorId).getInputScreenShareConsole().setEnabled(false);
        this.activeScreenFacilitatorId = activeScreenFacilitatorId;
    }

    public void setActiveSpeechPresenterId(String activeSpeechPresenterId) {
        this.activeSpeechPresenterId = activeSpeechPresenterId;
    }

    public void setActiveSpeechFacilitatorId(String activeSpeechFacilitatorId) {
        getFacilitatorConsole(activeSpeechFacilitatorId).getOutputAudioRelayConsole().setEnabled(true);
        getFacilitatorConsole(activeSpeechFacilitatorId).getInputAudioRelayConsole().setEnabled(false);
        this.activeSpeechFacilitatorId = activeSpeechFacilitatorId;
    }


    public ScreenShareConsoleImpl getAscendingScreenShareConsole() {
        return ascendingScreenShareConsole;
    }

    public ScreenShareConsoleImpl getDescendingScreenShareConsole() {
        return descendingScreenShareConsole;
    }

    public AudioRelayConsoleImpl getAscendingAudioRelayConsole() {
        return ascendingAudioRelayConsole;
    }

    public AudioRelayConsoleImpl getDescendingAudioRelayConsole() {
        return descendingAudioRelayConsole;
    }

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

    @Override
    public VirtualMeetingSnapshot getSnapshot() {
        return VirtualMeetingSnapshot.fromVirtualMeeting(this);
    }

    @Override
    public Collection<VMParticipant> getParticipants() {
        int count = 0;
        for(FacilitatorConsole console  : getFacilitatorConsoles())
        {
            if(console == null)
                continue;
            count += console.getParticipants().size();
        }
        int i = 0;
        VMParticipant[] results = new VMParticipant[count];
        for(FacilitatorConsole console  : getFacilitatorConsoles())
        {
            if(console== null)
                continue;
            for(VMParticipant participant : console.getParticipants())
            {
                results[i++] = participant;
            }
        }
        return Arrays.asList(results);
    }

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

    @Override
    public Collection<SharedTask> getSharedTasks() {
        updateSharedTaskPresenterNames();
        return sharedTasks.values();
    }

    @Override
    public SharedTask getSharedTask(String sharedTaskId) {
        updateSharedTaskPresenterNames();
        return sharedTasks.get(sharedTaskId);
    }

    @Override
    public String addSharedTask(String title, String description) {
        SharedTask task = new SharedTask();
        task.setTitle(title);
        task.setDescription(description);
        sharedTasks.put(task.getTaskId(),task);
        return task.getTaskId();
    }

    @Override
    public void removeSharedTask(String sharedTaskId) throws SharedTaskNotFoundException {
        SharedTask task = sharedTasks.get(sharedTaskId);
        if(task == null)
            throw new SharedTaskNotFoundException();

        sharedTasks.remove(sharedTaskId);
    }

    @Override
    public void modifySharedTask(String sharedTaskId, String newTitle, String newDescription) throws SharedTaskNotFoundException {
        SharedTask task = sharedTasks.get(sharedTaskId);
        if(task == null)
            throw new SharedTaskNotFoundException();
        task.setTitle(newTitle);
        task.setDescription(newDescription);
    }

    @Override
    public void assignSharedTask(String sharedTaskId, String facilitatorId, String presenterId) throws SharedTaskNotFoundException {
        SharedTask task = sharedTasks.get(sharedTaskId);
        if(task == null)
            throw new SharedTaskNotFoundException();
        task.setAssignedFacilitatorId(facilitatorId);
        task.setAssignedPresenterId(presenterId);
        updateSharedTaskPresenterName(task);
    }

    @Override
    public void unAssignSharedTask(String sharedTaskId) throws SharedTaskNotFoundException {
        SharedTask task = sharedTasks.get(sharedTaskId);
        if(task == null)
            throw new SharedTaskNotFoundException();
        task.setAssignedPresenterName(null);
        task.setAssignedFacilitatorId(null);
        task.setAssignedPresenterId(null);
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
                updateStatus();
                return i;
            }
        }

        throw new ArrayIndexOutOfBoundsException();

    }

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
        ascendingAudioRelayConsole.start();
        descendingAudioRelayConsole.start();

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
