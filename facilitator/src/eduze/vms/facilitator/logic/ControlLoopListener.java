package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;

/**
 * Created by Madhawa on 13/04/2016.
 */
public interface ControlLoopListener {
    public void updateReceived(VirtualMeetingSnapshot vm);
}
