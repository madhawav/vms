/**
 * VirtualMeetingSnapshot.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.virtualmeeting;

public class VirtualMeetingSnapshot  implements java.io.Serializable {
    private java.lang.String VMId;

    private java.lang.String activeScreenFacilitatorId;

    private java.lang.String activeScreenPresenterId;

    private java.lang.String activeSpeechFacilitatorId;

    private java.lang.String activeSpeechPresenterId;

    private eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus status;

    public VirtualMeetingSnapshot() {
    }

    public VirtualMeetingSnapshot(
           java.lang.String VMId,
           java.lang.String activeScreenFacilitatorId,
           java.lang.String activeScreenPresenterId,
           java.lang.String activeSpeechFacilitatorId,
           java.lang.String activeSpeechPresenterId,
           eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus status) {
           this.VMId = VMId;
           this.activeScreenFacilitatorId = activeScreenFacilitatorId;
           this.activeScreenPresenterId = activeScreenPresenterId;
           this.activeSpeechFacilitatorId = activeSpeechFacilitatorId;
           this.activeSpeechPresenterId = activeSpeechPresenterId;
           this.status = status;
    }


    /**
     * Gets the VMId value for this VirtualMeetingSnapshot.
     * 
     * @return VMId
     */
    public java.lang.String getVMId() {
        return VMId;
    }


    /**
     * Sets the VMId value for this VirtualMeetingSnapshot.
     * 
     * @param VMId
     */
    public void setVMId(java.lang.String VMId) {
        this.VMId = VMId;
    }


    /**
     * Gets the activeScreenFacilitatorId value for this VirtualMeetingSnapshot.
     * 
     * @return activeScreenFacilitatorId
     */
    public java.lang.String getActiveScreenFacilitatorId() {
        return activeScreenFacilitatorId;
    }


    /**
     * Sets the activeScreenFacilitatorId value for this VirtualMeetingSnapshot.
     * 
     * @param activeScreenFacilitatorId
     */
    public void setActiveScreenFacilitatorId(java.lang.String activeScreenFacilitatorId) {
        this.activeScreenFacilitatorId = activeScreenFacilitatorId;
    }


    /**
     * Gets the activeScreenPresenterId value for this VirtualMeetingSnapshot.
     * 
     * @return activeScreenPresenterId
     */
    public java.lang.String getActiveScreenPresenterId() {
        return activeScreenPresenterId;
    }


    /**
     * Sets the activeScreenPresenterId value for this VirtualMeetingSnapshot.
     * 
     * @param activeScreenPresenterId
     */
    public void setActiveScreenPresenterId(java.lang.String activeScreenPresenterId) {
        this.activeScreenPresenterId = activeScreenPresenterId;
    }


    /**
     * Gets the activeSpeechFacilitatorId value for this VirtualMeetingSnapshot.
     * 
     * @return activeSpeechFacilitatorId
     */
    public java.lang.String getActiveSpeechFacilitatorId() {
        return activeSpeechFacilitatorId;
    }


    /**
     * Sets the activeSpeechFacilitatorId value for this VirtualMeetingSnapshot.
     * 
     * @param activeSpeechFacilitatorId
     */
    public void setActiveSpeechFacilitatorId(java.lang.String activeSpeechFacilitatorId) {
        this.activeSpeechFacilitatorId = activeSpeechFacilitatorId;
    }


    /**
     * Gets the activeSpeechPresenterId value for this VirtualMeetingSnapshot.
     * 
     * @return activeSpeechPresenterId
     */
    public java.lang.String getActiveSpeechPresenterId() {
        return activeSpeechPresenterId;
    }


    /**
     * Sets the activeSpeechPresenterId value for this VirtualMeetingSnapshot.
     * 
     * @param activeSpeechPresenterId
     */
    public void setActiveSpeechPresenterId(java.lang.String activeSpeechPresenterId) {
        this.activeSpeechPresenterId = activeSpeechPresenterId;
    }


    /**
     * Gets the status value for this VirtualMeetingSnapshot.
     * 
     * @return status
     */
    public eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this VirtualMeetingSnapshot.
     * 
     * @param status
     */
    public void setStatus(eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VirtualMeetingSnapshot)) return false;
        VirtualMeetingSnapshot other = (VirtualMeetingSnapshot) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VMId==null && other.getVMId()==null) || 
             (this.VMId!=null &&
              this.VMId.equals(other.getVMId()))) &&
            ((this.activeScreenFacilitatorId==null && other.getActiveScreenFacilitatorId()==null) || 
             (this.activeScreenFacilitatorId!=null &&
              this.activeScreenFacilitatorId.equals(other.getActiveScreenFacilitatorId()))) &&
            ((this.activeScreenPresenterId==null && other.getActiveScreenPresenterId()==null) || 
             (this.activeScreenPresenterId!=null &&
              this.activeScreenPresenterId.equals(other.getActiveScreenPresenterId()))) &&
            ((this.activeSpeechFacilitatorId==null && other.getActiveSpeechFacilitatorId()==null) || 
             (this.activeSpeechFacilitatorId!=null &&
              this.activeSpeechFacilitatorId.equals(other.getActiveSpeechFacilitatorId()))) &&
            ((this.activeSpeechPresenterId==null && other.getActiveSpeechPresenterId()==null) || 
             (this.activeSpeechPresenterId!=null &&
              this.activeSpeechPresenterId.equals(other.getActiveSpeechPresenterId()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        if (getVMId() != null) {
            _hashCode += getVMId().hashCode();
        }
        if (getActiveScreenFacilitatorId() != null) {
            _hashCode += getActiveScreenFacilitatorId().hashCode();
        }
        if (getActiveScreenPresenterId() != null) {
            _hashCode += getActiveScreenPresenterId().hashCode();
        }
        if (getActiveSpeechFacilitatorId() != null) {
            _hashCode += getActiveSpeechFacilitatorId().hashCode();
        }
        if (getActiveSpeechPresenterId() != null) {
            _hashCode += getActiveSpeechPresenterId().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VirtualMeetingSnapshot.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "VirtualMeetingSnapshot"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VMId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "VMId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activeScreenFacilitatorId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ActiveScreenFacilitatorId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activeScreenPresenterId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ActiveScreenPresenterId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activeSpeechFacilitatorId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ActiveSpeechFacilitatorId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activeSpeechPresenterId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ActiveSpeechPresenterId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "sessionStatus"));
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
