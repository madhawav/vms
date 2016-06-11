import eduze.vms.facilitator.logic.ConnectionRequest;
import eduze.vms.facilitator.logic.FacilitatorController;
import eduze.vms.facilitator.logic.PresenterConnectionListener;
import eduze.vms.facilitator.logic.RequestAlreadyProcessedException;
import eduze.vms.presenter.logic.*;
import org.testng.Assert;

import java.net.MalformedURLException;

/**
 * Created by Admin on 6/11/2016.
 */
public class PresenterControllerTest {

    private FacilitatorController facilitatorController;
    private PresenterController presenterController;
    @org.testng.annotations.BeforeMethod
    public void setup() throws Exception
    {
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

            }

            @Override
            public void onDisconnected(String consoleId) {

            }
        });

        final FacilitatorConnector connector = FacilitatorConnector.connect(configuration1);

        FacilitatorConnector.ConnectionRequestState requestState = connector.checkConnectionRequestState();
        if(requestState == FacilitatorConnector.ConnectionRequestState.Success)
        {
            presenterController = connector.obtainController();
        }

        connector.setConnectionRequestStateListener(new FacilitatorConnector.ConnectionRequestStateListener() {
            @Override
            public void onSuccess(FacilitatorConnector sender) {
                try {
                    presenterController = sender.obtainController();
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


    }
    @org.testng.annotations.Test
    public void testStart() throws Exception {

    }

    @org.testng.annotations.Test
    public void testDisconnect() throws Exception {

    }

    @org.testng.annotations.Test
    public void testRequestScreenShare() throws Exception {

    }

    @org.testng.annotations.Test
    public void testRequestAudioShare() throws Exception {

    }

    @org.testng.annotations.Test
    public void testIsScreenShared() throws Exception {

    }

    @org.testng.annotations.Test
    public void testIsAudioShared() throws Exception {

    }

    @org.testng.annotations.Test
    public void testNotifyScreenSharedChanged() throws Exception {

    }

    @org.testng.annotations.Test
    public void testNotifyAudioSharedChanged() throws Exception {

    }

    @org.testng.annotations.Test
    public void testIsServerAcceptsScreenShare() throws Exception {

    }

    @org.testng.annotations.Test
    public void testIsServerAcceptsAudioShare() throws Exception {

    }

}