package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.ServerConnectionException;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeeting;

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

    public static VirtualMeetingSnapshot fromVirtualMeeting(VirtualMeeting vms) throws ServerConnectionException {
        try{
            eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot vm = vms.getSnapshot();
            VirtualMeetingSnapshot result = new VirtualMeetingSnapshot();
            result.setActiveScreenFacilitatorId(vm.getActiveScreenFacilitatorId());
            result.setActiveScreenPresenterId(vm.getActiveScreenPresenterId());
            result.setActiveSpeechFacilitatorId(vm.getActiveSpeechFacilitatorId());
            result.setActiveSpeechPresenterId(vm.getActiveSpeechPresenterId());

            eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus status = vm.getStatus();

            if(status == eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus.MeetingOnline)
                result.status = SessionStatus.MeetingOnline;
            if(status == eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus.NotReady)
                result.status = SessionStatus.NotReady;
            if(status == eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus.WaitingForFirstFacilitator)
                result.status = SessionStatus.WaitingForFirstFacilitator;
            if(status == eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus.WaitingForSecondFacilitator)
                result.status = SessionStatus.WaitingForSecondFacilitator;
            if(status == eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus.Adjourned)
                result.status = SessionStatus.Adjourned;
            return result;
        }
        catch (RemoteException ex)
        {
            throw new ServerConnectionException(ex);
        }

    }
}
