package eduze.vms.server.logic.webservices;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class AlreadyPairedException extends Exception {
    public AlreadyPairedException()
    {
        super("Facilitator Already Paired");
    }
}
