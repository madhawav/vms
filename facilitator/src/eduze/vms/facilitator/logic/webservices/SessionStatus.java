package eduze.vms.facilitator.logic.webservices;

import javax.xml.bind.annotation.XmlEnum;


import javax.xml.bind.annotation.XmlEnum;

/**
 * Created by Madhawa on 12/04/2016.
 */
@XmlEnum
public enum SessionStatus {
    NotReady,
    WaitingForFirstFacilitator,
    WaitingForSecondFacilitator,
    MeetingOnline
}
