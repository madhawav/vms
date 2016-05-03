package eduze.vms.presenter.logic;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * Facilitator Connection is not accepted
 */
public class FacilitatorConnectionNotReadyException extends Exception {
    public FacilitatorConnectionNotReadyException()
    {
        super("Facilitator Connection Request has failed or still pending");
    }
}
