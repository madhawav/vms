/**
 * FacilitatorManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eduze.vms.facilitator.logic.mpi.facilitatormanager;

public interface FacilitatorManager extends java.rmi.Remote {
    public void unPair(java.lang.String pairKey) throws java.rmi.RemoteException;
    public java.lang.String pair(java.lang.String name, java.lang.String password) throws java.rmi.RemoteException, eduze.vms.facilitator.logic.mpi.facilitatormanager.InvalidServerPasswordException, eduze.vms.facilitator.logic.mpi.facilitatormanager.AlreadyPairedException;
}
