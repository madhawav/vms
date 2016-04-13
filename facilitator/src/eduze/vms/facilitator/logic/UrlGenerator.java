package eduze.vms.facilitator.logic;

/**
 * Created by Madhawa on 13/04/2016.
 */
public class UrlGenerator {
    private UrlGenerator(){}

    public static String generateFacilitatorPublishUrl(int port)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/facilitator";
    }

    public static String generatePresenterConsolePublishUrl(int port,String consoleId)
    {
        return "http://0.0.0.0:" + String.valueOf(port) + "/presenter/" + consoleId;
    }

    public static String extractURL(String url)
    {
        url = url.trim();
        while(url.endsWith("/")|| url.endsWith("\\"))
        {
           url = url.substring(0,url.length()-1);
        }
        return url;
    }

    public static String generateServerAccessURL(String serverURL)
    {
        return serverURL + "/server";
    }

    public static String generateFacilitatorManagerAccessURL(String serverURL)
    {
        return serverURL + "/facilitator-manager";
    }

    public static String generateVMSessionManagerAccessURL(String serverURL)
    {
        return serverURL + "/vm-session-manager";
    }

    public static String generateVMAccessUrl(String serverUrl, String vmId)
    {
        return serverUrl + "/vm/"+vmId;
    }

    public static String generateFacilitatorConsoleAccessUrl(String serverUrl, String consoleId)
    {
        return serverUrl + "/facilitator/"+consoleId;
    }
}
