package eduze.vms.server.logic.webservices;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class MeetingAlreadyStartedException extends Exception {
    public MeetingAlreadyStartedException()
    {
        super("Meeting has started already");
    }
}
