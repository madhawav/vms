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

        //setup pair listeners to tunnel even from service to user
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

        serverService.start();
        running = true;
    }

    private void notifyMeetingAdjourned() {
        for(FacilitatorSessionListener l : facilitatorSessionListeners)
        {
            l.onMeetingAdjourned();
        }
    }

    private void notifyPairNameChanged(String pairKey, String oldName, String newName) {
        for(PairListener p : pairListeners)
            p.onPairNameChanged(pairKey,oldName,newName);
    }

    private void notifyFacilitatorConnected(Facilitator facilitator ,String consoleId) {
        for(FacilitatorSessionListener fsp : facilitatorSessionListeners)
            fsp.onConnected(facilitator,consoleId);
    }

    private void notifyFacilitatorDisconnected(Facilitator facilitator, String console_id) {
        for(FacilitatorSessionListener fsp : facilitatorSessionListeners)
            fsp.onDisconnected(facilitator, console_id);
    }

    public String getName() {
        if(isRunning())
            return startConfig.getName();
        else
            return serverService.getServerName();
    }

    public void setName(String name) throws Exception {
        if(running)
            throw new Exception("Already Started");
        this.startConfig.setName(name);
    }


    public void setPassword(String password) throws Exception {
        if(running)
            throw new Exception("Already Started");
        this.startConfig.setPassword(password);
    }

    public Collection<Facilitator> getPairedFacilitators()
    {
        return serverService.getFacilitatorManager().getFacilitators();
    }

    public void addPairedFacilitator(String facilitatorName, String facilitatorPairKey)
    {
        Facilitator facilitator = new Facilitator();
        facilitator.setName(facilitatorName);
        facilitator.setPairKey(facilitatorPairKey);
        serverService.getFacilitatorManager().addFacilitator(facilitator);
    }
    public void addPairListener(PairListener p)
    {
        pairListeners.add(p);
    }

    public void removePairListener(PairListener p)
    {
        pairListeners.remove(p);
    }

    public void addFacilitatorSessionListener(FacilitatorSessionListener p)
    {
        facilitatorSessionListeners.add(p);
    }

    public void removeFacilitatorSessionListener(FacilitatorSessionListener p)
    {
        facilitatorSessionListeners.remove(p);
    }

    void notifyPairAdded(Facilitator pair)
    {
        for(PairListener p : pairListeners)
            p.onPair(pair);
    }
    void notifyPairRemoved(Facilitator pair)
    {
        for(PairListener p : pairListeners)
            p.onUnPair(pair);
    }

    public void adjournMeeting() throws ServerNotReadyException {
       VMSessionManagerImpl sessionManager = serverService.getVmSessionManager();
        if(sessionManager != null)
        {
            sessionManager.adjournMeeting();
        }
    }
}
