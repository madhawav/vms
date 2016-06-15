package eduze.vms.facilitator.logic;

import eduze.vms.facilitator.logic.mpi.facilitatormanager.AlreadyPairedException;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManager;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.FacilitatorManagerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.facilitatormanager.InvalidServerPasswordException;
import eduze.vms.facilitator.logic.mpi.server.Server;
import eduze.vms.facilitator.logic.mpi.server.ServerImplServiceLocator;
import eduze.vms.facilitator.logic.mpi.vmsessionmanager.*;
import sun.security.util.Password;

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

/**
 * Manager for Retrieving Server Details, Pairing and Establishing Facilitator Server Connection
 */
public class ServerManager {
    //List of paired servers
    private HashMap<String,PairedServer> pairedServers = new HashMap<>();
    //Facilitator Controller
    private FacilitatorController facilitatorController;

    ServerManager(FacilitatorController controller)
    {
        this.facilitatorController = controller;
    }

    FacilitatorController getFacilitatorController()
    {
        return facilitatorController;
    }

    /**
     * Add a known server loaded from persistent storage
     * @param serverName Server Name
     * @param serverURL Server URL
     * @param pairKey Server Secret Key
     */
    public void addPairedServer(String serverName, String serverURL, String pairKey)
    {
        pairedServers.put(serverName,new PairedServer(serverName,serverURL,pairKey));
    }

    /**
     * Retrieve List of paired servers
     * @return
     */
    public Collection<PairedServer> getPairedServers()
    {
        return pairedServers.values();
    }

    /**
     * Retrieve Paired server from server name
     * @param name
     * @return
     */
    public PairedServer getPairedServerFromName(String name)
    {
        return pairedServers.get(name);
    }

    /**
     * Retrieve Server name from Server hosted at given server URL
     * @param serverURL URL of server
     * @return Server Name
     * @throws MalformedURLException
     * @throws ServerConnectionException
     */
    public String getServerName(String serverURL) throws MalformedURLException, ServerConnectionException{
        //Complete Server URL
        String url = UrlGenerator.extractURL(serverURL);
        //Create Service Locator
        ServerImplServiceLocator locator = new ServerImplServiceLocator();
        try
        {
            //Connect to Server
            Server server = locator.getServerImplPort(new URL(UrlGenerator.generateServerAccessURL(url)));
            //Retrieve Server Name
            return  server.getServerName();

        }

        catch (ServiceException e)
        {
            throw new ServerConnectionException(e);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }

    }

