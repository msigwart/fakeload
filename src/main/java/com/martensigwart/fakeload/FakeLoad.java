package com.martensigwart.fakeload;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * An object containing instructions for simulating system load.
 *
 * A {@code FakeLoad}, next to a {@link FakeLoadExecutor}, represents one of the two
 * main concepts of the FakeLoad Library. With instances of this type clients decide
 * what kinds of system loads they want to generate and for how long.
 *
 * <p>
 * Clients can specify what kind of load (e.g. CPU, RAM, …), how much of it (e.g. 80%, 1024MB, …)
 * and for how long a specific load (e.g. 10 s, 100 ms, …) is being simulated.
 * Also, a fake load can itself consist of other {@code FakeLoad} instances thus
 * giving the client the possibility to create more elaborate patterns of system load instructions.
 * </p>
 *
 * <p>
 * All setter methods of {@code FakeLoad} return a {@code FakeLoad} instance themselves, thus allowing a kind
 * of fluent interface like this:
 *
 * <pre>
 * {@code FakeLoad fakeload = FakeLoads.create()
 *      .lasting(10, TimeUnit.SECONDS)
 *      .withCpu(30)
 *      .withMemory(300, MemoryUnit.MB)
 *      .withDiskInput(10, MemoryUnit.KB)
 *      .withDiskOutput(20, MemoryUnit.MB);
 * }</pre>
 *
 * Class {@link FakeLoads} offers factory methods for creating {@code FakeLoad} instances.
 * Class {@link FakeLoadBuilder} is an implementation of the builder pattern to create {@code FakeLoad instances}.
 *
 * Note: {@code FakeLoad} objects created with classes {@code FakeLoads} and {@code FakeLoadBuilder}
 * are <b>immutable</b>. Thus, calling any of the setter methods will return newly constructed {@code FakeLoad}
 * instances, containing the load parameters of the "old" {@code FakeLoad} object, as well as the newly passed
 * load parameters. Therefore changing the load parameter of an existing {@code FakeLoad} object should
 * always be done like this:
 *
 * <pre>
 * // The original
 * {@code} FakeLoad fakeload = FakeLoads.create().lasting(10, TimeUnit.SECONDS.withCpu(...
 *
 * // "Modifying" the original
 * fakeload = fakeload.withCpu(50);
 * </pre>
 *
 *
 * @since 1.8
 * @see FakeLoadExecutor
 * @see FakeLoads
 * @see FakeLoadBuilder
 * @author Marten Sigwart
 */
public interface FakeLoad extends Iterable<FakeLoad>, Serializable {

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

    /**
     * Adds the specified {@code FakeLoad} object to this {@code FakeLoad}'s inner loads.
     * @param load the {@code FakeLoad} to be added to the inner loads
     * @return returns the {@code FakeLoad} including the newly added {@code FakeLoad}
     */
    FakeLoad addLoad(FakeLoad load);

    /**
     * Adds the specified {@code FakeLoad} objects to this {@code FakeLoad}'s inner loads.
     * @param loads the {@code FakeLoad} objects to be added to the inner loads
     * @return returns the {@code FakeLoad} including the newly added {@code FakeLoad} objects
     */
    FakeLoad addLoads(Collection<FakeLoad> loads);

    /**
     * Determines whether the specified {@code FakeLoad} object in contained within the {@code FakeLoad}
     * on which this method was called.
     * @param load the {@code FakeLoad} object to look for
     * @return true if {@code load} is contained within this {@code FakeLoad}
     */
    boolean contains(FakeLoad load);

    /**
     * Returns the inner {@code FakeLoad} objects of this {@code FakeLoad}
     * @return a collection of {@code FakeLoad} objects
     */
    Collection<FakeLoad> getInnerLoads();

    /**
     * Returns the CPU load in percent
     * @return an {@code int} containing the CPU load in percent
     */
    int getCpu();

    /**
     * Returns the memory load in bytes
     * @return a {@code long} containing the memory load in bytes
     */
    long getMemory();

    /**
     * Returns the disk input load in bytes per second
     * @return a {@code long} containing the disk input load in bytes per second
     */
    long getDiskInput();

    /**
     * Returns the disk output load in bytes per second
     * @return a {@code long} containing the disk output load in bytes per second
     */
    long getDiskOutput();

    /**
     * Returns the duration of this {@code FakeLoad} system load configuration.
     * The unit of the duration is determined by {@link #getTimeUnit()}.
     * @return the duration of this {@code FakeLoad}'s load instructions
     */
    long getDuration();

    /**
     * Returns the time unit of the duration specified by {@link #getDuration()}.
     * @return the time unit
     */
    TimeUnit getTimeUnit();

    /**
     * Returns the number of desired repetitions of this {@code FakeLoad} object.
     * The number of repetitions decides how many times the {@code FakeLoad} object
     * will be repeated during execution
     * @return the number of repetitions
     */
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
