package eduze.vms.server.logic.webservices;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class FacilitatorAlreadyConnectedException extends Exception {
    public FacilitatorAlreadyConnectedException()
    {
        super("Facilitator is connected already");
    }
}
