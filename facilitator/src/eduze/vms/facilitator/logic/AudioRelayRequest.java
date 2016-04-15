package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;

/**
 * Created by Madhawa on 14/04/2016.
 */
public class AudioRelayRequest extends AbstractShareRequest {
    public AudioRelayRequest(String presenterConsoleId,FacilitatorImpl facilitator) {
        super(presenterConsoleId,facilitator);
    }

    @Override
    public void honour() throws RequestAlreadyProcessedException, ServerConnectionException, InvalidIdException, ServerNotReadyException {
        if(answered)
            throw new RequestAlreadyProcessedException();
        facilitator.setAudioRelayAccessPresenter(super.presenterConsoleId);
        markAnswered();
    }

    @Override
    public void dismiss() throws RequestAlreadyProcessedException {
        if(answered)
            throw new RequestAlreadyProcessedException();
        markAnswered();
    }
}
