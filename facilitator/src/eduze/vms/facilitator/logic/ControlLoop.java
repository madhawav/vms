package eduze.vms.facilitator.logic;

import com.sun.org.apache.xpath.internal.operations.Mult;
import eduze.livestream.AudioReceiver;
import eduze.livestream.Multiplexer;
import eduze.livestream.ScreenReceiver;
import eduze.livestream.exchange.client.FrameBuffer;
import eduze.livestream.exchange.client.FrameBufferImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsole;


import eduze.vms.facilitator.logic.mpi.virtualmeeting.SessionStatus;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTask;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.webservices.AssignedTask;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;
import eduze.vms.facilitator.logic.webservices.PresenterConsole;
import eduze.vms.facilitator.logic.webservices.PresenterConsoleImpl;
import eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole;
import eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplServiceLocator;
import eduze.vms.foundation.logic.mpi.screenshareconsole.ScreenShareConsoleImplServiceLocator;

import javax.swing.*;
import javax.xml.rpc.ServiceException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * The Control Loop of Facilitator is responsible for Controlling streaming of data between Presenter, Facilitator and Server Subsystems.
 * Responsibilities:
 *  Notify presenters to enable or disable sharing of screen and audio
 *  Direct appropriate presenters output streams to Server
 *  Direct appropriate presenters output streams to UI
 *  Receive inputs streams from server and direct to UI
 */
public class ControlLoop extends Thread {
    private volatile boolean keepRunning = false; //Should the control loop continue to run?
    private volatile int sleepDelay = 1000; //Delay between control loop updates
    private volatile int screenShareReceiveDelay = 1000; //Delay between screen shares received from Server
    private Multiplexer screenSwitcher = null; //Multiplexer used to direct appropriate screen share streams to server
    private Multiplexer audioSwitcher = null; //Multiplexer used to direct appropriate audio relay streams to server

    private boolean mute = false; //is the audio mute?

    private String audioSwitcherInputURL = null; //current input URL set in audio switcher
    private String screenSwitcherInputURL = null; //current input URL set in screen switcher

    private FacilitatorImpl facilitator = null; //Facilitator Web Service provided to presenters by Facilitator
    private String facilitatorConsoleId  =  null; //Facilitator Console Id provided by Server
    private FacilitatorConsole facilitatorConsole = null; //MPI to Facilitator Console Provided by Server

    private eduze.vms.foundation.logic.mpi.screenshareconsole.ScreenShareConsole outputScreenShareConsole = null; //MPI to Output Screen Share Console provided by Server
    private String outScreenShareConsoleId = null; //ID of OutputScreenShareConsole provided by Server

    private eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole outputAudioRelayConsole = null; //MPI to Output Audio Relay Console provided by Server
    private String outAudioRelayConsoleId = null; //ID of OutputAudioRelayConsole provided by Server

    //MPI of Input Screen Share Console provided by Server
    private eduze.vms.foundation.logic.mpi.screenshareconsole.ScreenShareConsole inScreenShareConsole = null;
    private String inScreenShareConsoleId = null; //Input Screen Share Console ID of Server
    private FrameBuffer inScreenShareBuffer = null; //Input Screen share Frame buffer provided by server
    private ScreenReceiver screenReceiver = null; //Screen Receiver listening to frames from server

    //MPI of Input Audio Relay Console provided by Server
    private eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole inAudioRelayConsole = null;
    private String inAudioRelayConsoleId = null; //Input Audio Relay Console ID of Server
    private FrameBuffer inAudioRelayBuffer = null; //Input Audio Relay Frame Buffer provided by server
    private AudioReceiver audioReceiver = null; //Audio Receiver listening to frames from server

    private String serverURL = null; //URL of Server

    private String facilitatorURL = null; //URL of Facilitator
    private ControlLoopListener controlLoopListener = null; //Listener to events of control loop
    private VirtualMeetingSnapshot lastKnownVMSnapshot; //Last known status of Virtual Meeting. Switching decisions are taken by Control Loop based on the information in this.
    private ConnectivityManager connectivityManager;
    private CaptureReceivedListener captureReceivedListener = null; //Listener to be notified on reception of screen captures/audio relays

