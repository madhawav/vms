package eduze.vms.server.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Collection;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService
public interface FacilitatorConsole {
    @WebMethod
    public void disconnect();

    @WebMethod
    public String getConsoleId();
    @WebMethod
    public String getFacilitatorName();

    @WebMethod
    public String getInScreenShareConsoleId();

    @WebMethod
    public String getOutScreenShareConsoleId();

    @WebMethod
    public String getInAudioRelayConsoleId();

    @WebMethod
    public String getOutAudioRelayConsoleId();

    @WebMethod
    public boolean requestScreenAccess(@WebParam(name = "PresenterId") String presenterId, @WebParam(name = "IncludeAudio") boolean includeAudio);

    @WebMethod
    public boolean requestAudioRelayAccess(@WebParam(name = "PresenterId") String presenterId);

    @WebMethod
    public Collection<VMParticipant> getParticipants();

    @WebMethod
    public VMParticipant getParticipant(@WebParam(name = "ParticipantId") String participantId);

    @WebMethod
    public void setParticipants(@WebParam(name="Participants") Collection<VMParticipant> participants);

    /**
     * Adjourn the meeting
     */
    @WebMethod
    public void adjournMeeting();

    /**
     * Notify the server that facilitator is alive
     */
    @WebMethod
    public void notifyAlive();
}
