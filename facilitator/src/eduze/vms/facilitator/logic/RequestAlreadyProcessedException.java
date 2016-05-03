package eduze.vms.facilitator.logic;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Request has already been processed
 */
public class RequestAlreadyProcessedException extends Exception {
    public RequestAlreadyProcessedException()
    {
        super("Request already processed");
    }
}
