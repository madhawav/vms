package eduze.vms.presenter.logic;

import org.omg.CORBA.Environment;
import org.omg.CORBA.SystemException;

import java.util.ArrayList;

/**
 * Created by Admin on 6/13/2016.
 */
public class FacilitatorConnectivityMonitor {
    private long disconnectTimeout = 5000;
    private long pauseTimeout = 1000;
    private long signalTime = -1;
    private ConnectionStatus connectionStatus = ConnectionStatus.Online;
    private ArrayList<ConnectivityListener> connectivityListeners = new ArrayList<>();

    FacilitatorConnectivityMonitor(long disconnectTimeout, long pauseTimeout)
    {
        this.disconnectTimeout = disconnectTimeout;
        this.pauseTimeout = pauseTimeout;
        signalTime = System.currentTimeMillis();
    }

    void recordSignal()
    {
        signalTime = System.currentTimeMillis();
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public long getPauseTime()
    {
        if(connectionStatus == ConnectionStatus.Paused)
        {
            long currentTime = System.currentTimeMillis();
            long delta = currentTime - signalTime;
            return delta;
        }
        else return -1;
    }

    void pulse()
    {
        if(connectionStatus == ConnectionStatus.Disconnected)
            return;

        long currentTime = System.currentTimeMillis();
        long delta = currentTime - signalTime;
        ConnectionStatus newStatus = ConnectionStatus.Online;
        if(delta > disconnectTimeout)
            newStatus = ConnectionStatus.Disconnected;
        else if(delta > pauseTimeout)
            newStatus = ConnectionStatus.Paused;
        if(newStatus == ConnectionStatus.Disconnected && connectionStatus != ConnectionStatus.Disconnected)
        {
            connectionStatus = newStatus;
            notifyDisconnected();
        }else if(newStatus == ConnectionStatus.Paused && connectionStatus == ConnectionStatus.Online)
        {
            connectionStatus = newStatus;
            notifyPaused();
        }
        else if(newStatus == ConnectionStatus.Online && connectionStatus == ConnectionStatus.Paused)
        {
            connectionStatus = newStatus;
            notifyResumed();
        }
        else if(newStatus == ConnectionStatus.Paused)
        {
            connectionStatus = newStatus;
            notifyPausePulsed();
        }
        connectionStatus = newStatus;
        notifyUpdated();
    }

    private void notifyResumed() {
        for(ConnectivityListener listener : connectivityListeners)
            listener.onConnectionResumed();
    }

    private void notifyUpdated() {
        for(ConnectivityListener listener : connectivityListeners)
            listener.onUpdated();
    }
    private void notifyPaused() {
        for(ConnectivityListener listener : connectivityListeners)
            listener.onConnectionPaused();
    }

    private void notifyPausePulsed() {
        for(ConnectivityListener listener : connectivityListeners)
            listener.onPausePulse();
    }

    private void notifyDisconnected() {
        for(ConnectivityListener listener : connectivityListeners)
            listener.onConnectionTerminated();
    }

    public void addConnectivityListener(ConnectivityListener listener)
    {
        connectivityListeners.add(listener);
    }

    public void removeConnectivityListener(ConnectivityListener listener)
    {
        connectivityListeners.remove(listener);
    }

    public static enum ConnectionStatus
    {
        Online, Paused, Disconnected
    }
    public static interface ConnectivityListener
    {
        public void onConnectionTerminated();
        public void onConnectionPaused();
        public void onConnectionResumed();
        public void onUpdated();
        public void onPausePulse();
    }
}
