package eduze.vms.server.logic;

import eduze.vms.server.logic.webservices.*;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Admin on 6/14/2016.
 */
public class ConnectivityManager {
    private int connectivityTimeout = 10000;

    private Thread connectivityThread = null;

    private VirtualMeetingImpl virtualMeeting = null;
    private boolean running = false;

    public int getConnectivityTimeout() {
        return connectivityTimeout;
    }

    public void setConnectivityTimeout(int connectivityTimeout) {
        this.connectivityTimeout = connectivityTimeout;
    }

    public ConnectivityManager( final VirtualMeetingImpl virtualMeeting, int connectivityTimeout)
    {
        this.virtualMeeting = virtualMeeting;
        this.connectivityTimeout = connectivityTimeout;
        connectivityThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning())
                {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                for(FacilitatorConsoleImpl console : virtualMeeting.getFacilitatorConsoles())
                                {
                                    if(console != null)
                                        processConnectionLifetime(console);
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void processConnectionLifetime(FacilitatorConsoleImpl facilitatorConsole)
    {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - facilitatorConsole.getLastAliveNotifyTime();
        if(delta > connectivityTimeout)
        {
            facilitatorConsole.disconnect();
        }
    }

    public void start()
    {
        running = true;
        connectivityThread.start();
    }
    public synchronized void stop()
    {
        running = false;
    }

    public synchronized boolean isRunning() {
        return running;
    }
}
