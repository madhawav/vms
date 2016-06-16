package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fujitsu on 4/16/2016.
 */

/**
 * Supports Creation, Assignment and Deletion of Shared Tasks
 */
public class SharedTaskManager {
    private FacilitatorController controller; //Facilitator Controller that contain Shared Task Manager
    private SharedTask[] lastKnownSharedTaskList = null; //Last known shared task list
    private ArrayList<SharedTasksListener> sharedTasksListeners = new ArrayList<>(); //List of Shared Task Listeners

    /**
     * Constructor
     * @param controller Facilitator Controller
     */
    SharedTaskManager(FacilitatorController controller)
    {
        this.controller = controller;
    }

    /**
     * Retrieve Shared Tasks based on last state update from server
     * @return
     */
    public SharedTaskInfo[] getSharedTasks()
    {
        SharedTask[] sharedTasks =  controller.getVmStatus().getSharedTasks();
        if(sharedTasks == null)
            return new SharedTaskInfo[0];
        SharedTaskInfo[] results = new SharedTaskInfo[sharedTasks.length];
        for(int i = 0; i < results.length;i++)
        {
            results[i] = new SharedTaskInfo(sharedTasks[i]);
        }
        return results;
    }

    /**
     * Retrieve shared task from shared task id
     * @param sharedTaskId
     * @return
     */
    public SharedTaskInfo getSharedTask(String sharedTaskId)
    {
        SharedTask[] tasks = controller.getVmStatus().getSharedTasks();
        if(tasks == null)
            return null;
        for(SharedTask task : tasks)
        {
            //Found a shared task with matching id
            if(task.getTaskId().equals(sharedTaskId))
                return new SharedTaskInfo(task);
        }

        //no match
        return null;
    }

