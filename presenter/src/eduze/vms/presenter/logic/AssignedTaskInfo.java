package eduze.vms.presenter.logic;

import eduze.vms.presenter.logic.mpi.presenterconsole.AssignedTask;

/**
 * Created by Fujitsu on 4/16/2016.
 */
public class AssignedTaskInfo {
    private AssignedTask assignedTask;
    public String getTitle()
    {
        return assignedTask.getTitle();
    }
    public String getDescription()
    {
        return assignedTask.getDescription();
    }
    public String getId()
    {
        return assignedTask.getTaskId();
    }
    AssignedTaskInfo(AssignedTask task)
    {
        this.assignedTask = task;
    }
}
