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
    /**
     * A facilitator has connected to server
     * @param facilitator Description of facilitator
     * @param consoleId facilitator console id
     */
    public void onConnected(Facilitator facilitator, String consoleId);

    /**
     * A facilitator has disconnected from server
     * @param facilitator Description of disconnected facilitator
     * @param consoleId Console id of Facilitator Console
     */
    public void onDisconnected( Facilitator facilitator, String consoleId);
}
