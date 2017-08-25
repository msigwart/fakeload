package com.martensigwart.fakeload;

/**
 * Represents a thread simulating some system load. System load can be CPU, memory, Disk I/O, ...
 *
 * <p>
 * This type simulates some system load when started as a thread.
 * Further, it defines methods for getting and setting as well as increasing and decreasing
 * some load parameter. Implementing classes should use the load parameter, set by those methods,
 * for their simulation logic.
 *
 * <p>
 * Class {@link AbstractLoadSimulator} provides a skeleton implementation of a {@code LoadSimulator}.
 * Subclassing {@code AbstractLoadSimulator} is the preferred way for clients to create new
 * {@code LoadSimulator} classes, as it already provides common simulation behavior as well as
 * synchronization mechanisms.
 *
 * @since 1.8
 * @see Runnable
 * @see AbstractLoadSimulator
 * @author Marten Sigwart
 */
public interface LoadSimulator extends Runnable {

    /**
     * Executes the simulation logic.
     * This method should be used to implement the simulator's main simulation logic,
     * i.e. the actual system load (CPU, memory, etc.) for which this {@code LoadSimulator}
     * is responsible should be simulated.
     *
     * <p>
     * From {@link Runnable#run()}: {@inheritDoc}
     */
    @Override
    void run();

    /**
     * @return returns the load to be simulated
     */
    long getLoad();

    /**
     * Sets the load to be simulated.
     * @param desiredLoad the load to be simulated
     */
    void setLoad(long desiredLoad);

    /**
     * Increases the load of the simulator by {@code delta}.
     * @param delta the amount by which the load will be increased
     */
    void increaseLoad(long delta);

    /**
     * Decreases the load of the simulator by {@code delta}.
     * @param delta the amount by which the load will be decreased
     */
    void decreaseLoad(long delta);

    long getMaximumLoad();

    boolean isMaximumLoad();

    boolean isZeroLoad();
}
