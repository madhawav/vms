package eduze.vms.server.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * The Main Webservice providing basic information of Server to clients
 */
@WebService
public interface Server {
    /**
     * Retrieve name of Server
     * @return Server Name
     */
    @WebMethod
    public String getServerName();

    /**
     * Retrieve port of Server
     * @return Server Port
     */
    @WebMethod
    public int getPort();
}
