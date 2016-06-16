package eduze.vms.presenter.logic;

import eduze.vms.facilitator.logic.*;
import eduze.vms.facilitator.logic.mpi.virtualmeeting.SharedTaskNotFoundException;
import eduze.vms.facilitator.logic.webservices.AssignedTask;
import eduze.vms.server.logic.FacilitatorSessionListener;
import eduze.vms.server.logic.ServerController;
import eduze.vms.server.logic.webservices.Facilitator;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

/**
 * Created by Admin on 6/16/2016.
 */
public class AssignedTasksManagerTest {
    private ServerController serverController = null;
    private FacilitatorController facilitatorController = null;
    private PresenterController presenterController = null;

    private boolean setupDone = false;
    private CountDownLatch countDownLatch = null;

    private String presenterId;
    private String facilitatorId;
    private SharedTaskManager sharedTaskManager = null;
    @BeforeClass
    public void setUp() throws Exception {
        countDownLatch = new CountDownLatch(1);
        setupDone = false;
        serverController = new ServerController();
        serverController.setPort(6000);
        serverController.setPassword("password".toCharArray());
        serverController.addFacilitatorSessionListener(new FacilitatorSessionListener() {
            @Override
            public void onConnected(Facilitator facilitator, String consoleId) {
                facilitatorId = consoleId;
            }

            @Override
            public void onDisconnected(Facilitator facilitator, String consoleId) {

            }

            @Override
            public void onMeetingAdjourned() {

            }
        });
        serverController.start();

        FacilitatorController.Configuration facilitatorcConfiguration = new FacilitatorController.Configuration();
        facilitatorcConfiguration.setListenerPort(7000);
        facilitatorcConfiguration.setPassword("password".toCharArray());
        facilitatorController = FacilitatorController.start(facilitatorcConfiguration);

        ServerManager.PairedServer pairedServer = facilitatorController.getServerManager().pair("http://localhost:6000","password".toCharArray());
        facilitatorController.getServerManager().connect(pairedServer);

        facilitatorController.addConnectionListener(new PresenterConnectionListener() {
            @Override
            public void onConnectionRequested(ConnectionRequest connectionRequest) {
                try {
                    connectionRequest.accept();
                } catch (RequestAlreadyProcessedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConnected(String connectionRequestId, String consoleId) {

            }

            @Override
            public void onDisconnected(String consoleId) {

            }
        });

        FacilitatorConnector.Configuration configuration = new FacilitatorConnector.Configuration();
        configuration.setFacilitatorURL("http://localhost:7000");
        configuration.setFacilitatorPasskey("password".toCharArray());
        FacilitatorConnector connector =  FacilitatorConnector.connect(configuration);
        connector.setConnectionRequestStateListener(new FacilitatorConnector.ConnectionRequestStateListener() {
            @Override
            public void onSuccess(FacilitatorConnector sender) {

                try {
                    presenterController = sender.obtainController();
                    setupDone = true;
                } catch (FacilitatorConnectionNotReadyException e) {
                    e.printStackTrace();
                } catch (FacilitatorConnectionException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FacilitatorDisconnectedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }

            @Override
            public void onFailed(FacilitatorConnector sender) {
                setupDone = false;
                countDownLatch.countDown();
            }

            @Override
            public boolean onException(FacilitatorConnector sender, Exception e) {
                setupDone = false;
                countDownLatch.countDown();
                return false;
            }
        });


        countDownLatch.await(4000, TimeUnit.MILLISECONDS);
        if(!setupDone)
            Assert.fail("Presenter connection failed");


        sharedTaskManager = facilitatorController.getSharedTaskManager();

        presenterId = facilitatorController.getPresenters()[0].getPresenterConsoleId();
    }

    @AfterMethod
    public void afterMethod() throws Exception
    {
        //remove
        SharedTaskInfo[] sharedTaskInfos = sharedTaskManager.getSharedTasks();
        countDownLatch = new CountDownLatch(1);
        for(SharedTaskInfo taskInfo : sharedTaskInfos)
        {
            sharedTaskManager.removeSharedTask(taskInfo.getId());
        }

        //wait for update
        boolean found = true;
        while(found)
        {
            found = false;
            sharedTaskInfos = sharedTaskManager.getSharedTasks();
            for(SharedTaskInfo taskInfo : sharedTaskInfos)
            {
                found = true;
            }
            countDownLatch.await(100,TimeUnit.MILLISECONDS);
        }

    }
    @AfterClass
    public void tearDown() throws Exception {
        presenterController.disconnect();
        facilitatorController.finish();

    }


    @Test(singleThreaded = true)
    public void testGetAssignedTasks() throws Exception {
        countDownLatch = new CountDownLatch(2);
        String id = sharedTaskManager.createNewSharedTask("TestTitle","TestDescription");
        sharedTaskManager.assignTaskToPresenter(id,facilitatorId,presenterId);
        AssignedTasksManager assignedTasksManager = presenterController.getAssignedTasksManager();
        presenterController.addStateChangeListener(new ControlLoop.StateChangeListener() {
            @Override
            public void onScreenCaptureChanged(boolean newValue) {

            }

            @Override
            public void onAudioCaptureChanged(boolean newValue) {

            }

            @Override
            public void onControlLoopCycleCompleted() {
                countDownLatch.countDown();
            }

            @Override
            public void onFacilitatorDisconnected() {

            }
        });
        //wait for server update
        countDownLatch.await(2000,TimeUnit.MILLISECONDS);
        Collection<AssignedTaskInfo> assignedTaskInfos = assignedTasksManager.getAssignedTasks();
        for (AssignedTaskInfo taskInfo : assignedTaskInfos)
        {
            Assert.assertEquals(taskInfo.getDescription(),"TestDescription");
            Assert.assertEquals(taskInfo.getTitle(),"TestTitle");
            Assert.assertEquals(taskInfo.getId(),id);
            return;
        }
        Assert.fail("The assigned task was not detected by presenter");
    }


    private boolean testDone = false;
    @Test(singleThreaded = true)
    public void testNotifyTaskAssigned() throws Exception {
        testDone = false;
        countDownLatch = new CountDownLatch(1);
        final String id = sharedTaskManager.createNewSharedTask("TestAssigned","TestDescription");
        sharedTaskManager.assignTaskToPresenter(id,facilitatorId,presenterId);
        AssignedTasksManager assignedTasksManager = presenterController.getAssignedTasksManager();

        AssignedTasksManager.AssignedTaskListener listener = new AssignedTasksManager.AssignedTaskListener() {
            @Override
            public void onInitiated() {

            }

            @Override
            public void onTaskAssigned(AssignedTaskInfo task) {
                Assert.assertEquals(task.getDescription(),"TestDescription");
                Assert.assertEquals(task.getTitle(),"TestAssigned");
                Assert.assertEquals(task.getId(),id);
                testDone = true;
                countDownLatch.countDown();
                return;
            }

            @Override
            public void onTaskUnAssigned(AssignedTaskInfo task) {

            }

            @Override
            public void onTaskModified(AssignedTaskInfo oldTask, AssignedTaskInfo newTask) {

            }
        };
        assignedTasksManager.addAssignedTaskListener(listener);

        //wait for server update
        countDownLatch.await(2000,TimeUnit.MILLISECONDS);
        if(!testDone)
                Assert.fail("The assigned task was not detected by presenter");
        assignedTasksManager.removeAssignedTaskListener(listener);
    }

    private boolean testDone2 = false;
    @Test(singleThreaded = true)
    public void testNotifyTaskUnAssigned() throws Exception {
        testDone = false;
        testDone2 = false;
        countDownLatch = new CountDownLatch(2);
        final String id = sharedTaskManager.createNewSharedTask("TestAssigned","TestDescription");
        sharedTaskManager.assignTaskToPresenter(id,facilitatorId,presenterId);
        AssignedTasksManager assignedTasksManager = presenterController.getAssignedTasksManager();

        AssignedTasksManager.AssignedTaskListener listener = new AssignedTasksManager.AssignedTaskListener() {
            @Override
            public void onInitiated() {

            }

            @Override
            public void onTaskAssigned(AssignedTaskInfo task) {
                Assert.assertEquals(task.getDescription(),"TestDescription");
                Assert.assertEquals(task.getTitle(),"TestAssigned");
                Assert.assertEquals(task.getId(),id);


                try {
                    sharedTaskManager.unAssignTaskFromPresenter(id);
                    testDone = true;
                } catch (SharedTaskNotFoundException e) {
                    e.printStackTrace();
                } catch (ServerConnectionException e) {
                    e.printStackTrace();
                }

                //countDownLatch.countDown();
                return;
            }

            @Override
            public void onTaskUnAssigned(AssignedTaskInfo task) {
                Assert.assertEquals(task.getDescription(),"TestDescription");
                Assert.assertEquals(task.getTitle(),"TestAssigned");
                Assert.assertEquals(task.getId(),id);
                testDone2 = true;
                countDownLatch.countDown();
                return;
            }

            @Override
            public void onTaskModified(AssignedTaskInfo oldTask, AssignedTaskInfo newTask) {

            }
        };
        assignedTasksManager.addAssignedTaskListener(listener);

        //wait for server update
        countDownLatch.await(300000,TimeUnit.MILLISECONDS);
        if(!testDone)
            Assert.fail("The assigned task was not detected by presenter");

        if(!testDone2)
            Assert.fail("The assigned task was not unassigned from presenter");

        assignedTasksManager.removeAssignedTaskListener(listener);
    }

    @Test
    public void testNotifyTaskModified() throws Exception {

    }

    @Test
    public void testNotifyAssignedTasksInitiated() throws Exception {

    }

}