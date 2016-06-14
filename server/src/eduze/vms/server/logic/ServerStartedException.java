package eduze.vms.server.logic;

/**
 * Created by Fujitsu on 4/16/2016.
 */

/**
 * The Requested operation cannot be honoured since the server is in running state
 */
public class ServerStartedException extends Exception {
    ServerStartedException()
    {
        super("Server has already started");
    }
}
