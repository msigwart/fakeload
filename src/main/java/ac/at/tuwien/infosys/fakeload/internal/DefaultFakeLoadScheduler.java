package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Default implementation of the {@link FakeLoadScheduler} interface.
 */
public final class DefaultFakeLoadScheduler implements FakeLoadScheduler {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadScheduler.class);


    /**
     * Connection to the simulation infrastructure
     */
    private final SimulationInfrastructure infrastructure;

    public DefaultFakeLoadScheduler(SimulationInfrastructure infrastructure) {
        this.infrastructure = infrastructure;

    }


    @Override
    public Future<Void> schedule(FakeLoad fakeLoad) {
        return scheduleLoad(fakeLoad);
    }

    @Override
    public void shutdown() {
//        this.infrastructure.shutdown();
    }


    private Future<Void> scheduleLoad(FakeLoad fakeLoad) {
        log.debug("Started scheduling...");

        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        completableFuture.complete(null);       // Complete with null value as CompletableFuture is of type Void

        for (FakeLoad load: fakeLoad) {

            // schedule increase
            completableFuture = completableFuture.thenRunAsync(() -> {
                try {
                    log.debug("Increasing system load by {}", load);
                    infrastructure.increaseSystemLoadBy(load);

                } catch (MaximumLoadExceededException e) {
                    log.warn(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            });

            // schedule decrease
            completableFuture = completableFuture.thenRunAsync(() -> {
                try {
                    Thread.sleep(load.getTimeUnit().toMillis(load.getDuration()));
                } catch (InterruptedException e) {
                    // should never happen
                    throw new RuntimeException(e);
                }

                log.debug("Decreasing system load by {}", load);
                infrastructure.decreaseSystemLoadBy(load);

            });
        }


        log.debug("Finished scheduling.");
        return completableFuture;
    }



}
