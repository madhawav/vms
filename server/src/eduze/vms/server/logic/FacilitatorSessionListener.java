package eduze.vms.server.logic;

import eduze.vms.server.logic.webservices.Facilitator;
import eduze.vms.server.logic.webservices.FacilitatorConsole;
import eduze.vms.server.logic.webservices.FacilitatorConsoleImpl;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Listener to connection and disconnection of facilitators to server
 */
public interface FacilitatorSessionListener {

    public void onConnected(String consoleId, String facilitatorName);
    public void onDisconnected(Facilitator facilitator, String consoleId);
}