    /**
     * Constructor of Control Loop
     * @param facilitator Facilitator Web Service provided to presenter subsystems
     * @param serverUrl URL of Server
     * @param facilitatorPort Port used to host
     * @param connectivityManager Connectivity Manager to be notified on server connectivity
     * @throws MalformedURLException Port used to host Facilitator Web Service hosted by Facilitator and used by Presenter Subsystems
     * @throws ServerConnectionException Error in Connection to Server
     */
    public ControlLoop(FacilitatorImpl facilitator, String serverUrl, int facilitatorPort, ConnectivityManager connectivityManager) throws MalformedURLException, ServerConnectionException {
        this.connectivityManager = connectivityManager;
        this.facilitatorURL = UrlGenerator.generateLocalURL(facilitatorPort); //Generate URL to Facilitator Web Service
        this.serverURL = UrlGenerator.extractURL(serverUrl);
        this.facilitator = facilitator;
        this.facilitatorConsole = facilitator.getFacilitatorConsole();
        try {
            this.facilitatorConsoleId = facilitatorConsole.getConsoleId();
            setupScreenShare(); //Setup Screen Share Control Service
            setupAudioRelay(); //Setup Audio Relay Service

        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ServerConnectionException(e);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServerConnectionException(e);
        }
    }

    /**
     * Setup Screen Share Control Service
     * Screen Share Control Service determines transfer of screen share streams among Presenter, Faciltiator and Server
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws ServiceException
     */
    private void setupScreenShare() throws RemoteException, MalformedURLException, ServiceException {
        String serverUrl = this.serverURL;
        outScreenShareConsoleId = facilitatorConsole.getOutScreenShareConsoleId(); //Retrieve Output Screen Share Console ID for facilitator provided by server
        screenSwitcher = new Multiplexer(new URL(UrlGenerator.generateScreenShareConsoleBufferAccessUrl(serverUrl,outScreenShareConsoleId))); //Setup the Screen Share Multiplexer to write outputs to Output Screen Share Buffer provided by Server
        eduze.vms.foundation.logic.mpi.screenshareconsole.ScreenShareConsoleImplServiceLocator screenShareConsoleImplServiceLocator = new ScreenShareConsoleImplServiceLocator();  //Service Locator for Screen Share Service provided by Server
        outputScreenShareConsole = screenShareConsoleImplServiceLocator.getScreenShareConsoleImplPort(new URL(UrlGenerator.generateScreenShareConsoleAccessUrl(serverUrl,outScreenShareConsoleId))); //Obtain MPI Instance for Output Screen Share Console provided by Server

        inScreenShareConsoleId = facilitatorConsole.getInScreenShareConsoleId(); //Input Screen Share console id provided by Server
        inScreenShareConsole = screenShareConsoleImplServiceLocator.getScreenShareConsoleImplPort(new URL(UrlGenerator.generateScreenShareConsoleAccessUrl(serverUrl,inScreenShareConsoleId))); //Instance to MPI of Input Screen Share Console Provided by Server to Facilitator

        FrameBufferImplServiceLocator frameBufferImplServiceLocator = new FrameBufferImplServiceLocator(); //Service Locator to FrameBuffer Web Service
        inScreenShareBuffer = frameBufferImplServiceLocator.getFrameBufferImplPort(new URL(UrlGenerator.generateScreenShareConsoleBufferAccessUrl(serverUrl,inScreenShareConsoleId))); //Setup  a Web Service to listen to Screen Share frames received from server

        //Setup Screen Receiver to receive inputs from Server
        screenReceiver = new ScreenReceiver(inScreenShareBuffer,screenShareReceiveDelay);
        screenReceiver.addScreenReceivedListener(new ScreenReceiver.ScreenReceivedListener() {
            @Override
            public void ScreenReceived(byte[] bytes, BufferedImage bufferedImage) {
                screenReceiverDataReceived(bytes, bufferedImage);
            }
        });
        screenSwitcher.addDataReceivedListener(new Multiplexer.DataReceivedListener() {
            @Override
            public void DataReceived(byte[] bytes) {
                screenSwitcherDataReceived(bytes);
            }
        });
    }


