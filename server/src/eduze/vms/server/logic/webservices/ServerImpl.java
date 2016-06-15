package eduze.vms.server.logic.webservices;

import eduze.vms.server.logic.ConnectivityManager;
import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.PairListener;
import eduze.vms.server.logic.URLGenerator;
import sun.rmi.runtime.Log;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * The Main Webservice providing basic information of Server to clients
 */
@WebService(endpointInterface= "eduze.vms.server.logic.webservices.Server")
public class ServerImpl implements Server {
    //Manager of VM Sessions
    private VMSessionManagerImpl vmSessionManager = null;
    private  Configuration config = null; //Configuration of Server
    private FacilitatorManagerImpl facilitatorManager = null; //Manager of Known Facilitators
    public ServerImpl()
    {
    }

    /**
     * Retrieve the Virtual Meeting Session Manager
     * @return
     */
    @WebMethod(exclude = true)
    public VMSessionManagerImpl getVmSessionManager() {
        return vmSessionManager;
    }

    /**
     * Constructor for Server WebService
     * @param startConfiguration Initial Configuration of Server
     */
    public ServerImpl(Configuration startConfiguration)
    {
        instantiate(startConfiguration);
    }
    public void instantiate(Configuration startConfiguration)
    {
        config = startConfiguration;
        //Setup Support WebServices
        facilitatorManager = new FacilitatorManagerImpl(this);
        vmSessionManager = new VMSessionManagerImpl(this);
    }

    /**
     * Start the Server WebService
     */
    public void start() {
        Logger.getLogger(this.getClass()).info("Server Started " + URLGenerator.generateServerPublishURL(config.getPort()));
        Endpoint.publish(URLGenerator.generateServerPublishURL(config.getPort()),this);
        //Start support services
        facilitatorManager.start();
        vmSessionManager.start();;
    }

    /**
     * Retrieve name of Server
     * @return Server Name
     */
    @Override
    public String getServerName() {
        return config.getName();
    }

    /**
     * Retrieve port of Server
     * @return Server Port
     */
    @Override
    public int getPort() {
        return config.getPort();
    }

    /**
     * Retrieve Facilitator Manager WebService
     * @return FacilitatorManager WebService
     */
    public FacilitatorManagerImpl getFacilitatorManager() {
        return facilitatorManager;
    }

    /**
     * Verify Server Password
     * @param password Server Password
     * @return True if successful. Otherwise return false
     */
    public boolean verifyPassword(String password)
    {
        if(config.getPassword().equals(password))
            return true;
        else
            return false;
    }

    //Listener for Pair Events
    private PairListener pairListener = null;

    /**
     * Retrieve listener for pair events
     * @return Pair Listener
     */
    public PairListener getPairListener() {
        return pairListener;
    }

    /**
     * Assign the listener for Pair Events
     * @param pairListener
     */
    public void setPairListener(PairListener pairListener) {
        this.pairListener = pairListener;
    }

    //Listener for Facilitator Connection Related Events
    private FacilitatorSessionListener facilitatorSessionListener = null;

    /**
     * Retrieve listener used to Facilitator Connection Sessions
     * @return Facilitator Session Listener
     */
    public FacilitatorSessionListener getFacilitatorSessionListener() {
        return facilitatorSessionListener;
    }

    /**
     * Assign a listener to be reported on facilitator connection events
     * @param facilitatorSessionListener Facilitator Session Listener to be reported on Facilitator connection events
     */
    public void setFacilitatorSessionListener(FacilitatorSessionListener facilitatorSessionListener) {
        this.facilitatorSessionListener = facilitatorSessionListener;
    }

    /**
     * Retrieve Configuration assigned to Server
     * @return Start Configuration of Server
     */
    Configuration getConfiguration()
    {
        return config;
    }

    /**
     * Server Configuration Class
     */
    public static class Configuration implements Serializable
    {
        //ServerName
        private String name = "Server";
        private int port = 8000; //Server Port
        private String password = "pass"; //Server Password

        //Timeout used for automatic disconnection of Facilitators
        private int FacilitatorConnectivityTimeoutInterval = 10000;

        /**
         * Retrieve the timeout used for automatic disconnection of inactive facilitators
         * @return Timeout used for automatic disconnection of inactive facilitators
         */
        public int getFacilitatorConnectivityTimeoutInterval() {
            return FacilitatorConnectivityTimeoutInterval;
        }

        /**
         * Set the timeout used for automatic disconnection of inactive facilitators
         * @param facilitatorConnectivityTimeoutInterval Timeout used for automatic disconnection of inactive facilitators
         */
        public void setFacilitatorConnectivityTimeoutInterval(int facilitatorConnectivityTimeoutInterval) {
            FacilitatorConnectivityTimeoutInterval = facilitatorConnectivityTimeoutInterval;
        }

        //Buffer Sizes used for Streaming
        private int screenShareBufferSize = 2;
        private int audioRelayBufferSize = 5;

        /**
         * Retrieve Server Name
         * @return Server Name
         */
        public String getName() {
            return name;
        }

        /**
         * Set the Server Name
         * @param name Server Name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Retrieve Server Port
         * @return Server Port
         */
        public int getPort() {
            return port;
        }

        /**
         * Set the Server Port
         * @param port Server Port
         */
        public void setPort(int port) {
            this.port = port;
        }

        /**
         * Retrieve the Server Password
         * @return Server Password
         */
        public String getPassword() {
            return password;
        }

        /**
         * Set the Server Password
         * @param password Server Passwrod
         */
        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * Retrieve Audio Relay Buffer Size
         * @return Audio Relay Buffer Size
         */
        public int getAudioRelayBufferSize() {
            return audioRelayBufferSize;
        }

        /**
         * Retrieve Screen Share Buffer Size
         * @return Buffer Size used for Screen Share
         */
        public int getScreenShareBufferSize() {
            return screenShareBufferSize;
        }


        /**
         * Set the Audio Relay Buffer Size
         * @param audioRelayBufferSize Audio Relay Buffer Size
         */
        public void setAudioRelayBufferSize(int audioRelayBufferSize) {
            this.audioRelayBufferSize = audioRelayBufferSize;
        }

        /**
         * Set the screen share buffer size
         * @param screenShareBufferSize Screen share buffersi
         */
        public void setScreenShareBufferSize(int screenShareBufferSize) {
            this.screenShareBufferSize = screenShareBufferSize;
        }


    }
}
