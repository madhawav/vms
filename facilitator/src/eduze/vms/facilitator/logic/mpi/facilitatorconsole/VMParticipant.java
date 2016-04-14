/**
 * VMParticipant.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.facilitatorconsole;

public class VMParticipant  implements java.io.Serializable {
    private java.lang.String name;

    private java.lang.String presenterId;

    private java.lang.String facilitatorId;

    public VMParticipant() {
    }

    public VMParticipant(
           java.lang.String name,
           java.lang.String presenterId,
           java.lang.String facilitatorId) {
           this.name = name;
           this.presenterId = presenterId;
           this.facilitatorId = facilitatorId;
    }


    /**
     * Gets the name value for this VMParticipant.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this VMParticipant.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the presenterId value for this VMParticipant.
     * 
     * @return presenterId
     */
    public java.lang.String getPresenterId() {
        return presenterId;
    }


    /**
     * Sets the presenterId value for this VMParticipant.
     * 
     * @param presenterId
     */
    public void setPresenterId(java.lang.String presenterId) {
        this.presenterId = presenterId;
    }


    /**
     * Gets the facilitatorId value for this VMParticipant.
     * 
     * @return facilitatorId
     */
    public java.lang.String getFacilitatorId() {
        return facilitatorId;
    }


    /**
     * Sets the facilitatorId value for this VMParticipant.
     * 
     * @param facilitatorId
     */
    public void setFacilitatorId(java.lang.String facilitatorId) {
        this.facilitatorId = facilitatorId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VMParticipant)) return false;
        VMParticipant other = (VMParticipant) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.presenterId==null && other.getPresenterId()==null) || 
             (this.presenterId!=null &&
              this.presenterId.equals(other.getPresenterId()))) &&
            ((this.facilitatorId==null && other.getFacilitatorId()==null) || 
             (this.facilitatorId!=null &&
              this.facilitatorId.equals(other.getFacilitatorId())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getPresenterId() != null) {
            _hashCode += getPresenterId().hashCode();
        }
        if (getFacilitatorId() != null) {
            _hashCode += getFacilitatorId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VMParticipant.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "VMParticipant"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenterId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "presenterId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("facilitatorId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "facilitatorId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
