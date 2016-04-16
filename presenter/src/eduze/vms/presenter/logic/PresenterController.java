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
public class PresenterController {
    private final String presenterConsoleId;
    // Configuration configuration;
    private Facilitator facilitator = null;
    private PresenterConsole presenterConsole = null;
    private FacilitatorConnector connector = null;

    private ControlLoop controlLoop = null;

    private ArrayList<ControlLoop.StateChangeListener> stateChangeListeners = new ArrayList<>();



    private AssignedTasksManager assignedTasksManager = null;

    PresenterController(FacilitatorConnector facilitatorConnector, Facilitator facilitator, String presenterConsoleId) {
        this.connector = facilitatorConnector;
        this.facilitator = facilitator;
        this.presenterConsoleId = presenterConsoleId;
    }

    public FacilitatorConnector.Configuration getConfiguration()
    {
        return connector.getConfiguration();
    }

    void start() throws MalformedURLException, FacilitatorConnectionException {
        PresenterConsoleImplServiceLocator presenterConsoleImplServiceLocator = new PresenterConsoleImplServiceLocator();
        try {
            presenterConsole = presenterConsoleImplServiceLocator.getPresenterConsoleImplPort(new URL(URLGenerator.generatePresenterConsoleAccessUrl(getConfiguration().getFacilitatorURL(),presenterConsoleId)));
            presenterConsole.acknowledgeConnection();

            this.assignedTasksManager = new AssignedTasksManager(this);

            this.controlLoop = new ControlLoop(presenterConsole,getConfiguration().getFacilitatorURL());
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
                }
            });

            this.controlLoop.start();

        } catch (ServiceException e) {
            throw new FacilitatorConnectionException(e);
        } catch (RemoteException e) {
            throw new FacilitatorConnectionException(e);
        }
    }

    public void disconnect() throws FacilitatorConnectionException {
        try {
            presenterConsole.disconnect();
            controlLoop.stopRunning();
        } catch (RemoteException e) {
            throw new FacilitatorConnectionException(e);
        }

    }

    public boolean requestScreenShare(boolean includeAudio) throws FacilitatorConnectionException {
        try{
            return presenterConsole.requestScreenAccess(includeAudio);
        }
        catch (RemoteException e)
        {
            throw new FacilitatorConnectionException(e);
        }

    }

    public boolean requestAudioShare() throws FacilitatorConnectionException {
        try{
            return presenterConsole.requestAudioRelayAccess();
        }
        catch (RemoteException e)
        {
            throw new FacilitatorConnectionException(e);
        }

    }



    public boolean isScreenShared()
    {
        return controlLoop.isScreenShared();
    }

    public boolean isAudioShared()
    {
        return controlLoop.isAudioShared();
    }

    public void addStateChangeListener(ControlLoop.StateChangeListener listener)
    {
        stateChangeListeners.add(listener);
    }

    public void removeStateChangeListener(ControlLoop.StateChangeListener listener)
    {
        stateChangeListeners.remove(listener);
    }

    void notifyScreenSharedChanged(boolean newValue)
    {
        for(ControlLoop.StateChangeListener listener : stateChangeListeners)
        {
            listener.onScreenCaptureChanged(newValue);
        }
    }

    void notifyAudioSharedChanged(boolean newValue)
    {
        for(ControlLoop.StateChangeListener listener : stateChangeListeners)
        {
            listener.onAudioCaptureChanged(newValue);
        }
    }

    public AssignedTasksManager getAssignedTasksManager() {
        return assignedTasksManager;
    }

    public boolean isAllowedScreenShare() {
        return controlLoop.isAllowedScreenShare();
    }

    public void setAllowedScreenShare(boolean allowedScreenShare) {
        controlLoop.setAllowedScreenShare(allowedScreenShare);
    }

    public boolean isAllowedAudioShare() {
        return controlLoop.isAllowedAudioShare();
    }

    public void setAllowedAudioShare(boolean allowedAudioShare) {
        controlLoop.setAllowedAudioShare(allowedAudioShare);
    }

    public boolean isServerAcceptsScreenShare()
    {
        return controlLoop.isServerAcceptsScreenShare();
    }

    public boolean isServerAcceptsAudioShare()
    {
        return controlLoop.isServerAcceptsAudioShare();
    }
}