    private void setupAudioRelay() throws RemoteException, MalformedURLException, ServiceException {
        String serverUrl = this.serverURL;
        outAudioRelayConsoleId = facilitatorConsole.getOutAudioRelayConsoleId(); //Retrieve Output Audio Relay Console ID for facilitator provided by server
        audioSwitcher = new Multiplexer(new URL(UrlGenerator.generateAudioRelayFrameBufferAccessUrl(serverUrl,outAudioRelayConsoleId))); //Setup the Audio Relay Multiplexer to write outputs to Output Audio Relay Buffer provided by Server
        eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplServiceLocator audioRelayConsoleImplServiceLocator  = new AudioRelayConsoleImplServiceLocator(); //Service Locator for Audio Relay Service provided by Server
        outputAudioRelayConsole = audioRelayConsoleImplServiceLocator.getAudioRelayConsoleImplPort(new URL(UrlGenerator.generateAudioRelayConsoleAccessUrl(serverUrl,outAudioRelayConsoleId))); //Obtain MPI Instance for Output Audio Relay Console provided by Server

        inAudioRelayConsoleId = facilitatorConsole.getInAudioRelayConsoleId(); //Input Audio Relay console id provided by Server
        inAudioRelayConsole = audioRelayConsoleImplServiceLocator.getAudioRelayConsoleImplPort(new URL(UrlGenerator.generateAudioRelayConsoleAccessUrl(serverUrl,inAudioRelayConsoleId))); //Instance to MPI of Input Audio Relay Console Provided by Server to Facilitator

        FrameBufferImplServiceLocator frameBufferImplServiceLocator = new FrameBufferImplServiceLocator(); //Service Locator to FrameBuffer Web Service
        inAudioRelayBuffer = frameBufferImplServiceLocator.getFrameBufferImplPort(new URL(UrlGenerator.generateAudioRelayFrameBufferAccessUrl(serverUrl,inAudioRelayConsoleId))); //Setup a Web Service to listen to Audio Relay frames received from server

        //Setup Audio Receiver to receive inputs from Server
        audioReceiver = new AudioReceiver(inAudioRelayBuffer);
        audioReceiver.addAudioReceivedListener(new AudioReceiver.AudioReceivedListener() {
            @Override
            public void AudioReceived(byte[] bytes) {
                audioReceiverDataReceived(bytes);
            }

        });

    }


    /**
     * The Audio Receiver has received an audio frame from server
     * @param bytes
     */
    private void audioReceiverDataReceived(byte[] bytes) {
        //Check whether the input stream from server is the active input stream for audio relay
        VirtualMeetingSnapshot vm = getLastKnownVMSnapshot();
        if(!facilitatorConsoleId.equals(vm.getActiveSpeechFacilitatorId()))
        {
            //if so, dispatch to ui
            CaptureReceivedListener listener = getCaptureReceivedListener();
            if(listener != null)
            {
                listener.onAudioDataReceived(bytes,vm.getActiveScreenFacilitatorId(),vm.getActiveScreenPresenterId());
            }
        }
    }

    /**
     * The Screen Receiver has received has received data from server
     * @param bytes
     * @param bufferedImage
     */
    private void screenReceiverDataReceived(byte[] bytes, BufferedImage bufferedImage) {
        //Check whether the input stream from server is the active input stream for screen share
        VirtualMeetingSnapshot vm = getLastKnownVMSnapshot();
        if(!facilitatorConsoleId.equals(vm.getActiveScreenFacilitatorId()))
        {
            //if so, dispatch to ui
            CaptureReceivedListener listener = getCaptureReceivedListener();
            if(listener != null)
            {
                listener.onScreenCaptureReceived(bytes,bufferedImage,vm.getActiveScreenFacilitatorId(),vm.getActiveScreenPresenterId());
            }
        }

    }

    /**
     * The Screen Switcher has received a frame from Presenter
     * @param bytes
     */
    private void screenSwitcherDataReceived(byte[] bytes) {
        //Check whether the input stream from presenter is the active input stream for screen share
        VirtualMeetingSnapshot vm = getLastKnownVMSnapshot();
        if(facilitatorConsoleId.equals(vm.getActiveScreenFacilitatorId()))
        {
            //if so, dispatch to ui
            CaptureReceivedListener listener = getCaptureReceivedListener();
            if(listener != null)
            {
                try {
                    BufferedImage image = Util.generateBufferedImage(bytes);
                    listener.onScreenCaptureReceived(bytes,image,vm.getActiveScreenFacilitatorId(),vm.getActiveScreenPresenterId());
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onException(e);
                }
            }

        }
    }

