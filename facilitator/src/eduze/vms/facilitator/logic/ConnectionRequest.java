package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.webservices.ConnectionRequestState;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Represents a Connection Request from a presenter to facilitator
 */
public class ConnectionRequest {

    private ConnectionRequestState connectionRequestState; //State of connection request
    private FacilitatorImpl facilitator; //Facilitator

    /**
     * Constructor
     * @param connectionRequestState
     * @param facilitator
     */
    public ConnectionRequest( ConnectionRequestState connectionRequestState, FacilitatorImpl facilitator)
    {
        this.connectionRequestState = connectionRequestState;
        this.facilitator = facilitator;
    }

    /**
     * Retrieve Connection Request Id
     * @return
     */
    public String getConnectionRequestId() {
        return connectionRequestState.getConnectionRequestId();
    }

    /**
     * Retrieve Presenter Name
     * @return
     */
    public String getPresenterName() {
        return connectionRequestState.getPresenterName();
    }

    /**
     * Accept connection request
     * @throws RequestAlreadyProcessedException
     */
    public void accept() throws RequestAlreadyProcessedException {
        facilitator.acceptConnectionRequest(connectionRequestState.getConnectionRequestId());
    }

    /**
     * Reject Connection Request
     * @throws RequestAlreadyProcessedException
     */
    public void reject() throws RequestAlreadyProcessedException {facilitator.rejectConnectionRequest(connectionRequestState.getConnectionRequestId());}
}
