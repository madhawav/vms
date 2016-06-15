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
import java.util.ArrayList;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * Manage connection with Facilitator Subsystem
 */
public class FacilitatorConnector {
    //Startup Configuration
    private final Configuration configuration;

    //MPI from Facilitator
    private Facilitator facilitator = null;

    //UID of Connection request
    private String connectionRequestId = null;

    //Presenter Console ID provided by Facilitator
    private String presenterConsoleId = null;

    //Main access point to logic following connection establishment
    private PresenterController presenterController = null;

    /**
     * Constructor
     * @param configuration Facilitator Details and Presenter Configuration
     */
    private  FacilitatorConnector(Configuration configuration)
    {
        this.configuration = configuration;
        this.configuration.setFacilitatorURL(URLGenerator.extractURL(this.configuration.getFacilitatorURL()));
    }

    private ConnectionRequestStateListener connectionRequestStateListener = null; //Listener to connection request state changes
    private int connectionRequestUpdateInterval = 1000; //Frequency to check for connection request state changes
    private Thread connectionRequestStateNotifier = null; //Notifier on connection request state change

    /**
     * Setup event system for connection request
     */
    private void setupConnectionRequestStateNotifier()
    {
        //Worker thread
        connectionRequestStateNotifier = new Thread(new Runnable() {
        @Override
        public void run() {
            //A notifier must be available
            while(getConnectionRequestStateListener() != null)
            {
                //Obtain listener
                final ConnectionRequestStateListener listener = getConnectionRequestStateListener();
                if(listener == null)
                    break;
                try {
                    //Switch to UI Thread
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //Check connection state
                                ConnectionRequestState state = FacilitatorConnector.this.checkConnectionRequestState();
                                //Dispatch events
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
                                //Report exception
                                if(!listener.onException(FacilitatorConnector.this, e))
                                {
                                    setConnectionRequestStateListener(null);
                                }
                            }
                        }
                    });
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                    //Report exception
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
                    //Report exception
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
                    //Delay
                    Thread.currentThread().sleep(getConnectionRequestUpdateInterval());
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!listener.onException(FacilitatorConnector.this, e))
                            {
                                //Disable event system
                                setConnectionRequestStateListener(null);
                            }
                        }
                    });

                }
            }
        }
    });
    }

    /**
     * Initiate Connector by raising a connection request
     * @throws MalformedURLException
     * @throws FacilitatorConnectionException
     * @throws InvalidFacilitatorPasskeyException
     */
    private void start() throws MalformedURLException, FacilitatorConnectionException, InvalidFacilitatorPasskeyException {
        try {
            //Obtain Facilitator MPI
            FacilitatorImplServiceLocator facilitatorImplServiceLocator = new FacilitatorImplServiceLocator();
            facilitator = facilitatorImplServiceLocator.getFacilitatorImplPort(new URL(URLGenerator.generateFacilitatorAccess(configuration.getFacilitatorURL())));

            //Obtain connection request id
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
     * Obtain Controller if checkConnectionRequestState returns success. Return the handle to manage presenter system with connected facilitator.
     * @return Handle to manage presenter session
     */
    public PresenterController obtainController() throws FacilitatorConnectionNotReadyException, FacilitatorConnectionException, MalformedURLException, FacilitatorDisconnectedException {
        //Check for availability of presenter controller (if already connected)
        if(presenterController != null)
            return presenterController;
        //Check for connection state
        if(checkConnectionRequestState() == ConnectionRequestState.Success)
        {
            //Establish controller and return instance
            PresenterController controller = new PresenterController(this,facilitator,presenterConsoleId);
            controller.start();
            this.presenterController = controller;
            return controller;
        }
        else
        {
            //Connection request has not been honoured
            throw new FacilitatorConnectionNotReadyException();
        }

    }

    /**
     * Connect to a Facilitator Subsystem. Entry point to Presenter Subsystem.
     * @param configuration Configuration of Presenter and Access Details for Facilitator
     * @return Connector used to check connection state
     * @throws FacilitatorConnectionException
     * @throws InvalidFacilitatorPasskeyException
     * @throws MalformedURLException
     */
    public static FacilitatorConnector connect(Configuration configuration) throws FacilitatorConnectionException, InvalidFacilitatorPasskeyException, MalformedURLException {
        FacilitatorConnector connector = new FacilitatorConnector(configuration);
        connector.start();
        return  connector;
    }

    /**
     * Retrieve name of facilitator using Facilitator URL
     * @param facilitatorURL URL to access Facilitator
     * @return
     * @throws MalformedURLException
     * @throws FacilitatorConnectionException
     */
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

    /**
     * Retrieve Configuration provided at setup
     * @return
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Retrieve name of Facilitator
     * @return
     * @throws FacilitatorConnectionException
     */
    public String getFacilitatorName() throws FacilitatorConnectionException {
        try {
            return facilitator.getFacilitatorName();
        } catch (RemoteException e) {
            throw new FacilitatorConnectionException(e);
        }
    }

    /**
     * Check status of connection request to Facilitator
     * @return
     * @throws FacilitatorConnectionException
     */
    public ConnectionRequestState checkConnectionRequestState() throws FacilitatorConnectionException {
        try {
            //Obtain Connection Request Sate
            eduze.vms.presenter.logic.mpi.facilitator.ConnectionRequestState state =  facilitator.checkConnectionRequestState(connectionRequestId);
            //Is it answered?
            if(state.isPending())
                return ConnectionRequestState.Pending;
            //Is it successful?
            else if(state.isSuccessful())
            {
                //Obtain connection parameters
                presenterConsoleId = state.getPresenterConsoleId();
                return ConnectionRequestState.Success;
            }
            //Connection failed
            else return ConnectionRequestState.Failed;
        } catch (RemoteException e) {
            throw new FacilitatorConnectionException(e);
        }
    }

    /**
     * Retrieve Listener to changes in connection request
     * @return
     */
    public synchronized ConnectionRequestStateListener getConnectionRequestStateListener() {
        return connectionRequestStateListener;
    }

    /**
     * Assign a listener to retireve changes in states of connection request
     * @param connectionRequestStateListener
     */
    public synchronized void setConnectionRequestStateListener(ConnectionRequestStateListener connectionRequestStateListener) {
        this.connectionRequestStateListener = connectionRequestStateListener;
        //Start the event system if not started already
        if(connectionRequestStateListener != null)
        {
            if(connectionRequestStateNotifier == null || !connectionRequestStateNotifier.isAlive())
            {
                setupConnectionRequestStateNotifier();
                connectionRequestStateNotifier.start();
            }
        }
    }

    /**
     * Retrieve Connection Request Event System Update Interval
     * @return
     */
    public synchronized int getConnectionRequestUpdateInterval() {
        return connectionRequestUpdateInterval;
    }

    /**
     * Set Connection Request Event System Update Interval
     * @param connectionRequestUpdateInterval
     */
    public synchronized void setConnectionRequestUpdateInterval(int connectionRequestUpdateInterval) {
        this.connectionRequestUpdateInterval = connectionRequestUpdateInterval;
    }

    /**
     * Connection Request State
     */
    public enum ConnectionRequestState
    {
        Pending,Failed, Success
    }

    /**
     * Connection Request State Listener
     */
    public interface ConnectionRequestStateListener
    {
        /**
         * Connection Successful
         * @param sender
         */
        public void onSuccess(FacilitatorConnector sender);

        /**
         * Connection Failed
         * @param sender
         */
        public void onFailed(FacilitatorConnector sender);

        /**
         * Exception occurred at connection request event system
         * @param sender
         * @param e
         * @return True if listener should continue.
         */
        public boolean onException(FacilitatorConnector sender, Exception e);



    }

    /**
     * Configuration of Presenter Subsystem
     */
    public static class Configuration
    {
        //URL of Facilitator Subsystem
        private String facilitatorURL = "http://locallhost:7000";
        //Presenter Name
        private String presenterName = "Presenter";
        //Facilitator Password
        private String facilitatorPasskey = "password";

        //Timeouts used for connection
        private int connectionPauseTimeout = 1000;
        private int connectionTerminateTimeout = 15000;

        /**
         * Return timeout used for connectivity pausing
         * @return
         */
        public int getConnectionPauseTimeout() {
            return connectionPauseTimeout;
        }

        /**
         * Return timeout for connection termination
         * @return
         */
        public int getConnectionTerminateTimeout() {
            return connectionTerminateTimeout;
        }

        /**
         * Set the timeout used to pause connection
         * @param connectionPauseTimeout
         */
        public void setConnectionPauseTimeout(int connectionPauseTimeout) {
            this.connectionPauseTimeout = connectionPauseTimeout;
        }

        /**
         * Set the timeout used to terminate connection
         * @param connectionTerminateTimeout
         */
        public void setConnectionTerminateTimeout(int connectionTerminateTimeout) {
            this.connectionTerminateTimeout = connectionTerminateTimeout;
        }

        /**
         * Retrieve Facilitator URL
         * @return
         */
        public String getFacilitatorURL() {
            return facilitatorURL;
        }

        /**
         * Retrieve Presenter Name
         * @return
         */
        public String getPresenterName() {
            return presenterName;
        }

        /**
         * Set Facilitator URL
         * @param facilitatorURL
         */
        public void setFacilitatorURL(String facilitatorURL) {
            this.facilitatorURL = facilitatorURL;
        }

        /**
         * Set Presenter Name
         * @param presenterName
         */
        public void setPresenterName(String presenterName) {
            this.presenterName = presenterName;
        }

        /**
         * Retrieve RAW Facilitator Passkey
         * @return
         */
        public String getFacilitatorPasskey() {
            return facilitatorPasskey;
        }

        /**
         * Set Raw Facilitator Passkey
         * @param rawFacilitatorPasskey
         */
        public void setFacilitatorPasskey(String rawFacilitatorPasskey) {
            this.facilitatorPasskey = facilitatorPasskey;
        }

        /**
         * Set Facilitator Passkey
         * @param facilitatorPasskey
         */
        public void setFacilitatorPasskey(char[] facilitatorPasskey) {
            this.facilitatorPasskey = eduze.vms.foundation.logic.PasswordUtil.hashFacilitatorPassword(facilitatorPasskey);
        }
    }
}
