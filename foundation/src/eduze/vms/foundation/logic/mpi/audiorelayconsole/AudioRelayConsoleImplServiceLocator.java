/**
 * AudioRelayConsoleImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.foundation.logic.mpi.audiorelayconsole;

public class AudioRelayConsoleImplServiceLocator extends org.apache.axis.client.Service implements eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplService {

    public AudioRelayConsoleImplServiceLocator() {
    }


    public AudioRelayConsoleImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AudioRelayConsoleImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AudioRelayConsoleImplPort
    private java.lang.String AudioRelayConsoleImplPort_address = "http://0.0.0.0:9000/audio-relay/-1647728555";

    public java.lang.String getAudioRelayConsoleImplPortAddress() {
        return AudioRelayConsoleImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AudioRelayConsoleImplPortWSDDServiceName = "AudioRelayConsoleImplPort";

    public java.lang.String getAudioRelayConsoleImplPortWSDDServiceName() {
        return AudioRelayConsoleImplPortWSDDServiceName;
    }

    public void setAudioRelayConsoleImplPortWSDDServiceName(java.lang.String name) {
        AudioRelayConsoleImplPortWSDDServiceName = name;
    }

    public eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole getAudioRelayConsoleImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AudioRelayConsoleImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAudioRelayConsoleImplPort(endpoint);
    }

    public eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole getAudioRelayConsoleImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplPortBindingStub _stub = new eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplPortBindingStub(portAddress, this);
            _stub.setPortName(getAudioRelayConsoleImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAudioRelayConsoleImplPortEndpointAddress(java.lang.String address) {
        AudioRelayConsoleImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole.class.isAssignableFrom(serviceEndpointInterface)) {
                eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplPortBindingStub _stub = new eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplPortBindingStub(new java.net.URL(AudioRelayConsoleImplPort_address), this);
                _stub.setPortName(getAudioRelayConsoleImplPortWSDDServiceName());
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
        if ("AudioRelayConsoleImplPort".equals(inputPortName)) {
            return getAudioRelayConsoleImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://webservices.logic.foundation.vms.eduze/", "AudioRelayConsoleImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://webservices.logic.foundation.vms.eduze/", "AudioRelayConsoleImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AudioRelayConsoleImplPort".equals(portName)) {
            setAudioRelayConsoleImplPortEndpointAddress(address);
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
