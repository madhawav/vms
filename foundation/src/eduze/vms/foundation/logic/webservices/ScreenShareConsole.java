package eduze.vms.foundation.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService
public interface ScreenShareConsole {
   @WebMethod
   public int getUpdateInterval();

   @WebMethod
   public void setUpdateInterval(int interval);

   @WebMethod
   public boolean isEnabled();

   @WebMethod
   public String getConsoleId();
}
