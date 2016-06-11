import eduze.vms.server.logic.PairListener;
import eduze.vms.server.logic.ServerController;
import eduze.vms.server.logic.webservices.Facilitator;
import eduze.vms.server.logic.webservices.Server;
import eduze.vms.server.logic.webservices.ServerImpl;
import org.testng.Assert;

import static org.testng.Assert.*;

/**
 * Created by Admin on 6/11/2016.
 */
public class ServerControllerTest {
    private ServerController serverController;
    @org.testng.annotations.BeforeClass
    public void setUp() throws Exception {
        serverController = new ServerController();
        serverController.setName("TestServer");
        serverController.setPassword("123456");
        serverController.setPort(1234);

    }
    @org.testng.annotations.Test
    public void testIsRunning() throws Exception {
        Assert.assertEquals(serverController.isRunning(),false);
        serverController.start();
        Assert.assertEquals(serverController.isRunning(),true);
    }


}