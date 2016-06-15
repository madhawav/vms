import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.PairListener;
import eduze.vms.server.logic.ServerController;
import eduze.vms.server.logic.webservices.Facilitator;
import eduze.vms.server.logic.webservices.Server;
import eduze.vms.server.logic.webservices.ServerImpl;
import eduze.vms.server.logic.webservices.ServerNotReadyException;
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
    @org.testng.annotations.Test(groups = "start")
    public void testIsRunning() throws Exception {
        Assert.assertEquals(serverController.isRunning(),false);
        serverController.start();
        Assert.assertEquals(serverController.isRunning(),true);
    }

    private boolean done = false;
    @org.testng.annotations.Test(dependsOnGroups = "start")
    public void testAdjournMeeting() throws ServerNotReadyException {
        done = false;
        serverController.addFacilitatorSessionListener(new FacilitatorSessionListener() {
            @Override
            public void onConnected(Facilitator facilitator, String consoleId) {

            }

            @Override
            public void onDisconnected(Facilitator facilitator, String consoleId) {

            }

            @Override
            public void onMeetingAdjourned() {
                done  = true;
            }
        });
        serverController.adjournMeeting();
        if(!done)
            Assert.fail("Didn't notify adjourn");
    }


}