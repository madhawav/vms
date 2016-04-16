/**
 * SharedTask.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.virtualmeeting;

public class SharedTask  implements java.io.Serializable {
    private java.lang.String taskId;

    private java.lang.String title;

    private java.lang.String description;

    private java.lang.String assignedFacilitatorId;

    private java.lang.String assignedPresenterId;

    private java.lang.String assignedPresenterName;

    public SharedTask() {
    }

    public SharedTask(
           java.lang.String taskId,
           java.lang.String title,
           java.lang.String description,
           java.lang.String assignedFacilitatorId,
           java.lang.String assignedPresenterId,
           java.lang.String assignedPresenterName) {
           this.taskId = taskId;
           this.title = title;
           this.description = description;
           this.assignedFacilitatorId = assignedFacilitatorId;
           this.assignedPresenterId = assignedPresenterId;
           this.assignedPresenterName = assignedPresenterName;
    }


    /**
     * Gets the taskId value for this SharedTask.
     * 
     * @return taskId
     */
    public java.lang.String getTaskId() {
        return taskId;
    }


    /**
     * Sets the taskId value for this SharedTask.
     * 
     * @param taskId
     */
    public void setTaskId(java.lang.String taskId) {
        this.taskId = taskId;
    }


    /**
     * Gets the title value for this SharedTask.
     * 
     * @return title
     */
    public java.lang.String getTitle() {
        return title;
    }


    /**
     * Sets the title value for this SharedTask.
     * 
     * @param title
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }


    /**
     * Gets the description value for this SharedTask.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this SharedTask.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the assignedFacilitatorId value for this SharedTask.
     * 
     * @return assignedFacilitatorId
     */
    public java.lang.String getAssignedFacilitatorId() {
        return assignedFacilitatorId;
    }


    /**
     * Sets the assignedFacilitatorId value for this SharedTask.
     * 
     * @param assignedFacilitatorId
     */
    public void setAssignedFacilitatorId(java.lang.String assignedFacilitatorId) {
        this.assignedFacilitatorId = assignedFacilitatorId;
    }


    /**
     * Gets the assignedPresenterId value for this SharedTask.
     * 
     * @return assignedPresenterId
     */
    public java.lang.String getAssignedPresenterId() {
        return assignedPresenterId;
    }


    /**
     * Sets the assignedPresenterId value for this SharedTask.
     * 
     * @param assignedPresenterId
     */
    public void setAssignedPresenterId(java.lang.String assignedPresenterId) {
        this.assignedPresenterId = assignedPresenterId;
    }


    /**
     * Gets the assignedPresenterName value for this SharedTask.
     * 
     * @return assignedPresenterName
     */
    public java.lang.String getAssignedPresenterName() {
        return assignedPresenterName;
    }


    /**
     * Sets the assignedPresenterName value for this SharedTask.
     * 
     * @param assignedPresenterName
     */
    public void setAssignedPresenterName(java.lang.String assignedPresenterName) {
        this.assignedPresenterName = assignedPresenterName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SharedTask)) return false;
        SharedTask other = (SharedTask) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.taskId==null && other.getTaskId()==null) || 
             (this.taskId!=null &&
              this.taskId.equals(other.getTaskId()))) &&
            ((this.title==null && other.getTitle()==null) || 
             (this.title!=null &&
              this.title.equals(other.getTitle()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.assignedFacilitatorId==null && other.getAssignedFacilitatorId()==null) || 
             (this.assignedFacilitatorId!=null &&
              this.assignedFacilitatorId.equals(other.getAssignedFacilitatorId()))) &&
            ((this.assignedPresenterId==null && other.getAssignedPresenterId()==null) || 
             (this.assignedPresenterId!=null &&
              this.assignedPresenterId.equals(other.getAssignedPresenterId()))) &&
            ((this.assignedPresenterName==null && other.getAssignedPresenterName()==null) || 
             (this.assignedPresenterName!=null &&
              this.assignedPresenterName.equals(other.getAssignedPresenterName())));
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
        if (getTaskId() != null) {
            _hashCode += getTaskId().hashCode();
        }
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getAssignedFacilitatorId() != null) {
            _hashCode += getAssignedFacilitatorId().hashCode();
        }
        if (getAssignedPresenterId() != null) {
            _hashCode += getAssignedPresenterId().hashCode();
        }
        if (getAssignedPresenterName() != null) {
            _hashCode += getAssignedPresenterName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SharedTask.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "SharedTask"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taskId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TaskId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assignedFacilitatorId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AssignedFacilitatorId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assignedPresenterId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AssignedPresenterId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assignedPresenterName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AssignedPresenterName"));
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
