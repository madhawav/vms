package eduze.vms.facilitator.logic;

import com.sun.org.apache.xpath.internal.operations.Mult;
import eduze.livestream.AudioReceiver;
import eduze.livestream.Multiplexer;
import eduze.livestream.ScreenReceiver;
import eduze.livestream.exchange.client.FrameBuffer;
import eduze.livestream.exchange.client.FrameBufferImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsole;

import eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsoleImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;
import eduze.vms.facilitator.logic.webservices.PresenterConsoleImpl;
import eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole;
import eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplServiceLocator;

import javax.swing.*;
import javax.xml.rpc.ServiceException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * Created by Madhawa on 13/04/2016.
 */
public class ControlLoop extends Thread {
    private volatile boolean keepRunning = false;
    private volatile int sleepDelay = 1000;
    private volatile int screenShareReceiveDelay = 1000;
    private Multiplexer screenSwitcher = null;
    private Multiplexer audioSwitcher = null;

    private boolean mute = false;

    private String audioSwitcherInputURL = null;
    private String screenSwitcherInputURL = null;
    private FacilitatorImpl facilitator = null;

    private String facilitatorConsoleId  =  null;
    private FacilitatorConsole facilitatorConsole = null;

    private eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsole outputScreenShareConsole = null;
    private String outScreenShareConsoleId = null;

    private eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole outputAudioRelayConsole = null;
    private String outAudioRelayConsoleId = null;

    private eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsole inScreenShareConsole = null;
    private String inScreenShareConsoleId = null;
    private FrameBuffer inScreenShareBuffer = null;
    private ScreenReceiver screenReceiver = null;

    private eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsole inAudioRelayConsole = null;
    private String inAudioRelayConsoleId = null;
    private FrameBuffer inAudioRelayBuffer = null;
    private AudioReceiver audioReceiver = null;

    private String serverURL = null;

    private String facilitatorURL = null;
    private ControlLoopListener controlLoopListener = null;
    private VirtualMeetingSnapshot lastKnownVMSnapshot;

    private CaptureReceivedListener captureReceivedListener = null;



    public ControlLoop(FacilitatorImpl facilitator, String serverUrl, int facilitatorPort) throws MalformedURLException, ServerConnectionException {
        this.facilitatorURL = UrlGenerator.generateLocalURL(facilitatorPort);
        this.serverURL = UrlGenerator.extractURL(serverUrl);
        this.facilitator = facilitator;
        this.facilitatorConsole = facilitator.getFacilitatorConsole();
        try {
            this.facilitatorConsoleId = facilitatorConsole.getConsoleId();
            setupScreenShare();
            setupAudioRelay();
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ServerConnectionException(e);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServerConnectionException(e);
        }
    }

    private void setupScreenShare() throws RemoteException, MalformedURLException, ServiceException {
        String serverUrl = this.serverURL;
        outScreenShareConsoleId = facilitatorConsole.getOutScreenShareConsoleId();
        screenSwitcher = new Multiplexer(new URL(UrlGenerator.generateScreenShareConsoleBufferAccessUrl(serverUrl,outScreenShareConsoleId)));
        eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsoleImplServiceLocator screenShareConsoleImplServiceLocator = new ScreenShareConsoleImplServiceLocator();
        outputScreenShareConsole = screenShareConsoleImplServiceLocator.getScreenShareConsoleImplPort(new URL(UrlGenerator.generateScreenShareConsoleAccessUrl(serverUrl,outScreenShareConsoleId)));

        inScreenShareConsoleId = facilitatorConsole.getInScreenShareConsoleId();
        inScreenShareConsole = screenShareConsoleImplServiceLocator.getScreenShareConsoleImplPort(new URL(UrlGenerator.generateScreenShareConsoleAccessUrl(serverUrl,inScreenShareConsoleId)));

        FrameBufferImplServiceLocator frameBufferImplServiceLocator = new FrameBufferImplServiceLocator();
        inScreenShareBuffer = frameBufferImplServiceLocator.getFrameBufferImplPort(new URL(UrlGenerator.generateScreenShareConsoleBufferAccessUrl(serverUrl,inScreenShareConsoleId)));

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
        outAudioRelayConsoleId = facilitatorConsole.getOutAudioRelayConsoleId();
        audioSwitcher = new Multiplexer(new URL(UrlGenerator.generateAudioRelayFrameBufferAccessUrl(serverUrl,outAudioRelayConsoleId)));
        eduze.vms.foundation.logic.mpi.audiorelayconsole.AudioRelayConsoleImplServiceLocator audioRelayConsoleImplServiceLocator  = new AudioRelayConsoleImplServiceLocator();
        outputAudioRelayConsole = audioRelayConsoleImplServiceLocator.getAudioRelayConsoleImplPort(new URL(UrlGenerator.generateAudioRelayConsoleAccessUrl(serverUrl,outAudioRelayConsoleId)));

        inAudioRelayConsoleId = facilitatorConsole.getInAudioRelayConsoleId();
        inAudioRelayConsole = audioRelayConsoleImplServiceLocator.getAudioRelayConsoleImplPort(new URL(UrlGenerator.generateAudioRelayConsoleAccessUrl(serverUrl,inAudioRelayConsoleId)));

        FrameBufferImplServiceLocator frameBufferImplServiceLocator = new FrameBufferImplServiceLocator();
        inAudioRelayBuffer = frameBufferImplServiceLocator.getFrameBufferImplPort(new URL(UrlGenerator.generateAudioRelayFrameBufferAccessUrl(serverUrl,inAudioRelayConsoleId)));

        audioReceiver = new AudioReceiver(inAudioRelayBuffer);
        audioReceiver.addAudioReceivedListener(new AudioReceiver.AudioReceivedListener() {
            @Override
            public void AudioReceived(byte[] bytes) {
                audioReceiverDataReceived(bytes);
            }

        });

    }

