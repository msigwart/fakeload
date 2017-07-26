package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Default implementation of the {@link FakeLoadDispatcher} interface. The dispatcher is responsible
 * for scheduling and dispatching fake loads to an underlying simulation infrastructure.
 *
 * <p>
 * This concrete implementation consists of a {@link DefaultInfrastructure} instance and a scheduler.
 * The infrastructure is responsible for actually executing load instructions of {@link FakeLoad} objects
 * submitted via {@link #submitLoad(FakeLoad)}. The scheduler is responsible for scheduling the propagation of load
 * instructions to the infrastructure at the right time. Further the scheduler is also responsible for reducing the
 * load again once it has run.
 *
 * <p>
 * Multiple fake loads being executed simultaneously should produce a system load which is the aggregation of all
 * load instructions contained in these fake loads. If thread A executes a fake load of 20% CPU and thread B executes
 * a fake load of 30% CPU the resulting system load should be 20% + 30% = 50%.
 * The {@code DefaultFakeLoadDispatcher} is responsible for this aggregation as well as reporting any faults concerning
 * any passing of load limitations of the system. For example executing a CPU load of more than 100% is not possible,
 * therefore if accumulated CPU load of the FakeLoad instances being executed exceeds that an error should be thrown.
 *
 * <p>
 *
 *
 * <p>
 * The simulation infrastructure consists of multiple threads each with a different simulation task.
 * For more information on the simulation infrastructure see {@link DefaultInfrastructure}.
 *
 * @see FakeLoad
 * @see FakeLoadDispatcher
 * @since 1.8
 * @author Marten Sigwart
 */
public class DefaultFakeLoadDispatcher implements FakeLoadDispatcher {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadDispatcher.class);

    private int cpuLoad;
    private long memoryLoad;
    private long diskIOLoad;
    private long netIOLoad;


    /**
     * Executor Service for scheduling simulation loads
     */
    private ScheduledThreadPoolExecutor scheduler;

    /**
     * Connection to the simulation infrastructure
     */
    private final DefaultInfrastructure infrastructure;

    /**
     * Constructor
     */
    public DefaultFakeLoadDispatcher(DefaultInfrastructure infrastructure) {
        this.infrastructure = infrastructure;
        scheduler = new ScheduledThreadPoolExecutor(1);
    }


    @Override
    public Future<String> submitLoad(FakeLoad load) {
        return scheduleLoad(load);
    }

    private Future<String> scheduleLoad(FakeLoad fakeLoad) {
        log.debug("Scheduling load...");
        try {
            infrastructure.increaseCpu(fakeLoad.getCpuLoad());
            infrastructure.increaseMemory(fakeLoad.getMemoryLoad());
            infrastructure.increaseDiskIO(fakeLoad.getDiskIOLoad());
            infrastructure.increaseNetIO(fakeLoad.getNetIOLoad());

        } catch (MaximumLoadExceededException e) {
            throw new RuntimeException(e.getMessage());
        }

        Future<String> future = scheduler.schedule(() -> {

            infrastructure.decreaseCpu(fakeLoad.getCpuLoad());
            infrastructure.decreaseMemory(fakeLoad.getMemoryLoad());
            infrastructure.decreaseDiskIO(fakeLoad.getDiskIOLoad());
            infrastructure.decreaseNetIO(fakeLoad.getNetIOLoad());

            return "";

        }, fakeLoad.getDuration(), fakeLoad.getTimeUnit());


        log.debug("Finished scheduling.");
        return future;
    }


}
