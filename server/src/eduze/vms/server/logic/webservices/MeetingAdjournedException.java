package eduze.vms.server.logic.webservices;

/**
 * Unable to process request since the meeting is in adjourned state
 */
public class MeetingAdjournedException extends Exception {
    public MeetingAdjournedException()
    {
        super("Meeting adjourned");
    }
}
