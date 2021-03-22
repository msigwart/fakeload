package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * The default {@link FakeLoadExecutor} implementation.
 *
 * <p>
 * The {@code DefaultFakeLoadExecutor} executes a submitted {@code FakeLoad} using the
 * linked {@link SimulationInfrastructure}. Method {@code executeAsync(FakeLoad)} returns a {@link Future} which is done when
 * the execution of the submitted {@code FakeLoad} object has completed. By calling {@link Future#get()}
 * the {@code execute(FakeLoad)} method blocks until either execution has finished or an exception was thrown.
 * This blocking behavior conforms to the general contract as imposed by {@link FakeLoadExecutor#execute(FakeLoad)}.
 *
 * <p>
 * Multiple {@code FakeLoad} objects being executed simultaneously produce a system load which is the aggregation of all
 * load instructions contained in the objects. If thread A submits a {@code FakeLoad} of 20% CPU and thread B submits
 * a {@code FakeLoad} of 30% CPU the resulting system load should be ~ 50%.
 *
 * <p>
 * This class uses the {@link FakeLoad#iterator()} method to determine the order for
 * scheduling of the {@code FakeLoad} object.
 * As the iterator already takes the number of repetitions under account, the default
 * scheduler can just iterate over the submitted {@code FakeLoad} object in a foreach loop.
 *
 * @since 1.8
 * @see FakeLoadExecutor
 * @see SimulationInfrastructure
 * @see FakeLoad
 *
 * @author Marten Sigwart
 */
public final class DefaultFakeLoadExecutor implements FakeLoadExecutor {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadExecutor.class);

    /**
     * Connection to the simulation infrastructure
     */
    private final SimulationInfrastructure infrastructure;

    /**
     * Creates a new {@code DefaultFakeLoadExecutor} instance.
     * @param infrastructure the system load simulation infrastructure to be used
     */
    public DefaultFakeLoadExecutor(SimulationInfrastructure infrastructure) {
        this.infrastructure = infrastructure;
    }


    @Override
    public void execute(FakeLoad load) {
        try {
            Future<Void> execution = executeAsync(load);
            execution.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Future<Void> executeAsync(FakeLoad fakeLoad) {
        log.info("FakeLoad execution started");
        log.debug("Executing {}", fakeLoad);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> {
            FakeLoad lastStartedLoad = null;
            try {
                for (FakeLoad load: fakeLoad) {
                    log.trace("Increasing system load by {}", load);
                    lastStartedLoad = load;
                    infrastructure.increaseSystemLoadBy(load);
                    Thread.sleep(load.getTimeUnit().toMillis(load.getDuration()));
                    log.trace("Decreasing system load by {}", load);
                    infrastructure.decreaseSystemLoadBy(load);
                }
                lastStartedLoad = null;
                log.info("FakeLoad execution finished");
                return null;
            } catch (MaximumLoadExceededException e) {
                log.warn(e.getMessage());
                throw new RuntimeException(e.getMessage());
            } catch (InterruptedException e) {
                log.info("FakeLoad execution cancelled");
                // make sure to decrease the system load again when the thread was interrupted
                infrastructure.decreaseSystemLoadBy(lastStartedLoad);
                return null;
            } finally {
                executor.shutdown();
            }
        });
    }

    @Override
    public void shutdown() {
        infrastructure.shutdown();
    }
}
