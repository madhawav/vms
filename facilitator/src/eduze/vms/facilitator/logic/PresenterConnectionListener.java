package eduze.vms.facilitator.logic;

/**
 * Created by Madhawa on 13/04/2016.
 */
public interface PresenterConnectionListener {
    public void onConnectionRequested(ConnectionRequest connectionRequest);
    public void onConnected(String connectionRequestId, String consoleId);

}
