/**
 * Server.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.server;

public interface Server extends java.rmi.Remote {
    /**
     * Retrieve port of Server
     * @return Server Port
     */
    public int getPort() throws java.rmi.RemoteException;

    /**
     * Retrieve name of Server
     * @return Server Name
     */
    public java.lang.String getServerName() throws java.rmi.RemoteException;
}
