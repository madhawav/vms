package eduze.vms.foundation.logic.webservices;

import eduze.livestream.exchange.server.FrameBufferImpl;
import eduze.vms.foundation.logic.URLGenerator;
import eduze.vms.foundation.logic.PasswordUtil;
import org.apache.log4j.Logger;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Concrete Implementation of Screen Share Console Web Service
 */
@WebService(endpointInterface = "eduze.vms.foundation.logic.webservices.ScreenShareConsole")
public class ScreenShareConsoleImpl implements ScreenShareConsole {
    //Internal State Variables
    private boolean enabled = false;
    private int updateInterval = 1000;
    public String consoleId = "";
    private int port = 0;

    private Endpoint screenShareConsoleEndPoint = null;
    private Endpoint screenShareFrameBufferEndPoint = null;

    //Buffer used for communication
    private FrameBufferImpl frameBuffer = null;
    public ScreenShareConsoleImpl()
    {

    }

    /**
     * Constructor for Screen Share Console
     * @param port The port used to host WebService
     * @param bufferSize Size of Communication Buffer
     */
    public ScreenShareConsoleImpl(int port, int bufferSize)
    {
        this.port = port;
        consoleId = PasswordUtil.generateScreenShareConsoleId();
        frameBuffer = new FrameBufferImpl(bufferSize);
    }

    /**
     * Start the Screen Share Console Web Service
     */
    public void start()
    {
        Logger.getLogger(getClass()).info("Screen Share Console Started " + URLGenerator.generateScreenShareConsolePublishUrl(port,consoleId));
        Logger.getLogger(getClass()).info("Screen Share Frame Buffer Started " + URLGenerator.generateScreenShareFrameBufferPublishUrl(port,consoleId));
        screenShareFrameBufferEndPoint = Endpoint.publish(URLGenerator.generateScreenShareFrameBufferPublishUrl(port,consoleId),frameBuffer);
        screenShareConsoleEndPoint = Endpoint.publish(URLGenerator.generateScreenShareConsolePublishUrl(port,consoleId),this);
    }

    public void stop() {
        Logger.getLogger(getClass()).info("Screen Share Console Stopped " + URLGenerator.generateScreenShareConsolePublishUrl(port,consoleId));
        Logger.getLogger(getClass()).info("Screen Share Frame Buffer Stopped " + URLGenerator.generateScreenShareFrameBufferPublishUrl(port,consoleId));
        //screenShareFrameBufferEndPoint.stop();
        // screenShareConsoleEndPoint.stop();
    }

    /**
     * Retrieve update interval of channel
     * @return
     */
    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    /**
     * Set the update frequency of channel
     * @param interval
     */
    @Override
    public void setUpdateInterval(int interval) {
        this.updateInterval = interval;
    }

    /**
     * Retrieve whether channel is active
     * @return True if channel is active. Otherwise return False.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Return the console id
     * @return Console Id
     */
    @Override
    public String getConsoleId() {
        return consoleId;
    }

    /**
     * Retrieve Internal buffer used for communication
     * @return
     */
    public FrameBufferImpl getFrameBuffer()
    {
        return frameBuffer;
    }

    /**
     * Set whether Screen Share Console is enabled
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



}
