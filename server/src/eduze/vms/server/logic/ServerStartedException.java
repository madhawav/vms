package eduze.vms.server.logic;

/**
 * Created by Fujitsu on 4/16/2016.
 */
public class ServerStartedException extends Exception {
    ServerStartedException()
    {
        super("Server has already started");
    }
}
