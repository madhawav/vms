package eduze.vms.presenter.logic;

import eduze.vms.presenter.logic.mpi.presenterconsole.AssignedTask;

/**
 * Created by Fujitsu on 4/16/2016.
 */

/**
 * Structure representing information on an Assigned Task to Presenter
 */
public class AssignedTaskInfo {
    /**
     * The MPI to internal Assigned Task
     */
    private AssignedTask assignedTask;

    /**
     * Retrieve Title of Assigned Task
     * @return
     */
    public String getTitle()
    {
        return assignedTask.getTitle();
    }

    /**
     * Retrieve Description of Assigned Task
     * @return
     */
    public String getDescription()
    {
        return assignedTask.getDescription();
    }

    /**
     * Retrieve ID of Assigned Task
     * @return
     */
    public String getId()
    {
        return assignedTask.getTaskId();
    }

    /**
     * Constructor
     * @param task
     */
    AssignedTaskInfo(AssignedTask task)
    {
        this.assignedTask = task;
    }
}
