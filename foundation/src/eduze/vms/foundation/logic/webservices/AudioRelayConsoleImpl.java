package eduze.vms.foundation.logic.webservices;

import eduze.livestream.exchange.server.FrameBufferImpl;
import eduze.vms.foundation.logic.PasswordUtil;
import eduze.vms.foundation.logic.URLGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Fujitsu on 4/15/2016.
 */

/**
 * Concrete Implementation of Audio Relay Console Web Service
 */
@WebService(endpointInterface = "eduze.vms.foundation.logic.webservices.AudioRelayConsole")
public class AudioRelayConsoleImpl implements AudioRelayConsole {
    //Internal Parameters
    private boolean enabled = false;
    public String consoleId = "";
    private int port = 0;

    //Communication Buffer
    private FrameBufferImpl frameBuffer = null;
    public AudioRelayConsoleImpl()
    {

    }

    /**
     * Constructor to setup Audio Relay Console with Communication Buffer
     * @param port Host Port
     * @param bufferSize Communication Buffer Size
     */
    public AudioRelayConsoleImpl(int port, int bufferSize)
    {
        this.port = port;
        consoleId = PasswordUtil.generateAudioRelayConsoleId();
        frameBuffer = new FrameBufferImpl(bufferSize);
    }

    /**
     * Start the Audio Relay Console Web Service
     */
    public void start()
    {
        Logger.getLogger("DEBUG").log(Level.INFO, "Audio RelayConsole Started " + URLGenerator.generateAudioRelayConsolePublishUrl(port,consoleId));
        Logger.getLogger("DEBUG").log(Level.INFO, "Audio Relay Frame Buffer Started " + URLGenerator.generateAudioRelayFrameBufferPublishUrl(port,consoleId));
        //Publish Endpoints
        Endpoint.publish(URLGenerator.generateAudioRelayFrameBufferPublishUrl(port,consoleId),frameBuffer);
        Endpoint.publish(URLGenerator.generateAudioRelayConsolePublishUrl(port,consoleId),this);
    }


    /**
     * Retrieve whether channel is enabled
     * @return True if Channel is enabled. Otherwise return False
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Retrieve Audio Relay Console Id
     * @return
     */
    @Override
    public String getConsoleId() {
        return consoleId;
    }

    /**
     * Retrieve Frame Buffer used for communication
     * @return
     */
    public FrameBufferImpl getFrameBuffer()
    {
        return frameBuffer;
    }

    /**
     * Set Enabled State of Audio Channel
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
