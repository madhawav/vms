package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask;

/**
 * Created by Fujitsu on 4/16/2016.
 */

/**
 * Details of shared task assigned to a presenter
 */
public class SharedTaskInfo {
    //Shared Task MPI from Server
    private SharedTask sharedTask = null;

    /**
     * Constructor
     * @param task Shared Task MPI from Server
     */
    SharedTaskInfo(SharedTask task)
    {
        this.sharedTask = task;
    }

    /**
     * Get Shared Task Id
     * @return Shared Task Id
     */
    public String getId()
    {
        return sharedTask.getTaskId();
    }

    /**
     * Get Shared Task Title
     * @return Shared Task Title
     */
    public String getTitle()
    {
        return sharedTask.getTitle();
    }

    /**
     * Get Shared Task Description
     * @return Shared Task Description
     */
    public String getDescription()
    {
        return sharedTask.getDescription();
    }

    /**
     * Is the shared task assigned to a presenter?
     * @return True if shared task is assigned to a presenter. Otherwise return False.
     */
    public boolean isAssigned()
    {
        if(sharedTask.getAssignedPresenterId() == null)
            return false;
        if("".equals(sharedTask.getAssignedPresenterId()))
            return false;
        return true;
    }

    /**
     * Retrieve name of assigned presenter of shared task
     * @return
     */
    public String getAssignedPresenterName()
    {
        return sharedTask.getAssignedPresenterName();
    }

    /**
     * Retrieve assigned presenters presenter console id
     * @return
     */
    public String getAssignedPresenterId()
    {
        return sharedTask.getAssignedPresenterId();
    }

    /**
     * Retrieve assigned presenters facilitator console id
     * @return
     */
    public String getAssignedFacilitatorId()
    {
        return sharedTask.getAssignedFacilitatorId();
    }


}
