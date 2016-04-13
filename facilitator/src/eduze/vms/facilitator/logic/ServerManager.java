package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.facilitatormanager.AlreadyPairedException;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManager;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.InvalidServerPasswordException;
import eduze.vms.facilitator.logic.mpi.server.Server;
import eduze.vms.facilitator.logic.mpi.server.ServerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.ConnectionResult;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.VMSessionManager;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.VMSessionManagerImplServiceLocator;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Madhawa on 13/04/2016.
 */
public class ServerManager {
    private HashMap<String,PairedServer> pairedServers = new HashMap<>();
    private FacilitatorController facilitatorController;

    ServerManager(FacilitatorController controller)
    {
        this.facilitatorController = controller;
    }

    FacilitatorController getFacilitatorController()
    {
        return facilitatorController;
    }

    public void addPairedServer(String serverName, String serverURL, String pairKey)
    {
        pairedServers.put(serverName,new PairedServer(serverName,serverURL,pairKey));
    }

    public Collection<PairedServer> getPairedServers()
    {
        return pairedServers.values();
    }

    public String getServerName(String serverURL) throws MalformedURLException, ServerConnectionException {
        String url = UrlGenerator.extractURL(serverURL);
        ServerImplServiceLocator locator = new ServerImplServiceLocator();

        try
        {
            Server server = locator.getServerImplPort(new URL(UrlGenerator.generateServerAccessURL(url)));

            return  server.getServerName();

        }
        catch (ServiceException e)
        {
            throw new ServerConnectionException(e);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }

    }

    public PairedServer pair(String serverURL, String serverPassword) throws MalformedURLException, ServerConnectionException, AlreadyPairedException, InvalidServerPasswordException {
        String url = UrlGenerator.extractURL(serverURL);
        ServerImplServiceLocator locator = new ServerImplServiceLocator();
        FacilitatorManagerImplServiceLocator facilitatorManagerImplServiceLocator = new FacilitatorManagerImplServiceLocator();
        try
        {
            Server server = locator.getServerImplPort(new URL(UrlGenerator.generateServerAccessURL(url)));
            FacilitatorManager facilitatorManager = facilitatorManagerImplServiceLocator.getFacilitatorManagerImplPort(new URL(UrlGenerator.generateFacilitatorManagerAccessURL(url)));
            String pairKey = facilitatorManager.pair(getFacilitatorController().getConfiguration().getName(),serverPassword);
            PairedServer pairedServer = new PairedServer(server.getServerName(), url, pairKey);
            return  pairedServer;
        }
        catch (AlreadyPairedException e)
        {
            throw e;
        }
        catch (InvalidServerPasswordException e)
        {
            throw  e;
        }
        catch (ServiceException e)
        {
            throw new ServerConnectionException(e);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }

    public void unPair(PairedServer server) throws ServerConnectionException, MalformedURLException {
        String url = server.getServerURL();
        FacilitatorManagerImplServiceLocator facilitatorManagerImplServiceLocator = new FacilitatorManagerImplServiceLocator();
        try
        {
            FacilitatorManager facilitatorManager = facilitatorManagerImplServiceLocator.getFacilitatorManagerImplPort(new URL(UrlGenerator.generateFacilitatorManagerAccessURL(url)));
            facilitatorManager.unPair(server.getServerPairKey());
            pairedServers.remove(server);
        }

        catch (ServiceException e)
        {
            throw new ServerConnectionException(e);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }


    }


    public ServerConnectionController connect(PairedServer server) throws MalformedURLException, ServerConnectionException {
        String url = server.getServerURL();
        VMSessionManagerImplServiceLocator vmSessionManagerImplServiceLocator = new VMSessionManagerImplServiceLocator();
        try
        {
            VMSessionManager sessionManager = vmSessionManagerImplServiceLocator.getVMSessionManagerImplPort(new URL(UrlGenerator.generateVMSessionManagerAccessURL(url)));
            ConnectionResult result= sessionManager.connect(getFacilitatorController().getConfiguration().getName(),server.getServerPairKey());
            if(result.isSuccessful())
            {
                ServerConnectionController connectionController = new ServerConnectionController(getFacilitatorController(),url,result);
                return connectionController;
            }
            else
            {
                throw new ServerConnectionException(new Exception("Server actively refused connection"));
            }


        }

        catch (ServiceException e)
        {
            throw new ServerConnectionException(e);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }


    public static class PairedServer
    {
        private String serverName;
        private String serverURL;
        private String serverPairKey;

        public PairedServer(String serverName,String serverURL, String serverPairKey)
        {
            this.serverURL = serverURL;
            this.serverName = serverName;
            this.serverPairKey = serverPairKey;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getServerPairKey() {
            return serverPairKey;
        }

        public void setServerPairKey(String serverPairKey) {
            this.serverPairKey = serverPairKey;
        }

        public String getServerURL() {
            return serverURL;
        }

        public void setServerURL(String serverURL) {
            this.serverURL = serverURL;
        }
    }
}
