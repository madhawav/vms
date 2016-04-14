/**
 * FacilitatorImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.presenter.logic.mpi.facilitator;

public class FacilitatorImplServiceLocator extends org.apache.axis.client.Service implements eduze.vms.presenter.logic.mpi.facilitator.FacilitatorImplService {

    public FacilitatorImplServiceLocator() {
    }


    public FacilitatorImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FacilitatorImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for FacilitatorImplPort
    private java.lang.String FacilitatorImplPort_address = "http://0.0.0.0:7000/facilitator";

    public java.lang.String getFacilitatorImplPortAddress() {
        return FacilitatorImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FacilitatorImplPortWSDDServiceName = "FacilitatorImplPort";

    public java.lang.String getFacilitatorImplPortWSDDServiceName() {
        return FacilitatorImplPortWSDDServiceName;
    }

    public void setFacilitatorImplPortWSDDServiceName(java.lang.String name) {
        FacilitatorImplPortWSDDServiceName = name;
    }

    public eduze.vms.presenter.logic.mpi.facilitator.Facilitator getFacilitatorImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FacilitatorImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFacilitatorImplPort(endpoint);
    }

    public eduze.vms.presenter.logic.mpi.facilitator.Facilitator getFacilitatorImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            eduze.vms.presenter.logic.mpi.facilitator.FacilitatorImplPortBindingStub _stub = new eduze.vms.presenter.logic.mpi.facilitator.FacilitatorImplPortBindingStub(portAddress, this);
            _stub.setPortName(getFacilitatorImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFacilitatorImplPortEndpointAddress(java.lang.String address) {
        FacilitatorImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (eduze.vms.presenter.logic.mpi.facilitator.Facilitator.class.isAssignableFrom(serviceEndpointInterface)) {
                eduze.vms.presenter.logic.mpi.facilitator.FacilitatorImplPortBindingStub _stub = new eduze.vms.presenter.logic.mpi.facilitator.FacilitatorImplPortBindingStub(new java.net.URL(FacilitatorImplPort_address), this);
                _stub.setPortName(getFacilitatorImplPortWSDDServiceName());
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
        if ("FacilitatorImplPort".equals(inputPortName)) {
            return getFacilitatorImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservices.logic.facilitator.vms.eduze/", "FacilitatorImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservices.logic.facilitator.vms.eduze/", "FacilitatorImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FacilitatorImplPort".equals(portName)) {
            setFacilitatorImplPortEndpointAddress(address);
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
