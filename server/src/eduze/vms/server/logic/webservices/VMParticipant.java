package eduze.vms.server.logic.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Madhawa on 14/04/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="VMParticipant")
public class VMParticipant {
    @XmlElement(name = "name")
    protected String name = null;
    @XmlElement(name = "presenterId")
    protected String presenterId = null;

    @XmlElement(name = "facilitatorId")
    protected String facilitatorId = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresenterId() {
        return presenterId;
    }

    public void setPresenterId(String presenterId) {
        this.presenterId = presenterId;
    }

    public String getFacilitatorId() {
        return facilitatorId;
    }

    public void setFacilitatorId(String facilitatorId) {
        this.facilitatorId = facilitatorId;
    }
}