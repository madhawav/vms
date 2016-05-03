package eduze.vms.facilitator.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * UTility Functions
 */
public class Util {
    private Util()
    {

    }

    /**
     * Obtain Buffered Image from Raw byte stream
     * @param data
     * @return
     * @throws IOException
     */
    public static BufferedImage generateBufferedImage(byte[] data) throws IOException {
        ByteArrayInputStream stream = null;
        BufferedImage resultImage = null;
        try {
            stream = new ByteArrayInputStream(data);
            resultImage = ImageIO.read(stream);
            stream.close();
        } catch (IOException e) {
            throw e;
        }
        finally {
            if(stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                   throw e;
                }
        }
        return resultImage;

    }
}
