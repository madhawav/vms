package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.VMParticipant;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;
import eduze.vms.facilitator.logic.webservices.PresenterConsoleImpl;

import javax.xml.rpc.ServiceException;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Main access-point to UI. Instantiate by calling FacilitatorController.start(). This also starts the Facilitator WebServices used by Presenter Subsystems.
 */
public class FacilitatorController {
    private FacilitatorImpl facilitatorService = null; //WebService to Presenter
    private FacilitatorController.Configuration configuration = null; //Startup Configuration of Facilitator
    private boolean running = false; //has it started?
    private VirtualMeetingSnapshot vmStatus = null; //Last known state of Virtual Meeting

    private ControlLoop controlLoop = null; //The control loop that manages Facilitator and its stake holders
    private SharedTaskManager sharedTaskManager = null; //Manager for Shared Tasks (Assigned Tasks)

    /**
     * Construct by calling FacilitatorController.start()
     * @param config Facilitator Service Startup Configuration
     */
    private  FacilitatorController(FacilitatorController.Configuration config)
    {
        this.configuration = config;
    }

    //Controller to manage connection with server. Not set until a connection is established with server
    private ServerConnectionController serverConnectionController = null;

    /**
     * Set of listeners to connection requests from presenters
     */
    private ArrayList<PresenterConnectionListener> presenterConnectionListeners = new ArrayList<>();

    /**
     * Set of listeners to be notified on modifications to presenters parameters
     */
    private ArrayList<PresenterModifiedListener> presenterModifiedListeners = new ArrayList<>();

    //Set of listeners for Control Loop Events
    private ArrayList<ControlLoopListener> controlLoopListeners = new ArrayList<>();

    //Set of listeners notified on reception of capture frames
    private ArrayList<CaptureReceivedListener> captureReceivedListeners = new ArrayList<>();

    //Lister to be notified on share requests.
    private ShareRequestListener shareRequestListener = null;

    //Handles pairing and connection with server
    private ServerManager serverManager = null;

    /**
     * Retrieve whether Facilitator is connected to a server
     * @return True if connected to a server
     */
    public boolean isServerConnected()
    {
        if(serverConnectionController == null)
            return false;
        return serverConnectionController.isConnected();
    }

    //WebService offered to Presenter Subsystems
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
        //Setup Facilitator Web Service
        facilitatorService = new FacilitatorImpl(configuration);
        //Bridge listeners and events
        facilitatorService.setPresenterConnectionListener(new PresenterConnectionListener() {
            @Override
            public void onConnectionRequested(ConnectionRequest connectionRequest) {
                notifyConnectionRequested(connectionRequest);
            }

            @Override
            public void onConnected(String connectionRequestId, String consoleId) {
                notifyPresenterConnected(connectionRequestId,consoleId);
            }

            @Override
            public void onDisconnected(String consoleId) {
                notifyPresenterDisconnected(consoleId);
            }
        });
        facilitatorService.setPresenterModifiedListener(new PresenterModifiedListener() {
            @Override
            public void presenterNameChanged(String consoleId, String newName) {
                notifyPresenterNameChanged(consoleId,newName);
            }
        });

        facilitatorService.setShareRequestListener(new ShareRequestListener() {
            @Override
            public boolean onShareRequest(AbstractShareRequest shareRequest) {
                if(shareRequestListener == null)
                    return false;
                return shareRequestListener.onShareRequest(shareRequest);
            }
        });
        //Start Facilitator Web Service
        facilitatorService.start();

        //Setup Server Connection Manager
        serverManager = new ServerManager(this);

        running = true;
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
        //Setup Server Connection Controller
        ServerConnectionController connectionController = new ServerConnectionController(this,url,result);
        this.serverConnectionController =  connectionController;
        //Request Facilitator Service to establish Server connection
        this.facilitatorService.establishServerConnection(url,result.getFacilitatorConsoleId(),result.getVirtualMeetingConsoleId());

        //Setup Shared Task Manager
        sharedTaskManager = new SharedTaskManager(this);

