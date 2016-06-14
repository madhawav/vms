package eduze.vms.server.logic;

import eduze.vms.server.logic.webservices.*;

import javax.xml.ws.Endpoint;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * The Controller used by Server UI. Exposes all functionality of server to external
 */
public class ServerController {
    /**
     * Listeners used to give feedback to UI
     */
    private ArrayList<PairListener> pairListeners = new ArrayList<>();
    private ArrayList<FacilitatorSessionListener> facilitatorSessionListeners = new ArrayList<>();



    //Startup configuration of server
    private ServerImpl.Configuration startConfig = new ServerImpl.Configuration();

    //Internal service of server
    private ServerImpl serverService = null;

    private boolean running = false; //has the server started?

    /**
     * Constructor of Server Controller
     */
    public ServerController()
    {

    }

    /**
     * Return port used by server
     * @return server port
     */
    public int getPort() {
        return startConfig.getPort();
    }

    /**
     * Set the port used by service. Must be set before the server start
     * @param port Server port
     * @throws ServerStartedException The service has started already and hence port cannot be changed
     */
    public void setPort(int port) throws ServerStartedException {
        if(running)
            throw new ServerStartedException();
        startConfig.setPort(port);
    }

    /**
     * Returns whether server is running
     * @return True if server is running. Otherwise return false.
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * Start the service using previously set port and server name
     */
    public void start()  {
        serverService = new ServerImpl(startConfig); //setup server service

        //setup pair listeners to tunnel events from service to user
        serverService.setPairListener(new PairListener() {
            @Override
            public void onPair(Facilitator pairedFacilitator) {
                notifyPairAdded(pairedFacilitator);
            }

            @Override
            public void onUnPair(Facilitator pairedFacilitator) {
                notifyPairRemoved(pairedFacilitator);
            }

            @Override
            public void onPairNameChanged(String pairKey, String oldName, String newName) {
                notifyPairNameChanged(pairKey, oldName, newName);
            }
        });

        //Setup facilitator listener to tunnel events from service to user
        serverService.setFacilitatorSessionListener(new FacilitatorSessionListener() {
            @Override
            public void onConnected(Facilitator facilitator, String consoleId) {
                notifyFacilitatorConnected(facilitator,consoleId);
            }

            @Override
            public void onDisconnected(Facilitator facilitator, String consoleId) {
                notifyFacilitatorDisconnected(facilitator,consoleId);
            }

            @Override
            public void onMeetingAdjourned() {
                notifyMeetingAdjourned();
            }
        });

        //Start the Service
        serverService.start();
        running = true;
    }

    /**
     * Notify listeners that meeting has been adjourned
     */
    private void notifyMeetingAdjourned() {
        for(FacilitatorSessionListener l : facilitatorSessionListeners)
        {
            l.onMeetingAdjourned();
        }
    }

    /**
     * Notify listeners that a Facilitator has changed their name
     * @param pairKey Pairkey to identify the Facilitator
     * @param oldName Previous name of Facilitator
     * @param newName New name of Facilitator
     */
    private void notifyPairNameChanged(String pairKey, String oldName, String newName) {
        for(PairListener p : pairListeners)
            p.onPairNameChanged(pairKey,oldName,newName);
    }

    /**
     * Notify listeners that a Facilitator has been connected
     * @param facilitator Facilitator connected
     * @param consoleId Facilitator console id
     */
    private void notifyFacilitatorConnected(Facilitator facilitator ,String consoleId) {
        for(FacilitatorSessionListener fsp : facilitatorSessionListeners)
            fsp.onConnected(facilitator,consoleId);
    }

    /**
     * Notify listeners that a Facilitator has been disconnected
     * @param facilitator Disconnected Facilitator
     * @param console_id Facilitator console id
     */
    private void notifyFacilitatorDisconnected(Facilitator facilitator, String console_id) {
        for(FacilitatorSessionListener fsp : facilitatorSessionListeners)
            fsp.onDisconnected(facilitator, console_id);
    }

