package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * A screen and audio share request raised by presenter
 */
public class ScreenAudioShareRequest extends AbstractShareRequest {
    /**
     * Create a Screen Audio Share Request
     * @param presenterConsoleId Presenter Console Id
     * @param facilitator Facilitator Id
     */
    public ScreenAudioShareRequest(String presenterConsoleId,FacilitatorImpl facilitator) {
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
        facilitator.setScreenAccessPresenter(super.presenterConsoleId,true);
        markAnswered();
    }

    /**
     * Honour only screen sharing
     * @throws RequestAlreadyProcessedException
     * @throws ServerConnectionException
     * @throws InvalidIdException
     * @throws ServerNotReadyException
     */
    public void honourScreen() throws RequestAlreadyProcessedException, ServerConnectionException, InvalidIdException, ServerNotReadyException {
        if(answered)
            throw new RequestAlreadyProcessedException();
        facilitator.setScreenAccessPresenter(super.presenterConsoleId,false);
        markAnswered();
    }

    /**
     * Honour only audio sharing
     * @throws RequestAlreadyProcessedException
     * @throws ServerConnectionException
     * @throws InvalidIdException
     * @throws ServerNotReadyException
     */
    public void honourAudio() throws RequestAlreadyProcessedException, ServerConnectionException, InvalidIdException, ServerNotReadyException {
        if(answered)
            throw new RequestAlreadyProcessedException();
        facilitator.setAudioRelayAccessPresenter(super.presenterConsoleId);
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
