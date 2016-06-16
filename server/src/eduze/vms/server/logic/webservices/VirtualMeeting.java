package eduze.vms.server.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Collection;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * WebService that provides core information about virtual meeting to presenters
 */
@WebService
public interface VirtualMeeting {
    /**
     * Retrieve unique id of Virtual Meeting
     * @return Virtual Meeting Id
     */
    @WebMethod
    public String getVMId();

    /**
     * Retrieve Facilitator Id of Active Screen Share Presenter
     * @return Facilitator Id of Active Screen Share Presenter
     */
    @WebMethod
    public String getActiveScreenFacilitatorId();

    /**
     * Retrieve Presenter Id of Active Screen Share Presenter
     * @return Presenter Id of Active Screen Share Presenter
     */
    @WebMethod
    public String getActiveScreenPresenterId();

    /**
     * Retrieve Facilitator Id of Active Speech Share Presenter
     * @return Facilitator Id of Active Speech Share Presenter
     */
    @WebMethod
    public String getActiveSpeechFacilitatorId();
    /**
     * Retrieve Presenter Id of Active Speech Share Presenter
     * @return Presenter Id of Active Speech Share Presenter
     */
    @WebMethod
    public String getActiveSpeechPresenterId();

    /**
     * Retrieve Status of VirtualMeeting
     * @return Status of Virtual Meeting
     */
    @WebMethod
    public SessionStatus getStatus();

    /**
     * Retrieve snapshot of Virtual Meeting. If many getters of Virtual Meeting are to be called, its recommended to obtain
     * snapshot of VM once and re-use it
     * @return Snapshot of VirtualMeeting
     */
    @WebMethod
    public VirtualMeetingSnapshot getSnapshot();

    /**
     * Retrieve list of participants (presenters) of Virtual Meeting
     * @return List of participants of virtual meeting
     */
    @WebMethod
    public Collection<VMParticipant> getParticipants();

    /**
     * Obtain details of a Virtual Meeting Participant
     * @param participantId presenter console id of participant of which information is queried
     * @return Participant Information of participant with given presenterConsoleId
     */
    @WebMethod
    public VMParticipant getParticipant(@WebParam(name = "ParticipantId") String participantId);

    /**
     * Retrieve list of shared tasks of Virtual Meeting
     * @return List of shared tasks in virtual meeting
     */
    @WebMethod
    public Collection<SharedTask> getSharedTasks();

    /**
     * Retrieve shared task with given SharedTaskId
     * @param sharedTaskId Id of shared task to be retrieved
     * @return Shared Task with given SharedTaskId
     */
    @WebMethod
    public SharedTask getSharedTask(@WebParam(name = "SharedTaskId") String sharedTaskId);

    /**
     * Adds a new Shared Task to Virtual Meeting
     * @param title Title of Shared Task
     * @param description Description of Shared Task. Optional. can be null.
     * @return Shared Task Id
     */
    @WebMethod
    public String addSharedTask(@WebParam(name = "Title")String title, @WebParam(name="Description") String description);

    /**
     * Remove shared task with given sharedTaskId
     * @param sharedTaskId SharedTaskID of shared task to be removed
     * @throws SharedTaskNotFoundException Invalid shared task id or shared task is not found
     */
    @WebMethod
    public void removeSharedTask(@WebParam(name = "SharedTaskId") String sharedTaskId) throws SharedTaskNotFoundException;

    /**
     * Modify shared task to have new title and description. If a field is not to be changed, submit the existing value.
     * @param sharedTaskId SharedTaskId of shared task to be modified
     * @param newTitle new title of shared task
     * @param newDescription new description of shared task. Can be null to clear the description.
     */
    @WebMethod
    public void modifySharedTask(@WebParam(name="SharedTaskId") String sharedTaskId, @WebParam(name="NewTitle") String newTitle, @WebParam(name="NewDescription") String newDescription) throws SharedTaskNotFoundException;

    /**
     * Assign a shared task to a presenter
     * @param sharedTaskId SharedTaskId of shared task to be assigned
     * @param facilitatorId Facilitator ID of presenter to which shared task should be assigned
     * @param presenterId PresenterID of presenter to which shared task should be assigned
     */
    @WebMethod
    public void assignSharedTask(@WebParam(name="ShardTaskId") String sharedTaskId, @WebParam(name="FacilitatorId") String facilitatorId, @WebParam(name="PresenterId")String presenterId) throws SharedTaskNotFoundException;

    /**
     * Clear assignment of a shared task
     * @param sharedTaskId SharedTaskID of sharedtask to be unassigned
     */
    @WebMethod
    public void unAssignSharedTask(@WebParam(name="ShardTaskId") String sharedTaskId) throws SharedTaskNotFoundException;



}
