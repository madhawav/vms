package eduze.vms.presenter.logic;

import eduze.vms.presenter.logic.mpi.presenterconsole.AssignedTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Fujitsu on 4/16/2016.
 */

/**
 * Assigned Task Manager to manage tasks assigned to presenter
 */
public class AssignedTasksManager {
    //Presenter Controller
    private PresenterController controller;

    //Map of assigned tasks
    private HashMap<String,AssignedTask> assignedTasks = null;

    //listeners
    private ArrayList<AssignedTaskListener> assignedTaskListeners = new ArrayList<>();

    /**
     * Constructor of Assigned Task Manager
     * @param controller Presenter Controller
     */
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

    /**
     * Retrieve list of assigned tasks to presenter
     * @return List of Assigned Tasks to Presenter
     */
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

    /**
     * Add listener to be notified on addition/removal of assigned tasks
     * @param listener listener to be notified on addition and removal of assigned tasks
     */
    public void addAssignedTaskListener(AssignedTaskListener listener)
    {
        assignedTaskListeners.add(listener);
    }

    /**
     * Remove an assigned task listener
     * @param listener
     */
    public void removeAssignedTaskListener(AssignedTaskListener listener)
    {
        assignedTaskListeners.remove(listener);
    }

    /**
     * Notify listeners that a task has been assigned
     * @param task
     */
    void notifyTaskAssigned(AssignedTaskInfo task)
    {
        for(AssignedTaskListener listener: assignedTaskListeners)
        {
            listener.onTaskAssigned(task);
        }
    }

    /**
     * Notify listeners that a task has been unassigned
     * @param task
     */
    public void notifyTaskUnAssigned(AssignedTaskInfo task)
    {
        for(AssignedTaskListener listener: assignedTaskListeners)
        {
            listener.onTaskUnAssigned(task);
        }
    }

    /**
     * Notify listeners that a assigned task has been modified
     * @param oldTask previous state of task
     * @param newTask new state of task
     */
    public void notifyTaskModified(AssignedTaskInfo oldTask, AssignedTaskInfo newTask)
    {
        for(AssignedTaskListener listener: assignedTaskListeners)
        {
            listener.onTaskModified(oldTask,newTask);
        }
    }

    /**
     * Notify presenter that assigned tasks system has been started
     */
    public void notifyAssignedTasksInitiated()
    {
        for(AssignedTaskListener listener: assignedTaskListeners)
        {
            listener.onInitiated();
        }
    }

    /**
     * Retrieve details of an assigned task from shared task id
     * @param taskId shared task id
     * @return
     */
    public AssignedTaskInfo getAssignedTask(String taskId)
    {
        if(assignedTasks == null)
            return null;
        AssignedTask item = assignedTasks.get(taskId);
        if(item == null)
            return null;
        return new AssignedTaskInfo(item);
    }

    /**
     * Listener notified on events related to assigned tasks
     */
    public interface AssignedTaskListener
    {
        /**
         * Notify that assigned tasks system has been initiated
         */
        public void onInitiated();

        /**
         * Notify that a new task has been assigned
         * @param task New Task assigned
         */
        public void onTaskAssigned(AssignedTaskInfo task);

        /**
         * Notify that a task has been unassigned
         * @param task task that has been unassigned
         */
        public void onTaskUnAssigned(AssignedTaskInfo task);

        /**
         * Notify that a task has been modified
         * @param oldTask previous form of task
         * @param newTask new form of task
         */
        public void onTaskModified(AssignedTaskInfo oldTask, AssignedTaskInfo newTask);
    }
}
