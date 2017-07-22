package ac.at.tuwien.infosys.fakeload;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Represents the main API for the FakeLoad Library.
 * If clients want to create 'fake' system load they should instantiate a FakeLoad object with the desired system load
 * instructions and then, subsequently, call that object's execute method, which simulates the specified system loads.
 *
 * @Author Marten Sigwart
 * @since 1.0
 */
public interface FakeLoad {

    /**
     * Executes the fake sytem loads specified by the calling FakeLoad object.
     *
     * <p>This means the 'fake' system loads (CPU, memory, other FakeLoad objects, etc.) specified within the instance are
     * simulated and executed by the system. For example after calling {@code execute()} on a FakeLoad object with a
     * specified CPU load of 80% and a duration of 10 seconds, the system will generate a CPU load of 80% for the next 10 seconds.
     *
     * <p>
     * Note: This should be a blocking call, meaning within a single thread the method call blocks until the load simulation
     * has finished. This is due to the fact that a fake load represents some real computation.
     * A real computation would take all the time it needs (and would therefore block) until it has finished, therefore
     * the fake computation should do so as well. However, different threads calling {@code execute()} should be able to do so
     * at the same time.
     * </p>
     */
    void execute();

    /**
     * Returns a {@code FakeLoad} instance containing the specified duration.
     *
     * <p>The method could merely change an existing duration attribute (like a simple setter) in the case of
     * a mutable {@code FakeLoad} implementation or return a completely new {@code FakeLoad} instance if
     * the underlying implementation class is immutable.
     *
     * @param duration the duration of the returned FakeLoad
     * @param unit the time unit of the duration
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad lasting(long duration, TimeUnit unit);        // name alternative: for(10, TimeUnit.SECONDS)

    /**
     * Returns a {@code FakeLoad} instance with the number of specified repetitions set.
     *
     * <p>The method could merely change an existing repetitions attribute (like a simple setter) in the case of
     * a mutable {@code FakeLoad} implementation or return a completely new {@code FakeLoad} instance if
     * the underlying implementation class is immutable.
     *
     * @param noOfRepetitions number of repetitions of the returned FakeLoad
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad repeat(int noOfRepetitions);

    /**
     * Returns a {@code FakeLoad} instance with the specified CPU load.
     *
     * <p>The method could merely change an existing CPU load attribute (like a simple setter) in the case of
     * a mutable {@code FakeLoad} implementation or return a completely new {@code FakeLoad} instance if
     * the underlying implementation class is immutable.
     *
     * @param cpuLoad the fake CPU load in percent (0-100%)
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad withCpuLoad(int cpuLoad);

    /**
     * Returns a {@code FakeLoad} instance with the specified memory load.
     *
     * <p>The method could merely change an existing memory load attribute (like a simple setter) in the case of
     * a mutable {@code FakeLoad} implementation or return a completely new {@code FakeLoad} instance if
     * the underlying implementation class is immutable.
     *
     * @param amount the amount of memory to be simulated
     * @param unit the memory unit for the specified amount
     * @return returns the FakeLoad object containing the provided parameters.
     */
    FakeLoad withMemoryLoad(long amount, MemoryUnit unit);

    FakeLoad withDiskIOLoad(long diskIOLoad);

    FakeLoad withNetIOLoad(long netIOLoad);

    FakeLoad addLoad(FakeLoad load);

    FakeLoad addLoads(Collection<FakeLoad> loads);

    boolean contains(FakeLoad load);

    Collection<FakeLoad> getLoads();


    int getCpuLoad();

    long getMemoryLoad();

    long getDiskIOLoad();

    long getNetIOLoad();

    long getDuration();

    TimeUnit getTimeUnit();
}
