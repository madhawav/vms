package eduze.vms.server.logic.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Collection;

/**
 * The Main WebService offered for Facilitator Subsystem to interact with the Server.
 * Each Facilitator is provided with a unique Facilitator Console.
 */
@WebService
public interface FacilitatorConsole {
    /**
     * Disconnect from Server
     */
    @WebMethod
    public void disconnect();

    /**
     * Retrieve Facilitator Console Id
     * @return FacilitatorConsoleId
     */
    @WebMethod
    public String getConsoleId();

    /**
     * Retrieve Facilitator Name
     * @return Facilitators Name
     */
    @WebMethod
    public String getFacilitatorName();

    /**
     * Retrieve the ConsoleID used to send Screen Capture data to Facilitator
     * @return Screen Share Console ID of console used to transfer screen captures from Server to Facilitator
     */
    @WebMethod
    public String getInScreenShareConsoleId();

    /**
     * Retrieve the ConsoleID of console used to send Screen Capture data from Facilitator to Server
     * @return Screen Share Console ID of console used to transfer screen captures from Facilitator to Server
     */
    @WebMethod
    public String getOutScreenShareConsoleId();

    /**
     * Retrieve the ConsoleID used to send Audio Capture data to Facilitator
     * @return Audio Relay Console ID of console used to transfer audio captures from Server to Facilitator
     */
    @WebMethod
    public String getInAudioRelayConsoleId();

    /**
     * Retrieve the ConsoleID of console used to send Audio Capture data from Facilitator to Server
     * @return Audio Relay Console ID of console used to transfer audio captures from Facilitator to Server
     */
    @WebMethod
    public String getOutAudioRelayConsoleId();

    /**
     * Request Permission to Share Screen
     * @param presenterId Presenter ID of presenter requesting screen share
     * @param includeAudio Should audio relay be also requested?
     * @return True if request is taken to consideration. False if request is immediately rejected.
     */
    @WebMethod
    public boolean requestScreenAccess(@WebParam(name = "PresenterId") String presenterId, @WebParam(name = "IncludeAudio") boolean includeAudio);

    /**
     * Request Permission to share audio
     * @param presenterId Presenter ID of presenter requesting audio share
     * @return True if request is taken to consideration. False if request is immediately rejected.
     */
    @WebMethod
    public boolean requestAudioRelayAccess(@WebParam(name = "PresenterId") String presenterId);

    /**
     * Retrieve the list of Presenters reported to Server as connected to self
     * @return List of Participants reported to Server as connected to self
     */
    @WebMethod
    public Collection<VMParticipant> getParticipants();

    /**
     * Retrieve a participant from the list of presenters reported to Server as connected to Self
     * @param participantId Participant Id to retrieve Participant
     * @return Participant with given participant id
     */
    @WebMethod
    public VMParticipant getParticipant(@WebParam(name = "ParticipantId") String participantId);

    /**
     * Update the Server on set of presenters connected to self
     * @param participants Set of Presenters
     */
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
