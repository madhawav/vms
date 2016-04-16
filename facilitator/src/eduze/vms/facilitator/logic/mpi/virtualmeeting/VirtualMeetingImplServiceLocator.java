/**
 * VirtualMeetingImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.virtualmeeting;

public class VirtualMeetingImplServiceLocator extends org.apache.axis.client.Service implements eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingImplService {

    public VirtualMeetingImplServiceLocator() {
    }


    public VirtualMeetingImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public VirtualMeetingImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for VirtualMeetingImplPort
    private java.lang.String VirtualMeetingImplPort_address = "http://0.0.0.0:8000/vm/vm_129573348";

    public java.lang.String getVirtualMeetingImplPortAddress() {
        return VirtualMeetingImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String VirtualMeetingImplPortWSDDServiceName = "VirtualMeetingImplPort";

    public java.lang.String getVirtualMeetingImplPortWSDDServiceName() {
        return VirtualMeetingImplPortWSDDServiceName;
    }

    public void setVirtualMeetingImplPortWSDDServiceName(java.lang.String name) {
        VirtualMeetingImplPortWSDDServiceName = name;
    }

    public eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeeting getVirtualMeetingImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(VirtualMeetingImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getVirtualMeetingImplPort(endpoint);
    }

    public eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeeting getVirtualMeetingImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingImplPortBindingStub _stub = new eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingImplPortBindingStub(portAddress, this);
            _stub.setPortName(getVirtualMeetingImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setVirtualMeetingImplPortEndpointAddress(java.lang.String address) {
        VirtualMeetingImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeeting.class.isAssignableFrom(serviceEndpointInterface)) {
                eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingImplPortBindingStub _stub = new eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingImplPortBindingStub(new java.net.URL(VirtualMeetingImplPort_address), this);
                _stub.setPortName(getVirtualMeetingImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("VirtualMeetingImplPort".equals(inputPortName)) {
            return getVirtualMeetingImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "VirtualMeetingImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "VirtualMeetingImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("VirtualMeetingImplPort".equals(portName)) {
            setVirtualMeetingImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
