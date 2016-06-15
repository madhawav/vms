package eduze.vms.foundation.logic;
/**
 * Created by Fujitsu on 4/15/2016.
 */

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private static String hashPassword(char[] password)
    {
        MessageDigest cript = null;
        try {
            cript = MessageDigest.getInstance("SHA-256");
            cript.reset();
            cript.update(new String(password).getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String passwordHash = new BigInteger(1, cript.digest()).toString(16);

        return passwordHash;
    }
    /**
     * Hash server password
     * @param password
     * @return
     */
    public static String hashServerPassword(char[] password)
    {
       return hashPassword(password);
    }

    /**
     * Hash Facilitator Password
     * @param password
     * @return
     */
    public static String hashFacilitatorPassword(char[] password) {
        return hashPassword(password);
    }
}
