package com.martensigwart.fakeload;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Represents the main API for the FakeLoad Library.
 * With instances of this type clients decide what kinds of system loads they want to generate and for how long.
 *
 * <p>
 * Clients can specify what kind of load (e.g. CPU, RAM, …), how much of it (e.g. 80%, 1024MB, …)
 * and for how long a specific load (e.g. 10 s, 100 ms, …) is being simulated.
 * Also, a fake load can itself consist of other {@code FakeLoad} instances thus
 * giving the client the possibility to create complex patterns of system load instructions.
 * </p>
 *
 * <p>
 * Note: All setter methods return a {@code FakeLoad} instance themselves, thus allowing a kind
 * of fluent interface like this:
 * <pre>
 * {@code FakeLoad fakeload = FakeLoads.create()
 *      .lasting(10, TimeUnit.SECONDS)
 *      .withCpu(30)
 *      .withMemory(300, MemoryUnit.MB)
 *      .withDiskInput(10, MemoryUnit.KB)
 *      .withDiskOutput(20, MemoryUnit.MB);
 * }</pre>
 * Depending on whether the underlying {@code FakeLoad} implementation is mutable or immutable
 * these methods can either merely set specific load parameters or return a completely new
 * {@code FakeLoad} instance containing the new parameters, respectively.
 *
 *
 * @since 1.8
 * @see FakeLoads
 * @see FakeLoadExecutor
 * @author Marten Sigwart
 */
public interface FakeLoad extends Iterable<FakeLoad> {

    /**
     * Returns a {@code FakeLoad} instance containing the specified duration.
     *
     * @param duration the duration of the returned FakeLoad
     * @param unit the time unit of the duration
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad lasting(long duration, TimeUnit unit);

    /**
     * Returns a {@code FakeLoad} instance with the number of specified repetitions set.
     *
     * The default value is 1. That means a {@code FakeLoad} is executed exactly as many
     * times as the number of repetitions, e.g. a {@code FakeLoad} with repetitions set to 3,
     * will be executed exactly three times.
     *
     * @param noOfRepetitions number of repetitions of the returned FakeLoad
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad repeat(int noOfRepetitions);

    /**
     * Returns a {@code FakeLoad} instance with the specified CPU load.
     *
     * @param cpuLoad the fake CPU load in percent (0-100%)
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad withCpu(int cpuLoad);

    /**
     * Returns a {@code FakeLoad} instance with the specified memory load.
     *
     * @param amount the amount of memory to be simulated
     * @param unit the memory unit for the specified amount
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad withMemory(long amount, MemoryUnit unit);

    /**
     * Returns a {@code FakeLoad} instance with the specified disk input load as bytes/seconds.
     *
     * @param load the amount of disk input to be simulated
     * @param unit the memory unit for the specified amount per seconds
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad withDiskInput(long load, MemoryUnit unit);

    /**
     * Returns a {@code FakeLoad} instance with the specified disk output load as bytes/seconds.
     *
     * @param load the amount of disk output to be simulated
     * @param unit the memory unit for the specified amount per seconds
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad withDiskOutput(long load, MemoryUnit unit);

    FakeLoad addLoad(FakeLoad load);

    FakeLoad addLoads(Collection<FakeLoad> loads);

    boolean contains(FakeLoad load);

    /**
     * Returns the inner {@code FakeLoad} objects of this {@code FakeLoad}
     * @return a collection of {@code FakeLoad} objects
     */
    Collection<FakeLoad> getInnerLoads();

    int getCpu();

    long getMemory();

    long getDiskInput();

    long getDiskOutput();

    long getDuration();

    TimeUnit getTimeUnit();

    int getRepetitions();

    /**
     * Returns an iterator of this {@code FakeLoad} object.
     *
     * <p>
     * Generally, the iterator first returns the load information of the {@code FakeLoad} object on
     * which the method was called, and then the load information contained in its inner {@code FakeLoad} objects.
     * The iterator also takes the number of repetitions set by a {@code FakeLoad} into account.
     * If a simple {@code FakeLoad} has repetitions set to two, it will be returned twice by the iterator.
     * This applies also to any inner children {@code FakeLoad}s.
     * @return an iterator over {@code FakeLoad} objects
     */
    @Override
    Iterator<FakeLoad> iterator();
}
