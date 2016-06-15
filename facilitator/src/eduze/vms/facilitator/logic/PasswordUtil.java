package eduze.vms.facilitator.logic;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Utility Library for Password Hashing and Secret Key Generation
 */
public class PasswordUtil {
    //Random number generator
    private static Random ran = new Random();

    /**
     * Generate Connection Request Id
     * @return
     */
    public static String generateConnectionRequestId()
    {
        return "cr_"+String.valueOf(ran.nextInt());
    }

    /**
     * Generate Presenter Console Id
     * @return
     */
    public static String generatePresenterConsoleId(){return "pc_" + String.valueOf(ran.nextInt());}




    /**
     * Generate a Screen Share Console Id
     * @return
     */
    public static String generateScreenShareConsoleId()
    {
        return String.valueOf(ran.nextInt());
    }

}
