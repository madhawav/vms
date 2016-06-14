package eduze.vms.server.logic.webservices;

/**
 * The Server password submitted is invalid
 */
public class InvalidServerPasswordException extends Exception{
    public InvalidServerPasswordException()
    {
        super("Server Password is not correct");
    }
}
