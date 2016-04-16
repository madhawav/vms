package eduze.vms.presenter.logic;

import eduze.livestream.AudioCapturer;
import eduze.livestream.ScreenCapturer;
import eduze.livestream.exchange.client.FrameBufferImplServiceLocator;
import eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole;
import eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplServiceLocator;
import eduze.vms.presenter.logic.mpi.presenterconsole.AssignedTask;
import eduze.vms.presenter.logic.mpi.presenterconsole.PresenterConsole;
import eduze.vms.presenter.logic.mpi.screenshareconsole.ScreenShareConsole;
import eduze.vms.presenter.logic.mpi.screenshareconsole.ScreenShareConsoleImplServiceLocator;

import javax.swing.*;
import javax.xml.rpc.ServiceException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by Madhawa on 14/04/2016.
 */
public class ControlLoop extends Thread {
    private String facilitatorURL = null;
    private PresenterConsole presenterConsole = null;
    private ScreenShareConsole screenShareConsole = null;
    private AudioRelayConsole audioRelayConsole = null;
    private eduze.livestream.exchange.client.FrameBuffer screenShareBuffer = null;
    private eduze.livestream.exchange.client.FrameBuffer audioRelayBuffer = null;
    private ScreenCapturer screenCapturer = null;
    private AudioCapturer audioCapturer = null;
    private volatile int updateInterval = 1000;
    private volatile int screenCaptureInterval = 1000;

    private boolean running = false;

    private StateChangeListener stateChangeListener = null;

    private AssignedTask[] assignedTasks = null;

    private String screenShareConsoleId = null;
    private String audioRelayConsoleId = null;
    ControlLoop(PresenterConsole presenterConsole, String facilitatorURL) throws FacilitatorConnectionException, MalformedURLException {
        try {
            this.facilitatorURL = facilitatorURL;
            this.presenterConsole = presenterConsole;
            this.screenShareConsoleId = presenterConsole.getOutScreenShareConsoleId();
            this.audioRelayConsoleId = presenterConsole.getOutAudioRelayConsoleId();

            ScreenShareConsoleImplServiceLocator screenShareConsoleImplServiceLocator = new ScreenShareConsoleImplServiceLocator();
            screenShareConsole = screenShareConsoleImplServiceLocator.getScreenShareConsoleImplPort(new URL(URLGenerator.generateScreenShareConsoleAccessUrl(facilitatorURL,screenShareConsoleId)));

            AudioRelayConsoleImplServiceLocator audioRelayConsoleImplServiceLocator = new AudioRelayConsoleImplServiceLocator();
            audioRelayConsole = audioRelayConsoleImplServiceLocator.getAudioRelayConsoleImplPort(new URL(URLGenerator.generateAudioRelayConsoleAccessUrl(facilitatorURL,audioRelayConsoleId)));

            FrameBufferImplServiceLocator frameBufferImplServiceLocator = new FrameBufferImplServiceLocator();
            screenShareBuffer = frameBufferImplServiceLocator.getFrameBufferImplPort(new URL(URLGenerator.generateScreenShareConsoleBufferAccessUrl(facilitatorURL,screenShareConsoleId)));
            audioRelayBuffer = frameBufferImplServiceLocator.getFrameBufferImplPort(new URL(URLGenerator.generateAudioRelayFrameBufferAccessUrl(getFacilitatorURL(),audioRelayConsoleId)));

            screenCapturer = new ScreenCapturer(screenShareBuffer,screenCaptureInterval);
            audioCapturer = new AudioCapturer(audioRelayBuffer);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new FacilitatorConnectionException(e);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new FacilitatorConnectionException(e);
        }

    }

    public synchronized int getUpdateInterval() {
        return updateInterval;
    }

    public synchronized void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public synchronized String getFacilitatorURL() {
        return facilitatorURL;
    }

    @Override
    public void run()
    {
        while(isRunning())
        {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            assignedTasks = presenterConsole.getAssignedTasks();

                            if(screenShareConsole.isEnabled())
                            {
                                if(!screenCapturer.isCapturing()) {
                                    screenCapturer.startCapture();
                                    if(stateChangeListener != null)
                                        stateChangeListener.onScreenCaptureChanged(true);
                                }
                            }
                            else
                            {
                                if(screenCapturer.isCapturing()){
                                    screenCapturer.stopCapture();
                                    if(stateChangeListener != null)
                                        stateChangeListener.onScreenCaptureChanged(false);
                                }
                            }

                            if(audioRelayConsole.isEnabled())
                            {
                                if(!audioCapturer.isCapturing()) {
                                    audioCapturer.startCapture();
                                    if(stateChangeListener != null)
                                        stateChangeListener.onAudioCaptureChanged(true);
                                }
                            }
                            else
                            {
                                if(audioCapturer.isCapturing()){
                                    audioCapturer.stopCapture();
                                    if(stateChangeListener != null)
                                        stateChangeListener.onAudioCaptureChanged(false);
                                }
                            }
                            notifyControlLoopCycleCompleted();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            try {
                Thread.currentThread().sleep(getUpdateInterval());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void notifyControlLoopCycleCompleted() {
        if(stateChangeListener != null)
            stateChangeListener.onControlLoopCycleCompleted();
    }

    public synchronized boolean isRunning() {
        return running;
    }

    synchronized void setRunning(boolean value)
    {
        running = value;
    }

    @Override
    public void start()
    {
        setRunning(true);
        super.start();
    }

   public void stopRunning()
   {
       setRunning(false);
   }

    public synchronized int getScreenCaptureInterval() {
        return screenCaptureInterval;
    }

    public synchronized void setScreenCaptureInterval(int screenCaptureInterval) {
        this.screenCaptureInterval = screenCaptureInterval;
        this.screenCapturer.setCaptureInterval(screenCaptureInterval);
    }

    public synchronized boolean isScreenShared()
    {
        return screenCapturer.isCapturing();
    }

    public synchronized boolean isAudioShared()
    {
        return audioCapturer.isCapturing();
    }

    public synchronized StateChangeListener getStateChangeListener() {
        return stateChangeListener;
    }

    public synchronized void setStateChangeListener(StateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    public AssignedTask[] getAssignedTasks() {
        return assignedTasks;
    }


    /**
     * Created by Madhawa on 14/04/2016.
     */
    public static interface StateChangeListener {
        public void onScreenCaptureChanged(boolean newValue);

        public void onAudioCaptureChanged(boolean newValue);
        public void onControlLoopCycleCompleted();
    }
}
