package com.martensigwart.fakeload;

import java.util.concurrent.Future;

/**
 * Object that schedules submitted {@link FakeLoad} objects.
 * This interface provides a way of decoupling a FakeLoad's submission for execution from the
 * mechanics of how a FakeLoad's load instructions are actually scheduled for execution.
 *
 * <p>
 * This interface is provided as kind of utility type for the {@link DefaultFakeLoadExecutor}
 * to take over the responsibility of actually scheduling FakeLoads in a correct manner.
 * It is not part of the published API of the FakeLoad library.
 *
 * <p>
 * As the method provided by the {@code FakeLoadScheduler} {@link #schedule(FakeLoad)} returns
 * a Future, implementations of the {@code FakeLoadExecutor} should just call {@code Future.get()}
 * on the returned future. This would block the {@code execute()} method of the {@code FakeLoadExecutor}
 * as required by its contract.
 *
 * <p>
 * Class {@link DefaultFakeLoadScheduler} provides a default implementation of this interface.
 */
public interface FakeLoadScheduler {

    /**
     * Schedules a {@link FakeLoad}'s load instructions for execution at the right time.
     * @param fakeLoad the {@code FakeLoad} object to be scheduled.
     * @return a Future indicating whether the scheduled loads have executed
     * successfully or have thrown an error.
     */
    Future<Void> schedule(FakeLoad fakeLoad);

}
