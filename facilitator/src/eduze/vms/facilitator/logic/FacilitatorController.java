package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;
import eduze.vms.facilitator.logic.webservices.PresenterConsole;
import eduze.vms.facilitator.logic.webservices.PresenterConsoleImpl;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Main access-point to UI. Instantiate by calling FacilitatorController.start(). This also starts the Facilitator Server.
 */
public class FacilitatorController {
    private FacilitatorImpl facilitatorService = null; //WebService to Presenter
    private FacilitatorController.Configuration configuration = null; //Startup Configuration of Facilitator
    private boolean running = false; //has it started?
    private VirtualMeetingSnapshot vmStatus = null;

    private ControlLoop controlLoop = null;

    /**
     * Construct by calling FacilitatorController.start()
     * @param config
     */
    private  FacilitatorController(FacilitatorController.Configuration config)
    {
        this.configuration = config;
    }

    //Controller to manage connection with server. Not set until a connection is established with server
    private ServerConnectionController serverConnectionController = null;

    /**
     * Set of listeners to connections from presenters
     */
    private ArrayList<PresenterConnectionListener> presenterConnectionListeners = new ArrayList<>();

    private ArrayList<PresenterModifiedListener> presenterModifiedListeners = new ArrayList<>();

    private ArrayList<ControlLoopListener> controlLoopListeners = new ArrayList<>();

    //Handles pairing and connection with server
    private ServerManager serverManager = null;

    /**
     *
     * @return True if connected to a server
     */
    public boolean isServerConnected()
    {
        if(serverConnectionController == null)
            return false;
        return serverConnectionController.isConnected();
    }

    FacilitatorImpl getFacilitatorService() {
        return facilitatorService;
    }

    /**
     * Retrieve Controller to manage active connection with server
     * @return Controller to manage Server Connection
     */
    public ServerConnectionController getServerConnectionController()
    {
        return serverConnectionController;
    }

    /**
     * Start Facilitator Controller and WebServices to presenters
     */
    private void start()
    {
        facilitatorService = new FacilitatorImpl(configuration);
        facilitatorService.setPresenterConnectionListener(new PresenterConnectionListener() {
            @Override
            public void onConnectionRequested(ConnectionRequest connectionRequest) {
                notifyConnectionRequested(connectionRequest);
            }

            @Override
            public void onConnected(String connectionRequestId, String consoleId) {
                notifyConnected(connectionRequestId,consoleId);
            }
        });
        facilitatorService.setPresenterModifiedListener(new PresenterModifiedListener() {
            @Override
            public void presenterNameChanged(String consoleId, String newName) {
                notifyPresenterNameChanged(consoleId,newName);
            }
        });
        facilitatorService.start();

        serverManager = new ServerManager(this);

        running = true;
    }

    public Presenter getPresenter(String consoleId)
    {
        PresenterConsoleImpl console =  getFacilitatorService().getPresenterConsole(consoleId);
        if(console.isConnectionAcknowledged())
            return new Presenter(this,console);
        else return null;
    }

    public int getPresenterCount()
    {
        int count = 0;
        for(PresenterConsoleImpl console : getFacilitatorService().getPresenterConsoles())
        {
            if(console.isConnectionAcknowledged())
                count++;

        }
        return count;

    }

    public Presenter[] getPresenters()
    {
        Presenter[] presenters = new Presenter[getPresenterCount()];
        PresenterConsoleImpl[] presenterConsoles = new PresenterConsoleImpl[getFacilitatorService().getPresenterConsoles().size()];

        getFacilitatorService().getPresenterConsoles().toArray(presenterConsoles);

        int writeIndex = 0;
        for(int i = 0; i < presenterConsoles.length; i++){
            presenters[writeIndex++] = new Presenter(this,presenterConsoles[i]);
        }
        return presenters;
    }

    private void notifyConnected(String connectionRequestId, String consoleId) {
        for(PresenterConnectionListener connectionListener : presenterConnectionListeners)
            connectionListener.onConnected(connectionRequestId,consoleId);
    }


    /**
     * Retrieve Facilitator configuration
     * @return Facilitator Configuration
     */
    public  Configuration getConfiguration()
    {
        return configuration;
    }

    /**
     * Create and Start a FacilitatorController instance.
     * @param configuration
     * @return
     */
    public static FacilitatorController start(FacilitatorController.Configuration configuration)
    {
        FacilitatorController controller = new FacilitatorController(configuration);
        controller.start();
        return controller;
    }

    /**
     * Add a new listener to listen to connection requests from presenters
     * @param listener
     */
    public void addConnectionListener(PresenterConnectionListener listener)
    {
        presenterConnectionListeners.add(listener);
    }

    /**
     * Remove a connection listener that was listen to presenter connections
     * @param listener
     */
    public void removeConnectionListener(PresenterConnectionListener listener)
    {
        presenterConnectionListeners.remove(listener);
    }

