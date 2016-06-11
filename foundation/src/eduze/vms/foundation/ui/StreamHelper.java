package eduze.vms.foundation.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Admin on 6/2/2016.
 */
public class StreamHelper {
    private StreamHelper() {}

    public static void safelyClose(InputStream i)
    {
        if(i!= null)
        {
            try {
                i.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void safelyClose(OutputStream o)
    {
        if(o!= null)
        {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
