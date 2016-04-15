package eduze.vms.foundation.logic;
import java.net.URI;
import java.net.URISyntaxException;
/**
 * Created by Fujitsu on 4/15/2016.
 */

public class URLGenerator {

    public static String generateScreenShareFrameBufferPublishUrl(int port, String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/screen-share/"+ String.valueOf(consoleId) +"/buffer";
    }
    public static String generateScreenShareConsolePublishUrl(int port, String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/screen-share/"+ String.valueOf(consoleId);
    }

    public static String generateAudioRelayFrameBufferPublishUrl(int port, String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/audio-relay/"+ String.valueOf(consoleId) +"/buffer";
    }
    public static String generateAudioRelayConsolePublishUrl(int port, String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/audio-relay/"+ String.valueOf(consoleId);
    }

    public static String generateAudioRelayConsoleAccessUrl(String serverUrl, String consoleId)
    {
        return serverUrl + "/audio-relay/"+ String.valueOf(consoleId);
    }
    public static String generateAudioRelayFrameBufferAccessUrl(String serverUrl, String consoleId)
    {
        return serverUrl + "/audio-relay/"+ String.valueOf(consoleId) + "/buffer";
    }
}

