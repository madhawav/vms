/**
 * VMSessionManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.vmsessionmanager;

public interface VMSessionManager extends java.rmi.Remote {
    public eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult connect(java.lang.String arg0, java.lang.String arg1) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.UnknownFacilitatorException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.FacilitatorAlreadyConnectedException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.MeetingAlreadyStartedException, eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;
    public eduze.vms.facilitator.logic.mpi.vmsessionmanager.SessionStatus getStatus() throws java.rmi.RemoteException;
}
