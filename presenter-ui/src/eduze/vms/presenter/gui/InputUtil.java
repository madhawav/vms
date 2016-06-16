package eduze.vms.presenter.gui;

/**
 * Created by Admin on 6/10/2016.
 */
public class InputUtil {
    private InputUtil()
    {

    }
    public static boolean validatePresenterName(String name)
    {
        if(name != null && name.matches("^[a-zA-Z_0-9]+"))
        {
            return true;
        }
        else return false;
    }
}