    /**
     * Create a new shared task
     * @param title Title of shared task
     * @param description Description of shared task
     * @return Shared Task Id
     * @throws ServerConnectionException
     */
    public String createNewSharedTask(String title, String description) throws ServerConnectionException {
        try {
            return controller.getFacilitatorService().getVirtualMeeting().addSharedTask(title,description);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }

    /**
     * Removes a shared task from list of shared tasks
     * @param taskId SharedTaskId
     * @throws SharedTaskNotFoundException
     * @throws ServerConnectionException
     */
    public void removeSharedTask(String taskId) throws SharedTaskNotFoundException, ServerConnectionException {
        try {
            controller.getFacilitatorService().getVirtualMeeting().removeSharedTask(taskId);
        } catch (SharedTaskNotFoundException e)
        {
            throw e;
        }
        catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }

    /**
     * Modifies a shared task
     * @param taskId Task Id of task to be modified
     * @param title New title
     * @param description New description
     * @throws SharedTaskNotFoundException Shared Task is no longer presenter
     * @throws ServerConnectionException Error in Connection to Server
     */
    public void modifySharedTask(String taskId, String title, String description) throws SharedTaskNotFoundException, ServerConnectionException {
        try {
            controller.getFacilitatorService().getVirtualMeeting().modifySharedTask(taskId,title,description);
        } catch (SharedTaskNotFoundException e)
        {
            throw e;
        }
        catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }

    /**
     * Assign a shared task to a presenter
     * @param taskId Task Id
     * @param facilitatorId Facilitator Id of Presenter
     * @param presenterId Presenter Console Id of Presenter
     * @throws SharedTaskNotFoundException Invalid Task Id
     * @throws ServerConnectionException
     */
    public void assignTaskToPresenter(String taskId,String facilitatorId,String presenterId) throws SharedTaskNotFoundException, ServerConnectionException {
        try {
            controller.getFacilitatorService().getVirtualMeeting().assignSharedTask(taskId,facilitatorId,presenterId);
        } catch (SharedTaskNotFoundException e)
        {
            throw e;
        }
        catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }

    /**
     * Un-assign a shared task from presenter
     * @param taskId Shared task id
     * @throws SharedTaskNotFoundException
     * @throws ServerConnectionException
     */
    public void unAssignTaskFromPresenter(String taskId) throws SharedTaskNotFoundException, ServerConnectionException {
        try {
            controller.getFacilitatorService().getVirtualMeeting().unAssignSharedTask(taskId);
        } catch (SharedTaskNotFoundException e)
        {
            throw e;
        }
        catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }

    /**
     * Add a listener to listen to changes in Shared Tasks
     * @param listener
     */
    public void addSharedTasksListener(SharedTasksListener listener)
    {
        sharedTasksListeners.add(listener);
    }

    /**
     * Remove a Shared Task Listener
     * @param listener
     */
    public void removeSharedTasksListener(SharedTasksListener listener)
    {
        sharedTasksListeners.remove(listener);
    }

    /**
     * Generate events to notify listeners upon changes of Shared Tasks
     */
    void onVMStatusUpdate()
    {
        SharedTask[] newSharedTasks = controller.getVmStatus().getSharedTasks();
        //The server returns null in absence of shared tasks. Make it an empty array.
        if(newSharedTasks == null)
            newSharedTasks = new SharedTask[0];

        if(lastKnownSharedTaskList == null && newSharedTasks != null)
        {
            //Shared Task listener has just now got initialized
            lastKnownSharedTaskList = newSharedTasks;
            notifySharedTaskListenerInitiated();
            return;
        }

        //Use Hash Maps for fast searching
        HashMap<String,SharedTask> newTasksMap = new HashMap<>(); //Set of Shared Tasks in latest VM update
        HashMap<String,SharedTask> oldTasksMap = new HashMap<>(); //Set of shared tasks in previous VM update

        //Popilate hash maps
        for(SharedTask task : newSharedTasks)
        {
            newTasksMap.put(task.getTaskId(),task);
        }
        for(SharedTask task : lastKnownSharedTaskList)
        {
            oldTasksMap.put(task.getTaskId(),task);
        }

        //Update old list
        SharedTask[] oldTaskList = lastKnownSharedTaskList;
        lastKnownSharedTaskList = newSharedTasks;

        //search for new tasks
        for(SharedTask task : newSharedTasks)
        {
            if(oldTasksMap.get(task.getTaskId()) == null)
            {
                notifySharedTaskListenerNewSharedTask(new SharedTaskInfo(task));
            }
        }

        //search for deleted tasks
        for(SharedTask task : oldTaskList)
        {
            if(newTasksMap.get(task.getTaskId()) == null)
            {
                notifySharedTaskListenerRemoveSharedTask(new SharedTaskInfo(task));
            }
        }

        //Look for changes in existing set of tasks
        for(SharedTask task:newSharedTasks)
        {
            //see whether there was an old task
            if(oldTasksMap.get(task.getTaskId()) == null)
            {
                //if not and new one is assigned, separately report it
                if(task.getAssignedPresenterId() != null && !("".equals(task.getAssignedPresenterId())))
                {
                    //New task assignment
                    notifySharedTaskAssignmentChanged(null, new SharedTaskInfo(task));
                }
            }
            else
            {
                //find the corresponding old task
                SharedTask newTask = task;
                SharedTask oldTask = oldTasksMap.get(task.getTaskId());

                //check for changes in name and description
                boolean foundChange = false;
                //Check for modifications in title
                if(!oldTask.getTitle().equals(newTask.getTitle()))
                    foundChange = true;
                //Check for modifcations in title
                if(oldTask.getDescription() == null && newTask.getDescription() != null)
                {
                    foundChange = true;
                }
                else if(newTask.getDescription() == null && oldTask.getDescription() != null)
                {
                    foundChange = true;
                }
                else if(oldTask.getDescription()!= null && !(oldTask.getDescription().equals(newTask.getDescription())))
                {
                    foundChange = true;
                }
                //Notify changes
                if(foundChange)
                {
                    notifySharedTaskModified(new SharedTaskInfo(oldTask), new SharedTaskInfo(newTask));
                }

                //check for changes in assignment
                foundChange = false;
                if(oldTask.getAssignedPresenterId() == null && newTask.getAssignedPresenterId() != null)
                    foundChange = true;
                else if(newTask.getAssignedPresenterId() == null && oldTask.getAssignedPresenterId() != null)
                    foundChange = true;
                else if(oldTask.getAssignedPresenterId() != null && !(oldTask.getAssignedPresenterId().equals(newTask.getAssignedPresenterId())))
                    foundChange = true;
                //Notify Changes
                if(foundChange)
                    notifySharedTaskAssignmentChanged(new SharedTaskInfo(oldTask), new SharedTaskInfo(newTask));
            }
        }
       // lastKnownSharedTaskList = newSharedTasks;
    }

    /**
     * Notify modification in shared task
     * @param oldTask
     * @param newTask
     */
    private synchronized void notifySharedTaskModified(SharedTaskInfo oldTask, SharedTaskInfo newTask) {
        SharedTasksListener[] listeners = new SharedTasksListener[sharedTasksListeners.size()];
        sharedTasksListeners.toArray(listeners);
        for(SharedTasksListener listener : listeners)
            listener.onSharedTaskModified(oldTask,newTask);
    }

    /**
     * Notify change in task assignment
     * @param oldTask
     * @param newTask
     */
    private synchronized void notifySharedTaskAssignmentChanged(SharedTaskInfo oldTask, SharedTaskInfo newTask) {
        SharedTasksListener[] listeners = new SharedTasksListener[sharedTasksListeners.size()];
        sharedTasksListeners.toArray(listeners);
        for(SharedTasksListener listener : listeners)
            listener.onSharedTaskAssignmentChanged(oldTask, newTask);
    }

    /**
     * Notify removal of shared task
     * @param sharedTaskInfo
     */
    private synchronized void notifySharedTaskListenerRemoveSharedTask(SharedTaskInfo sharedTaskInfo) {
        SharedTasksListener[] listeners = new SharedTasksListener[sharedTasksListeners.size()];
        sharedTasksListeners.toArray(listeners);
        for(SharedTasksListener listener : listeners)
            listener.onSharedTaskRemoved(sharedTaskInfo);
    }

    /**
     * Notify creation of new shared task
     * @param task
     */
    private synchronized void notifySharedTaskListenerNewSharedTask(SharedTaskInfo task) {
        SharedTasksListener[] listeners = new SharedTasksListener[sharedTasksListeners.size()];
        sharedTasksListeners.toArray(listeners);
        for(SharedTasksListener listener : listeners)
            listener.onNewSharedTask(task);
    }

    /**
     * Notify inception of shared task listener
     */
    private synchronized void notifySharedTaskListenerInitiated() {
        SharedTasksListener[] listeners = new SharedTasksListener[sharedTasksListeners.size()];
        sharedTasksListeners.toArray(listeners);
        for(SharedTasksListener listener : listeners)
            listener.onInitiated();
    }

    /**
     * Interface for events of shared tasks
     */
    public static interface SharedTasksListener
    {
        /**
         * Shared Task System has be initiated
         */
        public void onInitiated();

        /**
         * A new shared task has been added
         * @param newTask
         */
        public void onNewSharedTask(SharedTaskInfo newTask);

        /**
         * A shared task has been removed
         * @param removedTask
         */
        public void onSharedTaskRemoved(SharedTaskInfo removedTask);

        /**
         * A shared task has been modified
         * @param oldTask
         * @param newTask
         */
        public void onSharedTaskModified(SharedTaskInfo oldTask, SharedTaskInfo newTask);

        /**
         * A shared task assignment has been changed
         * @param oldTask
         * @param newTask
         */
        public void onSharedTaskAssignmentChanged(SharedTaskInfo oldTask, SharedTaskInfo newTask);

    }

}
