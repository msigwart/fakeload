package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;

/**
 * Represents a simulation infrastructure for the FakeLoad Library.
 *
 * <p>
 * A simulation infrastructure is built up of different tasks. Each task serves a specific simulation purpose.
 * There will be tasks responsible for CPU load simulation, for memory simulation, etc. A {@code SimulationInfrastructure}
 * provides functionality for adjusting system loads via increase and decrease methods. Further, it provides methods for
 * starting and stopping the execution of simulator tasks in the infrastructure.
 *
 * @author Marten Sigwart
 * @since 1.8
 *
 */
public interface SimulationInfrastructure {

    /**
     * Starts the simulation infrastructure.
     */
    void start();

    /**
     * Stops the simulation infrastructure.
     */
    void stop();


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
