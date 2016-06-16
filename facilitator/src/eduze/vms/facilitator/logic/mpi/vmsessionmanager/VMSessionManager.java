/**
 * VMSessionManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.vmsessionmanager;

public interface VMSessionManager extends java.rmi.Remote {
    /**
     * Connect Facilitator to Virtual Meeting
     * @param name Name of Facilitator
     * @param pairKey PairKey provided to facilitator during pairing mechanism
     * @return Connection Result
     * @throws ServerNotReadyException The Server is not ready to accept facilitators
     * @throws MeetingAlreadyStartedException The meeting has been started already. Hence, no new facilitators can join.
     * @throws UnknownFacilitatorException The Facilitator is not known. Considering pairing again.
     * @throws FacilitatorAlreadyConnectedException Facilitator is already connected.
     * @throws MeetingAdjournedException Meeting has been adjourned and hence not accepting new Facilitators
     */
    public eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult connect(java.lang.String name, java.lang.String pairKey) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.UnknownFacilitatorException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.FacilitatorAlreadyConnectedException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.MeetingAdjournedException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.MeetingAlreadyStartedException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;

    /**
     * Retrieve Status of Virtual Meeting in Server
     * @return Virtual Meeting Status
     */
    public eduze.vms.facilitator.logic.mpi.vmsessionmanager.SessionStatus getStatus() throws java.rmi.RemoteException;
}
