package ac.at.tuwien.infosys.fakeload.internal;

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
     * Increases the CPU load by the specified value.
     *
     * @param cpuLoad the value by which CPU load is increased.
     * @throws MaximumLoadExceededException
     */
    void increaseCpu(long cpuLoad) throws MaximumLoadExceededException;

    /**
     * Increases the memory load by the specified value.
     *
     * @param memoryLoad the value by which memory load is increased.
     * @throws MaximumLoadExceededException
     */
    void increaseMemory(long memoryLoad) throws MaximumLoadExceededException;

    /**
     * Increases the disk IO load by the specified value.
     *
     * @param diskIOLoad the value by which disk IO load is increased.
     * @throws MaximumLoadExceededException
     */
    void increaseDiskIO(long diskIOLoad) throws MaximumLoadExceededException;

    /**
     * Increases the network IO load by the specified value.
     *
     * @param netIOLoad the value by which network IO load is increased.
     * @throws MaximumLoadExceededException
     */
    void increaseNetIO(long netIOLoad) throws MaximumLoadExceededException;

    /**
     * Decreases the CPU load by the specified value.
     *
     * @param cpuLoad the value by which CPU load is decreased.
     */
    void decreaseCpu(long cpuLoad);

    /**
     * Decreases the memory load by the specified value.
     *
     * @param memoryLoad the value by which memory load is decreased.
     */
    void decreaseMemory(long memoryLoad);

    /**
     * Decreases the disk IO load by the specified value.
     *
     * @param diskIOLoad the value by which disk IO load is decreased.
     */
    void decreaseDiskIO(long diskIOLoad);

    /**
     * Decreases the network IO load load by the specified value.
     *
     * @param netIOLoad the value by which network IO load is decreased.
     */
    void decreaseNetIO(long netIOLoad);
}
