package eduze.vms.facilitator.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Madhawa on 13/04/2016.
 */
@WebService
public interface Facilitator {
    /**
     * Retrieve name of facilitator
     * @return facilitator name
     */
    @WebMethod
    public String getFacilitatorName();

    /**
     * request connection from facilitator. facilitator has to manually accept it.
     * @param presenterName name of presenter
     * @param passKey facilitator passkey
     * @return connectionRequestCode
     */
    @WebMethod
    public String requestConnection(@WebParam(name = "PresenterName") String presenterName, @WebParam(name="PassKey") String passKey) throws InvalidFacilitatorPasskeyException;

    /**
     * Checks status of connection request
     * @param connectionRequestId connectionRequestId returned from requestConnectionMethod
     * @return
     */
    @WebMethod
    public ConnectionRequestState checkConnectionRequestState(@WebParam(name = "ConnectionRequestId")String connectionRequestId);

}
