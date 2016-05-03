package eduze.vms.foundation.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by Fujitsu on 4/15/2016.
 */

/**
 * Web Service Console for control of Audio Relay between nodes
 */
@WebService
public interface AudioRelayConsole {

    /**
     * Retrieve whether the Audio Relay Channel is active
     * @return True if channel is active. Otherwise return false
     */
    @WebMethod
    public boolean isEnabled();

    /**
     * Retrieve the Console Id
     * @return Console Id
     */
    @WebMethod
    public String getConsoleId();
}
