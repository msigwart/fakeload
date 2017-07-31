package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation of the {@link FakeLoadScheduler} interface.
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




    private Future<Void> scheduleLoad(FakeLoad fakeLoad) {
        log.debug("Scheduling load...");

        CompletableFuture<Void> completableFuture = CompletableFuture.completedFuture(null);

        for (FakeLoad load: fakeLoad) {
            completableFuture = completableFuture.thenApply(foo -> {
                try {
                    log.debug("Increasing system load by {}", load);
                    infrastructure.increaseSystemLoadBy(load);

                } catch (MaximumLoadExceededException e) {
                    log.debug(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
                return null;
            });

            completableFuture = completableFuture.thenApply(foo -> {
                try {
                    Thread.sleep(load.getTimeUnit().toMillis(load.getDuration()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                log.debug("Decreasing system load by {}", load);
                infrastructure.decreaseSystemLoadBy(load);

                return null;
            });
        }


        log.debug("Finished scheduling.");
        return completableFuture;
    }


    private Future<Void> scheduleDecrease(FakeLoad load, long offset) {
        return scheduler.schedule(() -> {

            infrastructure.decreaseSystemLoadBy(load);

            return null;

        }, offset, TimeUnit.MILLISECONDS);
    }

    private Future<Void> scheduleIncrease(FakeLoad load, long offset) {
        return scheduler.schedule(() -> {

            infrastructure.increaseSystemLoadBy(load);

            return null;
        }, offset, TimeUnit.MILLISECONDS);

    }

}
