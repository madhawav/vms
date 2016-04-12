package eduze.vms.server.logic.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Madhawa on 12/04/2016.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ConnectionResult")
public class ConnectionResult {
    @XmlElement(name="successful")
    protected boolean isSuccessful = false;
    @XmlElement(name="facilitatorConsoleId")
    protected String facilitatorConsoleId= null;
    @XmlElement(name="virtualMeetingConsoleId")
    protected String virtualMeetingConsoleId = null;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getFacilitatorConsoleId() {
        return facilitatorConsoleId;
    }

    public String getVirtualMeetingConsoleId() {
        return virtualMeetingConsoleId;
    }

    public void setFacilitatorConsoleId(String facilitatorConsoleId) {
        this.facilitatorConsoleId = facilitatorConsoleId;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public void setVirtualMeetingConsoleId(String virtualMeetingConsoleId) {
        this.virtualMeetingConsoleId = virtualMeetingConsoleId;
    }
}
