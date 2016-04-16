package eduze.vms.server.logic;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Support class to generate URLs related to server
 */
public class URLGenerator extends eduze.vms.foundation.logic.URLGenerator {
    /**
     * Generate URL used to publish Server WebService
     * @param port Server Port
     * @return URL to publish Server WebService
     */
    public static String generateServerPublishURL(int port) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/server";
    }

    /**
     * Generate a URL to publish FacilitatorManager WebService
     * @param port Server port
     * @return URL to publish FacilitatorMangager WebService
     */
    public static String generateFacilitatorManagerPublishURL(int port) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/facilitator-manager";
    }

    /**
     * Generate URL to publish VMSessionManager WebService
     * @param port Server Port
     * @return URL to publish VMSessionManager WebService
     */
    public static String generateVMSessionManagerPublishURL(int port) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/vm-session-manager";
    }

    /**
     * Generate URL to publish a VirtualMeeting WebService
     * @param port Server Port
     * @param vmId Virtual Meeting Id
     * @return URL to publush VirtualMeeting WebService
     */
    public static String generateVMPublishURL(int port, String vmId) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/vm/" + vmId;
    }

    /**
     * Generate URL to publish FacilitatorConsole WebService
     * @param port ServerPort
     * @param consoleId Facilitator Console Id
     * @return URL to publish FacilitatorConsole WebService
     */
    public static String generateFacilitatorConsolePublishURL(int port, String consoleId) {
        return "http://0.0.0.0:" + String.valueOf(port) +"/facilitator/" + consoleId;
    }


}

