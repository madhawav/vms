package eduze.vms.foundation.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by Fujitsu on 4/15/2016.
 */

@WebService
public interface AudioRelayConsole {

    @WebMethod
    public boolean isEnabled();

    @WebMethod
    public String getConsoleId();
}
