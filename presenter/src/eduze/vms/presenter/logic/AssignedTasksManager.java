package eduze.vms.presenter.logic;

import eduze.vms.presenter.logic.mpi.presenterconsole.AssignedTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Fujitsu on 4/16/2016.
 */
public class AssignedTasksManager {
    private PresenterController controller;
    private HashMap<String,AssignedTask> assignedTasks = null;

    private ArrayList<AssignedTaskListener> assignedTaskListeners = new ArrayList<>();
    public AssignedTasksManager(PresenterController controller)
    {
        this.controller = controller;
    }
    void onAssignedTasksUpdate(AssignedTask[] newTasks)
    {
        if(newTasks == null)
            newTasks = new AssignedTask[0];
        HashMap<String,AssignedTask> oldTasksMap = assignedTasks;
        assignedTasks = new HashMap<>();
        for(AssignedTask task : newTasks)
        {
            assignedTasks.put(task.getTaskId(),task);
        }
        HashMap<String,AssignedTask> newTasksMap = assignedTasks;

        if(oldTasksMap == null) //old tasks is null but new tasks exist. therefore its initiation
        {
            notifyAssignedTasksInitiated();
            return;
        }
        //old task map, before current update. can be null
        //new task map, after current update. not null
        //look for new tasks
        for(AssignedTask task : newTasksMap.values())
        {
            if(oldTasksMap.get(task.getTaskId()) == null)
            {
                notifyTaskAssigned(new AssignedTaskInfo(task));
            }
        }
        //look for removed tasks
        for(AssignedTask task : oldTasksMap.values())
        {
            if(newTasksMap.get(task.getTaskId()) == null)
            {
                notifyTaskUnAssigned(new AssignedTaskInfo(task));
            }
        }

        //look for modified tasks
        for(AssignedTask newT: newTasksMap.values())
        {
            AssignedTask oldT = oldTasksMap.get(newT.getTaskId());
            if(oldT == null)
                continue;
            boolean changed = false;
            if(!oldT.getTitle().equals(newT.getTitle()))
            {
                changed = true;
            }
            if(oldT.getDescription() == null && newT.getDescription() != null)
                changed = true;
            else if(newT.getDescription() == null && oldT.getDescription() != null)
                changed = true;
            else if(newT.getDescription() != null && !(newT.getDescription().equals(oldT.getDescription())))
                changed = true;
            if(changed)
                notifyTaskModified(new AssignedTaskInfo(oldT) , new AssignedTaskInfo(newT));
        }

    }

    public Collection<AssignedTaskInfo> getAssignedTasks()
    {
        if(assignedTasks == null)
            return Arrays.asList(new AssignedTaskInfo[0]);
        AssignedTaskInfo[] results = new AssignedTaskInfo[assignedTasks.size()];
        int i = 0;
        for(AssignedTask task : assignedTasks.values())
        {
            results[i++] = new AssignedTaskInfo(task);
        }
        return Arrays.asList(results);
    }

    public void addAssignedTaskListener(AssignedTaskListener listener)
    {
        assignedTaskListeners.add(listener);
    }
    public void removeAssignedTaskListener(AssignedTaskListener listener)
    {
        assignedTaskListeners.remove(listener);
    }

    public void notifyTaskAssigned(AssignedTaskInfo task)
    {
        for(AssignedTaskListener listener: assignedTaskListeners)
        {
            listener.onTaskAssigned(task);
        }
    }

    public void notifyTaskUnAssigned(AssignedTaskInfo task)
    {
        for(AssignedTaskListener listener: assignedTaskListeners)
        {
            listener.onTaskUnAssigned(task);
        }
    }

    public void notifyTaskModified(AssignedTaskInfo oldTask, AssignedTaskInfo newTask)
    {
        for(AssignedTaskListener listener: assignedTaskListeners)
        {
            listener.onTaskModified(oldTask,newTask);
        }
    }
    public void notifyAssignedTasksInitiated()
    {
        for(AssignedTaskListener listener: assignedTaskListeners)
        {
            listener.onInitiated();
        }
    }

    public AssignedTaskInfo getAssignedTask(String taskId)
    {
        if(assignedTasks == null)
            return null;
        AssignedTask item = assignedTasks.get(taskId);
        if(item == null)
            return null;
        return new AssignedTaskInfo(item);
    }

    public interface AssignedTaskListener
    {
        public void onInitiated();
        public void onTaskAssigned(AssignedTaskInfo task);
        public void onTaskUnAssigned(AssignedTaskInfo task);
        public void onTaskModified(AssignedTaskInfo oldTask, AssignedTaskInfo newTask);
    }
}
