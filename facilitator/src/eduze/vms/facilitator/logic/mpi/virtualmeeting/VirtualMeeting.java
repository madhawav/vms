/**
 * VirtualMeeting.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.virtualmeeting;

public interface VirtualMeeting extends java.rmi.Remote {
    public java.lang.String getActiveSpeechFacilitatorId() throws java.rmi.RemoteException;
    public java.lang.String getActiveSpeechPresenterId() throws java.rmi.RemoteException;
    public java.lang.String getActiveScreenPresenterId() throws java.rmi.RemoteException;
    public java.lang.String getActiveScreenFacilitatorId() throws java.rmi.RemoteException;
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus getStatus() throws java.rmi.RemoteException;
    public java.lang.String getVMId() throws java.rmi.RemoteException;
}
