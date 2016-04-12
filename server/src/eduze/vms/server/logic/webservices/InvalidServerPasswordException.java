package eduze.vms.server.logic.webservices;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class InvalidServerPasswordException extends Exception{
    public InvalidServerPasswordException()
    {
        super("Server Password is not correct");
    }
}
