package eduze.vms.server.logic.webservices;

import org.apache.axis.session.Session;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Collection;

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

    @WebMethod
    public Collection<VMParticipant> getParticipants();

    @WebMethod
    public VMParticipant getParticipant(@WebParam(name = "ParticipantId") String participantId);

}