        //Setup Control Loop
        controlLoop = new ControlLoop(this.facilitatorService,url,getConfiguration().getListenerPort());
        controlLoop.setControlLoopListener(new ControlLoopListener() {
            @Override
            public void updateReceived(VirtualMeetingSnapshot vm) {
                vmStatus = vm;
                if(sharedTaskManager != null)
                    sharedTaskManager.onVMStatusUpdate();
                notifyControlLoopUpdateReceived(vm);

            }

            @Override
            public void onMeetingAdjourned() throws ServerConnectionException {
                getServerConnectionController().disconnect();
                notifyMeetingAdjourned();
                //TODO: Disconnect from server
            }
        });

        //Subscribe Control Loop Listeners
        controlLoop.setCaptureReceivedListener(new CaptureReceivedListener() {
            @Override
            public void onScreenCaptureReceived(byte[] rawData, BufferedImage image, String facilitatorConsoleId, String presenterConsoleId) {
                notifyScreenShareCaptureReceived(rawData,image,facilitatorConsoleId,presenterConsoleId);
            }

            @Override
            public void onException(Exception e) {
                notifyCaptureException(e);
            }

            @Override
            public void onAudioDataReceived(byte[] bytes, String activeScreenFacilitatorId, String activeScreenPresenterId) {
                notifyAudioDataReceived(bytes, activeScreenFacilitatorId, activeScreenPresenterId);
            }
        });

