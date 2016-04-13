package eduze.vms.facilitator.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Madhawa on 13/04/2016.
 */
@WebService
public interface PresenterConsole {
    /**
     * Confirmation from presenter to facilitator
     */
    @WebMethod
    public void acknowledgeConnection();

    /**
     * Disconnect from Facilitator
     */
    @WebMethod
    public void disconnect();

    /**
     * Return console id
     * @return
     */
    @WebMethod
    public String getConsoleId();

    /**
     * Return name of Presenter as set in Facilitator
     * @return Name of Presenter
     */
    @WebMethod
    public String getName();

    /**
     * Set the name of presenter as recognized by facilitator
     * @param newName
     */
    @WebMethod
    public void setName(@WebParam(name = "NewName") String newName);

}
