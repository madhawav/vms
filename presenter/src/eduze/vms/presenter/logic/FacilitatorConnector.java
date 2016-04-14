package eduze.vms.presenter.logic;

import eduze.vms.presenter.logic.mpi.facilitator.ConnectionRequestState;
import eduze.vms.presenter.logic.mpi.facilitator.Facilitator;
import eduze.vms.presenter.logic.mpi.facilitator.FacilitatorImplServiceLocator;
import eduze.vms.presenter.logic.mpi.facilitator.InvalidFacilitatorPasskeyException;

import javax.swing.*;
import javax.xml.rpc.ServiceException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by Madhawa on 14/04/2016.
 */
public class FacilitatorConnector {
    private final Configuration configuration;
    private Facilitator facilitator = null;

    private String connectionRequestId = null;

    private String presenterConsoleId = null;

    private PresenterController presenterController = null;

    private  FacilitatorConnector(Configuration configuration)
    {
        this.configuration = configuration;
        this.configuration.setFacilitatorURL(URLGenerator.extractURL(this.configuration.getFacilitatorURL()));
    }

    private ConnectionRequestStateListener connectionRequestStateListener = null;
    private int connectionRequestUpdateInterval = 1000;
    private Thread connectionRequestStateNotifier = null;

    private void setupConnectionRequestStateNotifier()
    {
        connectionRequestStateNotifier = new Thread(new Runnable() {
        @Override
        public void run() {

            while(getConnectionRequestStateListener() != null)
            {
                final ConnectionRequestStateListener listener = getConnectionRequestStateListener();
                if(listener == null)
                    break;
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ConnectionRequestState state = FacilitatorConnector.this.checkConnectionRequestState();
                                if(state == ConnectionRequestState.Success)
                                {
                                    listener.onSuccess(FacilitatorConnector.this);
                                    setConnectionRequestStateListener(null);
                                }
                                else if(state == ConnectionRequestState.Failed)
                                {
                                    listener.onFailed(FacilitatorConnector.this);
                                    setConnectionRequestStateListener(null);
                                }

                            } catch (FacilitatorConnectionException e) {
                                e.printStackTrace();
                                if(!listener.onException(FacilitatorConnector.this, e))
                                {
                                    setConnectionRequestStateListener(null);
                                }
                            }
                        }
                    });
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!listener.onException(FacilitatorConnector.this, e))
                            {
                                setConnectionRequestStateListener(null);
                            }
                        }
                    });

                } catch (final InvocationTargetException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!listener.onException(FacilitatorConnector.this, e))
                            {
                                setConnectionRequestStateListener(null);
                            }
                        }
                    });
                }
                try {
                    Thread.currentThread().sleep(getConnectionRequestUpdateInterval());
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!listener.onException(FacilitatorConnector.this, e))
                            {
                                setConnectionRequestStateListener(null);
                            }
                        }
                    });

                }
            }
        }
    });
    }

    private void start() throws MalformedURLException, FacilitatorConnectionException, InvalidFacilitatorPasskeyException {
        try {
            FacilitatorImplServiceLocator facilitatorImplServiceLocator = new FacilitatorImplServiceLocator();
            facilitator = facilitatorImplServiceLocator.getFacilitatorImplPort(new URL(URLGenerator.generateFacilitatorAccess(configuration.getFacilitatorURL())));

            connectionRequestId = facilitator.requestConnection(configuration.getPresenterName(),configuration.getFacilitatorPasskey());


        } catch (ServiceException e) {
            throw new FacilitatorConnectionException(e);
        } catch (InvalidFacilitatorPasskeyException e) {
            throw e;
        } catch (RemoteException e) {
            throw new FacilitatorConnectionException(e);
        }
    }

    /**
     * Call this if checkConnectionRequestState returns success. Return the handle to manage presenter system with connected facilitator.
     * @return Handle to manage presenter session
     */
    public PresenterController obtainController() throws FacilitatorConnectionNotReadyException, FacilitatorConnectionException, MalformedURLException {
        if(presenterController != null)
            return presenterController;
        if(checkConnectionRequestState() == ConnectionRequestState.Success)
        {
            PresenterController controller = new PresenterController(this,facilitator,presenterConsoleId);
            controller.start();
            this.presenterController = controller;
            return controller;
        }
        else
        {
            throw new FacilitatorConnectionNotReadyException();
        }

    }

    public static FacilitatorConnector connect(Configuration configuration) throws FacilitatorConnectionException, InvalidFacilitatorPasskeyException, MalformedURLException {
        FacilitatorConnector connector = new FacilitatorConnector(configuration);
        connector.start();
        return  connector;
    }

    public static String getFacilitatorName(String facilitatorURL) throws MalformedURLException, FacilitatorConnectionException {

        try {
            FacilitatorImplServiceLocator facilitatorImplServiceLocator = new FacilitatorImplServiceLocator();
            Facilitator facilitator = facilitatorImplServiceLocator.getFacilitatorImplPort(new URL(facilitatorURL));
            return facilitator.getFacilitatorName();
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new FacilitatorConnectionException(e);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new FacilitatorConnectionException(e);
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getFacilitatorName() throws FacilitatorConnectionException {
        try {
            return facilitator.getFacilitatorName();
        } catch (RemoteException e) {
            throw new FacilitatorConnectionException(e);
        }
    }
    public ConnectionRequestState checkConnectionRequestState() throws FacilitatorConnectionException {
        try {
            eduze.vms.presenter.logic.mpi.facilitator.ConnectionRequestState state =  facilitator.checkConnectionRequestState(connectionRequestId);
            if(state.isPending())
                return ConnectionRequestState.Pending;
            else if(state.isSuccessful())
            {
                presenterConsoleId = state.getPresenterConsoleId();
                return ConnectionRequestState.Success;
            }

            else return ConnectionRequestState.Failed;
        } catch (RemoteException e) {
            throw new FacilitatorConnectionException(e);
        }
    }

    public synchronized ConnectionRequestStateListener getConnectionRequestStateListener() {
        return connectionRequestStateListener;
    }

    public synchronized void setConnectionRequestStateListener(ConnectionRequestStateListener connectionRequestStateListener) {
        this.connectionRequestStateListener = connectionRequestStateListener;
        if(connectionRequestStateListener != null)
        {
            if(connectionRequestStateNotifier == null || !connectionRequestStateNotifier.isAlive())
            {
                setupConnectionRequestStateNotifier();
                connectionRequestStateNotifier.start();
            }
        }
    }

    public synchronized int getConnectionRequestUpdateInterval() {
        return connectionRequestUpdateInterval;
    }

    public synchronized void setConnectionRequestUpdateInterval(int connectionRequestUpdateInterval) {
        this.connectionRequestUpdateInterval = connectionRequestUpdateInterval;
    }

    public enum ConnectionRequestState
    {
        Pending,Failed, Success
    }

    public interface ConnectionRequestStateListener
    {
        public void onSuccess(FacilitatorConnector sender);
        public void onFailed(FacilitatorConnector sender);

        /**
         *
         * @param sender
         * @param e
         * @return True if listener should continue.
         */
        public boolean onException(FacilitatorConnector sender, Exception e);


    }

    public static class Configuration
    {
        private String facilitatorURL = "http://locallhost:7000";
        private String presenterName = "Presenter";
        private String facilitatorPasskey = "password";

        public String getFacilitatorURL() {
            return facilitatorURL;
        }

        public String getPresenterName() {
            return presenterName;
        }

        public void setFacilitatorURL(String facilitatorURL) {
            this.facilitatorURL = facilitatorURL;
        }

        public void setPresenterName(String presenterName) {
            this.presenterName = presenterName;
        }

        public String getFacilitatorPasskey() {
            return facilitatorPasskey;
        }

        public void setFacilitatorPasskey(String facilitatorPasskey) {
            this.facilitatorPasskey = facilitatorPasskey;
        }
    }
}
