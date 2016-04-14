package eduze.vms.presenter.logic;

import eduze.vms.presenter.logic.mpi.facilitator.Facilitator;
import eduze.vms.presenter.logic.mpi.facilitator.FacilitatorImplServiceLocator;
import eduze.vms.presenter.logic.mpi.presenterconsole.PresenterConsole;
import eduze.vms.presenter.logic.mpi.presenterconsole.PresenterConsoleImplServiceLocator;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Madhawa on 14/04/2016.
 */
public class PresenterController {
    private final String presenterConsoleId;
    // Configuration configuration;
    Facilitator facilitator = null;
    PresenterConsole presenterConsole = null;

    FacilitatorConnector connector = null;

    PresenterController(FacilitatorConnector facilitatorConnector, Facilitator facilitator, String presenterConsoleId) {
        this.connector = facilitatorConnector;
        this.facilitator = facilitator;
        this.presenterConsoleId = presenterConsoleId;
    }

    public FacilitatorConnector.Configuration getConfiguration()
    {
        return connector.getConfiguration();
    }

    public void start() throws MalformedURLException, FacilitatorConnectionException {
        PresenterConsoleImplServiceLocator presenterConsoleImplServiceLocator = new PresenterConsoleImplServiceLocator();
        try {
            presenterConsole = presenterConsoleImplServiceLocator.getPresenterConsoleImplPort(new URL(URLGenerator.generatePresenterConsoleAccessUrl(getConfiguration().getFacilitatorURL(),presenterConsoleId)));
        } catch (ServiceException e) {
            throw new FacilitatorConnectionException(e);
        }
    }
}
