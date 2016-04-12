package eduze.vms.server.logic;

import eduze.vms.server.logic.webservices.Facilitator;

import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */

public interface PairListener {
    void onPair(Facilitator pairedFacilitator);
    void onUnPair(Facilitator pairedFacilitator);
    void onPairNameChanged(String pairKey, String oldName, String newName);
}
