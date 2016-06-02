package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;
import eduze.vms.facilitator.logic.webservices.PresenterConsoleImpl;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Model to provide information on a presenter to UI layer
 */
public class Presenter {
    //Internal connections to web services
    private FacilitatorController facilitatorController = null;
    private PresenterConsoleImpl presenterConsole = null;
    private FacilitatorImpl facilitator = null;

    /**
     * Constructor
     * @param facilitatorController
     * @param presenterConsole
     */
    Presenter(FacilitatorController facilitatorController, PresenterConsoleImpl presenterConsole)
    {
        this.facilitatorController = facilitatorController;
        this.presenterConsole = presenterConsole;
        this.facilitator = facilitatorController.getFacilitatorService();
    }

    /**
     * Retrieve Name of Presenter
     * @return Name
     */
    public String getPresenterName()
    {
        return presenterConsole.getName();
    }

    /**
     * Retrieve presenter console id
     * @return Presenter Console Id
     */
    public String getPresenterConsoleId()
    {
        return presenterConsole.getConsoleId();
    }

    /**
     * Retrieve whether presenter is sharing screen
     * @return True if presenter is sharing its screen. Otherwise return false
     */
    public boolean isScreenActive()
    {
        VirtualMeetingSnapshot vm = facilitatorController.getVmStatus();
        if(presenterConsole.getConsoleId().equals(vm.getActiveScreenPresenterId()) && facilitator.getFacilitatorConsoleId().equals(vm.getActiveScreenFacilitatorId()))
            return true;
        return false;
    }

    /**
     * Retrieve whether presenter is speech active
     * @return True if presenter is sharing its audio. Otherwise return false.
     */
    public boolean isSpeechActive()
    {
        VirtualMeetingSnapshot vm = facilitatorController.getVmStatus();
        if(presenterConsole.getConsoleId().equals(vm.getActiveSpeechPresenterId()) && facilitator.getFacilitatorConsoleId().equals(vm.getActiveSpeechFacilitatorId()))
            return true;
        return false;
    }

    /**
     * Make the Presenter Share Audio
     * @throws ServerConnectionException
     * @throws InvalidIdException
     * @throws ServerNotReadyException
     */
    public void setSpeechActive() throws ServerConnectionException, InvalidIdException, ServerNotReadyException {
        this.facilitatorController.setAudioRelayAccessPresenter(getPresenterConsoleId());
    }

    /**
     * Make the presenter share screen
     * @param includeAudio Include Audio Sharing
     * @throws ServerConnectionException
     * @throws InvalidIdException
     * @throws ServerNotReadyException
     */
    public void setScreenActive(boolean includeAudio) throws ServerConnectionException, InvalidIdException, ServerNotReadyException {
        this.facilitatorController.setScreenAccessPresenter(getPresenterConsoleId(),includeAudio);
    }

    /**
     * Disconnect presenter
     */
    public void disconnect() throws ServerConnectionException {
        presenterConsole.disconnect();
    }

    /**
     * Is the presenter connected to facilitator
     * @return
     */
    public boolean isConnected()
    {
        return presenterConsole.isConnected();
    }
}
