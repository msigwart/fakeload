package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * The default {@link FakeLoadExecutor} implementation.
 *
 * <p>
 * The {@code DefaultFakeLoadExecutor} executes a submitted {@code FakeLoad} by passing it
 * to the {@link FakeLoadScheduler#schedule(FakeLoad)} method which schedules each {@code FakeLoad}
 * for execution. Method {@code schedule(FakeLoad)} returns a {@link Future} which is done when
 * the execution of the submitted {@code FakeLoad} object has completed. By calling {@link Future#get()}
 * the {@code execute(FakeLoad)} method blocks until either execution has finished or an exception was thrown.
 * This blocking behavior conforms to the general contract as imposed by {@link FakeLoadExecutor#execute(FakeLoad)}.
 *
 * <p>
 * Multiple {@code FakeLoad} objects being executed simultaneously produce a system load which is the aggregation of all
 * load instructions contained in the objects. If thread A submits a {@code FakeLoad} of 20% CPU and thread B submits
 * a {@code FakeLoad} of 30% CPU the resulting system load should be ~ 50%.
 *
 *
 * @since 1.8
 * @see FakeLoadExecutor
 * @see FakeLoadScheduler
 * @see FakeLoad
 *
 * @author Marten Sigwart
 */
public final class DefaultFakeLoadExecutor implements FakeLoadExecutor {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadExecutor.class);

    /**
     * The scheduler used for scheduling {@link FakeLoad} objects for execution.
     */
    private final FakeLoadScheduler scheduler;

    /**
     * Creates a new {@code DefaultFakeLoadExecutor} instance.
     * @param scheduler the scheduler to be used by the newly created executor
     */
    public DefaultFakeLoadExecutor(FakeLoadScheduler scheduler) {
        this.scheduler = scheduler;
    }


    @Override
    public void execute(FakeLoad load) {
        try {
            log.info("Starting FakeLoad execution...");
            log.debug("Executing {}", load);
            Future<Void> execution = scheduler.schedule(load);
            execution.get();
            log.info("Finished FakeLoad execution.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Future<Void> executeAsync(FakeLoad load) {
        return scheduler.schedule(load);
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
    }
}
