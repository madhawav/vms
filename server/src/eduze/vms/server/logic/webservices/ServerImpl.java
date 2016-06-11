package eduze.vms.server.logic.webservices;

import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.PairListener;
import eduze.vms.server.logic.URLGenerator;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.io.Serializable;
import java.net.URISyntaxException;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService(endpointInterface= "eduze.vms.server.logic.webservices.Server")
public class ServerImpl implements Server {

    private VMSessionManagerImpl vmSessionManager = null;
    private  Configuration config = null;
    private FacilitatorManagerImpl facilitatorManager = null;
    public ServerImpl()
    {
    }

    @WebMethod(exclude = true)
    public VMSessionManagerImpl getVmSessionManager() {
        return vmSessionManager;
    }

    public ServerImpl(Configuration startConfiguration)
    {
        instantiate(startConfiguration);
    }
    public void instantiate(Configuration startConfiguration)
    {
        config = startConfiguration;
        facilitatorManager = new FacilitatorManagerImpl(this);
        vmSessionManager = new VMSessionManagerImpl(this);
    }

    public void start() {
        System.out.println("Server Started " + URLGenerator.generateServerPublishURL(config.getPort()));
        Endpoint.publish(URLGenerator.generateServerPublishURL(config.getPort()),this);
        facilitatorManager.start();
        vmSessionManager.start();;
    }
    @Override
    public String getServerName() {
        return config.getName();
    }

    @Override
    public int getPort() {
        return config.getPort();
    }

    public FacilitatorManagerImpl getFacilitatorManager() {
        return facilitatorManager;
    }

    public boolean verifyPassword(String password)
    {
        if(config.getPassword().equals(password))
            return true;
        else
            return false;
    }


    private PairListener pairListener = null;

    public PairListener getPairListener() {
        return pairListener;
    }

    public void setPairListener(PairListener pairListener) {
        this.pairListener = pairListener;
    }

    private FacilitatorSessionListener facilitatorSessionListener = null;

    public FacilitatorSessionListener getFacilitatorSessionListener() {
        return facilitatorSessionListener;
    }

    public void setFacilitatorSessionListener(FacilitatorSessionListener facilitatorSessionListener) {
        this.facilitatorSessionListener = facilitatorSessionListener;
    }


    Configuration getConfiguration()
    {
        return config;
    }

    public static class Configuration implements Serializable
    {
        private String name = "ServerImpl";
        private int port = 8000;
        private String password = "pass";

        private int screenShareBufferSize = 2;
        private int audioRelayBufferSize = 5;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getAudioRelayBufferSize() {
            return audioRelayBufferSize;
        }

        public int getScreenShareBufferSize() {
            return screenShareBufferSize;
        }

        public void setAudioRelayBufferSize(int audioRelayBufferSize) {
            this.audioRelayBufferSize = audioRelayBufferSize;
        }

        public void setScreenShareBufferSize(int screenShareBufferSize) {
            this.screenShareBufferSize = screenShareBufferSize;
        }


    }
}
