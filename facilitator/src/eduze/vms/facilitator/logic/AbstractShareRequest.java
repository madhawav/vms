package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ServerNotReadyException;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;

import java.util.Date;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * Abstract Request raised from presenter to facilitator
 */
public abstract class AbstractShareRequest {
    protected String presenterConsoleId; //Presenter console id of requester
    protected boolean answered = false; //Has the request been responded
    protected FacilitatorImpl facilitator; //Facilitator Service
    protected Date requestTime = new Date(); //Time of raising of request

    /**
     * Constructor.
     * @param presenterConsoleId Presenter Console ID of presenter raising request
     * @param facilitator Facilitator WebService
     */
    protected AbstractShareRequest(String presenterConsoleId, FacilitatorImpl facilitator)
    {
        this.presenterConsoleId = presenterConsoleId;
        this.facilitator = facilitator;
    }

    /**
     * Accept the request
     * @throws RequestAlreadyProcessedException Request has been answered already
     * @throws ServerConnectionException Connection error with Server
     * @throws InvalidIdException Invalid Presenter Console Id
     * @throws ServerNotReadyException Server is not ready
     */
    public abstract void honour() throws RequestAlreadyProcessedException, ServerConnectionException, InvalidIdException, ServerNotReadyException;

    /**
     * Dismiss the request
     * @throws RequestAlreadyProcessedException Request has been answered already
     */
    public abstract void dismiss() throws RequestAlreadyProcessedException;

    /**
     * Mark the request as answered
     */
    protected void markAnswered()
    {
        answered = true;
    }

    /**
     * Is the request answered?
     * @return True if request has been responded. Otherwise return false.
     */
    public boolean isAnswered()
    {
        return answered;
    }

    /**
     * Retrieve time at which request was raised
     * @return Request Raise Time
     */
    public Date getRequestTime() {
        return requestTime;
    }

    /**
     * Retrieve Presenter Console Id of requester
     * @return
     */
    public String getPresenterConsoleId()
    {
        return presenterConsoleId;
    }

    /**
     * Retrieve the name of presenter
     * @return Name of presenter
     */
    public String getPresenterName()
    {
        return facilitator.getPresenterConsole(presenterConsoleId).getName();
    }
}
