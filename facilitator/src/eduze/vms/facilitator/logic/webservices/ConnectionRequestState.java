package eduze.vms.facilitator.logic.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Madhawa on 13/04/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ConnectionRequestState")
public class ConnectionRequestState {
    @XmlElement(name="successful")
    protected boolean isSuccessful = false;
    @XmlElement(name="pending")
    protected boolean isPending = false;
    @XmlElement(name="PresenterConsoleId")
    protected String presenterConsoleId = null;
    @XmlElement(name="ConnectionRequestId")
    protected String connectionRequestId = null;
    @XmlElement(name="PresenterName")
    protected String presenterName = null;

    public ConnectionRequestState()
    {

    }

    public String getPresenterName() {
        return presenterName;
    }

    public void setPresenterName(String presenterName) {
        this.presenterName = presenterName;
    }

    public String getConnectionRequestId() {
        return connectionRequestId;
    }

    public void setConnectionRequestId(String connectionRequestId) {
        this.connectionRequestId = connectionRequestId;
    }


    public boolean isSuccessful() {
        return isSuccessful;
    }

    void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public boolean isPending() {
        return isPending;
    }

    void setPending(boolean pending) {
        isPending = pending;
    }

    public String getPresenterConsoleId() {
        return presenterConsoleId;
    }

    void setPresenterConsoleId(String presenterConsoleId) {
        this.presenterConsoleId = presenterConsoleId;
    }


}