    private void audioReceiverDataReceived(byte[] bytes) {
        VirtualMeetingSnapshot vm = getLastKnownVMSnapshot();
        if(!facilitatorConsoleId.equals(vm.getActiveSpeechFacilitatorId()))
        {
            //dispatch to ui
            CaptureReceivedListener listener = getCaptureReceivedListener();
            if(listener != null)
            {
                listener.onAudioDataReceived(bytes,vm.getActiveScreenFacilitatorId(),vm.getActiveScreenPresenterId());
            }
        }
    }

    private void screenReceiverDataReceived(byte[] bytes, BufferedImage bufferedImage) {

        VirtualMeetingSnapshot vm = getLastKnownVMSnapshot();
        if(!facilitatorConsoleId.equals(vm.getActiveScreenFacilitatorId()))
        {
            //dispatch to ui
            CaptureReceivedListener listener = getCaptureReceivedListener();
            if(listener != null)
            {
                listener.onScreenCaptureReceived(bytes,bufferedImage,vm.getActiveScreenFacilitatorId(),vm.getActiveScreenPresenterId());
            }
        }

    }

    private void screenSwitcherDataReceived(byte[] bytes) {
        VirtualMeetingSnapshot vm = getLastKnownVMSnapshot();
        if(facilitatorConsoleId.equals(vm.getActiveScreenFacilitatorId()))
        {
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


            //dispatch to ui
        }
    }

    public synchronized int getSleepDelay()
    {
        return sleepDelay;
    }

    public synchronized void setSleepDelay(int sleepDelay) {
        this.sleepDelay = sleepDelay;
    }

    public synchronized boolean isRunning()
    {
        return keepRunning;
    }

