package eduze.vms.foundation.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Web Service Console for control of Screen Share between nodes
 */
@WebService
public interface ScreenShareConsole {
    /**
     * Retrieve interval between screen capture transmissions
     * @return
     */
   @WebMethod
   public int getUpdateInterval();

    /**
     * Set the interval between screen capture transmissions
     * @param interval
     */
   @WebMethod
   public void setUpdateInterval(@WebParam(name = "interval") int interval);

    /**
     * Retrieve whether channel is enabled
     * @return True if channel is enabled. Otherwise return False
     */
   @WebMethod
   public boolean isEnabled();

    /**
     * Retrieve Screen Share Console Id
     * @return
     */
   @WebMethod
   public String getConsoleId();
}
