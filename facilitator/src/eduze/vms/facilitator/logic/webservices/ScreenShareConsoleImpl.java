package eduze.vms.facilitator.logic.webservices;

import eduze.livestream.exchange.server.FrameBufferImpl;
import eduze.vms.facilitator.logic.PasswordUtil;
import eduze.vms.facilitator.logic.UrlGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by Madhawa on 13/04/2016.
 */
@WebService(endpointInterface = "eduze.vms.facilitator.logic.webservices.ScreenShareConsole")
public class ScreenShareConsoleImpl implements ScreenShareConsole {
    private boolean enabled = false;
    private int updateInterval = 1000;
    public String consoleId = "";
    private int port = 0;

    private FrameBufferImpl frameBuffer = null;
    public ScreenShareConsoleImpl()
    {

    }

    public ScreenShareConsoleImpl(int port, int bufferSize)
    {
        this.port = port;
        consoleId = PasswordUtil.generateScreenShareConsoleId();
        frameBuffer = new FrameBufferImpl(bufferSize);
    }

    public void start()
    {
        System.out.println("Screen Share Console Started " + UrlGenerator.generateScreenShareConsolePublishUrl(port,consoleId));
        System.out.println("Screen Share Frame Buffer Started " + UrlGenerator.generateScreenShareFrameBufferPublishUrl(port,consoleId));
        Endpoint.publish(UrlGenerator.generateScreenShareFrameBufferPublishUrl(port,consoleId),frameBuffer);
        Endpoint.publish(UrlGenerator.generateScreenShareConsolePublishUrl(port,consoleId),this);
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public void setUpdateInterval(int interval) {
        this.updateInterval = interval;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getConsoleId() {
        return consoleId;
    }

    public FrameBufferImpl getFrameBuffer()
    {
        return frameBuffer;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}