    /**
     * Retrieve update delay of control loop
     * @return
     */
    public synchronized int getSleepDelay()
    {
        return sleepDelay;
    }

    /**
     * Set update delay of control loop
     * @param sleepDelay
     */
    public synchronized void setSleepDelay(int sleepDelay) {
        this.sleepDelay = sleepDelay;
    }

    /**
     * Retrieve whether Control Loop is running
     * @return True if the control loop is running
     */
    public synchronized boolean isRunning()
    {
        return keepRunning;
    }

    /**
     * Stop the control Loop
     */
    public synchronized void stopRunning()
    {
        keepRunning = false;
    }


    /**
     * Run the Control Loop
     */
    @Override
    public void run()
    {
        //Should the control loop be running?
        while(isRunning())
        {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        //Switch to UI Thread since we are interacting with objects used in UI thread
                        try {
                            //Obtain Virtual Meeting State
                            VirtualMeetingSnapshot vm = facilitator.getVirtualMeeting().getSnapshot();
                            String facilitatorCode = getFacilitatorConsoleId();

                            processScreenShare(vm,facilitatorCode); //Process Screen Share Controls
                            processAudioRelay(vm,facilitatorCode); //Process Audio Relay Controls
                            updateAssignedTasks(vm,facilitatorCode); //Process Assigned Tasks List
                            setLastKnownVMSnapshot(vm); //Remember the lastly received Virtual Meeting State

                            if(vm.getStatus() == SessionStatus.Adjourned)
                            {
                                if(controlLoopListener != null)
                                {
                                    try {
                                        controlLoopListener.onMeetingAdjourned();
                                        stopRunning();
                                    } catch (ServerConnectionException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                            connectivityManager.recordSignal();
                            if (controlLoopListener != null) {
                                controlLoopListener.updateReceived(vm); //Notify through control loop listener
                            }



                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        connectivityManager.serverPulse();
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            try {
                //Wait for control loop update delay
                Thread.currentThread().sleep(getSleepDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Control Loop Termination Logic
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //In UI Thread, release resources
                screenReceiver.stopReceiving();
                audioReceiver.stopReceiving();
                audioSwitcher.Stop();
                screenSwitcher.Stop();

                //Stop incoming traffic
                stopIncoming();
            }
        });
    }



    /**
     * Stop incoming traffic
     */
    private void stopIncoming() {

        for (PresenterConsoleImpl presenterConsole : facilitator.getPresenterConsoles()) {
            presenterConsole.getAudioRelayConsole().setEnabled(false);
            presenterConsole.getScreenShareConsole().setEnabled(false);
        }

    }

    /**
     * Process Audio Relay Controls
     * @param vm Virtual Meeting State
     * @param facilitatorCode FacilitatorConsoleId
     * @throws MalformedURLException
     * @throws RemoteException
     */
    private void processAudioRelay(VirtualMeetingSnapshot vm, String facilitatorCode) throws MalformedURLException, RemoteException {
        //Identify active audio relay presenter console
        PresenterConsoleImpl selectedPresenterConsole = null;
        for (PresenterConsoleImpl presenterConsole : facilitator.getPresenterConsoles()) {
            //Is presenterConsole the active audio relay source as per state variables of virtual meeting?
            if (facilitatorCode.equals(vm.getActiveSpeechFacilitatorId()) && presenterConsole.getConsoleId().equals(vm.getActiveSpeechPresenterId())) {
                //If so, mark the audio relay console as enabled. This indicate the presenter that it should capture and stream audio to facilitator
                selectedPresenterConsole = presenterConsole;
                presenterConsole.getAudioRelayConsole().setEnabled(true);
            } else {
                //If not, mark the audio relay console as disabled. This indicate the presenter that it should not send audio data to facilitator
                presenterConsole.getAudioRelayConsole().setEnabled(false);
            }
        }

        //Update the audio switcher to properly direct inputs from presenters to server
        if (selectedPresenterConsole != null) {
            //There is an active audio relay presenter connected to this facilitator
            String targetURL = UrlGenerator.generateAudioRelayFrameBufferAccessUrl(facilitatorURL, selectedPresenterConsole.getOutAudioRelayConsoleId());
            if (!targetURL.equals(audioSwitcherInputURL)) {
                //Setup the audio switcher to stream audio from active audio relay buffer to server
                audioSwitcherInputURL = targetURL;
                audioSwitcher.setInputURL(new URL(audioSwitcherInputURL));
            }
        } else {
            //There is no active audio relay presenter connected to this facilitator, therefore, disable multiplexer by setting input URL to null
            if (audioSwitcherInputURL != null) {
                audioSwitcherInputURL = null;
                audioSwitcher.setInputURL(null);
            }

        }

        //Check whether the server is expecting audio frames from facilitator
        if (outputAudioRelayConsole.isEnabled() && !audioSwitcher.isRunning())
            audioSwitcher.Start(); //Server expects audio frames from facilitator, therefore start switcher
        else if (!outputAudioRelayConsole.isEnabled() && audioSwitcher.isRunning())
           audioSwitcher.Stop(); //Server doesn't expect audio frames from facilitator, therefore stop switcher

        //Check whether the server is sending audio frames to facilitator
        boolean inAudioRelayEnabled = inAudioRelayConsole.isEnabled();
        if (inAudioRelayEnabled && !audioReceiver.isReceiving()) {
            audioReceiver.startReceiving(); //Server is sending audio frames, therefore start audio receiver
        } else if (!inAudioRelayEnabled && audioReceiver.isReceiving()) {
            audioReceiver.stopReceiving(); //The server is not sending audio frames, therefore stop audio receiver
        }

        //Apply mute settings
        audioReceiver.setPlayReceived(!isMute());

    }

    /**
     * Process Screen Share Controls
     * @param vm Virtual Meeting State
     * @param facilitatorCode Facilitator Console Id
     * @throws MalformedURLException
     * @throws RemoteException
     */
    private void processScreenShare(VirtualMeetingSnapshot vm, String facilitatorCode) throws MalformedURLException, RemoteException {
        //Identify active screen share presenter console
        PresenterConsoleImpl selectedPresenterConsole = null;
        for (PresenterConsoleImpl presenterConsole : facilitator.getPresenterConsoles()) {
            //Is presenterConsole the active screen share source as per state variables of virtual meeting?
            if (facilitatorCode.equals(vm.getActiveScreenFacilitatorId()) && presenterConsole.getConsoleId().equals(vm.getActiveScreenPresenterId())) {
                //If so, mark the screen share console as enabled. This indicate the presenter that it should capture and stream screen to facilitator
                selectedPresenterConsole = presenterConsole;
                presenterConsole.getScreenShareConsole().setEnabled(true);
            } else {
                //If not, mark the screen share console as disabled. This indicate the presenter that it should not send screen share to facilitator
                presenterConsole.getScreenShareConsole().setEnabled(false);
            }
        }


        //Update the screen switcher to properly direct inputs from presenters to server
        if (selectedPresenterConsole != null) {
            //There is an active screen share presenter connected to this facilitator
            String targetURL = UrlGenerator.generateScreenShareConsoleBufferAccessUrl(facilitatorURL, selectedPresenterConsole.getOutScreenShareConsoleId());
            if (!targetURL.equals(screenSwitcherInputURL)) {
                //Setup the screen switcher to stream screen from active screen relay buffer to server
                screenSwitcherInputURL = targetURL;
                screenSwitcher.setInputURL(new URL(screenSwitcherInputURL));
            }
        } else {
            if (screenSwitcherInputURL != null) {
                //There is no active screen share presenter connected to this facilitator, therefore, disable multiplexer by setting input URL to null
                screenSwitcherInputURL = null;
                screenSwitcher.setInputURL(null);
            }

        }

        //TODO: put update interval logic here
        //Check whether the server is expecting screen frames frames from facilitator
        if (outputScreenShareConsole.isEnabled() && !screenSwitcher.isRunning())
            screenSwitcher.Start(); //Server expects screen frames from facilitator, therefore start switcher
        else if (!outputScreenShareConsole.isEnabled() && screenSwitcher.isRunning())
            screenSwitcher.Stop(); //Server doesn't expect screen frames from facilitator, therefore start switcher

        //Check whether the server is sending screen frames to facilitator
        boolean inScreenShareEnabled = inScreenShareConsole.isEnabled();
        if (inScreenShareEnabled && !screenReceiver.isReceiving()) {
            screenReceiver.startReceiving(); //Server is sending screen frames, therefore start screen receiver
        } else if (!inScreenShareEnabled && screenReceiver.isReceiving()) {
            screenReceiver.stopReceiving(); //Server is not sending screen frames, therefore stop screen receiver
        }


    }

    /**
     * Update the list of assigned tasks based on inputs from Server
     * @param vm Virtual Meeting State
     * @param facilitatorCode Facilitator Console Id
     */
    public void updateAssignedTasks(VirtualMeetingSnapshot vm, String facilitatorCode)
    {
        //If presenter consoles are not available, then skip
        if(facilitator.getPresenterConsoles() == null)
            return;
        //Clear the assigned tasks list in each presenter console
        for(PresenterConsoleImpl presenterConsole : facilitator.getPresenterConsoles())
        {
            presenterConsole.clearAssignedTasks();
        }

        //If Virtual Meeting doesn't include shared tasks, then skip
        if(vm.getSharedTasks() == null)
            return;

        //Update the assigned tasks list in Presenter Consoles to reflect assigned tasks in Virtual Meeting State Variables
        for(SharedTask task : vm.getSharedTasks())
        {
            //Test for matching facilitator console id
            if(facilitatorCode.equals(task.getAssignedFacilitatorId()))
            {
                //Test for matching presenter id
                if(task.getAssignedPresenterId() != null)
                {
                    PresenterConsoleImpl assignedPresenter = facilitator.getPresenterConsole(task.getAssignedPresenterId());
                    //Match found
                    if(assignedPresenter != null)
                    {
                        //Add the assigned task to presenter console
                        assignedPresenter.addAssignedTask(AssignedTask.fromSharedTask(task));
                    }
                }
            }
        }

    }

    /**
     * Start the Control Loop
     */
    @Override
    public void start()
    {
        keepRunning = true;
        super.start();
    }

    /**
     * Retrieve Control Loop Listener
     * @return
     */
    public synchronized ControlLoopListener getControlLoopListener() {
        return controlLoopListener;
    }

    /**
     * Assign a Control Loop Listener
     * @param controlLoopListener
     */
    public synchronized void setControlLoopListener(ControlLoopListener controlLoopListener) {
        this.controlLoopListener = controlLoopListener;
    }

    /**
     * Retrieve update delay for screen capture reception
     * @return
     */
    public synchronized int getScreenShareReceiveDelay() {
        return screenShareReceiveDelay;
    }

    /**
     * Set update delay for screen capture receptions
     * @param screenShareReceiveDelay
     */
    public synchronized void setScreenShareReceiveDelay(int screenShareReceiveDelay) {
        this.screenShareReceiveDelay = screenShareReceiveDelay;
        //TODO: screenReceiver.setInterval(value)
    }

    /**
     * Set the last received virtual meeting state from control loop
     * @param lastKnownVMSnapshot
     */
    private synchronized void setLastKnownVMSnapshot(VirtualMeetingSnapshot lastKnownVMSnapshot) {
        this.lastKnownVMSnapshot = lastKnownVMSnapshot;
    }

    /**
     * Obtain the last received virtual meeting state from control loop
     * @return
     */
    public synchronized VirtualMeetingSnapshot getLastKnownVMSnapshot() {
        return lastKnownVMSnapshot;
    }

    /**
     * Retrieve Facilitator Console Id
     * @return
     */
    private synchronized String getFacilitatorConsoleId() {
        return facilitatorConsoleId;
    }

    /**
     * Receive listener notified on reception of capture frames
     * @return
     */
    public synchronized CaptureReceivedListener getCaptureReceivedListener() {
        return captureReceivedListener;
    }

    /**
     * Assign a listener to be notified on reception of capture frames
     * @param captureReceivedListener
     */
    public synchronized void setCaptureReceivedListener(CaptureReceivedListener captureReceivedListener) {
        this.captureReceivedListener = captureReceivedListener;
    }

    /**
     * Retrieve mute state of audio received
     * @return
     */
    public synchronized boolean isMute() {
        return mute;
    }

    /**
     * Set mute state of audio received
     * @param mute
     */
    public synchronized void setMute(boolean mute) {
        this.mute = mute;
    }
}
