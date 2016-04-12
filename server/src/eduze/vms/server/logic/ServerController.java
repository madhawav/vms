package eduze.vms.server.logic;

import eduze.vms.server.logic.webservices.Facilitator;
import eduze.vms.server.logic.webservices.FacilitatorConsoleImpl;
import eduze.vms.server.logic.webservices.ServerImpl;

import javax.xml.ws.Endpoint;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Madhawa on 12/04/2016.
 */
public class ServerController {

    private ArrayList<PairListener> pairListeners = new ArrayList<>();
    private ArrayList<FacilitatorSessionListener> facilitatorSessionListeners = new ArrayList<>();

    private ServerImpl.Configuration startConfig = new ServerImpl.Configuration();
    private ServerImpl serverService = null;

    private boolean running = false;

    public ServerController()
    {

    }

    public int getPort() {
        return startConfig.getPort();
    }

    public void setPort(int port) throws Exception {
        if(running)
            throw new Exception("Already Started");
        startConfig.setPort(port);
    }

    public boolean isRunning()
    {
        return running;
    }

    public void start() throws URISyntaxException {
        serverService = new ServerImpl(startConfig);

        serverService.setPairListener(new PairListener() {
            @Override
            public void onPair(Facilitator pairedFacilitator) {
                notifyPairAdded(pairedFacilitator);
            }

            @Override
            public void onUnPair(Facilitator pairedFacilitator) {
                notifyPairRemoved(pairedFacilitator);
            }

            @Override
            public void onPairNameChanged(String pairKey, String oldName, String newName) {
                notifyPairNameChanged(pairKey, oldName, newName);
            }
        });

        serverService.setFacilitatorSessionListener(new FacilitatorSessionListener() {
            @Override
            public void onConnected(FacilitatorConsoleImpl console) {
                notifyFacilitatorConnected(console);
            }

            @Override
            public void onDisconnected(Facilitator facilitator, String consoleId) {
                notifyFacilitatorDisconnected(facilitator,consoleId);
            }
        });

        serverService.start();
        running = true;
    }

    private void notifyPairNameChanged(String pairKey, String oldName, String newName) {
        for(PairListener p : pairListeners)
            p.onPairNameChanged(pairKey,oldName,newName);
    }

    private void notifyFacilitatorConnected(FacilitatorConsoleImpl console) {
        for(FacilitatorSessionListener fsp : facilitatorSessionListeners)
            fsp.onConnected(console);
    }

    private void notifyFacilitatorDisconnected(Facilitator facilitator, String console_id) {
        for(FacilitatorSessionListener fsp : facilitatorSessionListeners)
            fsp.onDisconnected(facilitator,console_id);
    }

    public String getName() {
        if(isRunning())
            return startConfig.getName();
        else
            return serverService.getServerName();
    }

    public void setName(String name) throws Exception {
        if(running)
            throw new Exception("Already Started");
        this.startConfig.setName(name);
    }


    public void setPassword(String password) throws Exception {
        if(running)
            throw new Exception("Already Started");
        this.startConfig.setPassword(password);
    }

    public void addPairListener(PairListener p)
    {
        pairListeners.add(p);
    }

    public void removePairListener(PairListener p)
    {
        pairListeners.remove(p);
    }

    public void addFacilitatorSessionListener(FacilitatorSessionListener p)
    {
        facilitatorSessionListeners.add(p);
    }

    public void removeFacilitatorSessionListener(FacilitatorSessionListener p)
    {
        facilitatorSessionListeners.remove(p);
    }

    void notifyPairAdded(Facilitator pair)
    {
        for(PairListener p : pairListeners)
            p.onPair(pair);
    }
    void notifyPairRemoved(Facilitator pair)
    {
        for(PairListener p : pairListeners)
            p.onUnPair(pair);
    }
}
