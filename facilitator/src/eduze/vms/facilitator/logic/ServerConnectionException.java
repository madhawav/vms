package eduze.vms.facilitator.logic;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Server Connection Exception
 */
public class ServerConnectionException extends Exception {
    public ServerConnectionException(Throwable innerException)
    {
        super("Unable to connect to server",innerException);
    }
}
