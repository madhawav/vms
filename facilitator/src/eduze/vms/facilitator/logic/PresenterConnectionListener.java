package eduze.vms.facilitator.logic;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Listener for Presenter Connections
 */
public interface PresenterConnectionListener {
    /**
     * A presenter has requested to connect with facilitator
     * @param connectionRequest Connection Request information
     */
    public void onConnectionRequested(ConnectionRequest connectionRequest);

    /**
     * A Presenter has successfully connected with facilitator
     * @param connectionRequestId Unique ID of connection request
     * @param consoleId Presenter Console Id
     */
    public void onConnected(String connectionRequestId, String consoleId);

    /**
     * A Presenter has been disconnected from Facilitator
     * @param consoleId
     */
    public void onDisconnected(String consoleId);

    //TODO: onDisconnect method should be added
}
