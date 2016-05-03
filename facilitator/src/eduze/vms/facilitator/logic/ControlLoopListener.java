package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Listner to events of control loop
 */
public interface ControlLoopListener {
    /**
     * A control loop has completed an update
     * @param vm Virtual Meeting State received upon update
     */
    public void updateReceived(VirtualMeetingSnapshot vm);
}
