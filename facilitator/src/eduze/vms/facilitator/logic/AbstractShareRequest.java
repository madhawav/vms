package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;

import java.util.Date;

/**
 * Created by Madhawa on 14/04/2016.
 */
public abstract class AbstractShareRequest {
    protected String presenterConsoleId;
    protected boolean answered = false;
    protected FacilitatorImpl facilitator;
    protected Date requestTime = new Date();

    protected AbstractShareRequest(String presenterConsoleId, FacilitatorImpl facilitator)
    {
        this.presenterConsoleId = presenterConsoleId;
        this.facilitator = facilitator;
    }

    public abstract void honour() throws RequestAlreadyProcessedException, ServerConnectionException, InvalidIdException, ServerNotReadyException;
    public abstract void dismiss() throws RequestAlreadyProcessedException;

    protected void markAnswered()
    {
        answered = true;
    }

    public boolean isAnswered()
    {
        return answered;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public String getPresenterConsoleId()
    {
        return presenterConsoleId;
    }

    public String getPresenterName()
    {
        return facilitator.getPresenterConsole(presenterConsoleId).getName();
    }
}
