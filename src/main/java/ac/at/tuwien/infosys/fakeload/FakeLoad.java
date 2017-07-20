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

    void execute();

    FakeLoad withDuration(long duration, TimeUnit unit);

    FakeLoad withCpuLoad(int cpuLoad);

    FakeLoad withMemoryLoad(long amount, MemoryUnit unit);

    FakeLoad withDiskIOLoad(long diskIOLoad);

    FakeLoad withNetIOLoad(long netIOLoad);

    FakeLoad addLoad(FakeLoad load);

    FakeLoad addLoads(Collection<FakeLoad> loads);

    boolean contains(FakeLoad load);

    Collection<FakeLoad> getLoads();

    void simulateCpu();

    void simulateMemory();

    void simulateNetIO();

    void simulateDiskIO();

}
