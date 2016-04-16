package eduze.vms.foundation.logic;
/**
 * Created by Fujitsu on 4/15/2016.
 */

import java.util.Random;

public class PasswordUtil {
    private static Random ran = new Random();
    private PasswordUtil()
    {

    }

    public static String generateScreenShareConsoleId()
    {
        return String.valueOf(ran.nextInt());
    }

    public static String generateAudioRelayConsoleId()
    {
        return String.valueOf(ran.nextInt());
    }

    public static String generateSharedTaskId()
    {
        return String.valueOf(ran.nextInt());
    }

}
