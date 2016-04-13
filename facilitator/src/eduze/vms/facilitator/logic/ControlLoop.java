package eduze.vms.facilitator.logic;

import eduze.livestream.Multiplexer;
import eduze.vms.facilitator.logic.mpi.facilitatorconsole.FacilitatorConsole;

import eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsoleImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.VirtualMeetingSnapshot;
import eduze.vms.facilitator.logic.webservices.FacilitatorImpl;
import eduze.vms.facilitator.logic.webservices.PresenterConsoleImpl;

import javax.swing.*;
import javax.xml.rpc.ServiceException;
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
    private Multiplexer switcher = null;
    private String switcherInputURL = null;
    private FacilitatorImpl facilitator = null;

    private FacilitatorConsole facilitatorConsole = null;
    private eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsole outputConsole = null;
    private String outScreenShareConsoleId = null;
    private String serverURL = null;

    private String facilitatorURL = null;
    private ControlLoopListener controlLoopListener = null;


    public ControlLoop(FacilitatorImpl facilitator, String serverUrl, int facilitatorPort) throws MalformedURLException, ServerConnectionException {
        this.facilitatorURL = UrlGenerator.generateLocalURL(facilitatorPort);
        this.serverURL = UrlGenerator.extractURL(serverUrl);
        this.facilitator = facilitator;
        this.facilitatorConsole = facilitator.getFacilitatorConsole();
        try {
            outScreenShareConsoleId = facilitatorConsole.getOutScreenShareConsoleId();
            switcher = new Multiplexer(new URL(UrlGenerator.generateScreenShareConsoleBufferAccessUrl(serverUrl,outScreenShareConsoleId)));
            eduze.vms.facilitator.logic.mpi.screenshareconsole.ScreenShareConsoleImplServiceLocator screenShareConsoleImplServiceLocator = new ScreenShareConsoleImplServiceLocator();
            outputConsole = screenShareConsoleImplServiceLocator.getScreenShareConsoleImplPort(new URL(UrlGenerator.generateScreenShareConsoleAccessUrl(serverUrl,outScreenShareConsoleId)));
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ServerConnectionException(e);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServerConnectionException(e);
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
                            String facilitatorCode = facilitatorConsole.getConsoleId();
                            if(facilitatorCode.equals(vm.getActiveScreenFacilitatorId()))
                            {
                                for(PresenterConsoleImpl presenterConsole : facilitator.getPresenterConsoles())
                                {
                                    if(presenterConsole.getConsoleId().equals(vm.getActiveScreenPresenterId()))
                                    {
                                        selectedPresenterConsole = presenterConsole;
                                        presenterConsole.getScreenShareConsole().setEnabled(true);
                                    }
                                    else
                                    {
                                        presenterConsole.getScreenShareConsole().setEnabled(false);
                                    }
                                }
                            }

                            if(selectedPresenterConsole != null)
                            {
                                String targetURL = UrlGenerator.generateScreenShareConsoleBufferAccessUrl(facilitatorURL,selectedPresenterConsole.getOutScreenShareConsoleId());
                                if(!targetURL.equals(switcherInputURL))
                                {
                                    switcherInputURL = targetURL;
                                    switcher.setInputURL(new URL(switcherInputURL));
                                }
                            }
                            else
                            {
                                if(switcherInputURL != null)
                                {
                                    switcherInputURL = null;
                                    switcher.setInputURL(null);
                                }

                            }

                            //TODO: put update interval logic here
                            if(outputConsole.isEnabled() && !switcher.isRunning())
                                switcher.Start();
                            else if(!outputConsole.isEnabled() && switcher.isRunning())
                                switcher.Stop();

                            if(controlLoopListener != null)
                            {
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
}
