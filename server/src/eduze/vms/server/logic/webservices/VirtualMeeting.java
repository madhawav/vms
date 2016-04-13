package eduze.vms.server.logic.webservices;

import org.apache.axis.session.Session;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService
public interface VirtualMeeting {
    @WebMethod
    public String getVMId();

    @WebMethod
    public String getActiveScreenFacilitatorId();

    @WebMethod
    public String getActiveScreenPresenterId();

    @WebMethod
    public String getActiveSpeechFacilitatorId();

    @WebMethod
    public String getActiveSpeechPresenterId();

    @WebMethod
    public SessionStatus getStatus();

    @WebMethod
    public VirtualMeetingSnapshot getSnapshot();
}
