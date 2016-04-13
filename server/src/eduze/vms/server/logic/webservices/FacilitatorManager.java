package eduze.vms.server.logic.webservices;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService
public interface FacilitatorManager {
    public void unPair(@WebParam(name = "PairKey") String pairKey);
    public String pair(@WebParam(name="Name") String name, @WebParam(name="Password") String password) throws AlreadyPairedException, InvalidServerPasswordException;
}
