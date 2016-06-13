package eduze.vms.presenter.logic;

import eduze.vms.presenter.logic.mpi.facilitator.Facilitator;
import eduze.vms.presenter.logic.mpi.presenterconsole.PresenterConsole;
import eduze.vms.presenter.logic.mpi.presenterconsole.PresenterConsoleImplServiceLocator;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * Main Controller for managing logic of Presenter Subsystem
 */
public class PresenterController {
    private FacilitatorConnectivityMonitor connectivityMonitor;
    //Presenter console id provided by facilitator
    private final String presenterConsoleId;
    //Facilitator MPI provided by Facilitator Web Service
    private Facilitator facilitator = null;
    //Presenter Console MPI provided by Facilitator Web Service
    private PresenterConsole presenterConsole = null;
    //Connector to manage connections with Facilitator
    private FacilitatorConnector connector = null;
    //Control Mechanism of Presenter Subsystem
    private ControlLoop controlLoop = null;

    //Event listeners from UI
    private ArrayList<ControlLoop.StateChangeListener> stateChangeListeners = new ArrayList<>();

    //Assigned tasks manager
    private AssignedTasksManager assignedTasksManager = null;

    /**
     * Constructor to Presenter Controller. Presenter Controller should be constructed by following procedure in Facilitator Connector.
     * @param facilitatorConnector Facilitator Connector used to establish connection
     * @param facilitator Facilitator MPI provided by Facilitator
     * @param presenterConsoleId Presenter Console id
     */
    PresenterController(FacilitatorConnector facilitatorConnector, Facilitator facilitator, String presenterConsoleId) {
        this.connector = facilitatorConnector;
        this.facilitator = facilitator;
        this.presenterConsoleId = presenterConsoleId;
        this.connectivityMonitor = new FacilitatorConnectivityMonitor(facilitatorConnector.getConfiguration().getConnectionTerminateTimeout(),facilitatorConnector.getConfiguration().getConnectionPauseTimeout());
    }

    /**
     * Retrieve the connectivity monitor associated with controller
     * @return ConnectivityMonitor
     */
    public FacilitatorConnectivityMonitor getConnectivityMonitor() {
        return connectivityMonitor;
    }

    /**
     * Retrieve Configuration of Presenter Subsystem
     * @return
     */
    public FacilitatorConnector.Configuration getConfiguration()
    {
        return connector.getConfiguration();
    }

