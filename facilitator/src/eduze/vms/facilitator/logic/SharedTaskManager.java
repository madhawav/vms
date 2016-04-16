package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fujitsu on 4/16/2016.
 */
public class SharedTaskManager {
    private FacilitatorController controller;
    private SharedTask[] lastKnownSharedTaskList = null;
    private ArrayList<SharedTasksListener> sharedTasksListeners = new ArrayList<>();
    SharedTaskManager(FacilitatorController controller)
    {
        this.controller = controller;
    }
    public SharedTaskInfo[] getSharedTasks()
    {
        SharedTask[] sharedTasks =  controller.getVmStatus().getSharedTasks();
        SharedTaskInfo[] results = new SharedTaskInfo[sharedTasks.length];
        for(int i = 0; i < results.length;i++)
        {
            results[i] = new SharedTaskInfo(sharedTasks[i]);
        }
        return results;
    }

    public SharedTaskInfo getSharedTask(String sharedTaskId)
    {
        SharedTask[] tasks = controller.getVmStatus().getSharedTasks();
        for(SharedTask task : tasks)
        {
            if(task.getTaskId().equals(sharedTaskId))
                return new SharedTaskInfo(task);
        }

        return null;
    }

    public String createNewSharedTask(String title, String description) throws ServerConnectionException {
        try {
            return controller.getFacilitatorService().getVirtualMeeting().addSharedTask(title,description);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }

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
    public void addSharedTasksListener(SharedTasksListener listener)
    {
        sharedTasksListeners.add(listener);
    }

    public void removeSharedTasksListener(SharedTasksListener listener)
    {
        sharedTasksListeners.remove(listener);
    }

    void onVMStatusUpdate()
    {
        SharedTask[] newSharedTasks = controller.getVmStatus().getSharedTasks();
        if(newSharedTasks == null)
            newSharedTasks = new SharedTask[0];

        if(lastKnownSharedTaskList == null && newSharedTasks != null)
        {
            lastKnownSharedTaskList = newSharedTasks;
            notifySharedTaskListenerInitiated();
            return;
        }
        HashMap<String,SharedTask> newTasksMap = new HashMap<>();
        HashMap<String,SharedTask> oldTasksMap = new HashMap<>();
        for(SharedTask task : newSharedTasks)
        {
            newTasksMap.put(task.getTaskId(),task);
        }
        for(SharedTask task : lastKnownSharedTaskList)
        {
            oldTasksMap.put(task.getTaskId(),task);
        }

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

        for(SharedTask task:newSharedTasks)
        {
            //see whether there was an old task
            if(oldTasksMap.get(task.getTaskId()) == null)
            {
                //if not and new one is assigned, separately report it
                if(task.getAssignedPresenterId() != null && !("".equals(task.getAssignedPresenterId())))
                {
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
                if(!oldTask.getTitle().equals(newTask.getTitle()))
                    foundChange = true;
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
                if(foundChange)
                    notifySharedTaskAssignmentChanged(new SharedTaskInfo(oldTask), new SharedTaskInfo(newTask));
            }
        }
       // lastKnownSharedTaskList = newSharedTasks;
    }

    private void notifySharedTaskModified(SharedTaskInfo oldTask, SharedTaskInfo newTask) {
        for(SharedTasksListener listener : sharedTasksListeners)
            listener.onSharedTaskModified(oldTask,newTask);
    }

    private void notifySharedTaskAssignmentChanged(SharedTaskInfo oldTask, SharedTaskInfo newTask) {
        for(SharedTasksListener listener : sharedTasksListeners)
            listener.onSharedTaskAssignmentChanged(oldTask, newTask);
    }

    private void notifySharedTaskListenerRemoveSharedTask(SharedTaskInfo sharedTaskInfo) {
        for(SharedTasksListener listener : sharedTasksListeners)
            listener.onSharedTaskRemoved(sharedTaskInfo);
    }

    private void notifySharedTaskListenerNewSharedTask(SharedTaskInfo task) {
        for(SharedTasksListener listener : sharedTasksListeners)
            listener.onNewSharedTask(task);
    }

    private void notifySharedTaskListenerInitiated() {
        for(SharedTasksListener listener : sharedTasksListeners)
            listener.onInitiated();
    }

    public static interface SharedTasksListener
    {
        public void onInitiated();
        public void onNewSharedTask(SharedTaskInfo newTask);
        public void onSharedTaskRemoved(SharedTaskInfo removedTask);
        public void onSharedTaskModified(SharedTaskInfo oldTask, SharedTaskInfo newTask);
        public void onSharedTaskAssignmentChanged(SharedTaskInfo oldTask, SharedTaskInfo newTask);

    }

}
