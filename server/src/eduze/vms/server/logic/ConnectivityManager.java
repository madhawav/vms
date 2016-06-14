package eduze.vms.server.logic;

import eduze.vms.server.logic.webservices.*;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Admin on 6/14/2016.
 */

/**
 * The connectivity manager is responsible for handling connection lifetime of facilitator connections
 */
public class ConnectivityManager {
    //Timeout to drop facilitator connection
    private int connectivityTimeout = 10000;

    //Thread to handle automatic disconnection
    private Thread connectivityThread = null;

    private VirtualMeetingImpl virtualMeeting = null;
    private boolean running = false;

    /**
     * Retrieve the timeout for automatic disconnection of facilitator
     * @return Automatic disconnection timeout for Facilitator
     */
    public int getConnectivityTimeout() {
        return connectivityTimeout;
    }

    /**
     * Set the automatic connection timeout for Facilitator
     * @param connectivityTimeout Automatic connection timeout for facilitator
     */
    public void setConnectivityTimeout(int connectivityTimeout) {
        this.connectivityTimeout = connectivityTimeout;
    }

    /**
     * Constructor for Connectivity
     * @param virtualMeeting Virtual Meeting with Facilitators
     * @param connectivityTimeout Timeout used for Facilitator Connections
     */
    public ConnectivityManager( final VirtualMeetingImpl virtualMeeting, int connectivityTimeout)
    {
        this.virtualMeeting = virtualMeeting;
        this.connectivityTimeout = connectivityTimeout;

        //Setup the connection lifetime thead
        connectivityThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //While allowed to run
                while (isRunning())
                {
                    try {
                        //Switch to UI Thread
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                //Process Connection Lifetime for each Facilitator
                                for(FacilitatorConsoleImpl console : virtualMeeting.getFacilitatorConsoles())
                                {
                                    if(console != null)
                                        processConnectionLifetime(console);
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        //Loop through for interruptions
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    try {
                        //Sleep interval to check for connection lifetime
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Process connectivity lifetime for a Facilitator
     * @param facilitatorConsole
     */
    private void processConnectionLifetime(FacilitatorConsoleImpl facilitatorConsole)
    {
        //Establish inactivity time
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - facilitatorConsole.getLastAliveNotifyTime();
        if(delta > connectivityTimeout)
        {
            //disconnect timedout connections
            facilitatorConsole.disconnect();
        }
    }

    /**
     * Start the Connectivity Manager
     */
    public void start()
    {
        if(isRunning())
            return;
        running = true;
        connectivityThread.start();
    }

    /**
     * Stop the connectivity Manager
     */
    public synchronized void stop()
    {
        running = false;
    }

    /**
     * Retrieve whether the ConnectivityManager is running
     * @return
     */
    public synchronized boolean isRunning() {
        return running;
    }
}
