/**
 * FacilitatorConsole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.facilitatorconsole;

public interface FacilitatorConsole extends java.rmi.Remote {
    public void disconnect() throws java.rmi.RemoteException;
    public java.lang.String getConsoleId() throws java.rmi.RemoteException;
    public java.lang.String getInScreenShareConsoleId() throws java.rmi.RemoteException;
    public java.lang.String getFacilitatorName() throws java.rmi.RemoteException;
    public eduze.vms.facilitator.logic.mpi.facilitatorconsole.VMParticipant[] getParticipants() throws java.rmi.RemoteException;
    public java.lang.String getOutScreenShareConsoleId() throws java.rmi.RemoteException;
    public java.lang.String getInAudioRelayConsoleId() throws java.rmi.RemoteException;
    public boolean requestScreenAccess(java.lang.String presenterId, boolean includeAudio) throws java.rmi.RemoteException;
    public boolean requestAudioRelayAccess(java.lang.String presenterId) throws java.rmi.RemoteException;
    public void setParticipants(eduze.vms.facilitator.logic.mpi.facilitatorconsole.VMParticipant[] participants) throws java.rmi.RemoteException;
    public java.lang.String getOutAudioRelayConsoleId() throws java.rmi.RemoteException;
    public eduze.vms.facilitator.logic.mpi.facilitatorconsole.VMParticipant getParticipant(java.lang.String participantId) throws java.rmi.RemoteException;
}
