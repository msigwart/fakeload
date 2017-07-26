package ac.at.tuwien.infosys.fakeload;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Represents the main API for the FakeLoad Library.
 * With instances of this type clients can create and execute 'fake' system load.
 *
 * <p>
 * The client can specify what kind of load (e.g. CPU, RAM, …), how much of it (e.g. 80%, 1024MB, …)
 * and for how long a specific load (e.g. 10 s, 100 ms, …) is being simulated.
 * </p>
 *
 * <p>
 * Further, a fake load can itself consist of other {@code FakeLoad} instances thus giving the client the possibility to
 * create complex patterns of system load instructions.
 * </p>
 *
 * TODO How to use
 * they should instantiate a FakeLoad object with the desired system load
 * instructions and then, subsequently, call that object's execute method, which simulates the specified system loads.
 *
 * @Author Marten Sigwart
 * @since 1.8
 */
public interface FakeLoad {

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
