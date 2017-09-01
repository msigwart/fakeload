package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Default implementation of the {@code FakeLoadScheduler} interface.
 *
 * {@link FakeLoad} objects are scheduled for execution via the {@link #schedule(FakeLoad)} method.
 *
 * This class uses the {@link FakeLoad#iterator()} method to determine the order for
 * scheduling of the {@code FakeLoad} object.
 * As the iterator already takes the number of repetitions under account, the default
 * scheduler can just iterate over the submitted {@code FakeLoad} object in a foreach loop.
 *
 * The actual scheduling is done using {@link CompletableFuture}s and the common thread pool.
 *
 * @since 1.8
 * @see FakeLoadScheduler
 * @see FakeLoad
 */
public final class DefaultFakeLoadScheduler implements FakeLoadScheduler {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadScheduler.class);


    /**
     * Connection to the simulation infrastructure
     */
    private final SimulationInfrastructure infrastructure;


    /**
     * Creates a new {@code DefaultFakeLoadScheduler} instance using the specified infrastructure
     * @param infrastructure the simulation infrastructure to be used
     */
    public DefaultFakeLoadScheduler(SimulationInfrastructure infrastructure) {
        this.infrastructure = infrastructure;

    }


    @Override
    public Future<Void> schedule(FakeLoad fakeLoad) {
        return scheduleLoad(fakeLoad);
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
