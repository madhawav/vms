/**
 * AudioRelayConsole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.foundation.logic.mpi.audiorelayconsole;

public interface AudioRelayConsole extends java.rmi.Remote {
    /**
     * Retrieve whether the Audio Relay Channel is active
     * @return True if channel is active. Otherwise return false
     */
    public boolean isEnabled() throws java.rmi.RemoteException;

    /**
     * Retrieve the Console Id
     * @return Console Id
     */
    public java.lang.String getConsoleId() throws java.rmi.RemoteException;
}
