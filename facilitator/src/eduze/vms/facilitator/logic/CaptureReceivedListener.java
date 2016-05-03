package eduze.vms.facilitator.logic;

import java.awt.image.BufferedImage;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * Listener to be notified on reception of screen shares and audio shares
 */
public interface CaptureReceivedListener {
    /**
     * A Screen Capture has been received
     * @param rawData RawData of Encoded Screen Share Image
     * @param image Image as a BufferedImage
     * @param facilitatorConsoleId FacilitatorConsoleID of sender
     * @param presenterConsoleId PresenterConsoleID of sender
     */
    public void onScreenCaptureReceived(byte[] rawData, BufferedImage image, String facilitatorConsoleId, String presenterConsoleId);

    /**
     * The Receiver has incurred an exception in background thread
     * @param e
     */
    public void onException(Exception e);

    /**
     * An audio frame has been received
     * @param rawData RawData of audio frame
     * @param facilitatorConsoleId FacilitatorConsoleID of Sender
     * @param presenterConsoleId PresenterConsoleID of sender
     */
    public void onAudioDataReceived(byte[] rawData, String facilitatorConsoleId, String presenterConsoleId);
}
