/**
 * PresenterConsole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.presenter.logic.mpi.presenterconsole;

public interface PresenterConsole extends java.rmi.Remote {
    public java.lang.String getName() throws java.rmi.RemoteException;
    public void setName(java.lang.String newName) throws java.rmi.RemoteException;
    public void disconnect() throws java.rmi.RemoteException;
    public java.lang.String getConsoleId() throws java.rmi.RemoteException;
    public java.lang.String getOutAudioRelayConsoleId() throws java.rmi.RemoteException;
    public java.lang.String getOutScreenShareConsoleId() throws java.rmi.RemoteException;
    public void acknowledgeConnection() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.ServerConnectionException;
    public boolean requestAudioRelayAccess() throws java.rmi.RemoteException;
    public boolean requestScreenAccess(boolean includeAudio) throws java.rmi.RemoteException;
    public eduze.vms.presenter.logic.mpi.presenterconsole.VirtualMeetingSnapshot getVMSnapshot() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.ServerConnectionException;
}
