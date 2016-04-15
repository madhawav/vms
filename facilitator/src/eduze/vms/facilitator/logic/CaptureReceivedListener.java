package eduze.vms.facilitator.logic;

import java.awt.image.BufferedImage;

/**
 * Created by Madhawa on 14/04/2016.
 */
public interface CaptureReceivedListener {
    public void onScreenCaptureReceived(byte[] rawData, BufferedImage image, String facilitatorConsoleId, String presenterConsoleId);
    public void onException(Exception e);

    public void onAudioDataReceived(byte[] rawData, String facilitatorConsoleId, String presenterConsoleId);
}
