package eduze.vms.presenter.logic;

import javax.xml.rpc.ServiceException;

/**
 * Created by Madhawa on 14/04/2016.
 */
public class FacilitatorConnectionException extends Exception {
    public FacilitatorConnectionException(Exception innerException) {
        super(innerException);
    }
}
