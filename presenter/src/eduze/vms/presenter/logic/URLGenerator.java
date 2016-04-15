package eduze.vms.presenter.logic;

/**
 * Created by Madhawa on 14/04/2016.
 */
public class URLGenerator extends eduze.vms.foundation.logic.URLGenerator {
    private URLGenerator()
    {

    }

    public static String generateFacilitatorAccess(String facilitatorURL)
    {
        return facilitatorURL + "/facilitator";
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

    public static String generatePresenterConsoleAccessUrl(String facilitatorURL,String consoleId)
    {
        return facilitatorURL + "/presenter/" + consoleId;
    }

    public static String generateScreenShareConsoleAccessUrl(String serverUrl, String consoleId)
    {
        return serverUrl + "/screen-share/"+ String.valueOf(consoleId);
    }
    public static String generateScreenShareConsoleBufferAccessUrl(String serverUrl, String consoleId)
    {
        return serverUrl + "/screen-share/"+ String.valueOf(consoleId) + "/buffer";
    }
}
