package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.*;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsole;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsoleImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeeting;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingImplServiceLocator;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.swing.*;
import javax.xml.rpc.ServiceException;
import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Main WebService to Presenters. Central Point of Control.
 */
@WebService(endpointInterface = "eduze.vms.facilitator.logic.webservices.Facilitator")
public class FacilitatorImpl implements Facilitator  {
    private FacilitatorController.Configuration configuration = null;
    private HashMap<String,ConnectionRequestState> connectionRequests = new HashMap<>();
    private PresenterConnectionListener presenterConnectionListener = null;
    private PresenterModifiedListener presenterModifiedListener = null;
    private HashMap<String,PresenterConsoleImpl> presenterConsoles = new HashMap<>();

    private String serverURL = null;
    private String facilitatorConsoleId = null;
    private String virtualMeetingId = null;

    private FacilitatorConsole facilitatorConsole = null;
    private VirtualMeeting virtualMeeting = null;

    public void establishServerConnection(String serverURL, String facilitatorConsoleId, String virtualMeetingId) throws MalformedURLException, ServerConnectionException {
        VirtualMeetingImplServiceLocator vmLocator = new VirtualMeetingImplServiceLocator();
        FacilitatorConsoleImplServiceLocator facilitatorLocator = new FacilitatorConsoleImplServiceLocator();

        this.facilitatorConsoleId = facilitatorConsoleId;
        this.virtualMeetingId = virtualMeetingId;
        try{
            virtualMeeting = vmLocator.getVirtualMeetingImplPort(new URL(UrlGenerator.generateVMAccessUrl(serverURL,virtualMeetingId)));
            facilitatorConsole = facilitatorLocator.getFacilitatorConsoleImplPort(new URL(UrlGenerator.generateFacilitatorConsoleAccessUrl(serverURL,facilitatorConsoleId)));
        }
        catch (ServiceException e)
        {
            throw new ServerConnectionException(e);
        }

    }

    public FacilitatorImpl()
    {

    }

    public Collection<PresenterConsoleImpl> getPresenterConsoles()
    {
        return presenterConsoles.values();
    }

    @WebMethod(exclude = true)
    public PresenterConsoleImpl getPresenterConsole(String consoleId) {
        return presenterConsoles.get(consoleId);
    }

    @Override
    public ConnectionRequestState checkConnectionRequestState(String connectionRequestId) {
        return connectionRequests.get(connectionRequestId);
    }

    public PresenterConnectionListener getPresenterConnectionListener() {
        return presenterConnectionListener;
    }

    public void setPresenterConnectionListener(PresenterConnectionListener presenterConnectionListener) {
        this.presenterConnectionListener = presenterConnectionListener;
    }

    public FacilitatorImpl(FacilitatorController.Configuration configuration)
    {
        this.configuration = configuration;
    }

    boolean verifyPassword(String password)
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
                    if(getPresenterConnectionListener() != null)
                        getPresenterConnectionListener().onConnectionRequested(request);
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


    public void rejectConnectionRequest(String connectionRequestId) throws RequestAlreadyProcessedException {
        ConnectionRequestState connectionRequest = connectionRequests.get(connectionRequestId);
        if(connectionRequest.isPending())
        {
            connectionRequest.setSuccessful(false);
            connectionRequest.setPending(false);
        }
        else
        {
            throw new RequestAlreadyProcessedException();
        }

    }
    public void acceptConnectionRequest(String connectionRequestId) throws RequestAlreadyProcessedException {

        ConnectionRequestState connectionRequest = connectionRequests.get(connectionRequestId);

        if(connectionRequest.isPending())
        {
            PresenterConsoleImpl presenterConsole = new PresenterConsoleImpl(this,connectionRequest.getPresenterName());
            presenterConsole.setConnectionRequestId(connectionRequest.getConnectionRequestId());
            presenterConsoles.put(presenterConsole.getConsoleId(),presenterConsole);
            presenterConsole.start();
            connectionRequest.setPresenterConsoleId(presenterConsole.getConsoleId());
            connectionRequest.setSuccessful(true);
            connectionRequest.setPending(false);
        }
        else
            throw new RequestAlreadyProcessedException();


    }


    public PresenterModifiedListener getPresenterModifiedListener() {
        return presenterModifiedListener;
    }

    public void setPresenterModifiedListener(PresenterModifiedListener presenterModifiedListener) {
        this.presenterModifiedListener = presenterModifiedListener;
    }

    public VirtualMeeting getVirtualMeeting() {
        return virtualMeeting;
    }

    public FacilitatorConsole getFacilitatorConsole() {
        return facilitatorConsole;
    }

    public String getFacilitatorConsoleId() {
        return facilitatorConsoleId;
    }

    public String getVirtualMeetingId() {
        return virtualMeetingId;
    }



}