package eduze.vms.server.logic.webservices;

/**
 * Unable to honour request since the meeting has already started
 */
public class MeetingAlreadyStartedException extends Exception {
    public MeetingAlreadyStartedException()
    {
        super("Meeting has started already");
    }
}
