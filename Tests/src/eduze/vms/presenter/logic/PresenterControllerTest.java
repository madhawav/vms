package eduze.vms.presenter.logic;

import eduze.vms.facilitator.logic.*;
import eduze.vms.presenter.logic.mpi.facilitator.InvalidFacilitatorPasskeyException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

import java.net.MalformedURLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Admin on 6/11/2016.
 */
public class PresenterControllerTest {

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private FacilitatorController facilitatorController;
    private PresenterController presenterController;

    @org.testng.annotations.BeforeClass
    public void wholeSetup() throws FacilitatorConnectionException, MalformedURLException, InvalidFacilitatorPasskeyException, InterruptedException, FacilitatorConnectionNotReadyException, FacilitatorDisconnectedException {
        FacilitatorController.Configuration configuration = new FacilitatorController.Configuration();
        configuration.setListenerPort(7000);
        configuration.setPassword("12345");
        configuration.setName("TestFacilitator");
        this.facilitatorController = FacilitatorController.start(configuration);


        final FacilitatorConnector.Configuration configuration1 = new FacilitatorConnector.Configuration();
        configuration1.setFacilitatorPasskey(configuration.getPassword());
        configuration1.setFacilitatorURL("http://localhost:" + String.valueOf(configuration.getListenerPort()));
        configuration1.setPresenterName("TestPresenter");

        facilitatorController.addConnectionListener(new PresenterConnectionListener() {
            @Override
            public void onConnectionRequested(ConnectionRequest connectionRequest) {
                if(connectionRequest.getPresenterName().equals(configuration1.getPresenterName()))
                {
                    try {
                        connectionRequest.accept();
                    } catch (RequestAlreadyProcessedException e) {
                        Assert.fail("Connection Request failed",e);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onConnected(String connectionRequestId, String consoleId) {
                System.out.println("Connected");
            }

            @Override
            public void onDisconnected(String consoleId) {

            }
        });


        final FacilitatorConnector connector = FacilitatorConnector.connect(configuration1);


        connector.setConnectionRequestStateListener(new FacilitatorConnector.ConnectionRequestStateListener() {
            @Override
            public void onSuccess(FacilitatorConnector sender) {
                try {
                    presenterController = sender.obtainController();
                    System.out.println("Success");
                    countDownLatch.countDown();
                } catch (FacilitatorConnectionNotReadyException e) {
                    e.printStackTrace();
                } catch (FacilitatorConnectionException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FacilitatorDisconnectedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(FacilitatorConnector sender) {

            }

            @Override
            public boolean onException(FacilitatorConnector sender, Exception e) {
                Assert.fail("Error on Connection to Facilitator",e);
                return false;
            }
        });

        countDownLatch.await(5000, TimeUnit.MILLISECONDS);
        FacilitatorConnector.ConnectionRequestState requestState = connector.checkConnectionRequestState();
        if(requestState == FacilitatorConnector.ConnectionRequestState.Success)
        {
            presenterController = connector.obtainController();
        }
    }

    @AfterClass
    public void shutdown() throws ServerConnectionException, FacilitatorConnectionException {

        facilitatorController.finish();

    }
    @org.testng.annotations.Test(singleThreaded = true, groups = "active")
    public void testStart() throws Exception {
        Assert.assertTrue(facilitatorController.isRunning());
        Assert.assertNotNull(presenterController);
    }

    @org.testng.annotations.Test(singleThreaded = true, dependsOnGroups = "active")
    public void testDisconnect() throws Exception {
        presenterController.disconnect();
    }

    AbstractShareRequest shareRequest = null;

    @org.testng.annotations.Test(groups = "active")
    public void testRequestScreenShare() throws Exception {
        facilitatorController.setShareRequestListener(new ShareRequestListener() {
            @Override
            public boolean onShareRequest(AbstractShareRequest shareRequest) {
                if(shareRequest instanceof ScreenShareRequest)
                {
                    PresenterControllerTest.this.shareRequest = shareRequest;
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        Assert.assertTrue(presenterController.requestScreenShare(false));
    }

    @org.testng.annotations.Test(groups = "active")
    public void testRequestAudioShare() throws Exception {
        facilitatorController.setShareRequestListener(new ShareRequestListener() {
            @Override
            public boolean onShareRequest(AbstractShareRequest shareRequest) {
                if(shareRequest instanceof ScreenShareRequest)
                {
                    PresenterControllerTest.this.shareRequest = shareRequest;
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        Assert.assertTrue(presenterController.requestScreenShare(false));
    }


}