package eduze.vms.facilitator.logic;

import java.util.Random;

/**
 * Created by Madhawa on 13/04/2016.
 */
public class PasswordUtil {
    private static Random ran = new Random();
    public static String generateConnectionRequestId()
    {
        return "cr_"+String.valueOf(ran.nextInt());
    }
    public static String generatePresenterConsoleId(){return "pc_" + String.valueOf(ran.nextInt());}

    public static String hashPasskey(char[] password)
    {
        return String.valueOf(password);
    }
}
