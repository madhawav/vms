package eduze.vms.facilitator.logic;

import eduze.livestream.Multiplexer;
import eduze.livestream.ScreenReceiver;
import eduze.livestream.exchange.client.FrameBuffer;
import eduze.livestream.exchange.client.FrameBufferImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsole;

import eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsoleImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeeting;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;
import eduze.vms.facilitator.logic.webservices.PresenterConsoleImpl;
import javafx.stage.Screen;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.rpc.ServiceException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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

    private String screenSwitcherInputURL = null;
    private FacilitatorImpl facilitator = null;

    private String facilitatorConsoleId  =  null;
    private FacilitatorConsole facilitatorConsole = null;
    private eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsole outputScreenShareConsole = null;
    private String outScreenShareConsoleId = null;

    private eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsole inScreenShareConsole = null;
    private String inScreenShareConsoleId = null;
    private FrameBuffer inScreenShareBuffer = null;
    private ScreenReceiver screenReceiver = null;

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
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ServerConnectionException(e);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServerConnectionException(e);
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
                            PresenterConsoleImpl selectedPresenterConsole = null;
                            String facilitatorCode = getFacilitatorConsoleId();

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

    public synchronized void setLastKnownVMSnapshot(VirtualMeetingSnapshot lastKnownVMSnapshot) {
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
}