        //Start Control Loop
        controlLoop.start();
    }



    void notifyServerDisconnected() throws ServerConnectionException {
        controlLoop.stopRunning();
        controlLoop = null;
        sharedTaskManager = null;
        facilitatorService.disconnectServerConnection();

    }


    /**
     * Retrieve a presenter connected to Facilitator using presenter console id
     * @param consoleId
     * @return
     */
    public Presenter getPresenter(String consoleId)
    {
        PresenterConsoleImpl console =  getFacilitatorService().getPresenterConsole(consoleId);
        //Filter out unacknowledged presenter consoles since they have not completed connection process
        if(console.isConnectionAcknowledged())
            return new Presenter(this,console);
        else return null;
    }

    /**
     * Retrieve total number of presenters connected to facilitator
     * @return
     */
    public int getPresenterCount()
    {
        int count = 0;
        for(PresenterConsoleImpl console : getFacilitatorService().getPresenterConsoles())
        {
            //Exclude unacknowledged presenter consoles
            if(console.isConnectionAcknowledged())
                count++;

        }
        return count;

    }

    /**
     * Retrieve List of presenters connected to facilitator
     * @return
     */
    public Presenter[] getPresenters()
    {
        Presenter[] presenters = new Presenter[getPresenterCount()];
        PresenterConsoleImpl[] presenterConsoles = new PresenterConsoleImpl[getFacilitatorService().getPresenterConsoles().size()];

        getFacilitatorService().getPresenterConsoles().toArray(presenterConsoles);

        int writeIndex = 0;
        /**
         * Exclude presenters who havent acknowledged connection
         */
        for(int i = 0; i < presenterConsoles.length; i++){
            if(presenterConsoles[i].isConnectionAcknowledged())
                presenters[writeIndex++] = new Presenter(this,presenterConsoles[i]);
        }
        return presenters;
    }

    /**
     * Retrieve Manager for Shared Tasks
     * @return
     * @throws ServerNotReadyException Server is not connected yet
     */
    public SharedTaskManager getSharedTaskManager() throws ServerNotReadyException {
        if(sharedTaskManager == null)
            throw new ServerNotReadyException();
        return sharedTaskManager;
    }

    /**
     * Retrieve list of Virtual Meeting Participants
     * @return
     */
    public VirtualMeetingParticipantInfo[] getVMParticipants()
    {
        //if not initiated, return empty array
        if(vmStatus == null)
            return new VirtualMeetingParticipantInfo[0];
        if(vmStatus.getParticipants() == null)
            return new VirtualMeetingParticipantInfo[0];

        //Generate list of VirtualMeetingParticipantInto to be sent to UI layer
        VirtualMeetingParticipantInfo[] results = new VirtualMeetingParticipantInfo[vmStatus.getParticipants().length];
        for(int i = 0; i <results.length; i++)
        {
            results[i] = VirtualMeetingParticipantInfo.fromVMParticipant(vmStatus.getParticipants()[i]);
        }
        return results;
    }

    /**
     * Retrieve Virtual Meeting Participant Information from Presenter Console Id
     * @param presenterId Presenter Console Id
     * @return
     */
    public VirtualMeetingParticipantInfo getVMParticipant(String presenterId)
    {
        //If not initiated, return null
        if(vmStatus == null)
            return null;
        if(vmStatus.getParticipants() == null)
            return null;

        //Search for relevant participant in known participants
      //  VirtualMeetingParticipantInfo[] results = new VirtualMeetingParticipantInfo[vmStatus.getParticipants().length];

        for(VMParticipant participant : vmStatus.getParticipants())
        {
            if(presenterId.equals(participant.getPresenterId()))
            {
                return VirtualMeetingParticipantInfo.fromVMParticipant(participant);
            }
        }
        /*
        for(int i = 0; i <results.length; i++)
        {
            if(presenterId.equals(results[i].getPresenterId()))
            {
                //Create and return VirtualMeetingParticipantInfo object
                return VirtualMeetingParticipantInfo.fromVMParticipant(vmStatus.getParticipants()[i]);
            }

        }*/
        //No match found
        return null;
    }

    /**
     * Notify connection listeners that a presenter has been connected
     * @param connectionRequestId Connection Request ID informed upon receipt of connection request
     * @param consoleId Presenter Console Id
     */
    private void notifyPresenterConnected(String connectionRequestId, String consoleId) {
        for(PresenterConnectionListener connectionListener : presenterConnectionListeners)
            connectionListener.onConnected(connectionRequestId,consoleId);
    }

    /**
     * Notifies listeners that a presenter has been disconnected
     * @param consoleId
     */
    private void notifyPresenterDisconnected(String consoleId) {
        for(PresenterConnectionListener connectionListener : presenterConnectionListeners)
            connectionListener.onDisconnected(consoleId);
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

    private void notifyMeetingAdjourned() throws ServerConnectionException {
        for(ControlLoopListener listener : controlLoopListeners)
            listener.onMeetingAdjourned();
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
     * Add a new listener to listen to capture frames received which are to be sent to user
     * @param listener
     */
    public void addCaptureReceivedListener(CaptureReceivedListener listener)
    {
       captureReceivedListeners.add(listener);
    }

    /**
     * Remove a presenterModifiedListener
     * @param listener
     */
    public void removeCaptureReceivedListener(CaptureReceivedListener listener)
    {
        captureReceivedListeners.remove(listener);
    }

    /**
     * Notify Capture exception
     * @param e Exception
     */
    private void notifyCaptureException(Exception e)
    {
        for(CaptureReceivedListener listener : captureReceivedListeners)
        {
            listener.onException(e);
        }
    }

    public void adjournMeeting() throws ServerConnectionException {

        getFacilitatorService().adjournMeeting();
    }

    /**
     * Notify reception of screen share capture
     * @param data Encoded Raw Data of Image
     * @param image BufferedImage
     * @param facilitatorConsoleId Facilitator Console Id
     * @param presenterConsoleId Presenter Console Id
     */
    private void notifyScreenShareCaptureReceived(byte[] data, BufferedImage image, String facilitatorConsoleId, String presenterConsoleId)
    {
        for(CaptureReceivedListener listener : captureReceivedListeners)
        {
            listener.onScreenCaptureReceived(data,image,facilitatorConsoleId,presenterConsoleId);
        }
    }

    /**
     * Retrieve Facilitator Console Id
     * @return Facilitator Console Id
     */
    public String getFacilitatorId()
    {
        return facilitatorService.getFacilitatorConsoleId();
    }

    /**
     * Notify reception of audio frame
     * @param bytes Raw ByteStream of Data
     * @param activeScreenFacilitatorId Facilitator Console Id
     * @param activeScreenPresenterId Presenter Console Id
     */
    private void notifyAudioDataReceived(byte[] bytes, String activeScreenFacilitatorId, String activeScreenPresenterId) {
        for(CaptureReceivedListener listener : captureReceivedListeners)
        {
            listener.onAudioDataReceived(bytes,activeScreenFacilitatorId,activeScreenPresenterId);
        }
    }

    /**
     *
     * @return True if WebServices for Presenters have started
     */
    public boolean isRunning() {
        return running;
    }


    /**
     * Notify Presenter Name Changed
     * @param id Presenter Id
     * @param name Name of Presenter
     */
    void notifyPresenterNameChanged(String id, String name)
    {
        for(PresenterModifiedListener listener : presenterModifiedListeners)
            listener.presenterNameChanged(id,name);
    }

    /**
     * Retrieve Virtual Meeting State
     * @return Virtual Meeting State
     */
    public VirtualMeetingSnapshot getVmStatus() {
        return vmStatus;
    }

    /**
     * Set the Active Screen Share Presenter
     * @param presenterConsoleId Presenter Console Id
     * @param includeAudio Include audio share
     * @throws ServerConnectionException Server Connection Error
     * @throws InvalidIdException Invalid Presenter Console Id
     * @throws ServerNotReadyException Server Not Ready
     */
    public void setScreenAccessPresenter(String presenterConsoleId, boolean includeAudio) throws ServerConnectionException, InvalidIdException, ServerNotReadyException {
        getFacilitatorService().setScreenAccessPresenter(presenterConsoleId,includeAudio);
    }

    /**
     * Set the Active Audio Relay Presenter
     * @param presenterConsoleId Presenter Console Id
     * @throws ServerConnectionException Server Error
     * @throws InvalidIdException Invalid Presenter Console Id
     * @throws ServerNotReadyException Server is not ready
     */
    public void setAudioRelayAccessPresenter(String presenterConsoleId) throws ServerConnectionException, InvalidIdException, ServerNotReadyException {
        getFacilitatorService().setAudioRelayAccessPresenter(presenterConsoleId);
    }

    /**
     * Retrieve Share Request Listener
     * @return
     */
    public ShareRequestListener getShareRequestListener() {
        return shareRequestListener;
    }

    /**
     * Set the Share Request Listener
     * @param shareRequestListener
     */
    public void setShareRequestListener(ShareRequestListener shareRequestListener) {
        this.shareRequestListener = shareRequestListener;
    }


    /**
     * Terminate the Facilitator Controller
     */
    public void finish() throws ServerConnectionException {

            if(getServerConnectionController() != null)
                getServerConnectionController().disconnect();
            for(Presenter presenter : getPresenters())
            {
                presenter.disconnect();
            }

    }

    /**
     * Configuration of Facilitator. Mainly includes configuration on WebServices for Presenters.
     */
    public static class Configuration implements java.io.Serializable
    {
        private String name = "Facilitator"; //Name of Facilitator
        private int listenerPort = 7000; //Port used by Facilitators Web Services

        //Buffer Configuration used by FrameBuffers
        private int screenShareBufferSize = 2;
        private int audioRelayBufferSize = 5;

        //Pass Key of Faciliator
        private String password = "password";

        /**
         * Retrieve port used by Facilitators Web Service
         * @return
         */
        public int getListenerPort() {
            return listenerPort;
        }

        /**
         * Set name of Facilitator
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * Return password of facilitator
         * @return Password that presenters must provide to connect
         */
        public String getPassword() {
            return password;
        }

        /**
         * Return Audio Relay Buffers Size
         * @return
         */
        public int getAudioRelayBufferSize() {
            return audioRelayBufferSize;
        }

        /**
         * Return Screen Share Buffers Size
         * @return
         */
        public int getScreenShareBufferSize() {
            return screenShareBufferSize;
        }

        /**
         * Set Audio Relay Buffer Size
         * @param audioRelayBufferSize
         */
        public void setAudioRelayBufferSize(int audioRelayBufferSize) {
            this.audioRelayBufferSize = audioRelayBufferSize;
        }

        /**
         * Set Screen Share Buffer Size
         * @param screenShareBufferSize
         */
        public void setScreenShareBufferSize(int screenShareBufferSize) {
            this.screenShareBufferSize = screenShareBufferSize;
        }

        /**
         * Set port used by Facilitator Web Services
         * @param listenerPort
         */
        public void setListenerPort(int listenerPort) {
            this.listenerPort = listenerPort;
        }


        /**
         * Set name of Facilitator
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Set Password that presenters should provide to connect
         * @param password Password that presenters should provide to connect
         */
        public void setPassword(String password) {
            this.password = password;
        }

    }

}
