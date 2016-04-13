package eduze.vms.server.logic.webservices;

import eduze.vms.PasswordUtil;
import eduze.vms.server.logic.URLGenerator;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Madhawa on 12/04/2016.
 */
@WebService(endpointInterface= "eduze.vms.server.logic.webservices.FacilitatorManager")
public class FacilitatorManagerImpl implements FacilitatorManager {
    private ServerImpl server = null;
    private HashMap<String,Facilitator> facilitators = new HashMap<>();

    public FacilitatorManagerImpl()
    {

    }

    public FacilitatorManagerImpl(ServerImpl server)
    {
        this.server = server;
    }

    Facilitator getFacilitator(String pairKey)
    {
        return facilitators.get(pairKey);
    }

    @Override
    public String pair(String name, String password) throws AlreadyPairedException, InvalidServerPasswordException {
        for(Facilitator fac:facilitators.values())
        {
            if(fac.getName().equals(name))
                throw new AlreadyPairedException();
        }

        if(!server.verifyPassword(password))
            throw new InvalidServerPasswordException();

        Facilitator facilitator = new Facilitator();
        facilitator.setName(name);
        facilitator.setPairKey(PasswordUtil.hashPairKey(name));
        facilitators.put(facilitator.getPairKey(),facilitator);

        if(server.getPairListener() != null)
            server.getPairListener().onPair(facilitator);

        return  facilitator.getPairKey();
    }

    @Override
    public void unPair(String pairKey) {
        if(facilitators.containsKey(pairKey))
            facilitators.remove(pairKey);

    }


    public void start() {
        System.out.println("FacilitatorManager Started " +URLGenerator.generateFacilitatorManagerPublishURL(server.getPort()) );
        Endpoint.publish(URLGenerator.generateFacilitatorManagerPublishURL(server.getPort()),this);
    }

    /**
     * Use this method to load list of facilitators from persistent storage
     * @param faci
     */
    public void addFacilitator(Facilitator faci)
    {
        if(facilitators.containsKey(faci.getPairKey()))
            throw new IllegalArgumentException("Facilitator already paired");
        facilitators.put(faci.getPairKey(),faci);
    }
}
