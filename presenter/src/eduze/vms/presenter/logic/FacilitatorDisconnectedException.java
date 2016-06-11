package eduze.vms.presenter.logic;

/**
 * Created by Admin on 6/11/2016.
 */
public class FacilitatorDisconnectedException extends Exception {
    public FacilitatorDisconnectedException(Exception innerException)
    {
        super("Facilitator has been disconnected from presenter",innerException);
    }
}
