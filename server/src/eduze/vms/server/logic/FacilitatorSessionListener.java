package eduze.vms.server.logic;

import eduze.vms.server.logic.webservices.Facilitator;
import eduze.vms.server.logic.webservices.FacilitatorConsole;
import eduze.vms.server.logic.webservices.FacilitatorConsoleImpl;

/**
 * Created by Madhawa on 12/04/2016.
 */
public interface FacilitatorSessionListener {
    public void onConnected(FacilitatorConsoleImpl console);
    public void onDisconnected(Facilitator facilitator, String consoleId);
}
