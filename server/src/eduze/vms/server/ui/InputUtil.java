package eduze.vms.server.ui;
public class InputUtil {
    private InputUtil()
    {}
    public static boolean validateServerName(String name)
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

}
