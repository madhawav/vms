/**
 * VirtualMeeting.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.virtualmeeting;

public interface VirtualMeeting extends java.rmi.Remote {
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot getSnapshot() throws java.rmi.RemoteException;
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask getSharedTask(java.lang.String arg0) throws java.rmi.RemoteException;
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus getStatus() throws java.rmi.RemoteException;
    public java.lang.String getVMId() throws java.rmi.RemoteException;
    public java.lang.String addSharedTask(java.lang.String title, java.lang.String description) throws java.rmi.RemoteException;
    public java.lang.String getActiveSpeechFacilitatorId() throws java.rmi.RemoteException;
    public java.lang.String getActiveScreenPresenterId() throws java.rmi.RemoteException;
    public void unAssignSharedTask(java.lang.String shardTaskId) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask[] getSharedTasks() throws java.rmi.RemoteException;
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant[] getParticipants() throws java.rmi.RemoteException;
    public java.lang.String getActiveSpeechPresenterId() throws java.rmi.RemoteException;
    public void removeSharedTask(java.lang.String sharedTaskId) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant getParticipant(java.lang.String participantId) throws java.rmi.RemoteException;
    public void assignSharedTask(java.lang.String shardTaskId, java.lang.String facilitatorId, java.lang.String presenterId) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;
    public java.lang.String getActiveScreenFacilitatorId() throws java.rmi.RemoteException;
    public void modifySharedTask(java.lang.String sharedTaskId, java.lang.String newTitle, java.lang.String newDescrition) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;
}
