/**
 * ConnectionRequestState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.presenter.logic.mpi.facilitator;

public class ConnectionRequestState  implements java.io.Serializable {
    private boolean successful;

    private boolean pending;

    private java.lang.String presenterConsoleId;

    private java.lang.String connectionRequestId;

    private java.lang.String presenterName;

    public ConnectionRequestState() {
    }

    public ConnectionRequestState(
           boolean successful,
           boolean pending,
           java.lang.String presenterConsoleId,
           java.lang.String connectionRequestId,
           java.lang.String presenterName) {
           this.successful = successful;
           this.pending = pending;
           this.presenterConsoleId = presenterConsoleId;
           this.connectionRequestId = connectionRequestId;
           this.presenterName = presenterName;
    }


    /**
     * Gets the successful value for this ConnectionRequestState.
     * 
     * @return successful
     */
    public boolean isSuccessful() {
        return successful;
    }


    /**
     * Sets the successful value for this ConnectionRequestState.
     * 
     * @param successful
     */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }


    /**
     * Gets the pending value for this ConnectionRequestState.
     * 
     * @return pending
     */
    public boolean isPending() {
        return pending;
    }


    /**
     * Sets the pending value for this ConnectionRequestState.
     * 
     * @param pending
     */
    public void setPending(boolean pending) {
        this.pending = pending;
    }


    /**
     * Gets the presenterConsoleId value for this ConnectionRequestState.
     * 
     * @return presenterConsoleId
     */
    public java.lang.String getPresenterConsoleId() {
        return presenterConsoleId;
    }


    /**
     * Sets the presenterConsoleId value for this ConnectionRequestState.
     * 
     * @param presenterConsoleId
     */
    public void setPresenterConsoleId(java.lang.String presenterConsoleId) {
        this.presenterConsoleId = presenterConsoleId;
    }


    /**
     * Gets the connectionRequestId value for this ConnectionRequestState.
     * 
     * @return connectionRequestId
     */
    public java.lang.String getConnectionRequestId() {
        return connectionRequestId;
    }


    /**
     * Sets the connectionRequestId value for this ConnectionRequestState.
     * 
     * @param connectionRequestId
     */
    public void setConnectionRequestId(java.lang.String connectionRequestId) {
        this.connectionRequestId = connectionRequestId;
    }


    /**
     * Gets the presenterName value for this ConnectionRequestState.
     * 
     * @return presenterName
     */
    public java.lang.String getPresenterName() {
        return presenterName;
    }


    /**
     * Sets the presenterName value for this ConnectionRequestState.
     * 
     * @param presenterName
     */
    public void setPresenterName(java.lang.String presenterName) {
        this.presenterName = presenterName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConnectionRequestState)) return false;
        ConnectionRequestState other = (ConnectionRequestState) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.successful == other.isSuccessful() &&
            this.pending == other.isPending() &&
            ((this.presenterConsoleId==null && other.getPresenterConsoleId()==null) || 
             (this.presenterConsoleId!=null &&
              this.presenterConsoleId.equals(other.getPresenterConsoleId()))) &&
            ((this.connectionRequestId==null && other.getConnectionRequestId()==null) || 
             (this.connectionRequestId!=null &&
              this.connectionRequestId.equals(other.getConnectionRequestId()))) &&
            ((this.presenterName==null && other.getPresenterName()==null) || 
             (this.presenterName!=null &&
              this.presenterName.equals(other.getPresenterName())));
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
        _hashCode += (isPending() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getPresenterConsoleId() != null) {
            _hashCode += getPresenterConsoleId().hashCode();
        }
        if (getConnectionRequestId() != null) {
            _hashCode += getConnectionRequestId().hashCode();
        }
        if (getPresenterName() != null) {
            _hashCode += getPresenterName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConnectionRequestState.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.logic.facilitator.vms.eduze/", "ConnectionRequestState"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("successful");
        elemField.setXmlName(new javax.xml.namespace.QName("", "successful"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pending");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pending"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenterConsoleId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PresenterConsoleId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("connectionRequestId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ConnectionRequestId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presenterName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PresenterName"));
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
