package eduze.vms.foundation.logic;
/**
 * Created by Fujitsu on 4/15/2016.
 */

import java.util.Random;

/**
 * Utility tool for hashing of passwords and generation of security keys
 */
public class PasswordUtil {
    private static Random ran = new Random(); //Random generator for key generation
    private PasswordUtil()
    {

    }

    /**
     * Generate Screen Share Console Id
     * @return Screen Share Console Id
     */
    public static String generateScreenShareConsoleId()
    {
        return String.valueOf(ran.nextInt());
    }

    /**
     * Generate Audio Relay Console Id
     * @return Audio Relay Console Id
     */
    public static String generateAudioRelayConsoleId()
    {
        return String.valueOf(ran.nextInt());
    }

    /**
     * Generate Shared Task Id
     * @return Shared Task Id
     */
    public static String generateSharedTaskId()
    {
        return String.valueOf(ran.nextInt());
    }

}
