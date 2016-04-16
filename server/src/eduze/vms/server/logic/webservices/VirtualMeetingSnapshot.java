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

    public Collection<SharedTask> getSharedTasks() {
        return sharedTasks;
    }

    public void setSharedTasks(Collection<SharedTask> sharedTasks) {
        this.sharedTasks = sharedTasks;
    }

    public Collection<VMParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(Collection<VMParticipant> participants) {
        this.participants = participants;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public String getActiveScreenFacilitatorId() {
        return activeScreenFacilitatorId;
    }

    public String getActiveScreenPresenterId() {
        return activeScreenPresenterId;
    }

    public String getActiveSpeechFacilitatorId() {
        return activeSpeechFacilitatorId;
    }

    public String getActiveSpeechPresenterId() {
        return activeSpeechPresenterId;
    }

    public String getVmId() {
        return vmId;
    }

    public void setActiveScreenFacilitatorId(String activeScreenFacilitatorId) {
        this.activeScreenFacilitatorId = activeScreenFacilitatorId;
    }

    public void setActiveScreenPresenterId(String activeScreenPresenterId) {
        this.activeScreenPresenterId = activeScreenPresenterId;
    }

    public void setVmId(String vmId) {
        this.vmId = vmId;
    }

    public void setActiveSpeechFacilitatorId(String activeSpeechFacilitatorId) {
        this.activeSpeechFacilitatorId = activeSpeechFacilitatorId;
    }

    public void setActiveSpeechPresenterId(String activeSpeechPresenterId) {
        this.activeSpeechPresenterId = activeSpeechPresenterId;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public static VirtualMeetingSnapshot fromVirtualMeeting(VirtualMeeting vm) {
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

