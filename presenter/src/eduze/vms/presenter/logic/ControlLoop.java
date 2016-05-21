package eduze.vms.presenter.logic;

import eduze.livestream.AudioCapturer;
import eduze.livestream.ScreenCapturer;
import eduze.livestream.exchange.client.FrameBufferImplServiceLocator;
import eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole;
import eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplServiceLocator;
import eduze.vms.foundation.logic.mpi.screenshareconsole.ScreenShareConsole;
import eduze.vms.foundation.logic.mpi.screenshareconsole.ScreenShareConsoleImplServiceLocator;


import javax.swing.*;
import javax.xml.rpc.ServiceException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by Madhawa on 14/04/2016.
 */

/**
 * The main class responsible for Controlling of Presenter Subsystem
 * Responsibilities:
 *  Start/Stop Streaming of Screen Captures and Audio Captures to Facilitator
 *  Receive Assigned Tasks from Facilitator
 */
public class ControlLoop extends Thread {
    private String facilitatorURL = null; //URL of Facilitator
    private PresenterConsole presenterConsole = null; //Presenter Console MPI provided by Facilitator
    private ScreenShareConsole screenShareConsole = null; //Screen Share console used to send screen captures to Facilitator
    private AudioRelayConsole audioRelayConsole = null; //Audio Relay Console used to send audio captures to facilitator
    private eduze.livestream.exchange.client.FrameBuffer screenShareBuffer = null; //The buffer used to transfer screen captures to facilitator
    private eduze.livestream.exchange.client.FrameBuffer audioRelayBuffer = null; //The buffer used to transfer audio captures to facilitator
    private ScreenCapturer screenCapturer = null; //The screen capturer that captures frames from System
    private AudioCapturer audioCapturer = null; //The audio capturer that captures frames from System
    private volatile int updateInterval = 1000; //The update frequency of control system
    private volatile int screenCaptureInterval = 1000; //The frequency of capture of screens

    private boolean running = false; //Is the control loop running?

    private StateChangeListener stateChangeListener = null; //The listener to changes in state of system

    private AssignedTask[] assignedTasks = null; //List of assigned tasks for presenter

    private String screenShareConsoleId = null;
    private String audioRelayConsoleId = null;

    //Presenters consent to share
    private boolean allowedScreenShare = false;
    private boolean allowedAudioShare = false;

    //Servers consent to accept share
    private boolean serverAcceptsScreenShare = false;
    private boolean serverAcceptsAudioShare = false;

