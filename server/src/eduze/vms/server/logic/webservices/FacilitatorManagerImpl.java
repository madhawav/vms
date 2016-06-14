package eduze.vms.server.logic.webservices;

import eduze.vms.server.logic.PasswordUtil;
import eduze.vms.server.logic.URLGenerator;
import org.apache.log4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.Collection;
import java.util.HashMap;

/**
 * Manage Known Facilitator Devices (Pairing and Un-pairing)
 */
@WebService(endpointInterface= "eduze.vms.server.logic.webservices.FacilitatorManager")
public class FacilitatorManagerImpl implements FacilitatorManager {
    //Server Implementation
    private ServerImpl server = null;

    //HashMap of Known Facilitators
    private HashMap<String,Facilitator> facilitators = new HashMap<>();

    public FacilitatorManagerImpl()
    {

    }

    /**
     * Retrieve Server
     * @param server
     */
    public FacilitatorManagerImpl(ServerImpl server)
    {
        this.server = server;
    }

    /**
     * Retrieve Facilitator using PairKey
     * @param pairKey PairKey of Facilitator
     * @return Facilitator Pair Information
     */
    @WebMethod(exclude = true)
    public Facilitator getFacilitator(String pairKey)
    {
        return facilitators.get(pairKey);
    }

    /**
     * Retrieve list of Facilitators
     * @return List of Facilitators with Pair Information
     */
    @WebMethod(exclude = true)
    public Collection<Facilitator> getFacilitators()
    {
        return facilitators.values();
    }


    /**
     * Pair the Facilitator
     * @param name Name of Facilitator
     * @param password Server Password
     * @return Pair-Key of Facilitator
     * @throws AlreadyPairedException The Facilitator is already paired
     * @throws InvalidServerPasswordException Server Password is invalid
     */
    @Override
    public String pair(String name, String password) throws AlreadyPairedException, InvalidServerPasswordException {
        //Check for already paired status
        for(Facilitator fac:facilitators.values())
        {
            if(fac.getName().equals(name))
                throw new AlreadyPairedException();
        }

        //Verify password
        if(!server.verifyPassword(password))
            throw new InvalidServerPasswordException();

        //Record Pair
        Facilitator facilitator = new Facilitator();
        facilitator.setName(name);
        facilitator.setPairKey(PasswordUtil.hashPairKey(name));
        facilitators.put(facilitator.getPairKey(),facilitator);

        //Notify listeners
        if(server.getPairListener() != null)
            server.getPairListener().onPair(facilitator);

        //Return PairKey
        return  facilitator.getPairKey();
    }

    /**
     * Un-pair the Facilitator
     * @param pairKey PairKey provided to Facilitator
     */
    @Override
    public void unPair(String pairKey) {
        //Locate Facilitator
        if(facilitators.containsKey(pairKey))
        {
            Facilitator facilitator = facilitators.get(pairKey);
            //remove Facilitator
            facilitators.remove(pairKey);

            //Notify listeners
            if(server.getPairListener() != null)
                server.getPairListener().onUnPair(facilitator);
        }
    }

    /**
     * Start the Facilitator Manager Service
     */
    public void start() {
        Logger.getLogger(this.getClass()).info("FacilitatorManager Started " +URLGenerator.generateFacilitatorManagerPublishURL(server.getPort()));
        Endpoint.publish(URLGenerator.generateFacilitatorManagerPublishURL(server.getPort()),this);
    }

    /**
     * Add a known Facilitator to Paired List of Facilitators. Used to load known Facilitators from Persistent Storage
     * @param facilitator Facilitator to be added to pair list
     * @throws IllegalArgumentException if Facilitator is already paired
     */
    public void addFacilitator(Facilitator facilitator)
    {
        if(facilitators.containsKey(facilitator.getPairKey()))
            throw new IllegalArgumentException("Facilitator already paired");
        facilitators.put(facilitator.getPairKey(),facilitator);
    }
}
