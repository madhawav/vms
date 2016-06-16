/**
 * ScreenShareConsole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.foundation.logic.mpi.screenshareconsole;

public interface ScreenShareConsole extends java.rmi.Remote {

    /**
     * Retrieve whether channel is enabled
     * @return True if channel is enabled. Otherwise return False
     */
    public boolean isEnabled() throws java.rmi.RemoteException;
    /**
     * Retrieve interval between screen capture transmissions
     * @return
     */
    public int getUpdateInterval() throws java.rmi.RemoteException;

    /**
     * Set the interval between screen capture transmissions
     * @param interval
     */
    public void setUpdateInterval(int interval) throws java.rmi.RemoteException;

    /**
     * Retrieve Screen Share Console Id
     * @return
     */
    public java.lang.String getConsoleId() throws java.rmi.RemoteException;
}
