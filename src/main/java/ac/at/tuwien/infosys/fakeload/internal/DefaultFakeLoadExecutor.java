package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import ac.at.tuwien.infosys.fakeload.FakeLoadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * A {@link FakeLoadExecutor} that executes each submitted {@link FakeLoad} by passing passing it on to an underlying
 * {@link SimulationInfrastructure}.
 *
 * <p>
 * This class consists of a {@code SimulationInfrastructure} instance and a scheduler.
 * The infrastructure is responsible for actually executing load instructions of {@code FakeLoad} objects
 * submitted via {@code execute(FakeLoad)}. The scheduler is responsible for scheduling the propagation of load
 * instructions to the infrastructure at the right time. Further the scheduler is also responsible for reducing the
 * load again once it has run.
 *
 * <p>
 * Multiple {@code FakeLoad} objects being executed simultaneously produce a system load which is the aggregation of all
 * load instructions contained in the objects. If thread A submits a {@code FakeLoad} of 20% CPU and thread B submits
 * a {@code FakeLoad} of 30% CPU the resulting system load should be ~ 50%.
 *
 * <p>TODO NOT UP TO DATE ANYMORE
 * The {@code DefaultFakeLoadExecutor} is responsible for this aggregation as well as reporting any faults concerning
 * any passing of load limitations of the system. For example executing a CPU load of more than 100% is not possible,
 * therefore if accumulated CPU load of the FakeLoad instances being executed exceeds that an error should be thrown.
 *
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public final class DefaultFakeLoadExecutor implements FakeLoadExecutor {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadExecutor.class);


    /**
     * Executor Service for scheduling simulation loads
     */
    private ScheduledThreadPoolExecutor scheduler;

    /**
     * Connection to the simulation infrastructure
     */
    private final SimulationInfrastructure infrastructure;

    /**
     * Constructor
     */
    public DefaultFakeLoadExecutor(SimulationInfrastructure infrastructure) {
        this.infrastructure = infrastructure;
        scheduler = new ScheduledThreadPoolExecutor(1);
    }


    @Override
    public void execute(FakeLoad load) {
        try {
            Future<String> future = scheduleLoad(load);
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Future<String> scheduleLoad(FakeLoad load) {
        log.debug("Scheduling load...");
        try {
            infrastructure.increaseCpu(load.getCpuLoad());
            infrastructure.increaseMemory(load.getMemoryLoad());
            infrastructure.increaseDiskIO(load.getDiskIOLoad());
            infrastructure.increaseNetIO(load.getNetIOLoad());

        } catch (MaximumLoadExceededException e) {
            throw new RuntimeException(e.getMessage());
        }

        Future<String> future = scheduler.schedule(() -> {

            infrastructure.decreaseCpu(load.getCpuLoad());
            infrastructure.decreaseMemory(load.getMemoryLoad());
            infrastructure.decreaseDiskIO(load.getDiskIOLoad());
            infrastructure.decreaseNetIO(load.getNetIOLoad());

            return "";

        }, load.getDuration(), load.getTimeUnit());


        log.debug("Finished scheduling.");
        return future;
    }


}
