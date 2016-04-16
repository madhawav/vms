package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask;

/**
 * Created by Fujitsu on 4/16/2016.
 */
public class SharedTaskInfo {
    private SharedTask sharedTask = null;
    SharedTaskInfo(SharedTask task)
    {
        this.sharedTask = task;
    }

    public String getId()
    {
        return sharedTask.getTaskId();
    }

    public String getTitle()
    {
        return sharedTask.getTitle();
    }

    public String getDescription()
    {
        return sharedTask.getDescription();
    }
    public boolean isAssigned()
    {
        if(sharedTask.getAssignedPresenterId() == null)
            return false;
        if("".equals(sharedTask.getAssignedPresenterId()))
            return false;
        return true;
    }

    public String getAssignedPresenterName()
    {
        return sharedTask.getAssignedPresenterName();
    }

    public String getAssignedPresenterId()
    {
        return sharedTask.getAssignedPresenterId();
    }

    public String getAssignedFacilitatorId()
    {
        return sharedTask.getAssignedFacilitatorId();
    }


}
