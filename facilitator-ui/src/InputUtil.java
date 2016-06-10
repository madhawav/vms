/**
 * Created by Admin on 6/10/2016.
 */
public class InputUtil {
    private InputUtil()
    {}
    public static boolean validateFacilitatorName(String name)
    {
        if(name != null && name.matches("^[a-zA-Z_0-9]+"))
        {
            return true;
        }
        else return false;
    }
    public static boolean validatePort(String port)
    {
        if(port != null && port.matches("^[0-9]+"))
        {
            int p = Integer.parseInt(port);
            if(p <= 1024 || p > 65535)
                return false;
            return true;
        }
        return false;
    }

    public static boolean validateSharedTaskTitle(String title)
    {
        if(title == null)
            return false;
        if(title.trim().length() == 0)
            return false;
        return true;
    }
    public static boolean validateSharedTaskDescription(String description)
    {
        if(description == null)
            return false;
        return true;
    }
}
