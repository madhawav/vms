package eduze.vms.server.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Collection;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService
public interface VirtualMeeting {
    @WebMethod
    public String getVMId();

    @WebMethod
    public String getActiveScreenFacilitatorId();

    @WebMethod
    public String getActiveScreenPresenterId();

    @WebMethod
    public String getActiveSpeechFacilitatorId();

    @WebMethod
    public String getActiveSpeechPresenterId();

    @WebMethod
    public SessionStatus getStatus();

    @WebMethod
    public VirtualMeetingSnapshot getSnapshot();

    @WebMethod
    public Collection<VMParticipant> getParticipants();

    @WebMethod
    public VMParticipant getParticipant(@WebParam(name = "ParticipantId") String participantId);

    @WebMethod
    public Collection<SharedTask> getSharedTasks();

    @WebMethod
    public SharedTask getSharedTask(String sharedTaskId);

    /**
     * Adds a new Shared Task to Virtual Meeting
     * @param title Title of Shared Task
     * @param description Description of Shared Task. Optional. can be null.
     * @return Shared Task Id
     */
    @WebMethod
    public String addSharedTask(@WebParam(name = "Title")String title, @WebParam(name="Description") String description);

    @WebMethod
    public void removeSharedTask(@WebParam(name = "SharedTaskId") String sharedTaskId) throws SharedTaskNotFoundException;

    /**
     * Modify shared task to have new title and description. If a field is not to be changed, submit the existing value.
     * @param sharedTaskId
     * @param newTitle
     * @param newDescription Optional. Can be null to clear the description.
     */
    @WebMethod
    public void modifySharedTask(@WebParam(name="SharedTaskId") String sharedTaskId, @WebParam(name="NewTitle") String newTitle, @WebParam(name="NewDescrition") String newDescription) throws SharedTaskNotFoundException;

    /**
     * Assign a shared task to a presenter
     * @param sharedTaskId
     * @param facilitatorId
     * @param presenterId
     */
    @WebMethod
    public void assignSharedTask(@WebParam(name="ShardTaskId") String sharedTaskId, @WebParam(name="FacilitatorId") String facilitatorId, @WebParam(name="PresenterId")String presenterId) throws SharedTaskNotFoundException;

    /**
     * Clear assignment of a shared task
     * @param sharedTaskId
     */
    @WebMethod
    public void unAssignSharedTask(@WebParam(name="ShardTaskId") String sharedTaskId) throws SharedTaskNotFoundException;

}
