/**
 * PresenterConsole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.presenter.logic.mpi.presenterconsole;

public interface PresenterConsole extends java.rmi.Remote {
    /**
     * Return name of Presenter as set in Facilitator
     * @return Name of Presenter
     */
    public java.lang.String getName() throws java.rmi.RemoteException;

    /**
     * Set the name of presenter as recognized by facilitator
     * @param newName
     */
    public void setName(java.lang.String newName) throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.DisconnectedException;

    /**
     * Disconnect from Facilitator
     */
    public void disconnect() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.ServerConnectionException;

    /**
     * Retrieve assigned tasks of presenter
     * @return
     */
    public eduze.vms.presenter.logic.mpi.presenterconsole.AssignedTask[] getAssignedTasks() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.DisconnectedException;

    /**
     * Confirmation from presenter to facilitator
     */
    public void acknowledgeConnection() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.ServerConnectionException;

    /**
     * Retrieve ID for ScreenShareConsole
     * @return
     */
    public java.lang.String getOutScreenShareConsoleId() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.DisconnectedException;

    /**
     * Retrieve ID for AudioRelayConsole
     * @return
     */
    public java.lang.String getOutAudioRelayConsoleId() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.DisconnectedException;

    /**
     * Request permission to share screen
     * @param includeAudio True to request audio share as well
     * @return true if request is considered. false if request is immediately rejected.
     */
    public boolean requestScreenAccess(boolean includeAudio) throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.DisconnectedException;

    /**
     * Request permission to speak
     * @return True if request is being considered. False if request is immediately rejected.
     */
    public boolean requestAudioRelayAccess() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.DisconnectedException;

    /**
     * Return console id
     * @return
     */
    public java.lang.String getConsoleId() throws java.rmi.RemoteException;

    /**
     * Return snapshot of current status of virtual meeting
     * @return Snapshot of Virtual Meeting
     */
    public eduze.vms.presenter.logic.mpi.presenterconsole.VirtualMeetingSnapshot getVMSnapshot() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.ServerConnectionException;

    /**
     * Notify the Facilitator that presenter is alive
     * @throws DisconnectedException
     */
    public void notifyAlive() throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.presenterconsole.DisconnectedException;
}
