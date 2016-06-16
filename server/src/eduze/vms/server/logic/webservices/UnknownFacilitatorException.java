package eduze.vms.server.logic.webservices;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Invalid Facilitator Id. Or Facilitator has not paired.
 */
public class UnknownFacilitatorException extends Exception {
    public UnknownFacilitatorException()
    {
        super("Facilitator is not known. Re-pair.");
    }
}
