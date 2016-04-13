package eduze.vms.facilitator.logic.webservices;

/**
 * Created by Madhawa on 13/04/2016.
 */
public class InvalidFacilitatorPasskeyException extends Exception {
    public InvalidFacilitatorPasskeyException()
    {
        super("Facilitator passkey invalid.");
    }
}