    /**
     * Constructor
     * @param presenterConsole Presenter Console provided by Facilitator
     * @param facilitatorURL URL of Facilitator
     * @throws FacilitatorConnectionException Error in Connection to Facilitator
     * @throws MalformedURLException Error in Facilitator URL
     */
    ControlLoop(PresenterConsole presenterConsole, String facilitatorURL) throws FacilitatorConnectionException, MalformedURLException {
        try {
            //Store parameters
            this.facilitatorURL = facilitatorURL;
            this.presenterConsole = presenterConsole;
            this.screenShareConsoleId = presenterConsole.getOutScreenShareConsoleId();
            this.audioRelayConsoleId = presenterConsole.getOutAudioRelayConsoleId();

            //Access Screen Share Console MPI
            ScreenShareConsoleImplServiceLocator screenShareConsoleImplServiceLocator = new ScreenShareConsoleImplServiceLocator();
            screenShareConsole = screenShareConsoleImplServiceLocator.getScreenShareConsoleImplPort(new URL(URLGenerator.generateScreenShareConsoleAccessUrl(facilitatorURL,screenShareConsoleId)));

            //Access Audio Relay Console MPI
            AudioRelayConsoleImplServiceLocator audioRelayConsoleImplServiceLocator = new AudioRelayConsoleImplServiceLocator();
            audioRelayConsole = audioRelayConsoleImplServiceLocator.getAudioRelayConsoleImplPort(new URL(URLGenerator.generateAudioRelayConsoleAccessUrl(facilitatorURL,audioRelayConsoleId)));

            //Access Communication buffer MPIs
            FrameBufferImplServiceLocator frameBufferImplServiceLocator = new FrameBufferImplServiceLocator();
            screenShareBuffer = frameBufferImplServiceLocator.getFrameBufferImplPort(new URL(URLGenerator.generateScreenShareConsoleBufferAccessUrl(facilitatorURL,screenShareConsoleId)));
            audioRelayBuffer = frameBufferImplServiceLocator.getFrameBufferImplPort(new URL(URLGenerator.generateAudioRelayFrameBufferAccessUrl(getFacilitatorURL(),audioRelayConsoleId)));

            //Setup capturers
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

    /**
     * Retrieve update interval of Control Loop
     * @return
     */
    public synchronized int getUpdateInterval() {
        return updateInterval;
    }

    /**
     * Set update interval of Control Loop
     * @param updateInterval
     */
    public synchronized void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    /**
     * Retrieve URL of Facilitator
     * @return
     */
    public synchronized String getFacilitatorURL() {
        return facilitatorURL;
    }

    /**
     * Start the Control Loop
     */
    @Override
    public void run()
    {
        //Check for consent to run
        while(isRunning())
        {
            try {
                //Operate in UI Thread
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Retrieve list of assigned tasks
                            assignedTasks = presenterConsole.getAssignedTasks();

                            //update variables to externals
                            setServerAcceptsAudioShare(audioRelayConsole.isEnabled());
                            setServerAcceptsScreenShare(screenShareConsole.isEnabled());

                            //Update state of screen capture to suite facilitator requirements
                            if(isServerAcceptsScreenShare() && isAllowedScreenShare())
                            {
                                //Should start capturing if its not running
                                if(!screenCapturer.isCapturing()) {
                                    screenCapturer.startCapture();
                                    //Notify listener
                                    if(stateChangeListener != null)
                                        stateChangeListener.onScreenCaptureChanged(true);
                                }
                            }
                            else
                            {
                                //Should stop capturing if its running
                                if(screenCapturer.isCapturing()){
                                    screenCapturer.stopCapture();
                                    if(stateChangeListener != null)
                                        stateChangeListener.onScreenCaptureChanged(false);
                                }
                            }

                            /**
                             * Update state of Audio Capture to suite Facilitator Requirement
                             */
                            if(isServerAcceptsAudioShare() && isAllowedAudioShare())
                            {
                                //Facilitator is expecting us to send audio, therefore, start audio capture unless its started already
                                if(!audioCapturer.isCapturing()) {
                                    audioCapturer.startCapture();
                                    if(stateChangeListener != null)
                                        stateChangeListener.onAudioCaptureChanged(true);
                                }
                            }
                            else
                            {
                                //Facilitator is not expecting us to send audio. Therefore, stop audio capture unless its already stopped
                                if(audioCapturer.isCapturing()){
                                    audioCapturer.stopCapture();
                                    if(stateChangeListener != null)
                                        stateChangeListener.onAudioCaptureChanged(false);
                                }
                            }

                            //Notify Control Loop Listeners
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

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(audioCapturer.isCapturing())
                    audioCapturer.stopCapture();
                if(screenCapturer.isCapturing())
                    screenCapturer.stopCapture();
            }
        });
    }

    /**
     * Notify Control Loop Cycle Completion
     */
    private synchronized void notifyControlLoopCycleCompleted() {
        if(stateChangeListener != null)
            stateChangeListener.onControlLoopCycleCompleted();
    }

    /**
     * Retrieve running state of Control Loop
     * @return
     */
    public synchronized boolean isRunning() {
        return running;
    }

    /**
     * Set the running state of control loop
     * @param value
     */
    private synchronized void setRunning(boolean value)
    {
        running = value;
    }

    /**
     * Start the Control Loop
     */
    @Override
    public void start()
    {
        setRunning(true);
        super.start();
    }

    /**
     * Stop the control loop
     */
    public void stopRunning()
   {
       setRunning(false);
   }

    /**
     * Retrieve interval of screen capture
     * @return
     */
    public synchronized int getScreenCaptureInterval() {
        return screenCaptureInterval;
    }

    /**
     * Set interval of screen capture
     * @param screenCaptureInterval
     */
    public synchronized void setScreenCaptureInterval(int screenCaptureInterval) {
        this.screenCaptureInterval = screenCaptureInterval;
        this.screenCapturer.setCaptureInterval(screenCaptureInterval);
    }

    /**
     * Retrieve whether screen is being shared now
     * @return True if screen is being shared now
     */
    public synchronized boolean isScreenShared()
    {
        return screenCapturer.isCapturing();
    }

    /**
     * Retrieve whether audio is being shared now
     * @return True if audio is being shared now
     */
    public synchronized boolean isAudioShared()
    {
        return audioCapturer.isCapturing();
    }

    /**
     * Retrieve Listener to changes in state of Presenter
     * @return
     */
    public synchronized StateChangeListener getStateChangeListener() {
        return stateChangeListener;
    }

    /**
     * Set the listener to changes in state of presenter
     * @param stateChangeListener
     */
    public synchronized void setStateChangeListener(StateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    /**
     * Retrieve assigned tasks for presenter
     * @return
     */
    public AssignedTask[] getAssignedTasks() {
        return assignedTasks;
    }

    /**
     * Retrieve whether Presenter has given consent to share screen
     * @return
     */
    public synchronized boolean isAllowedScreenShare() {
        return allowedScreenShare;
    }

    /**
     * Set whether presenter has given consent to share screen
     * @param allowedScreenShare
     */
    public synchronized void setAllowedScreenShare(boolean allowedScreenShare) {
        this.allowedScreenShare = allowedScreenShare;
    }

    /**
     * Retrieve whether presenter has given consent to share audio
     * @return
     */
    public synchronized boolean isAllowedAudioShare() {
        return allowedAudioShare;
    }

    /**
     * Set whether presenter has given consent to share audio
     * @param allowedAudioShare
     */
    public synchronized void setAllowedAudioShare(boolean allowedAudioShare) {
        this.allowedAudioShare = allowedAudioShare;
    }

    /**
     * Retrieve whether facilitator is ready to accept audio share
     * @return
     */
    public synchronized boolean isServerAcceptsAudioShare() {
        return serverAcceptsAudioShare;
    }

    /**
     * Retrieve whether facilitator is ready to accept screen share
     * @return
     */
    public synchronized boolean isServerAcceptsScreenShare() {
        return serverAcceptsScreenShare;
    }

    /**
     * Set whether facilitator is ready to accept screen share
     * @param value
     */
    synchronized void setServerAcceptsScreenShare(boolean value)
    {
        serverAcceptsScreenShare = value;
    }

    /**
     * Set whether facilitator is ready to accept audio share
     * @param value
     */
    synchronized void setServerAcceptsAudioShare(boolean value)
    {
        serverAcceptsAudioShare = value;
    }

    /**
     * Created by Madhawa on 14/04/2016.
     */
    /**
     * Listener to changes in States of Presenter
     */
    public static interface StateChangeListener {
        /**
         * Screen capture state has been changed
         * @param newValue
         */
        public void onScreenCaptureChanged(boolean newValue);

        /**
         * Audio Capture state has been changed
         * @param newValue
         */
        public void onAudioCaptureChanged(boolean newValue);

        /**
         * Control Loop cycle has been completed
         */
        public void onControlLoopCycleCompleted();
    }
}