    /**
     * Start Controller
     * @throws MalformedURLException
     * @throws FacilitatorConnectionException
     */
    void start() throws MalformedURLException, FacilitatorConnectionException, FacilitatorDisconnectedException {
        //Establish Connection to Web Services
        PresenterConsoleImplServiceLocator presenterConsoleImplServiceLocator = new PresenterConsoleImplServiceLocator();
        try {
            //Acknowledge Connection
            presenterConsole = presenterConsoleImplServiceLocator.getPresenterConsoleImplPort(new URL(URLGenerator.generatePresenterConsoleAccessUrl(getConfiguration().getFacilitatorURL(),presenterConsoleId)));
            presenterConsole.acknowledgeConnection();

            //Setup Assigned Tasks Manager
            this.assignedTasksManager = new AssignedTasksManager(this);

            //Setup Control Loop
            this.controlLoop = new ControlLoop(presenterConsole,getConfiguration().getFacilitatorURL(),connectivityMonitor);
            this.controlLoop.setStateChangeListener(new ControlLoop.StateChangeListener() {
                @Override
                public void onScreenCaptureChanged(boolean newValue) {
                    notifyScreenSharedChanged(newValue);
                }

                @Override
                public void onAudioCaptureChanged(boolean newValue) {
                    notifyAudioSharedChanged(newValue);
                }

                @Override
                public void onControlLoopCycleCompleted() {
                    assignedTasksManager.onAssignedTasksUpdate(controlLoop.getAssignedTasks());
                    notifyControlLoopCycleCompleted();
                }

                @Override
                public void onFacilitatorDisconnected() {
                    notifyFacilitatorDisconnected();
                }
            });

            //Start Control Loop
            this.controlLoop.start();

        } catch (ServiceException e) {
            throw new FacilitatorConnectionException(e);
        } catch (RemoteException e) {
            throw new FacilitatorConnectionException(e);
        } catch (FacilitatorDisconnectedException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void notifyFacilitatorDisconnected() {
        for(ControlLoop.StateChangeListener listener : stateChangeListeners)
        {
            listener.onFacilitatorDisconnected();
        }
    }

    private void notifyControlLoopCycleCompleted() {
        for(ControlLoop.StateChangeListener listener : stateChangeListeners)
        {
            listener.onControlLoopCycleCompleted();
        }
    }

    /**
     * Disconnect from Facilitator
     * @throws FacilitatorConnectionException
     */
    public void disconnect() throws FacilitatorConnectionException {
        try {
            presenterConsole.disconnect();
            controlLoop.stopRunning();
        } catch (RemoteException e) {
            throw new FacilitatorConnectionException(e);
        }

    }

    /**
     * Request from facilitator, permission to share screen
     * @param includeAudio include audio share request
     * @return False if request is immediately denied. True if request is being considered.
     * @throws FacilitatorConnectionException
     */
    public boolean requestScreenShare(boolean includeAudio) throws FacilitatorConnectionException {
        try{
            return presenterConsole.requestScreenAccess(includeAudio);
        }
        catch (RemoteException e)
        {
            throw new FacilitatorConnectionException(e);
        }

    }

    /**
     * Request permission from facilitator to share audio
     * @return True if request is being considered. False if request is immediately rejected.
     * @throws FacilitatorConnectionException
     */
    public boolean requestAudioShare() throws FacilitatorConnectionException {
        try{
            return presenterConsole.requestAudioRelayAccess();
        }
        catch (RemoteException e)
        {
            throw new FacilitatorConnectionException(e);
        }

    }


    /**
     * Retrieve whether the screen shared
     * @return
     */
    public boolean isScreenShared()
    {
        return controlLoop.isScreenShared();
    }

    /**
     * Retrieve whether the audio is shared
     * @return
     */
    public boolean isAudioShared()
    {
        return controlLoop.isAudioShared();
    }

    /**
     * Register a listener to state changes of Presenter Logic
     * @param listener
     */
    public void addStateChangeListener(ControlLoop.StateChangeListener listener)
    {
        stateChangeListeners.add(listener);
    }

    /**
     * Removes a StateChange listener
     * @param listener
     */
    public void removeStateChangeListener(ControlLoop.StateChangeListener listener)
    {
        stateChangeListeners.remove(listener);
    }

    /**
     * Notify listeners on change in screen share state
     * @param newValue is screen shared enabled
     */
    void notifyScreenSharedChanged(boolean newValue)
    {
        for(ControlLoop.StateChangeListener listener : stateChangeListeners)
        {
            listener.onScreenCaptureChanged(newValue);
        }
    }

    /**
     * Notify listeners on change in audio share state
     * @param newValue is audio shared enabled
     */
    void notifyAudioSharedChanged(boolean newValue)
    {
        for(ControlLoop.StateChangeListener listener : stateChangeListeners)
        {
            listener.onAudioCaptureChanged(newValue);
        }
    }

    /**
     * Retrieve assinged tasks manager
     * @return
     */
    public AssignedTasksManager getAssignedTasksManager() {
        return assignedTasksManager;
    }

    /**
     * Retrieve whether presenter has given consent to share screen
     * @return
     */
    public boolean isAllowedScreenShare() {
        return controlLoop.isAllowedScreenShare();
    }

    /**
     * Set whether presenter has given consent to share screen
     * @param allowedScreenShare
     */
    public void setAllowedScreenShare(boolean allowedScreenShare) {
        controlLoop.setAllowedScreenShare(allowedScreenShare);
    }

    /**
     * Retrieve whether presenter has given consent to share audio
     * @return
     */
    public boolean isAllowedAudioShare() {
        return controlLoop.isAllowedAudioShare();
    }

    /**
     * Set whether presenter has given consent to share audio
     * @param allowedAudioShare
     */
    public void setAllowedAudioShare(boolean allowedAudioShare) {
        controlLoop.setAllowedAudioShare(allowedAudioShare);
    }

    /**
     * Retrieve whether facilitator is willing to accept screen share
     * @return
     */
    public boolean isServerAcceptsScreenShare()
    {
        return controlLoop.isServerAcceptsScreenShare();
    }

    /**
     * Retrieve whether facilitator is willing to acccept audio share
     * @return
     */
    public boolean isServerAcceptsAudioShare()
    {
        return controlLoop.isServerAcceptsAudioShare();
    }
}
