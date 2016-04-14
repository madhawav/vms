package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;
import eduze.vms.facilitator.logic.webservices.PresenterConsoleImpl;

/**
 * Created by Madhawa on 13/04/2016.
 */
public class Presenter {
    private FacilitatorController facilitatorController = null;
    private PresenterConsoleImpl presenterConsole = null;
    private FacilitatorImpl facilitator = null;
    Presenter(FacilitatorController facilitatorController, PresenterConsoleImpl presenterConsole)
    {
        this.facilitatorController = facilitatorController;
        this.presenterConsole = presenterConsole;
        this.facilitator = facilitatorController.getFacilitatorService();
    }
    public String getPresenterName()
    {
        return presenterConsole.getName();
    }
    public String getPresenterConsoleId()
    {
        return presenterConsole.getConsoleId();
    }

    public boolean isScreenActive()
    {
        VirtualMeetingSnapshot vm = facilitatorController.getVmStatus();
        if(presenterConsole.getConsoleId().equals(vm.getActiveScreenPresenterId()) && facilitator.getFacilitatorConsoleId().equals(vm.getActiveScreenFacilitatorId()))
            return true;
        return false;
    }

    public void setScreenActive(boolean includeAudio) throws ServerConnectionException, InvalidIdException, ServerNotReadyException {
        this.facilitatorController.setScreenAccessPresenter(getPresenterConsoleId(),includeAudio);
    }

    public boolean isConnected()
    {
        return presenterConsole.isConnected();
    }
}
