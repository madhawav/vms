package eduze.vms.server.logic;

import eduze.vms.server.logic.webservices.Facilitator;

import javax.jws.WebService;

/**
 * Created by Madhawa on 12/04/2016.
 */

/**
 * Listener interface on paired facilitators. Listen to these events and modify persistent storage accordingly.
 */
public interface PairListener {
    /**
     * raised when a new facilitator is paired.
     * @param pairedFacilitator Details on newly paired facilitator
     */
    public void onPair(Facilitator pairedFacilitator);

    /**
     * raised when a facilitator is un-paired
     * @param pairedFacilitator Details on un-paired facilitator
     */
    public void onUnPair(Facilitator pairedFacilitator);

    /**
     * Raised when the name of a paired facilitator is changed. Incur when an already paired facilitator connect using a different facilitator name
     * @param pairKey Pair-key of modified facilitator
     * @param oldName Previous name of facilitator
     * @param newName New name of facilitator
     */
    public void onPairNameChanged(String pairKey, String oldName, String newName);
}
