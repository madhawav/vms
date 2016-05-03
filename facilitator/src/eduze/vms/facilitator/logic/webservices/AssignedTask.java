package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Fujitsu on 4/16/2016.
 */

/**
 * Represents a task assigned to presenter from a facilitator. Used for communication of assigned tasks from Facilitator to its presenters.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="AssignedTask")
public class AssignedTask {
    @XmlElement(name="taskId") //unique id of task
    protected String taskId = null;
    @XmlElement(name="Title") //title of task
    protected String title = null;
    @XmlElement(name="Description") //description of task
    protected String description = null;

    /**
     * Set the title of task
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return unique id of task
     * @param taskId
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * Set description of task
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return description of task
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieve unique id of task
     * @return
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * Retrieve title of task
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Create an AssignedTask from a SharedTask MPI for Server
     * @param sharedTask
     * @return
     */
    public static AssignedTask fromSharedTask(SharedTask sharedTask)
    {
        AssignedTask result = new AssignedTask();
        result.setTitle(sharedTask.getTitle());
        result.setDescription(sharedTask.getDescription());
        result.setTaskId(sharedTask.getTaskId());
        return result;
    }
}
