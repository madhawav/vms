/**
 * FacilitatorConsole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.facilitatorconsole;

public interface FacilitatorConsole extends java.rmi.Remote {
    /**
     * Disconnect from Server
     */
    public void disconnect() throws java.rmi.RemoteException;
    /**
     * Notify the server that facilitator is alive
     */
    public void notifyAlive() throws java.rmi.RemoteException;
    /**
     * Retrieve Facilitator Console Id
     * @return FacilitatorConsoleId
     */
    public java.lang.String getConsoleId() throws java.rmi.RemoteException;

    /**
     * Adjourn the meeting
     */
    public void adjournMeeting() throws java.rmi.RemoteException;
    /**
     * Retrieve the ConsoleID of console used to send Screen Capture data from Facilitator to Server
     * @return Screen Share Console ID of console used to transfer screen captures from Facilitator to Server
     */
    public java.lang.String getOutScreenShareConsoleId() throws java.rmi.RemoteException;
    /**
     * Request Permission to share audio
     * @param presenterId Presenter ID of presenter requesting audio share
     * @return True if request is taken to consideration. False if request is immediately rejected.
     */
    public boolean requestAudioRelayAccess(java.lang.String presenterId) throws java.rmi.RemoteException;
    /**
     * Retrieve the list of Presenters reported to Server as connected to self
     * @return List of Participants reported to Server as connected to self
     */
    public eduze.vms.facilitator.logic.mpi.facilitatorconsole.VMParticipant[] getParticipants() throws java.rmi.RemoteException;

    /**
     * Update the Server on set of presenters connected to self
     * @param participants Set of Presenters
     */
    public void setParticipants(eduze.vms.facilitator.logic.mpi.facilitatorconsole.VMParticipant[] participants) throws java.rmi.RemoteException;
    /**
     * Retrieve the ConsoleID used to send Screen Capture data to Facilitator
     * @return Screen Share Console ID of console used to transfer screen captures from Server to Facilitator
     */
    public java.lang.String getInScreenShareConsoleId() throws java.rmi.RemoteException;
    /**
     * Request Permission to Share Screen
     * @param presenterId Presenter ID of presenter requesting screen share
     * @param includeAudio Should audio relay be also requested?
     * @return True if request is taken to consideration. False if request is immediately rejected.
     */
    public boolean requestScreenAccess(java.lang.String presenterId, boolean includeAudio) throws java.rmi.RemoteException;
    /**
     * Retrieve Facilitator Name
     * @return Facilitators Name
     */
    public java.lang.String getFacilitatorName() throws java.rmi.RemoteException;
    /**
     * Retrieve the ConsoleID used to send Audio Capture data to Facilitator
     * @return Audio Relay Console ID of console used to transfer audio captures from Server to Facilitator
     */
    public java.lang.String getInAudioRelayConsoleId() throws java.rmi.RemoteException;
    /**
     * Retrieve the ConsoleID of console used to send Audio Capture data from Facilitator to Server
     * @return Audio Relay Console ID of console used to transfer audio captures from Facilitator to Server
     */
    public java.lang.String getOutAudioRelayConsoleId() throws java.rmi.RemoteException;
    /**
     * Retrieve a participant from the list of presenters reported to Server as connected to Self
     * @param participantId Participant Id to retrieve Participant
     * @return Participant with given participant id
     */
    public eduze.vms.facilitator.logic.mpi.facilitatorconsole.VMParticipant getParticipant(java.lang.String participantId) throws java.rmi.RemoteException;
}
