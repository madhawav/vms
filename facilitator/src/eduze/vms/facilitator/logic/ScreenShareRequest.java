package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * A screen share request raised by presenter
 */
public class ScreenShareRequest extends AbstractShareRequest {
    /**
     * Create a Screen Share Request
     * @param presenterConsoleId Presenter Console Id
     * @param facilitator Facilitator Id
     */
    public ScreenShareRequest(String presenterConsoleId,FacilitatorImpl facilitator) {
        super(presenterConsoleId,facilitator);
    }

    /**
     * Accept the Request
     * @throws RequestAlreadyProcessedException Request has been processed already
     * @throws ServerConnectionException Server connection exception
     * @throws InvalidIdException Invalid presenter console
     * @throws ServerNotReadyException Server is not ready
     */
    @Override
    public void honour() throws RequestAlreadyProcessedException, ServerConnectionException, InvalidIdException, ServerNotReadyException {
        if(answered)
            throw new RequestAlreadyProcessedException();
        facilitator.setScreenAccessPresenter(super.presenterConsoleId,false);
        markAnswered();
    }

    /**
     * Dismiss the Request
     * @throws RequestAlreadyProcessedException
     */
    @Override
    public void dismiss() throws RequestAlreadyProcessedException {
        if(answered)
            throw new RequestAlreadyProcessedException();
        markAnswered();
    }
}
