package eduze.vms.server.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService
public interface FacilitatorConsole {
    @WebMethod
    public void disconnect();

    @WebMethod
    public String getConsoleId();
    @WebMethod
    public String getFacilitatorName();

    @WebMethod
    public String getInScreenShareConsoleId();

    @WebMethod
    public String getOutScreenShareConsoleId();

    @WebMethod
    public boolean requestScreenAccess(@WebParam(name = "PresenterId") String presenterId, @WebParam(name = "IncludeAudio") boolean includeAudio);
}
