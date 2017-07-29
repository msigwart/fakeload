package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 29.07.17.
 */
public final class DefaultFakeLoadScheduler implements FakeLoadScheduler {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadScheduler.class);


    /**
     * Executor Service for scheduling simulation loads
     */
    private ScheduledThreadPoolExecutor scheduler;

    /**
     * Connection to the simulation infrastructure
     */
    private final SimulationInfrastructure infrastructure;

    public DefaultFakeLoadScheduler(SimulationInfrastructure infrastructure) {
        this.infrastructure = infrastructure;
        scheduler = new ScheduledThreadPoolExecutor(1);

    }


    @Override
    public Future<Void> schedule(FakeLoad fakeLoad) {
        return scheduleLoad(fakeLoad);
    }




    private Future<Void> scheduleLoad(FakeLoad load) {
        log.debug("Scheduling load...");


        // TODO not correct yet
        Future<Void> future = null;
        long offset = 0L;
        for (FakeLoad f: load) {
            scheduleIncrease(f, offset);
            offset += f.getTimeUnit().toMillis(f.getDuration());
            future = scheduleDecrease(f, offset);
        }

        log.debug("Finished scheduling.");
        return future;
    }


    private Future<Void> scheduleDecrease(FakeLoad load, long offset) {
        return scheduler.schedule(() -> {

            infrastructure.decreaseCpu(load.getCpuLoad());
            infrastructure.decreaseMemory(load.getMemoryLoad());
            infrastructure.decreaseDiskIO(load.getDiskIOLoad());
            infrastructure.decreaseNetIO(load.getNetIOLoad());

            return null;

        }, offset, TimeUnit.MILLISECONDS);
    }

    private Future<Void> scheduleIncrease(FakeLoad load, long offset) {
        return scheduler.schedule(() -> {

            infrastructure.increaseCpu(load.getCpuLoad());
            infrastructure.increaseMemory(load.getMemoryLoad());
            infrastructure.increaseDiskIO(load.getDiskIOLoad());
            infrastructure.increaseNetIO(load.getNetIOLoad());

            return null;
        }, offset, TimeUnit.MILLISECONDS);

    }

}