    public synchronized void stopRunning()
    {
        keepRunning = false;
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
                        //develop the logic here
                        try {
                            VirtualMeetingSnapshot vm = facilitator.getVirtualMeeting().getSnapshot();
                            String facilitatorCode = getFacilitatorConsoleId();

                            processScreenShare(vm,facilitatorCode);
                            processAudioRelay(vm,facilitatorCode);

                            setLastKnownVMSnapshot(vm);
                            if (controlLoopListener != null) {
                                controlLoopListener.updateReceived(vm);
                            }

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
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
                Thread.currentThread().sleep(getSleepDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                screenReceiver.stopReceiving();
                audioReceiver.stopReceiving();
                audioSwitcher.Stop();
                screenReceiver.stopReceiving();
            }
        });
    }

    private void processAudioRelay(VirtualMeetingSnapshot vm, String facilitatorCode) throws MalformedURLException, RemoteException {
        PresenterConsoleImpl selectedPresenterConsole = null;

        for (PresenterConsoleImpl presenterConsole : facilitator.getPresenterConsoles()) {
            if (facilitatorCode.equals(vm.getActiveSpeechFacilitatorId()) && presenterConsole.getConsoleId().equals(vm.getActiveSpeechPresenterId())) {
                selectedPresenterConsole = presenterConsole;
                presenterConsole.getAudioRelayConsole().setEnabled(true);
            } else {
                presenterConsole.getAudioRelayConsole().setEnabled(false);
            }
        }


        if (selectedPresenterConsole != null) {
            String targetURL = UrlGenerator.generateAudioRelayFrameBufferAccessUrl(facilitatorURL, selectedPresenterConsole.getOutAudioRelayConsoleId());
            if (!targetURL.equals(audioSwitcherInputURL)) {
                audioSwitcherInputURL = targetURL;
                audioSwitcher.setInputURL(new URL(audioSwitcherInputURL));
            }
        } else {
            if (audioSwitcherInputURL != null) {
                audioSwitcherInputURL = null;
                audioSwitcher.setInputURL(null);
            }

        }

        if (outputAudioRelayConsole.isEnabled() && !audioSwitcher.isRunning())
            audioSwitcher.Start();
        else if (!outputAudioRelayConsole.isEnabled() && audioSwitcher.isRunning())
           audioSwitcher.Stop();

        boolean inAudioRelayEnabled = inAudioRelayConsole.isEnabled();
        if (inAudioRelayEnabled && !audioReceiver.isReceiving()) {
            audioReceiver.startReceiving();
        } else if (!inAudioRelayEnabled && audioReceiver.isReceiving()) {
            audioReceiver.stopReceiving();
        }

        audioReceiver.setPlayReceived(!isMute());

    }

    private void processScreenShare(VirtualMeetingSnapshot vm, String facilitatorCode) throws MalformedURLException, RemoteException {
        PresenterConsoleImpl selectedPresenterConsole = null;

        for (PresenterConsoleImpl presenterConsole : facilitator.getPresenterConsoles()) {
            if (facilitatorCode.equals(vm.getActiveScreenFacilitatorId()) && presenterConsole.getConsoleId().equals(vm.getActiveScreenPresenterId())) {
                selectedPresenterConsole = presenterConsole;
                presenterConsole.getScreenShareConsole().setEnabled(true);
            } else {
                presenterConsole.getScreenShareConsole().setEnabled(false);
            }
        }



        if (selectedPresenterConsole != null) {
            String targetURL = UrlGenerator.generateScreenShareConsoleBufferAccessUrl(facilitatorURL, selectedPresenterConsole.getOutScreenShareConsoleId());
            if (!targetURL.equals(screenSwitcherInputURL)) {
                screenSwitcherInputURL = targetURL;
                screenSwitcher.setInputURL(new URL(screenSwitcherInputURL));
            }
        } else {
            if (screenSwitcherInputURL != null) {
                screenSwitcherInputURL = null;
                screenSwitcher.setInputURL(null);
            }

        }

        //TODO: put update interval logic here
        if (outputScreenShareConsole.isEnabled() && !screenSwitcher.isRunning())
            screenSwitcher.Start();
        else if (!outputScreenShareConsole.isEnabled() && screenSwitcher.isRunning())
            screenSwitcher.Stop();

        boolean inScreenShareEnabled = inScreenShareConsole.isEnabled();
        if (inScreenShareEnabled && !screenReceiver.isReceiving()) {
            screenReceiver.startReceiving();
        } else if (!inScreenShareEnabled && screenReceiver.isReceiving()) {
            screenReceiver.stopReceiving();
        }


    }

    @Override
    public void start()
    {
        keepRunning = true;
        super.start();
    }

    public synchronized ControlLoopListener getControlLoopListener() {
        return controlLoopListener;
    }

    public synchronized void setControlLoopListener(ControlLoopListener controlLoopListener) {
        this.controlLoopListener = controlLoopListener;
    }

    public synchronized int getScreenShareReceiveDelay() {
        return screenShareReceiveDelay;
    }

    public synchronized void setScreenShareReceiveDelay(int screenShareReceiveDelay) {
        this.screenShareReceiveDelay = screenShareReceiveDelay;
        //TODO: screenReceiver.setInterval(value)
    }

    private synchronized void setLastKnownVMSnapshot(VirtualMeetingSnapshot lastKnownVMSnapshot) {
        this.lastKnownVMSnapshot = lastKnownVMSnapshot;
    }

    public synchronized VirtualMeetingSnapshot getLastKnownVMSnapshot() {
        return lastKnownVMSnapshot;
    }

    private synchronized String getFacilitatorConsoleId() {
        return facilitatorConsoleId;
    }


    public synchronized CaptureReceivedListener getCaptureReceivedListener() {
        return captureReceivedListener;
    }

    public synchronized void setCaptureReceivedListener(CaptureReceivedListener captureReceivedListener) {
        this.captureReceivedListener = captureReceivedListener;
    }

    public synchronized boolean isMute() {
        return mute;
    }

    public synchronized void setMute(boolean mute) {
        this.mute = mute;
    }
}
