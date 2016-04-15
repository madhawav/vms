package eduze.vms.server.logic.webservices;

import eduze.vms.server.logic.URLGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService(endpointInterface= "eduze.vms.server.logic.webservices.VMSessionManager")
public class VMSessionManagerImpl implements VMSessionManager {

    private VirtualMeetingImpl virtualMeeting = null;
    private  ServerImpl server = null;
    public VMSessionManagerImpl() throws Exception {

    }

    public VMSessionManagerImpl(ServerImpl server)
    {
        this.server = server;
        this.virtualMeeting = new VirtualMeetingImpl(this);
    }

    @Override
    public SessionStatus getStatus() {
        return virtualMeeting.getStatus();
    }


    @Override
    public ConnectionResult connect(String name, String pairKey) throws ServerNotReadyException, MeetingAlreadyStartedException, UnknownFacilitatorException, FacilitatorAlreadyConnectedException {

        if(virtualMeeting.getStatus()== SessionStatus.MeetingOnline)
            throw new MeetingAlreadyStartedException();
        if(virtualMeeting.getStatus() == SessionStatus.WaitingForSecondFacilitator || virtualMeeting.getStatus()==SessionStatus.WaitingForFirstFacilitator)
        {
            Facilitator facilitator = server.getFacilitatorManager().getFacilitator(pairKey);
            if(facilitator == null)
                throw new UnknownFacilitatorException();

            if(!facilitator.getName().equals(name))
            {
                if(server.getPairListener() != null)
                    server.getPairListener().onPairNameChanged(facilitator.getPairKey(),facilitator.name,name);
                facilitator.setName(name);
            }

            for(FacilitatorConsoleImpl faci : virtualMeeting.getFacilitatorConsoles())
            {
                if(faci != null)
                    if(faci.getFacilitator().getPairKey().equals(pairKey))
                        throw new FacilitatorAlreadyConnectedException();
            }

            FacilitatorConsoleImpl facilitatorConsole = new FacilitatorConsoleImpl(virtualMeeting,facilitator);
            int slotId = virtualMeeting.addNewFacilitatorConsole(facilitatorConsole);
            facilitatorConsole.initialize(slotId);

            facilitatorConsole.start();
            virtualMeeting.updateStatus();

            ConnectionResult result = new ConnectionResult();
            result.setSuccessful(true);
            result.setFacilitatorConsoleId(facilitatorConsole.getConsoleId());
            result.setVirtualMeetingConsoleId(virtualMeeting.getVMId());


            if(server.getFacilitatorSessionListener() != null)
                server.getFacilitatorSessionListener().onConnected(facilitatorConsole);

            return result;
        }
        else
            throw new ServerNotReadyException();
    }

    public void start()
    {
        System.out.println("VMSession Manager Started " +URLGenerator.generateVMSessionManagerPublishURL(server.getPort()) );
        Endpoint.publish(URLGenerator.generateVMSessionManagerPublishURL(server.getPort()),this);
        virtualMeeting.start();
    }

    public ServerImpl getServer() {
        return server;
    }
}
