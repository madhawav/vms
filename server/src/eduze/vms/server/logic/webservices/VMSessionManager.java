package eduze.vms.server.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService
public interface VMSessionManager {
    @WebMethod
    public SessionStatus getStatus();
    @WebMethod
    public ConnectionResult connect(String name, String pairKey) throws ServerNotReadyException, MeetingAlreadyStartedException, UnknownFacilitatorException, FacilitatorAlreadyConnectedException,MeetingAdjournedException;

}
