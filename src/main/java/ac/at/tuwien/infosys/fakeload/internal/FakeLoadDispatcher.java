package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
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
        connection.increaseAndGetCpu(fakeLoad.getCpuLoad());
        connection.increaseAndGetMemory(fakeLoad.getMemoryLoad());
        connection.increaseAndGetDiskIO(fakeLoad.getDiskIOLoad());
        connection.increaseAndGetNetIO(fakeLoad.getNetIOLoad());

        Future<String> future = scheduler.schedule(() -> {

            cpuLoad     = connection.decreaseAndGetCpu(fakeLoad.getCpuLoad());
            memoryLoad  = connection.decreaseAndGetMemory(fakeLoad.getMemoryLoad());
            diskIOLoad  = connection.decreaseAndGetDiskIO(fakeLoad.getDiskIOLoad());
            netIOLoad   = connection.decreaseAndGetNetIO(fakeLoad.getNetIOLoad());

            if (cpuLoad==0 && memoryLoad==0 && diskIOLoad==0 && netIOLoad==0 ) {
                scheduler.schedule(() -> {
                    log.debug("Shutting down infrastructure...");
                    infraManager.stopInfrastructure();
                    log.debug("Shut down infrastructure");
                    scheduler.shutdown();
                    return "";
                }, 5, TimeUnit.SECONDS);
            }

            return "";

        }, fakeLoad.getDuration(), fakeLoad.getTimeUnit());


        log.debug("Finished scheduling.");
        return future;
    }


}
