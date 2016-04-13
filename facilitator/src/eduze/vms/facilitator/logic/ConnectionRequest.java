package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.webservices.ConnectionRequestState;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;

/**
 * Created by Madhawa on 13/04/2016.
 */
public class ConnectionRequest {

    private ConnectionRequestState connectionRequestState;
    private FacilitatorImpl facilitator;



    public ConnectionRequest( ConnectionRequestState connectionRequestState, FacilitatorImpl facilitator)
    {

        this.connectionRequestState = connectionRequestState;
        this.facilitator = facilitator;
    }

    public String getConnectionRequestId() {
        return connectionRequestState.getConnectionRequestId();
    }


    public String getPresenterName() {
        return connectionRequestState.getPresenterName();
    }
    public void accept() throws RequestAlreadyProcessedException {
        facilitator.acceptConnectionRequest(connectionRequestState.getConnectionRequestId());
    }

    public void reject() throws RequestAlreadyProcessedException {facilitator.rejectConnectionRequest(connectionRequestState.getConnectionRequestId());}


}
