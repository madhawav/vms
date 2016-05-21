package eduze.vms.server.logic.webservices;

/**
 * Created by Admin on 5/21/2016.
 */
public class MeetingAdjournedException extends Exception {
    public MeetingAdjournedException()
    {
        super("Meeting adjourned");
    }
}
