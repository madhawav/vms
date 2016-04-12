package eduze.vms.server.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService
public interface Server {
    @WebMethod
    public String getServerName();
    @WebMethod
    public int getPort();
}
