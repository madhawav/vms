package eduze.vms.foundation.logic.webservices;

import eduze.livestream.exchange.server.FrameBufferImpl;
import eduze.vms.foundation.logic.PasswordUtil;
import eduze.vms.foundation.logic.URLGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by Fujitsu on 4/15/2016.
 */
@WebService(endpointInterface = "eduze.vms.foundation.logic.webservices.AudioRelayConsole")
public class AudioRelayConsoleImpl implements AudioRelayConsole {
    private boolean enabled = false;
    public String consoleId = "";
    private int port = 0;

    private FrameBufferImpl frameBuffer = null;
    public AudioRelayConsoleImpl()
    {

    }

    public AudioRelayConsoleImpl(int port, int bufferSize)
    {
        this.port = port;
        consoleId = PasswordUtil.generateAudioRelayConsoleId();
        frameBuffer = new FrameBufferImpl(bufferSize);
    }

    public void start()
    {
        System.out.println("Audio RelayConsole Started " + URLGenerator.generateAudioRelayConsolePublishUrl(port,consoleId));
        System.out.println("Audio Relay Frame Buffer Started " + URLGenerator.generateAudioRelayFrameBufferPublishUrl(port,consoleId));
        Endpoint.publish(URLGenerator.generateAudioRelayFrameBufferPublishUrl(port,consoleId),frameBuffer);
        Endpoint.publish(URLGenerator.generateAudioRelayConsolePublishUrl(port,consoleId),this);
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
