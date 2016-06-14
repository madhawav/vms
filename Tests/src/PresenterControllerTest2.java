import eduze.vms.facilitator.logic.ConnectionRequest;
import eduze.vms.facilitator.logic.FacilitatorController;
import eduze.vms.facilitator.logic.PresenterConnectionListener;
import eduze.vms.facilitator.logic.RequestAlreadyProcessedException;
import eduze.vms.presenter.logic.*;
import eduze.vms.presenter.logic.mpi.facilitator.InvalidFacilitatorPasskeyException;
import org.junit.Assert;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

/**
 * Created by Admin on 6/14/2016.
 */
public class PresenterControllerTest2 {
    private static FacilitatorController facilitatorController;
    private static PresenterController presenterController;

    @org.junit.Before
    public void setup() throws FacilitatorConnectionException, MalformedURLException, InvalidFacilitatorPasskeyException, FacilitatorConnectionNotReadyException, FacilitatorDisconnectedException {
        FacilitatorController.Configuration configuration = new FacilitatorController.Configuration();
        configuration.setListenerPort(7000);
        configuration.setPassword("12345");
        configuration.setName("TestFacilitator");
        facilitatorController = FacilitatorController.start(configuration);


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
                        Assert.fail("Connection Request failed");
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
                Assert.fail("Error on Connection to Facilitator");
                return false;
            }
        });
    }
    @org.junit.Test
    public void start() throws Exception {

    }

    @org.junit.Test
    public void disconnect() throws Exception {

    }

    @org.junit.Test
    public void requestScreenShare() throws Exception {

    }

    @org.junit.Test
    public void requestAudioShare() throws Exception {

    }

    @org.junit.Test
    public void isScreenShared() throws Exception {

    }

    @org.junit.Test
    public void isAudioShared() throws Exception {

    }

    @org.junit.Test
    public void isAllowedScreenShare() throws Exception {

    }

    @org.junit.Test
    public void isAllowedAudioShare() throws Exception {

    }

    @org.junit.Test
    public void isServerAcceptsScreenShare() throws Exception {

    }

    @org.junit.Test
    public void isServerAcceptsAudioShare() throws Exception {

    }

}