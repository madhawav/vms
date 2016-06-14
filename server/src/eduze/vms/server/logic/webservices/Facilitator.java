package eduze.vms.server.logic.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Contains information on a Known (paired) Facilitator
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Facilitator")
public class Facilitator implements Serializable{
    //Name of Facilitator
    @XmlElement(name="name")
    protected String name = "";

    //Pairkey of Facilitator
    @XmlElement(name="pairKey")
    private String pairKey = "";

    /**
     * Retrieve name of Facilitator
     * @return Facilitator Name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of Facilitator
     * @param name Facilitator Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the Pairkey of Facilitator
     * @return PairKey
     */
    public String getPairKey() {
        return pairKey;
    }

    /**
     * Set the PairKey of Facilitator
     * @param pairKey PairKey of Facilitator
     */
    public void setPairKey(String pairKey) {
        this.pairKey = pairKey;
    }
}
