/**
 * PresenterConsole.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.presenter.logic.mpi.presenterconsole;

public interface PresenterConsole extends java.rmi.Remote {
    public void disconnect() throws java.rmi.RemoteException;
    public java.lang.String getConsoleId() throws java.rmi.RemoteException;
    public void acknowledgeConnection() throws java.rmi.RemoteException;
}
