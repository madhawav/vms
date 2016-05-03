package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * Details on Virtual Meeting Participant
 */
public class VirtualMeetingParticipantInfo {
    private String name;
    private String facilitatorId;
    private String presenterId;


    /**
     * Retrieve name of Virtual Meeting Participant
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of Virtual Meeting Participant
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Facilitator ID
     * @return
     */
    public String getFacilitatorId() {
        return facilitatorId;
    }

    /**
     * Set Facilitator Id
     * @param facilitatorId
     */
    public void setFacilitatorId(String facilitatorId) {
        this.facilitatorId = facilitatorId;
    }

    /**
     * Get Presenter Console Id
     * @return
     */
    public String getPresenterId() {
        return presenterId;
    }

    /**
     * Set Presenter Console Id
     * @param presenterId
     */
    public void setPresenterId(String presenterId) {
        this.presenterId = presenterId;
    }

    /**
     * Generate VirtualMeetingParticipantInfo from VMParticipant MPI of Server
     * @param participant
     * @return
     */
    static VirtualMeetingParticipantInfo fromVMParticipant(VMParticipant participant)
    {
        VirtualMeetingParticipantInfo result = new VirtualMeetingParticipantInfo();
        result.setFacilitatorId(participant.getFacilitatorId());
        result.setName(participant.getName());
        result.setPresenterId(participant.getPresenterId());
        return result;
    }
}
