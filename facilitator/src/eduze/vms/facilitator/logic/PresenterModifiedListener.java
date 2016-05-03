package eduze.vms.facilitator.logic;

/**
 * Created by Madhawa on 13/04/2016.
 */

/**
 * Listener to be notified on changes of parameters of presenter
 */
public interface PresenterModifiedListener {
    public void presenterNameChanged(String consoleId, String newName);
}
