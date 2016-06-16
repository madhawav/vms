/**
 * Facilitator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.presenter.logic.mpi.facilitator;

public interface Facilitator extends java.rmi.Remote {
    /**
     * request connection from facilitator. facilitator has to manually accept it.
     * @param presenterName name of presenter
     * @param passKey facilitator passkey
     * @return connectionRequestCode
     */
    public java.lang.String requestConnection(java.lang.String presenterName, java.lang.String passKey) throws java.rmi.RemoteException, eduze.vms.presenter.logic.mpi.facilitator.InvalidFacilitatorPasskeyException;

    /**
     * Checks status of connection request
     * @param connectionRequestId connectionRequestId returned from requestConnectionMethod
     * @return
     */
    public eduze.vms.presenter.logic.mpi.facilitator.ConnectionRequestState checkConnectionRequestState(java.lang.String connectionRequestId) throws java.rmi.RemoteException;

    /**
     * Retrieve name of facilitator
     * @return facilitator name
     */
    public java.lang.String getFacilitatorName() throws java.rmi.RemoteException;
}
