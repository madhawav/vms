/**
 * ConnectionResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.vmsessionmanager;

public class ConnectionResult  implements java.io.Serializable {
    private boolean successful;

    private java.lang.String facilitatorConsoleId;

    private java.lang.String virtualMeetingConsoleId;

    public ConnectionResult() {
    }

    public ConnectionResult(
           boolean successful,
           java.lang.String facilitatorConsoleId,
           java.lang.String virtualMeetingConsoleId) {
           this.successful = successful;
           this.facilitatorConsoleId = facilitatorConsoleId;
           this.virtualMeetingConsoleId = virtualMeetingConsoleId;
    }


    /**
     * Gets the successful value for this ConnectionResult.
     * 
     * @return successful
     */
    public boolean isSuccessful() {
        return successful;
    }


    /**
     * Sets the successful value for this ConnectionResult.
     * 
     * @param successful
     */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }


    /**
     * Gets the facilitatorConsoleId value for this ConnectionResult.
     * 
     * @return facilitatorConsoleId
     */
    public java.lang.String getFacilitatorConsoleId() {
        return facilitatorConsoleId;
    }


    /**
     * Sets the facilitatorConsoleId value for this ConnectionResult.
     * 
     * @param facilitatorConsoleId
     */
    public void setFacilitatorConsoleId(java.lang.String facilitatorConsoleId) {
        this.facilitatorConsoleId = facilitatorConsoleId;
    }


    /**
     * Gets the virtualMeetingConsoleId value for this ConnectionResult.
     * 
     * @return virtualMeetingConsoleId
     */
    public java.lang.String getVirtualMeetingConsoleId() {
        return virtualMeetingConsoleId;
    }


    /**
     * Sets the virtualMeetingConsoleId value for this ConnectionResult.
     * 
     * @param virtualMeetingConsoleId
     */
    public void setVirtualMeetingConsoleId(java.lang.String virtualMeetingConsoleId) {
        this.virtualMeetingConsoleId = virtualMeetingConsoleId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConnectionResult)) return false;
        ConnectionResult other = (ConnectionResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.successful == other.isSuccessful() &&
            ((this.facilitatorConsoleId==null && other.getFacilitatorConsoleId()==null) || 
             (this.facilitatorConsoleId!=null &&
              this.facilitatorConsoleId.equals(other.getFacilitatorConsoleId()))) &&
            ((this.virtualMeetingConsoleId==null && other.getVirtualMeetingConsoleId()==null) || 
             (this.virtualMeetingConsoleId!=null &&
              this.virtualMeetingConsoleId.equals(other.getVirtualMeetingConsoleId())));
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
        _hashCode += (isSuccessful() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getFacilitatorConsoleId() != null) {
            _hashCode += getFacilitatorConsoleId().hashCode();
        }
        if (getVirtualMeetingConsoleId() != null) {
            _hashCode += getVirtualMeetingConsoleId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConnectionResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "ConnectionResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("successful");
        elemField.setXmlName(new javax.xml.namespace.QName("", "successful"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("facilitatorConsoleId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "facilitatorConsoleId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("virtualMeetingConsoleId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "virtualMeetingConsoleId"));
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
