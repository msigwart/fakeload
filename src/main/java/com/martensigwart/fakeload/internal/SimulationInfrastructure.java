package com.martensigwart.fakeload.internal;

import com.martensigwart.fakeload.FakeLoad;

/**
 * Represents a simulation infrastructure for the FakeLoad Library.
 *
 * <p>
 * A simulation infrastructure is built up of different tasks. Each task serves a specific simulation purpose.
 * There should be tasks responsible for CPU load simulation, for memory simulation, etc. A {@code SimulationInfrastructure}
 * provides functionality for adjusting system loads via increase and decrease methods.
 *
 * @author Marten Sigwart
 * @since 1.8
 *
 */
public interface SimulationInfrastructure {


    /**
     * Increases the system load simulated by the simulation infrastructure
     * by the specified {@link FakeLoad} instance
     * @param load the load by which the overall system load is increased
     * @throws MaximumLoadExceededException in case the specified increase of the system load
     * would exceed the allowed maximum load of the system
     */
    void increaseSystemLoadBy(FakeLoad load) throws MaximumLoadExceededException;

    /**
     * Decreases the system load simulated by the simulation infrastructure
     * by the specified {@link FakeLoad}
     * @param load the load by which the overall system load is decreased
     * @throws RuntimeException
     */
    void decreaseSystemLoadBy(FakeLoad load);
}
