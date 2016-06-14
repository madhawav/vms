package eduze.vms.server.logic.webservices;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Manage Known Facilitator Devices (Pairing and Un-pairing)
 */
@WebService
public interface FacilitatorManager {
    /**
     * Un-pair the Facilitator
     * @param pairKey PairKey provided to Facilitator
     */
    public void unPair(@WebParam(name = "PairKey") String pairKey);

    /**
     * Pair the Facilitator
     * @param name Name of Facilitator
     * @param password Server Password
     * @return Pair-Key of Facilitator
     * @throws AlreadyPairedException The Facilitator is already paired
     * @throws InvalidServerPasswordException Server Password is invalid
     */
    public String pair(@WebParam(name="Name") String name, @WebParam(name="Password") String password) throws AlreadyPairedException, InvalidServerPasswordException;
}