    /**
     * Add a new listener to listen to control loop events
     * @param listener
     */
    public void addControlLoopListener(ControlLoopListener listener)
    {
        controlLoopListeners.add(listener);
    }

    /**
     * Remove a control loop listener
     * @param listener
     */
    public void removeControlLoopListener(ControlLoopListener listener)
    {
        controlLoopListeners.remove(listener);
    }

    /**
     * Notify user that a presenter is requesting to connect
     * @param cr ConnectionRequest with details on presenter
     */
    private void notifyConnectionRequested(ConnectionRequest cr)
    {
        for(PresenterConnectionListener listener : presenterConnectionListeners)
            listener.onConnectionRequested(cr);
    }

    /**
     * Notify user that a control loop update has been received
     * @param vm VirtualMeetingSnapshot
     */
    private void notifyControlLoopUpdateReceived(VirtualMeetingSnapshot vm)
    {
        for(ControlLoopListener listener : controlLoopListeners)
            listener.updateReceived(vm);
    }

    /**
     * Retrieve ServerManager used to manage connection with server
     * @return ServerManager of FacilitatorController
     * @throws ServiceNotStartedException if FacilitatorServer has not started
     */
    public ServerManager getServerManager() throws ServiceNotStartedException {
        if(serverManager == null)
            throw new ServiceNotStartedException("Start Facilitator before calling getServerManager");
        return serverManager;
    }


    /**
     * Add a new listener to listen to listen to changes of presenters
     * @param listener
     */
    public void addPresenterModifiedListener(PresenterModifiedListener listener)
    {
        presenterModifiedListeners.add(listener);
    }

    /**
     * Remove a presenterModifiedListener
     * @param listener
     */
    public void removePresenterModifiedListener(PresenterModifiedListener listener)
    {
        presenterModifiedListeners.remove(listener);
    }

    /**
     *
     * @return True if WebServices for Presenters have started
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Notification from ServerManager that connection was made successfully to server. Setup the ConnectionController in this method.
     * @param url Server URL
     * @param result Connection Result received from Server
     * @throws MalformedURLException Error in Server URL
     * @throws ServiceException Communication Error with Server
     * @throws ServerConnectionException Communication Error with Server
     */
    void notifyServerConnected(String url, ConnectionResult result) throws MalformedURLException, ServiceException, ServerConnectionException {
        ServerConnectionController connectionController = new ServerConnectionController(this,url,result);
        this.serverConnectionController =  connectionController;
        this.facilitatorService.establishServerConnection(url,result.getFacilitatorConsoleId(),result.getVirtualMeetingConsoleId());

        controlLoop = new ControlLoop(this.facilitatorService,url,getConfiguration().getListenerPort());
        controlLoop.setControlLoopListener(new ControlLoopListener() {
            @Override
            public void updateReceived(VirtualMeetingSnapshot vm) {
                vmStatus = vm;
                notifyControlLoopUpdateReceived(vm);

            }
        });
        controlLoop.start();
    }

    void notifyPresenterNameChanged(String id, String name)
    {
        for(PresenterModifiedListener listener : presenterModifiedListeners)
            listener.presenterNameChanged(id,name);
    }

    public VirtualMeetingSnapshot getVmStatus() {
        return vmStatus;
    }

    public void setScreenAccessPresenter(String presenterConsoleId, boolean includeAudio) throws ServerConnectionException {
        try {
            getFacilitatorService().getFacilitatorConsole().requestScreenAccess(presenterConsoleId,includeAudio);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }

    /**
     * Configuration of Facilitator. Mainly includes configuration on WebServices for Presenters.
     */
    public static class Configuration
    {
        private String name = "Facilitator";
        private int listenerPort = 7000;

        private int screenShareBufferSize = 2;
        private int audioRelayBufferSize = 5;

        private String password = "password";

        public int getListenerPort() {
            return listenerPort;
        }

        public String getName() {
            return name;
        }

        /**
         *
         * @return Password that presenters must provide to connect
         */
        public String getPassword() {
            return password;
        }

        public int getAudioRelayBufferSize() {
            return audioRelayBufferSize;
        }

        public int getScreenShareBufferSize() {
            return screenShareBufferSize;
        }

        public void setAudioRelayBufferSize(int audioRelayBufferSize) {
            this.audioRelayBufferSize = audioRelayBufferSize;
        }

        public void setScreenShareBufferSize(int screenShareBufferSize) {
            this.screenShareBufferSize = screenShareBufferSize;
        }

        public void setListenerPort(int listenerPort) {
            this.listenerPort = listenerPort;
        }



        public void setName(String name) {
            this.name = name;
        }

        /**
         *
         * @param password Password that persenters should provide to connect
         */
        public void setPassword(String password) {
            this.password = password;
        }
    }

}
