package eduze.vms.facilitator.logic;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * Listener for share request from server
 */
public interface ShareRequestListener {
    public boolean onShareRequest(AbstractShareRequest shareRequest);
}
