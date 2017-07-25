package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * This class is responsible for scheduling and dispatching fake loads to the simulation infrastructure.
 *
 * <p>
 * Whenever a {@link FakeLoad}'s {@code execute()} method is called, the fake load gets propagated to
 * the {@code FakeLoadDispatcher} instance. Then, the system load instructions contained within the FakeLoad object
 * are parsed and scheduled before being dispatched to the simulation infrastructure.
 *
 * <p>
 * Multiple fake loads being executed simultaneously should produce a system load which is the aggregation of all
 * load instructions contained in these fake loads. If thread A executes a fake load of 20% CPU and thread B executes
 * a fake load of 30% CPU the resulting system load should be 20% + 30% = 50%.
 * The {@code FakeLoadDispatcher} is responsible for this aggregation as well as reporting any faults concerning
 * any passing of load limitations of the system. For example executing a CPU load of more than 100% is not possible,
 * therefore if accumulated CPU load of the FakeLoad instances being executed exceeds that an error should be thrown.
 *
 * <p>
 *
 *
 * <p>
 * The simulation infrastructure consists of multiple threads each with a different simulation task.
 * For more information on the simulation infrastructure see {@link InfrastructureManager}.
 *
 * @see FakeLoad
 * @see InfrastructureManager
 * @since 1.8
 * @Author Marten Sigwart
 */
public enum FakeLoadDispatcher {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(FakeLoadDispatcher.class);

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

    private final SimulationInfrastructure infrastructure;

    /**
     * Constructor
     */
    FakeLoadDispatcher() {
        infrastructure = SimulationInfrastructure.INSTANCE;
        connection = infrastructure.getConnection();
        scheduler = new ScheduledThreadPoolExecutor(1);
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
                    infrastructure.stop();
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
