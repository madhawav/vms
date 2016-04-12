/**
 * ServerImplService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.server;

public interface ServerImplService extends javax.xml.rpc.Service {
    public java.lang.String getServerImplPortAddress();

    public eduze.vms.facilitator.logic.mpi.server.Server getServerImplPort() throws javax.xml.rpc.ServiceException;

    public eduze.vms.facilitator.logic.mpi.server.Server getServerImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
