package eduze.vms.server.logic.webservices;

/**
 * The Facilitator is already connected.
 */
public class FacilitatorAlreadyConnectedException extends Exception {
    public FacilitatorAlreadyConnectedException()
    {
        super("Facilitator is connected already");
    }
}
