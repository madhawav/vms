package eduze.vms.server.logic.webservices;

/**
 * Created by Madhawa on 13/04/2016.
 */


import javax.jws.WebMethod;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.rmi.RemoteException;

/**
 * Created by Madhawa on 13/04/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="VirtualMeetingSnapshot")
public class VirtualMeetingSnapshot {
    @XmlElement(name="VMId")
    protected String vmId = null;
    @XmlElement(name="ActiveScreenFacilitatorId")
    protected String activeScreenFacilitatorId = null;

    @XmlElement(name="ActiveScreenPresenterId")
    protected String activeScreenPresenterId = null;

    @XmlElement(name="ActiveSpeechFacilitatorId")
    protected String activeSpeechFacilitatorId = null;

    @XmlElement(name="ActiveSpeechPresenterId")
    protected String activeSpeechPresenterId = null;

    @XmlElement(name="Status")
    protected SessionStatus status = null;

    public SessionStatus getStatus() {
        return status;
    }

    public String getActiveScreenFacilitatorId() {
        return activeScreenFacilitatorId;
    }

    public String getActiveScreenPresenterId() {
        return activeScreenPresenterId;
    }

    public String getActiveSpeechFacilitatorId() {
        return activeSpeechFacilitatorId;
    }

    public String getActiveSpeechPresenterId() {
        return activeSpeechPresenterId;
    }

    public String getVmId() {
        return vmId;
    }

    public void setActiveScreenFacilitatorId(String activeScreenFacilitatorId) {
        this.activeScreenFacilitatorId = activeScreenFacilitatorId;
    }

    public void setActiveScreenPresenterId(String activeScreenPresenterId) {
        this.activeScreenPresenterId = activeScreenPresenterId;
    }

    public void setVmId(String vmId) {
        this.vmId = vmId;
    }

    public void setActiveSpeechFacilitatorId(String activeSpeechFacilitatorId) {
        this.activeSpeechFacilitatorId = activeSpeechFacilitatorId;
    }

    public void setActiveSpeechPresenterId(String activeSpeechPresenterId) {
        this.activeSpeechPresenterId = activeSpeechPresenterId;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public static VirtualMeetingSnapshot fromVirtualMeeting(VirtualMeeting vm) {
            VirtualMeetingSnapshot result = new VirtualMeetingSnapshot();
            result.setActiveScreenFacilitatorId(vm.getActiveScreenFacilitatorId());
            result.setActiveScreenPresenterId(vm.getActiveScreenPresenterId());
            result.setActiveSpeechFacilitatorId(vm.getActiveSpeechFacilitatorId());
            result.setActiveSpeechPresenterId(vm.getActiveSpeechFacilitatorId());
            result.setStatus(vm.getStatus());
            result.setVmId(vm.getVMId());
            return result;


    }
}

