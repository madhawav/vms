package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Fujitsu on 4/16/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="AssignedTask")
public class AssignedTask {
    @XmlElement(name="taskId")
    protected String taskId = null;
    @XmlElement(name="Title")
    protected String title = null;
    @XmlElement(name="Description")
    protected String description = null;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public static AssignedTask fromSharedTask(SharedTask sharedTask)
    {
        AssignedTask result = new AssignedTask();
        result.setTitle(sharedTask.getTitle());
        result.setDescription(sharedTask.getDescription());
        result.setTaskId(sharedTask.getTaskId());
        return result;
    }
}
