package eduze.vms.server.logic.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents results of connection request
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ConnectionResult")
public class ConnectionResult {

    //Is the request successful
    @XmlElement(name="successful")
    protected boolean isSuccessful = false;

    //The facilitator console id assigned to facilitator if the request is successful
    @XmlElement(name="facilitatorConsoleId")
    protected String facilitatorConsoleId= null;

    //virtual meeting id to which facilitator is assigned if the request is successful
    @XmlElement(name="virtualMeetingConsoleId")
    protected String virtualMeetingConsoleId = null;

    /**
     * Retrieve whether the connection request is successful
     * @return True if connection request is successful. Otherwise False.
     */
    public boolean isSuccessful() {
        return isSuccessful;
    }

    /**
     * Retrieve the FacilitatorConsoleId assigned to Facilitator upon successful connection
     * @return Facilitator Console ID (i.e. Facilitator Id)
     */
    public String getFacilitatorConsoleId() {
        return facilitatorConsoleId;
    }

    /**
     * Retrieve virtual meeting id of virtual meeting to which facilitator is assigned, provided that connection request is accepted
     * @return Virtual Meeting ID of VM
     */
    public String getVirtualMeetingConsoleId() {
        return virtualMeetingConsoleId;
    }

    /**
     * Set the unique id assigned to Facilitator
     * @param facilitatorConsoleId Facilitator Console Id (i.e. Facilitator Id)
     */
    public void setFacilitatorConsoleId(String facilitatorConsoleId) {
        this.facilitatorConsoleId = facilitatorConsoleId;
    }

    /**
     * Set True if connection is successful. Otherwise set False.
     * @param successful
     */
    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    /**
     * Set the ID of Virtual meeting to which Facilitator is connected
     * @param virtualMeetingConsoleId Virtual Meeting ID
     */
    public void setVirtualMeetingConsoleId(String virtualMeetingConsoleId) {
        this.virtualMeetingConsoleId = virtualMeetingConsoleId;
    }
}
