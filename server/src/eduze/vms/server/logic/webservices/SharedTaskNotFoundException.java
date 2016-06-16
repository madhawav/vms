package eduze.vms.server.logic.webservices;

/**
 * Created by Fujitsu on 4/16/2016.
 */

/**
 * Invalid SharedTask Id or SharedTask is not found
 */
public class SharedTaskNotFoundException extends Exception {
    public SharedTaskNotFoundException()
    {
        super("Shard task is not found");
    }
}
