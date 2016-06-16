package eduze.vms.server.logic.webservices;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Server is not ready
 */
public class ServerNotReadyException extends Exception {
    public ServerNotReadyException()
    {
        super("Server is busy");
    }
}
