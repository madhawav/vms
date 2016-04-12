package eduze.vms.server.logic.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Madhawa on 12/04/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Facilitator")
public class Facilitator {
    @XmlElement(name="name")
    protected String name = "";

    @XmlElement(name="pairKey")
    private String pairKey = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPairKey() {
        return pairKey;
    }

    public void setPairKey(String pairKey) {
        this.pairKey = pairKey;
    }
}
