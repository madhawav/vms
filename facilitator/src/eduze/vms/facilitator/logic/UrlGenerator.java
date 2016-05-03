package eduze.vms.facilitator.logic;

import eduze.vms.foundation.logic.URLGenerator;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Helper Class to generate URLs used by Facilitator
 */
public class UrlGenerator extends URLGenerator {
    private UrlGenerator(){}

    /**
     * Generate URL used to publish Facilitator WebService
     * @param port
     * @return
     */
    public static String generateFacilitatorPublishUrl(int port)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/facilitator";
    }

    /**
     * Generate URL used to publish presenter console
     * @param port
     * @param consoleId
     * @return
     */
    public static String generatePresenterConsolePublishUrl(int port,String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/presenter/" + consoleId;
    }

    /**
     * Format URL to support helper functions
     * @param url
     * @return
     */
    public static String extractURL(String url)
    {
        url = url.trim();
        while(url.endsWith("/")|| url.endsWith("\\"))
        {
           url = url.substring(0,url.length()-1);
        }
        return url;
    }

    /**
     * Generate Server Access URL
     * @param serverURL
     * @return
     */
    public static String generateServerAccessURL(String serverURL)
    {
        return serverURL + "/server";
    }

    /**
     * Generate Facilitator Manager Access URL
     * @param serverURL
     * @return
     */
    public static String generateFacilitatorManagerAccessURL(String serverURL)
    {
        return serverURL + "/facilitator-manager";
    }

    /**
     * Generate VM Session Manager Access URL
     * @param serverURL
     * @return
     */
    public static String generateVMSessionManagerAccessURL(String serverURL)
    {
        return serverURL + "/vm-session-manager";
    }

    /**
     * Generate VirtualMeeting Access URL
     * @param serverUrl
     * @param vmId
     * @return
     */
    public static String generateVMAccessUrl(String serverUrl, String vmId)
    {
        return serverUrl + "/vm/"+vmId;
    }

    /**
     * Generate Facilitator Console Access URL
     * @param serverUrl
     * @param consoleId
     * @return
     */
    public static String generateFacilitatorConsoleAccessUrl(String serverUrl, String consoleId)
    {
        return serverUrl + "/facilitator/"+consoleId;
    }

    /**
     * Generate Screen Share Console Access URL
     * @param serverUrl
     * @param consoleId
     * @return
     */
    public static String generateScreenShareConsoleAccessUrl(String serverUrl, String consoleId)
    {
        return serverUrl + "/screen-share/"+ String.valueOf(consoleId);
    }

    /**
     * Generate Screen Share Console Buffer Access URL
     * @param serverUrl
     * @param consoleId
     * @return
     */
    public static String generateScreenShareConsoleBufferAccessUrl(String serverUrl, String consoleId)
    {
        return serverUrl + "/screen-share/"+ String.valueOf(consoleId) + "/buffer";
    }

    /**
     * Generate Screen Share Frame Buffer Publish URL
     * @param port
     * @param consoleId
     * @return
     */
    public static String generateScreenShareFrameBufferPublishUrl(int port, String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/screen-share/"+ String.valueOf(consoleId) +"/buffer";
    }

    /**
     * Generate Screen Share Console Publish URL
     * @param port
     * @param consoleId
     * @return
     */
    public static String generateScreenShareConsolePublishUrl(int port, String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/screen-share/"+ String.valueOf(consoleId);
    }

    /**
     * Generate Localhost URL from port
     * @param port
     * @return
     */
    public static String generateLocalURL(int port)
    {
        return "http://localhost:"+String.valueOf(port);
    }
}
