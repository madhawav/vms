package eduze.vms.server.logic.webservices;

/**
 * The Facilitator is already paired to server
 */
public class AlreadyPairedException extends Exception {
    public AlreadyPairedException()
    {
        super("Facilitator Already Paired");
    }
}
