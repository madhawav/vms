/**
 * Created by Madhawa on 12/04/2016.
 */
package eduze.vms;

import java.util.Random;

public class PasswordUtil {
    private static Random ran = new Random();
    private PasswordUtil()
    {

    }
    // TODO: Write a proper hashPassword function here
    public static String hashPassword(char[] password)
    {
        return String.valueOf(password);
    }
    //this should include a random element
    public static String hashPairKey(String key)
    {

        return String.valueOf(key)+"-" + String.valueOf(ran.nextInt());
    }

    public static String generateVMId()
    {
        return "vm_" + String.valueOf(ran.nextInt());
    }

    public static String generateConsoleId()
    {
        return "con_" + String.valueOf(ran.nextInt());
    }

    public static String generateShareConsoleId()
    {
        return String.valueOf(ran.nextInt());
    }


}
