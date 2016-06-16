package eduze.vms.server.logic.webservices;

import eduze.vms.foundation.logic.PasswordUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Fujitsu on 4/16/2016.
 */

/**
 * A shared task between facilitators, that could be assigned to a presenter
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="SharedTask")
public class SharedTask {
    /**
     * Unique ID of Shared Task
     */
    @XmlElement(name="TaskId")
    protected String taskId = PasswordUtil.generateSharedTaskId();

    /**
     * Title of Shared task.
     */
    @XmlElement(name="Title")
    protected String title= null;

    /**
     * Description of shared task (optional)
     */
    @XmlElement(name="Description")
    protected String description= null;

    /**
     * Facilitator ID if task is assigned. null otherwise
     */
    @XmlElement(name="AssignedFacilitatorId")
    protected String assignedFacilitatorId = null;

    /**
     * Presenter ID if task is assigned. null otherwise
     */
    @XmlElement(name="AssignedPresenterId")
    protected String assignedPresenterId = null;

    /**
     * Presenter Name if task is assigned. null otherwise
     */
    @XmlElement(name="AssignedPresenterName")
    protected String assignedPresenterName = null;

    public String getAssignedPresenterName() {
        return assignedPresenterName;
    }

    public void setAssignedPresenterName(String assignedPresenterName) {
        this.assignedPresenterName = assignedPresenterName;
    }

    public String getAssignedFacilitatorId() {
        return assignedFacilitatorId;
    }

    public String getAssignedPresenterId() {
        return assignedPresenterId;
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

    public void setAssignedFacilitatorId(String assignedFacilitatorId) {
        this.assignedFacilitatorId = assignedFacilitatorId;
    }

    public void setAssignedPresenterId(String assignedPresenterId) {
        this.assignedPresenterId = assignedPresenterId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
