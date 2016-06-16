/**
 * VirtualMeeting.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.virtualmeeting;

public interface VirtualMeeting extends java.rmi.Remote {
    /**
     * Retrieve snapshot of Virtual Meeting. If many getters of Virtual Meeting are to be called, its recommended to obtain
     * snapshot of VM once and re-use it
     * @return Snapshot of VirtualMeeting
     */
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot getSnapshot() throws java.rmi.RemoteException;

    /**
     * Retrieve Status of VirtualMeeting
     * @return Status of Virtual Meeting
     */
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus getStatus() throws java.rmi.RemoteException;

    /**
     * Retrieve unique id of Virtual Meeting
     * @return Virtual Meeting Id
     */
    public java.lang.String getVMId() throws java.rmi.RemoteException;

    /**
     * Retrieve shared task with given SharedTaskId
     * @param sharedTaskId Id of shared task to be retrieved
     * @return Shared Task with given SharedTaskId
     */
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask getSharedTask(java.lang.String sharedTaskId) throws java.rmi.RemoteException;

    /**
     * Adds a new Shared Task to Virtual Meeting
     * @param title Title of Shared Task
     * @param description Description of Shared Task. Optional. can be null.
     * @return Shared Task Id
     */
    public java.lang.String addSharedTask(java.lang.String title, java.lang.String description) throws java.rmi.RemoteException;

    /**
     * Retrieve Facilitator Id of Active Screen Share Presenter
     * @return Facilitator Id of Active Screen Share Presenter
     */
    public java.lang.String getActiveScreenFacilitatorId() throws java.rmi.RemoteException;

    /**
     * Obtain details of a Virtual Meeting Participant
     * @param participantId presenter console id of participant of which information is queried
     * @return Participant Information of participant with given presenterConsoleId
     */
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant getParticipant(java.lang.String participantId) throws java.rmi.RemoteException;

    /**
     * Assign a shared task to a presenter
     * @param shardTaskId SharedTaskId of shared task to be assigned
     * @param facilitatorId Facilitator ID of presenter to which shared task should be assigned
     * @param presenterId PresenterID of presenter to which shared task should be assigned
     */
    public void assignSharedTask(java.lang.String shardTaskId, java.lang.String facilitatorId, java.lang.String presenterId) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;

    /**
     * Retrieve list of participants (presenters) of Virtual Meeting
     * @return List of participants of virtual meeting
     */
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant[] getParticipants() throws java.rmi.RemoteException;

    /**
     * Retrieve Presenter Id of Active Screen Share Presenter
     * @return Presenter Id of Active Screen Share Presenter
     */
    public java.lang.String getActiveScreenPresenterId() throws java.rmi.RemoteException;

    /**
     * Remove shared task with given sharedTaskId
     * @param sharedTaskId SharedTaskID of shared task to be removed
     * @throws SharedTaskNotFoundException Invalid shared task id or shared task is not found
     */
    public void removeSharedTask(java.lang.String sharedTaskId) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;

    /**
     * Clear assignment of a shared task
     * @param shardTaskId SharedTaskID of sharedtask to be unassigned
     */
    public void unAssignSharedTask(java.lang.String shardTaskId) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;

    /**
     * Modify shared task to have new title and description. If a field is not to be changed, submit the existing value.
     * @param sharedTaskId SharedTaskId of shared task to be modified
     * @param newTitle new title of shared task
     * @param newDescription new description of shared task. Can be null to clear the description.
     */
    public void modifySharedTask(java.lang.String sharedTaskId, java.lang.String newTitle, java.lang.String newDescription) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;

    /**
     * Retrieve Facilitator Id of Active Speech Share Presenter
     * @return Facilitator Id of Active Speech Share Presenter
     */
    public java.lang.String getActiveSpeechFacilitatorId() throws java.rmi.RemoteException;

    /**
     * Retrieve Presenter Id of Active Speech Share Presenter
     * @return Presenter Id of Active Speech Share Presenter
     */
    public java.lang.String getActiveSpeechPresenterId() throws java.rmi.RemoteException;

    /**
     * Retrieve list of shared tasks of Virtual Meeting
     * @return List of shared tasks in virtual meeting
     */
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask[] getSharedTasks() throws java.rmi.RemoteException;
}
