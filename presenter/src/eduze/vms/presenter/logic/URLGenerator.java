package eduze.vms.presenter.logic;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * Utility class for generation of URLS used in system
 */
public class URLGenerator extends eduze.vms.foundation.logic.URLGenerator {
    private URLGenerator()
    {

    }

    /**
     * Generate Facilitator Access URL
     * @param facilitatorURL
     * @return
     */
    public static String generateFacilitatorAccess(String facilitatorURL)
    {
        return facilitatorURL + "/facilitator";
    }

    /**
     * Extract useful portion from URL that should be passed to other functions
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
     * Generate Presenter Console Access URL
     * @param facilitatorURL
     * @param consoleId
     * @return
     */
    public static String generatePresenterConsoleAccessUrl(String facilitatorURL,String consoleId)
    {
        return facilitatorURL + "/presenter/" + consoleId;
    }



}
