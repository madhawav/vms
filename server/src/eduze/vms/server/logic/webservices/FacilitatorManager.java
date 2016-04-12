package eduze.vms.server.logic.webservices;

import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService
public interface FacilitatorManager {
    public void unPair(String pairKey);
    public String pair(String name, String password) throws AlreadyPairedException, InvalidServerPasswordException;
}
