package eduze.vms.presenter.logic;

/**
 * Created by Madhawa on 14/04/2016.
 */
public class URLGenerator {
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
}
