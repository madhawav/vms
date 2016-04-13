package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.*;

import javax.jws.WebService;
import javax.swing.*;
import javax.xml.ws.Endpoint;
import java.util.HashMap;

/**
 * Created by Madhawa on 13/04/2016.
 */
@WebService(endpointInterface = "eduze.vms.facilitator.logic.webservices.Facilitator")
public class FacilitatorImpl implements Facilitator  {
    private FacilitatorController.Configuration configuration = null;
    private HashMap<String,ConnectionRequestState> connectionRequests = new HashMap<>();
    private ConnectionListener connectionListener = null;

    private HashMap<String,PresenterConsoleImpl> presenterConsoles = new HashMap<>();

    public FacilitatorImpl()
    {

    }

    @Override
    public ConnectionRequestState checkConnectionRequestState(String connectionRequestId) {
        return connectionRequests.get(connectionRequestId);
    }

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public FacilitatorImpl(FacilitatorController.Configuration configuration)
    {
        this.configuration = configuration;
    }

    public boolean verifyPassword(String password)
    {
        if(configuration.getPassword().equals(password))
            return true;
        else
            return false;
    }

    public void start()
    {
        Endpoint.publish(UrlGenerator.generateFacilitatorPublishUrl(configuration.getListenerPort()),this);
        System.out.println("Facilitator Console Started " +UrlGenerator.generateFacilitatorPublishUrl(configuration.getListenerPort()));
    }

    FacilitatorController.Configuration getConfiguration()
    {
        return configuration;
    }

    @Override
    public String getFacilitatorName() {
        return configuration.getName();
    }

    @Override
    public String requestConnection(String presenterName, String passKey) throws InvalidFacilitatorPasskeyException {
        if(verifyPassword(passKey))
        {
            String crId = PasswordUtil.generateConnectionRequestId();
            ConnectionRequestState connectionRequestState = new ConnectionRequestState();
            connectionRequestState.setPending(true);
            connectionRequestState.setSuccessful(false);
            connectionRequestState.setPresenterConsoleId(null);
            connectionRequestState.setConnectionRequestId(crId);
            connectionRequestState.setPresenterName(presenterName);
            connectionRequests.put(crId,connectionRequestState);

            final ConnectionRequest request = new ConnectionRequest(connectionRequestState,this);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if(getConnectionListener() != null)
                        getConnectionListener().onConnectionRequested(request);
                }
            });

            return crId;
        }
        else throw new InvalidFacilitatorPasskeyException();
    }


    void disconnectPresenter(PresenterConsoleImpl console)
    {
        console.stop();
        presenterConsoles.remove(console.getConsoleId());
    }


    public void rejectConnectionRequest(String connectionRequestId)
    {
        ConnectionRequestState connectionRequest = connectionRequests.get(connectionRequestId);
        connectionRequest.setSuccessful(false);
        connectionRequest.setPending(false);
    }
    public void acceptConnectionRequest(String connectionRequestId) {

        ConnectionRequestState connectionRequest = connectionRequests.get(connectionRequestId);

        PresenterConsoleImpl presenterConsole = new PresenterConsoleImpl(this,connectionRequest.getPresenterName());
        presenterConsoles.put(presenterConsole.getConsoleId(),presenterConsole);
        presenterConsole.start();
        connectionRequest.setPresenterConsoleId(presenterConsole.getConsoleId());
        connectionRequest.setSuccessful(true);
        connectionRequest.setPending(false);

    }

}
