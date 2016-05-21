package eduze.vms.facilitator.logic.webservices;

/**
 * Created by Admin on 5/21/2016.
 */

/**
 * The presenter is disconnected
 */
public class DisconnectedException extends Exception {
    public DisconnectedException()
    {
        super("Presenter is disconnected");
    }
}