    /**
     * Retrieve name of Server
     * @return Server Name
     */
    public String getName() {
        if(isRunning())
            return startConfig.getName();
        else
            return serverService.getServerName();
    }

    /**
     * Set the name of Server
     * @param name ServerName
     * @throws ServerStartedException Server has been started already
     */
    public void setName(String name) throws Exception {
        if(running)
            throw new ServerStartedException();
        this.startConfig.setName(name);
    }

    /**
     * Set the password of Server
     * @param password new password
     * @throws ServerStartedException Server has started already
     */
    public void setPassword(String password) throws Exception {
        if(running)
            throw new ServerStartedException();
        this.startConfig.setPassword(password);
    }

    /**
     * Retrieve the list of paired facilitators
     * @return Paired Facilitators
     */
    public Collection<Facilitator> getPairedFacilitators()
    {
        return serverService.getFacilitatorManager().getFacilitators();
    }

    /**
     * Add a Facilitator information to list of paired facilitators
     * @param facilitatorName Name of Facilitator
     * @param facilitatorPairKey Pairkey provided to Facilitator
     */
    public void addPairedFacilitator(String facilitatorName, String facilitatorPairKey)
    {
        //Instantiate Facilitator Structure
        Facilitator facilitator = new Facilitator();
        facilitator.setName(facilitatorName);
        facilitator.setPairKey(facilitatorPairKey);
        //Store new Facilitator
        serverService.getFacilitatorManager().addFacilitator(facilitator);
    }

    /**
     * Add a PairListener
     * @param pairListener Listener to be notified on pair events
     */
    public void addPairListener(PairListener pairListener)
    {
        pairListeners.add(pairListener);
    }

    /**
     * Remove a pair listener
     * @param pairListener listener to be not notified on pair events
     */
    public void removePairListener(PairListener pairListener)
    {
        pairListeners.remove(pairListener);
    }

    /**
     * Add a listener to be notified on facilitator connection events
     * @param facilitatorSessionListener Listener to be notified
     */
    public void addFacilitatorSessionListener(FacilitatorSessionListener facilitatorSessionListener)
    {
        facilitatorSessionListeners.add(facilitatorSessionListener);
    }

    /**
     * Remove a Facilitator Session Listener
     * @param facilitatorSessionListener Listener to be not notified
     */
    public void removeFacilitatorSessionListener(FacilitatorSessionListener facilitatorSessionListener)
    {
        facilitatorSessionListeners.remove(facilitatorSessionListener);
    }

    /**
     * Notify listeners that a new pair is made
     * @param pair
     */
    void notifyPairAdded(Facilitator pair)
    {
        for(PairListener p : pairListeners)
            p.onPair(pair);
    }

    /**
     * Notify listeners that a pair is removed
     * @param pair
     */
    void notifyPairRemoved(Facilitator pair)
    {
        for(PairListener p : pairListeners)
            p.onUnPair(pair);
    }

    /**
     * Adjourn the meeting
     * @throws ServerNotReadyException The server is not in a state to adjourn a meeting
     */
    public void adjournMeeting() throws ServerNotReadyException {
       VMSessionManagerImpl sessionManager = serverService.getVmSessionManager();
        if(sessionManager != null)
        {
            sessionManager.adjournMeeting();
        }
    }

    /**
     * Retrieve the timeout used to disconnect Facilitator connections automatically
     * @return Timeout for Facilitator Connections
     */
    public int getFacilitatorConnectivityTimeoutInterval() {
        return startConfig.getFacilitatorConnectivityTimeoutInterval();
    }

    /**
     * Set the timeout required to disconnect Facilitator connections automatically
     * @param facilitatorConnectivityTimeoutInterval Timeout for Facilitator connections
     */
    public void setFacilitatorConnectivityTimeoutInterval(int facilitatorConnectivityTimeoutInterval) {
        startConfig.setFacilitatorConnectivityTimeoutInterval(facilitatorConnectivityTimeoutInterval);
    }
}
