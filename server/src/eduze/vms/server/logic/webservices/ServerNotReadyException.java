package eduze.vms.server.logic.webservices;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class ServerNotReadyException extends Exception {
    public ServerNotReadyException()
    {
        super("Server is busy");
    }
}
