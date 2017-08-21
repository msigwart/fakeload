package com.martensigwart.fakeload;

import java.util.Collection;
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
 * {@code FakeLoad fakeload = FakeLoads.createLoad()
 *      .lasting(10, TimeUnit.SECONDS)
 *      .withCpu(30)
 *      .withMemory(300, MemoryUnit.MB)
 *      .withDiskInput(10, MemoryUnit.KB);
 * }</pre>
 * Depending on whether the underlying {@code FakeLoad} implementation is mutable or immutable
 * these methods can either merely set specific load parameters or return a completely new
 * {@code FakeLoad} instance containing the new parameters, respectively.
 *
 *
 * @author Marten Sigwart
 * @since 1.8
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
    FakeLoad withCpu(long cpuLoad);

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
     * @param load the amount of disk inpput to be simulated
     * @param unit the memory unit for the specified amount
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad withDiskInput(long load, MemoryUnit unit);

    FakeLoad addLoad(FakeLoad load);

    FakeLoad addLoads(Collection<FakeLoad> loads);

    boolean contains(FakeLoad load);

    Collection<FakeLoad> getInnerLoads();

    long getCpuLoad();

    long getMemoryLoad();

    long getDiskIOLoad();

    long getDuration();

    TimeUnit getTimeUnit();

    int getRepetitions();
}
