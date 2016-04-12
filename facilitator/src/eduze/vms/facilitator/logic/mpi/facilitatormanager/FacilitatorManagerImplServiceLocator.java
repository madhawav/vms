/**
 * FacilitatorManagerImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.facilitatormanager;

public class FacilitatorManagerImplServiceLocator extends org.apache.axis.client.Service implements eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplService {

    public FacilitatorManagerImplServiceLocator() {
    }


    public FacilitatorManagerImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FacilitatorManagerImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for FacilitatorManagerImplPort
    private java.lang.String FacilitatorManagerImplPort_address = "http://0.0.0.0:8000/facilitator-manager";

    public java.lang.String getFacilitatorManagerImplPortAddress() {
        return FacilitatorManagerImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FacilitatorManagerImplPortWSDDServiceName = "FacilitatorManagerImplPort";

    public java.lang.String getFacilitatorManagerImplPortWSDDServiceName() {
        return FacilitatorManagerImplPortWSDDServiceName;
    }

    public void setFacilitatorManagerImplPortWSDDServiceName(java.lang.String name) {
        FacilitatorManagerImplPortWSDDServiceName = name;
    }

    public eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManager getFacilitatorManagerImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FacilitatorManagerImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFacilitatorManagerImplPort(endpoint);
    }

    public eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManager getFacilitatorManagerImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplPortBindingStub _stub = new eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplPortBindingStub(portAddress, this);
            _stub.setPortName(getFacilitatorManagerImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFacilitatorManagerImplPortEndpointAddress(java.lang.String address) {
        FacilitatorManagerImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManager.class.isAssignableFrom(serviceEndpointInterface)) {
                eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplPortBindingStub _stub = new eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplPortBindingStub(new java.net.URL(FacilitatorManagerImplPort_address), this);
                _stub.setPortName(getFacilitatorManagerImplPortWSDDServiceName());
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
        if ("FacilitatorManagerImplPort".equals(inputPortName)) {
            return getFacilitatorManagerImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "FacilitatorManagerImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservices.logic.server.vms.eduze/", "FacilitatorManagerImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FacilitatorManagerImplPort".equals(portName)) {
            setFacilitatorManagerImplPortEndpointAddress(address);
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
