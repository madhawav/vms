/**
 * Created by Madhawa on 12/04/2016.
 */
package eduze.vms.server.logic;

import java.util.Random;

/**
 * Utility class for hashing and generating secure keys
 */
public class PasswordUtil {
    //Random generator used to generate secure keys
    private static Random ran = new Random();
    private PasswordUtil()
    {

    }

    /**
     * Hash server password using irreversible hash function
     * @param password
     * @return
     */
    // TODO: Write a proper hashPassword function here
    public static String hashPassword(char[] password)
    {
        return String.valueOf(password);
    }

    /**
     * generate a pair-key for newly paired facilitator
     * @param key seed to generate pairKey (this is name of facilitator)
     * @return generated pair-key
     */
    public static String hashPairKey(String key)
    {
        return String.valueOf(key)+"-" + String.valueOf(ran.nextInt());
    }

    /**
     * Generate a virtual meeting id
     * @return virtual meeting id
     */
    public static String generateVMId()
    {
        return "vm_" + String.valueOf(ran.nextInt());
    }

    /**
     * Generate a facilitator console id (Facilitator id)
     * @return facilitator console id
     */
    public static String generateFacilitatorConsoleId()
    {
        return "con_" + String.valueOf(ran.nextInt());
    }



}
