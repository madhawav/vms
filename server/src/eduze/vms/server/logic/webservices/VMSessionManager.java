package eduze.vms.server.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Interface for Facilitators to connect to Server
 */
@WebService
public interface VMSessionManager {
    /**
     * Retrieve Status of Virtual Meeting in Server
     * @return Virtual Meeting Status
     */
    @WebMethod
    public SessionStatus getStatus();

    /**
     * Connect Facilitator to Virtual Meeting
     * @param name Name of Facilitator
     * @param pairKey PairKey provided to facilitator during pairing mechanism
     * @return Connection Result
     * @throws ServerNotReadyException The Server is not ready to accept facilitators
     * @throws MeetingAlreadyStartedException The meeting has been started already. Hence, no new facilitators can join.
     * @throws UnknownFacilitatorException The Facilitator is not known. Considering pairing again.
     * @throws FacilitatorAlreadyConnectedException Facilitator is already connected.
     * @throws MeetingAdjournedException Meeting has been adjourned and hence not accepting new Facilitators
     */
    @WebMethod
    public ConnectionResult connect(@WebParam(name = "Name") String name, @WebParam(name = "PairKey") String pairKey) throws ServerNotReadyException, MeetingAlreadyStartedException, UnknownFacilitatorException, FacilitatorAlreadyConnectedException,MeetingAdjournedException;

}
