package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import ac.at.tuwien.infosys.fakeload.LoadPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * This class is responsible for dispatching fake load requests to the simulation infrastructure.
 *
 *
 * @Author Marten Sigwart
 */
public class FakeLoadDispatcher {

    private static final Logger log = LoggerFactory.getLogger(FakeLoadDispatcher.class);

//-------------------------------------------------------------
// Singleton Methods
//-------------------------------------------------------------

    /** Singleton instance */
    private static FakeLoadDispatcher instance;

    /**
     * Retrieves this class' singleton instance
     * @return the singleton FakeLoadDispatcher instance
     */
    public static FakeLoadDispatcher getInstance() {
        if (instance == null) {
            instance = new FakeLoadDispatcher();
        }
        return instance;
    }


    private int cpuLoad;
    private long memoryLoad;
    private long diskIOLoad;
    private long netIOLoad;


    /**
     * Represents the connection to the simulation infrastructure through which load requests can be dispatched.
     */
    private Connection connection;

    /** Executor Service for scheduling simulation loads */
    private ScheduledThreadPoolExecutor scheduler;

    private final InfrastructureManager infraManager;

    /**
     * Private constructor for use in singleton pattern.
     */
    private FakeLoadDispatcher() {
        this.infraManager = new InfrastructureManager();
        this.connection = new InfrastructureManager().createConnection();
        this.scheduler = new ScheduledThreadPoolExecutor(1);
    }



    public Future<String> submitLoad(FakeLoad load) {
        return scheduleLoad(load);
    }

    private Future<String> scheduleLoad(FakeLoad fakeLoad) {
        log.debug("Scheduling load...");
        connection.increaseCpu(fakeLoad.getCpuLoad());
        connection.increaseMemory(fakeLoad.getMemoryLoad());
        connection.increaseDiskIO(fakeLoad.getDiskIOLoad());
        connection.increaseNetIO(fakeLoad.getNetIOLoad());

        Future<String> future = scheduler.schedule(() -> {

            connection.decreaseCpu(fakeLoad.getCpuLoad());
            connection.decreaseMemory(fakeLoad.getMemoryLoad());
            connection.decreaseDiskIO(fakeLoad.getDiskIOLoad());
            connection.decreaseNetIO(fakeLoad.getNetIOLoad());

            return "";

        }, fakeLoad.getDuration(), fakeLoad.getTimeUnit());

        scheduler.schedule(() -> {
            log.debug("Shutting down infrastructure...");
            infraManager.stopInfrastructure();
            log.debug("Shut down infrastructure");
            scheduler.shutdown();
            return "";
        }, 60, TimeUnit.SECONDS);

        log.debug("Finished scheduling.");
        return future;
    }


}
