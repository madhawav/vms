package eduze.vms.facilitator.logic.webservices;

import javax.jws.WebMethod;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Madhawa on 13/04/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ConnectionResult")
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

}
