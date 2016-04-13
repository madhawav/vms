package eduze.vms.facilitator.logic.webservices;

import eduze.vms.facilitator.logic.PasswordUtil;
import eduze.vms.facilitator.logic.UrlGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by Madhawa on 13/04/2016.
 */
@WebService(endpointInterface = "eduze.vms.facilitator.logic.webservices.PresenterConsole")
public class PresenterConsoleImpl implements PresenterConsole {
    private FacilitatorImpl facilitator = null;
    private String name = null;
    private String consoleId = null;
    private boolean connectionAcknowledged = false;
    private Endpoint endpoint = null;
    public PresenterConsoleImpl()
    {

    }

    public PresenterConsoleImpl(FacilitatorImpl facilitator, String name)
    {
        this.facilitator = facilitator;
        this.name = name;
        consoleId = PasswordUtil.generatePresenterConsoleId();
    }

    public void start()
    {
        endpoint = Endpoint.publish(UrlGenerator.generatePresenterConsolePublishUrl(facilitator.getConfiguration().getListenerPort(),consoleId),this);
        System.out.println("Presenter Console Started " + UrlGenerator.generatePresenterConsolePublishUrl(facilitator.getConfiguration().getListenerPort(),consoleId));
    }

    void stop()
    {
        //endpoint.stop();
    }

    FacilitatorImpl getFacilitator()
    {
        return facilitator;
    }


    public boolean isConnectionAcknowledged()
    {
        return connectionAcknowledged;
    }

    @Override
    public void acknowledgeConnection() {
        connectionAcknowledged = true;
    }

    @Override
    public void disconnect() {
        getFacilitator().disconnectPresenter(this);
    }

    @Override
    public String getConsoleId() {
        return consoleId;
    }
}