    /**
     * Pair a Server to Facilitator
     * @param serverURL Server URL
     * @param serverPassword Password to be submitted to server
     * @return PairedServer with details of server
     * @throws MalformedURLException Invalid Server URL
     * @throws ServerConnectionException Error in connection to server
     * @throws AlreadyPairedException Already paired
     * @throws InvalidServerPasswordException Invalid Server Password
     */
    public PairedServer pair(String serverURL, char[] serverPassword) throws MalformedURLException, ServerConnectionException, AlreadyPairedException, InvalidServerPasswordException {
        //Complete Server URL
        String url = UrlGenerator.extractURL(serverURL);
        ServerImplServiceLocator locator = new ServerImplServiceLocator();
        FacilitatorManagerImplServiceLocator facilitatorManagerImplServiceLocator = new FacilitatorManagerImplServiceLocator();
        try
        {
            //Obtain Server service
            Server server = locator.getServerImplPort(new URL(UrlGenerator.generateServerAccessURL(url)));
            //Obtain Facilitator Manager of Server
            FacilitatorManager facilitatorManager = facilitatorManagerImplServiceLocator.getFacilitatorManagerImplPort(new URL(UrlGenerator.generateFacilitatorManagerAccessURL(url)));
            //Request pair
            String pairKey = facilitatorManager.pair(getFacilitatorController().getConfiguration().getName(), eduze.vms.foundation.logic.PasswordUtil.hashServerPassword(serverPassword));
            //Create a paired server with pair details
            PairedServer pairedServer = new PairedServer(server.getServerName(), url, pairKey);
            //store paired server
            pairedServers.put(pairedServer.getServerName(),pairedServer);
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

    /**
     * Unpair a server from facilitator
     * @param server Paired Server
     * @throws ServerConnectionException
     * @throws MalformedURLException
     */
    public void unPair(PairedServer server) throws ServerConnectionException, MalformedURLException {
        //Obtain Server URL
        String url = server.getServerURL();
        //Obain Facilitator Manager
        FacilitatorManagerImplServiceLocator facilitatorManagerImplServiceLocator = new FacilitatorManagerImplServiceLocator();
        try
        {
            FacilitatorManager facilitatorManager = facilitatorManagerImplServiceLocator.getFacilitatorManagerImplPort(new URL(UrlGenerator.generateFacilitatorManagerAccessURL(url)));
            //request unpair
            facilitatorManager.unPair(server.getServerPairKey());

        }

        catch (ServiceException e)
        {
            throw new ServerConnectionException(e);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
        finally {
            //forget device
            pairedServers.remove(server.getServerName());
        }


    }

    /**
     * Connect to Server and obtain Server Connection Controller
     * @param server Server URL
     * @return Server Connection Controller
     * @throws MalformedURLException Invalid Server URL
     * @throws ServerConnectionException Server Connection Error
     * @throws ServerNotReadyException Server busy
     * @throws MeetingAlreadyStartedException Meeting has started already. Connection refused
     * @throws UnknownFacilitatorException Facilitator has not paired previously
     * @throws FacilitatorAlreadyConnectedException Facilitator is already connected
     */
    public ServerConnectionController connect(PairedServer server) throws MalformedURLException, ServerConnectionException, ServerNotReadyException, MeetingAlreadyStartedException, UnknownFacilitatorException, FacilitatorAlreadyConnectedException, MeetingAdjournedException {
        //Obtain server url
        String url = server.getServerURL();

        VMSessionManagerImplServiceLocator vmSessionManagerImplServiceLocator = new VMSessionManagerImplServiceLocator();
        try
        {
            //Obtain Virtual Meeting Session Manager MPI
            VMSessionManager sessionManager = vmSessionManagerImplServiceLocator.getVMSessionManagerImplPort(new URL(UrlGenerator.generateVMSessionManagerAccessURL(url)));
            //Request connection
            ConnectionResult result= sessionManager.connect(getFacilitatorController().getConfiguration().getName(),server.getServerPairKey());
            if(result.isSuccessful())
            {
                //Connection successful
                facilitatorController.notifyServerConnected(url,result);
                return getFacilitatorController().getServerConnectionController();
            }
            else
            {
                throw new ServerConnectionException(new Exception("Server actively refused connection"));
            }


        }
        catch (ServerNotReadyException e)
        {
            throw e;
        }
        catch (MeetingAdjournedException e){
            throw e;
        }
        catch (MeetingAlreadyStartedException e)
        {
            throw e;
        }
        catch(UnknownFacilitatorException e)
        {
            throw e;
        }
        catch(FacilitatorAlreadyConnectedException e)
        {
            throw e;
        }
        catch (ServiceException e)
        {
            throw new ServerConnectionException(e);
        } catch (RemoteException e) {
            throw new ServerConnectionException(e);
        }
    }


    /**
     * A paired server known by facilitator
     */
    public static class PairedServer implements java.io.Serializable
    {
        private String serverName; //Server Name
        private String serverURL; //Server URL
        private String serverPairKey; //Server Pair Key

        /**
         * Constructor
         * @param serverName Server Name
         * @param serverURL Server URL
         * @param serverPairKey Server Password
         */
        public PairedServer(String serverName,String serverURL, String serverPairKey)
        {
            this.serverURL = serverURL;
            this.serverName = serverName;
            this.serverPairKey = serverPairKey;
        }

        /**
         * Retrieve Server Name
         * @return
         */
        public String getServerName() {
            return serverName;
        }

        /**
         * Set the Server Name
         * @param serverName
         */
        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        /**
         * Retrieve Server Pair Key
         * @return Server Pair Key
         */
        public String getServerPairKey() {
            return serverPairKey;
        }

        /**
         * Set Server Pair Key
         * @param serverPairKey
         */
        public void setServerPairKey(String serverPairKey) {
            this.serverPairKey = serverPairKey;
        }

        /**
         * Retrieve Server URL
         * @return
         */
        public String getServerURL() {
            return serverURL;
        }

        /**
         * Set Server URL
         * @param serverURL
         */
        public void setServerURL(String serverURL) {
            this.serverURL = serverURL;
        }
    }
}
