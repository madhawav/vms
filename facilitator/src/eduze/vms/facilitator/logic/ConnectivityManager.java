package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.webservices.PresenterConsoleImpl;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by Admin on 6/13/2016.
 */
public class ConnectivityManager {
    private FacilitatorController controller;
    private Thread workerThread = null;
    private boolean running = false;
    private int updateInterval = 1000;
    private ArrayList<ServerConnectivityListener> serverConnectivityListeners = new ArrayList<>();

    private long serverSignalTime = -1;
    private ServerConnectivityStatus serverConnectivityStatus = ServerConnectivityStatus.Disconnected;

    public synchronized boolean isRunning() {
        return running;
    }


    public ConnectivityManager(final FacilitatorController controller)
    {
        this.controller = controller;
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning())
                {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                for(PresenterConsoleImpl console : controller.getFacilitatorService().getPresenterConsoles())
                                {
                                    console.updateConnectionStatus();
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
        });
    }

    public synchronized int getUpdateInterval() {
        return updateInterval;
    }

    public synchronized void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }



    public void start()
    {
        running = true;
        workerThread.start();
    }

    public synchronized void stop()
    {
        running = false;
    }

    public void addServerConnectivityListener(ServerConnectivityListener listener)
    {
        serverConnectivityListeners.add(listener);
    }

    public void removeServerConnectivityListener(ServerConnectivityListener listener)
    {
        serverConnectivityListeners.remove(listener);
    }

    public void recordServerConnected()
    {
        serverConnectivityStatus = ServerConnectivityStatus.Online;
        this.serverSignalTime = System.currentTimeMillis();
    }

    public void recordSignal()
    {
        this.serverSignalTime = System.currentTimeMillis();
    }

    public void recordServerDisconnected()
    {
        this.serverConnectivityStatus = ServerConnectivityStatus.Disconnected;
    }

    private void notifyServerDisconnected()
    {
        for(ServerConnectivityListener listener : serverConnectivityListeners)
        {
            listener.onConnectionTerminated();
        }
    }

    private void notifyServerPaused()
    {
        for(ServerConnectivityListener listener : serverConnectivityListeners)
        {
            listener.onConnectionPause();
        }
    }

    private void notifyServerResumed()
    {
        for(ServerConnectivityListener listener : serverConnectivityListeners)
        {
            listener.onConnectionResumed();
        }
    }

    private void notifyPauseUpdate()
    {
        for(ServerConnectivityListener listener : serverConnectivityListeners)
        {
            listener.onConnectionPauseUpdate();
        }
    }

    public long serverPauseTime()
    {
        if(serverConnectivityStatus == ServerConnectivityStatus.Paused)
        {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - serverSignalTime;
            return deltaTime;
        }
        return -1;
    }

    public void serverPulse()
    {
        if(serverConnectivityStatus == ServerConnectivityStatus.Disconnected)
            return;
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - serverSignalTime;

        ServerConnectivityStatus nextStatus = ServerConnectivityStatus.Online;
        if(deltaTime > controller.getConfiguration().getServerConnectionLostTimeout())
        {
            nextStatus = ServerConnectivityStatus.Disconnected;
        }
        else if(deltaTime > controller.getConfiguration().getServerConnectionPauseTimeout())
        {
            nextStatus = ServerConnectivityStatus.Paused;
        }
        if(nextStatus == ServerConnectivityStatus.Disconnected && serverConnectivityStatus != ServerConnectivityStatus.Disconnected)
        {
            serverConnectivityStatus = nextStatus;
            try {
                controller.getServerConnectionController().disconnect();
            } catch (ServerConnectionException e) {
                e.printStackTrace();
            }
            notifyServerDisconnected();
        }
        else if(nextStatus == ServerConnectivityStatus.Paused && serverConnectivityStatus == ServerConnectivityStatus.Online)
        {
            serverConnectivityStatus = nextStatus;
            notifyServerPaused();
        }
        else if(nextStatus == ServerConnectivityStatus.Online && serverConnectivityStatus == ServerConnectivityStatus.Paused)
        {
            serverConnectivityStatus = nextStatus;
            notifyServerResumed();
        }
        else if(nextStatus == ServerConnectivityStatus.Paused && serverConnectivityStatus == ServerConnectivityStatus.Paused)
        {
            notifyPauseUpdate();
        }

        serverConnectivityStatus = nextStatus;
    }

    public ServerConnectivityStatus getServerConnectivityStatus() {
        return serverConnectivityStatus;
    }

    public interface ServerConnectivityListener
    {
        public void onConnectionPause();
        public void onConnectionResumed();
        public void onConnectionTerminated();
        public void onConnectionPauseUpdate();
    }

    public enum ServerConnectivityStatus
    {
        Online, Paused, Disconnected
    }
}