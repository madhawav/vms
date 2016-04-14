package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant;

/**
 * Created by Madhawa on 14/04/2016.
 */
public class VirtualMeetingParticipantInfo {
    private String name;
    private String facilitatorId;
    private String presenterId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacilitatorId() {
        return facilitatorId;
    }

    public void setFacilitatorId(String facilitatorId) {
        this.facilitatorId = facilitatorId;
    }

    public String getPresenterId() {
        return presenterId;
    }

    public void setPresenterId(String presenterId) {
        this.presenterId = presenterId;
    }

    static VirtualMeetingParticipantInfo fromVMParticipant(VMParticipant participant)
    {
        VirtualMeetingParticipantInfo result = new VirtualMeetingParticipantInfo();
        result.setFacilitatorId(participant.getFacilitatorId());
        result.setName(participant.getName());
        result.setPresenterId(participant.getPresenterId());
        return result;
    }
}
