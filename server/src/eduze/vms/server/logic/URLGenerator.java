package eduze.vms.server.logic;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class URLGenerator {
    public static String generateServerPublishURL(int port) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/server";
    }

    public static String generateFacilitatorManagerPublishURL(int port) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/facilitator-manager";
    }

    public static String generateVMSessionManagerPublishURL(int port) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/vm-session-manager";
    }

    public static String generateVMPublishURL(int port, String vmId) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/vm/" + vmId;
    }


    public static String generateFacilitatorConsolePublishURL(int port, String consoleId) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/facilitator/" + consoleId;
    }

    public static String generateScreenShareFrameBufferPublishUrl(int port, String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/screen-share/"+ String.valueOf(consoleId) +"/buffer";
    }
    public static String generateScreenShareConsolePublishUrl(int port, String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/screen-share/"+ String.valueOf(consoleId);
    }
}

