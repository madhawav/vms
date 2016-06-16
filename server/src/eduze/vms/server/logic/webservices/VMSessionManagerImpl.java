package eduze.vms.server.logic.webservices;

import eduze.vms.server.logic.ConnectivityManager;
import eduze.vms.server.logic.URLGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Interface for Facilitators to connect to Server
 */
@WebService(endpointInterface= "eduze.vms.server.logic.webservices.VMSessionManager")
public class VMSessionManagerImpl implements VMSessionManager {

    private VirtualMeetingImpl virtualMeeting = null; //virtual meeting hosted by server
    private  ServerImpl server = null; //server
    private ConnectivityManager connectivityManager; //connectivity manager used for automatic disconnection

    public VMSessionManagerImpl() throws Exception {

    }

    /**
     * Adjourn the meeting. Prepare for reset when everyone disconnect.
     * @throws ServerNotReadyException The server is not ready to adjourn meeting
     */
    public void adjournMeeting() throws ServerNotReadyException {
        if(virtualMeeting != null)
            virtualMeeting.adjournMeeting();
        else throw new ServerNotReadyException();
    }

    /**
     * Constructor of Virtual Meeting Session Manager
     * @param server Server WebService
     */
    public VMSessionManagerImpl(ServerImpl server)
    {
        this.server = server;
        this.virtualMeeting = new VirtualMeetingImpl(this);
        connectivityManager = new ConnectivityManager(virtualMeeting,server.getConfiguration().getFacilitatorConnectivityTimeoutInterval());
    }

    /**
     * Retrieve Status of Virtual Meeting in Server
     * @return Virtual Meeting Status
     */
    @Override
    public SessionStatus getStatus() {
        return virtualMeeting.getStatus();
    }

    /**
     * Connect Facilitator to Virtual Meeting
     * @param name Name of Facilitator
     * @param pairKey PairKey provided to facilitator during pairing mechanism
     * @return Connection Result
     * @throws ServerNotReadyException The Server is not ready to accept facilitators
     * @throws MeetingAlreadyStartedException The meeting has been started already. Hence, no new facilitators can join.
     * @throws UnknownFacilitatorException The Facilitator is not known. Considering pairing again.
     * @throws FacilitatorAlreadyConnectedException Facilitator is already connected.
     * @throws MeetingAdjournedException Meeting has been adjourned and hence not accepting new Facilitators
     */
    @Override
    public ConnectionResult connect(String name, String pairKey) throws ServerNotReadyException, MeetingAlreadyStartedException, UnknownFacilitatorException, FacilitatorAlreadyConnectedException, MeetingAdjournedException {
        //Check for compatibility of meeting status
        if(virtualMeeting.getStatus() == SessionStatus.Adjourned)
            throw new MeetingAdjournedException();
        if(virtualMeeting.getStatus()== SessionStatus.MeetingOnline)
            throw new MeetingAlreadyStartedException();
        if(virtualMeeting.getStatus() == SessionStatus.WaitingForSecondFacilitator || virtualMeeting.getStatus()==SessionStatus.WaitingForFirstFacilitator)
        {
            //compatible state
            //check for pairing
            Facilitator facilitator = server.getFacilitatorManager().getFacilitator(pairKey);
            if(facilitator == null)
                throw new UnknownFacilitatorException();

            //update pair information to reflect name changes
            if(!facilitator.getName().equals(name))
            {
                if(server.getPairListener() != null)
                    server.getPairListener().onPairNameChanged(facilitator.getPairKey(),facilitator.name,name);
                facilitator.setName(name);
            }
            //check whether facilitator is already connected
            for(FacilitatorConsoleImpl faci : virtualMeeting.getFacilitatorConsoles())
            {
                if(faci != null)
                    if(faci.getFacilitator().getPairKey().equals(pairKey))
                        throw new FacilitatorAlreadyConnectedException();
            }

            //create and add console. Obtain slot.
            FacilitatorConsoleImpl facilitatorConsole = new FacilitatorConsoleImpl(virtualMeeting,facilitator);
            int slotId = virtualMeeting.addNewFacilitatorConsole(facilitatorConsole);
            facilitatorConsole.initialize(slotId);

            //start the console service
            facilitatorConsole.start();

            //produce connection result
            ConnectionResult result = new ConnectionResult();
            result.setSuccessful(true);

            //add facilitator console id and virutal meeting id to results
            result.setFacilitatorConsoleId(facilitatorConsole.getConsoleId());
            result.setVirtualMeetingConsoleId(virtualMeeting.getVMId());

            //notify listeners on new facilitator connected
            if(server.getFacilitatorSessionListener() != null)
                server.getFacilitatorSessionListener().onConnected(facilitator,facilitatorConsole.getConsoleId());

            return result;
        }
        else
            throw new ServerNotReadyException();
    }

    /**
     * Start WebService
     */
    public void start()
    {
        org.apache.log4j.Logger.getLogger(getClass()).info("VMSession Manager Started " +URLGenerator.generateVMSessionManagerPublishURL(server.getPort()) );
        Endpoint.publish(URLGenerator.generateVMSessionManagerPublishURL(server.getPort()),this);
        virtualMeeting.start();
        connectivityManager.start();
    }

    /**
     * Retrieve Server of VMSessionManager
     * @return
     */
    public ServerImpl getServer() {
        return server;
    }

    /**
     * Reset the virtual meeting so a new meeting can be started.
     */
    public void resetVirtualMeeting() {
        this.virtualMeeting.stop();
        connectivityManager.stop();

        this.virtualMeeting = new VirtualMeetingImpl(this);
        connectivityManager = new ConnectivityManager(virtualMeeting,server.getConfiguration().getFacilitatorConnectivityTimeoutInterval());

        Logger.getLogger("Info").log(Level.INFO,"Virtual Meeting Reset");
        virtualMeeting.start();
        connectivityManager.start();
    }
}
