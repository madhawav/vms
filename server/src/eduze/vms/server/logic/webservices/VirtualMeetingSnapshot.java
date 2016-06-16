package eduze.vms.server.logic.webservices;

/**
 * Created by Madhawa on 13/04/2016.
 */


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * A Snapshot of state variables of Virtual Meeting
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="VirtualMeetingSnapshot")
public class VirtualMeetingSnapshot {
    @XmlElement(name="VMId")
    protected String vmId = null;
    @XmlElement(name="ActiveScreenFacilitatorId")
    protected String activeScreenFacilitatorId = null;

    @XmlElement(name="ActiveScreenPresenterId")
    protected String activeScreenPresenterId = null;

    @XmlElement(name="ActiveSpeechFacilitatorId")
    protected String activeSpeechFacilitatorId = null;

    @XmlElement(name="ActiveSpeechPresenterId")
    protected String activeSpeechPresenterId = null;

    @XmlElement(name="Status")
    protected SessionStatus status = null;

    @XmlElement(name="Participants")
    protected Collection<VMParticipant> participants = null;

    @XmlElement(name = "SharedTasks")
    protected Collection<SharedTask> sharedTasks = null;

    /**
     * Retrieve SharedTasks in Virtual Meeting snapshot
     * @return Shared Tasks in Virtual Meeting snapshot
     */
    public Collection<SharedTask> getSharedTasks() {
        return sharedTasks;
    }

    /**
     * Set SharedTasks in Virtual Meeting Snapshot
     * @param sharedTasks Set of SharedTasks to be assigned
     */
    public void setSharedTasks(Collection<SharedTask> sharedTasks) {
        this.sharedTasks = sharedTasks;
    }

    /**
     * Retrieve list of participants of Virtual Meeting snapshot
     * @return List of participants of virtual meeting snapshot
     */
    public Collection<VMParticipant> getParticipants() {
        return participants;
    }

    /**
     * Set the list of participants of virtual meeting snapshot
     * @param participants list of participants of Virtual Meeting Snapshot
     */
    public void setParticipants(Collection<VMParticipant> participants) {
        this.participants = participants;
    }

    /**
     * Retrieve status of Virtual Meeting Snapshot
     * @return Status of Virtual Meeting Snapshot
     */
    public SessionStatus getStatus() {
        return status;
    }

    /**
     * Retrieve Facilitator Id of Active Screen Share Presenter
     * @return Facilitator Id of Active Screen Share Presenter
     */
    public String getActiveScreenFacilitatorId() {
        return activeScreenFacilitatorId;
    }

    /**
     * Retrieve Presenter Id of Active Screen Share Presenter
     * @return Presenter Id of Active Screen Share Presenter
     */
    public String getActiveScreenPresenterId() {
        return activeScreenPresenterId;
    }

    /**
     * Retrieve Facilitator Id of Active Speech Share Presenter
     * @return Facilitator Id of Active Speech Share Presenter
     */
    public String getActiveSpeechFacilitatorId() {
        return activeSpeechFacilitatorId;
    }

    /**
     * Retrieve Presenter Id of Active Speech Share Presenter
     * @return Presenter Id of Active Speech Share Presenter
     */
    public String getActiveSpeechPresenterId() {
        return activeSpeechPresenterId;
    }

    /**
     * Retrieve Id of Virtual Meeting
     * @return ID of virtual meeting
     */
    public String getVmId() {
        return vmId;
    }

    /**
     * Set the Facilitator Id of Active Screen Presenter
     * @param activeScreenFacilitatorId Facilitator Id of Active Screen Presenter
     */
    public void setActiveScreenFacilitatorId(String activeScreenFacilitatorId) {
        this.activeScreenFacilitatorId = activeScreenFacilitatorId;
    }

    /**
     * Set the Presenter Id of Active Screen Presenter
     * @param activeScreenPresenterId Presenter Id of Active Screen Presenter
     */
    public void setActiveScreenPresenterId(String activeScreenPresenterId) {
        this.activeScreenPresenterId = activeScreenPresenterId;
    }

    /**
     * Set the ID of Virtual Meeting
     * @param vmId Virtual Meeting Id
     */
    public void setVmId(String vmId) {
        this.vmId = vmId;
    }

    /**
     * Set the Facilitator Id of Active Speech Presenter
     * @param activeSpeechFacilitatorId Facilitator Id of Active Speech Presenter
     */
    public void setActiveSpeechFacilitatorId(String activeSpeechFacilitatorId) {
        this.activeSpeechFacilitatorId = activeSpeechFacilitatorId;
    }

    /**
     * Set the Presenter Id of Active Speech Presenter
     * @param activeSpeechPresenterId Presenter Id of Active Speech Presenter
     */
    public void setActiveSpeechPresenterId(String activeSpeechPresenterId) {
        this.activeSpeechPresenterId = activeSpeechPresenterId;
    }

    /**
     * Set the status of Virtual Meeting Snapshot
     * @param status Virtual Meeting Status
     */
    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    /**
     * Obtain the Snapshot from instance of Virtual Meeting Class
     * @param vm Source Virtual Meeting
     * @return Snapshot of Virtual Meeting
     */
    public static VirtualMeetingSnapshot fromVirtualMeeting(VirtualMeeting vm) {
            //fill snapshot
            VirtualMeetingSnapshot result = new VirtualMeetingSnapshot();
            result.setActiveScreenFacilitatorId(vm.getActiveScreenFacilitatorId());
            result.setActiveScreenPresenterId(vm.getActiveScreenPresenterId());
            result.setActiveSpeechFacilitatorId(vm.getActiveSpeechFacilitatorId());
            result.setActiveSpeechPresenterId(vm.getActiveSpeechPresenterId());
            result.setStatus(vm.getStatus());
            result.setVmId(vm.getVMId());
            result.setParticipants(vm.getParticipants());
            result.setSharedTasks(vm.getSharedTasks());
            return result;
    }
}

