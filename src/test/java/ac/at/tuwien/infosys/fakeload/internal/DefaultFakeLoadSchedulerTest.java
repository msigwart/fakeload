package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import ac.at.tuwien.infosys.fakeload.FakeLoads;
import ac.at.tuwien.infosys.fakeload.MemoryUnit;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DefaultFakeLoadScheduler}.
 *
 * @author Marten Sigwart
 */
public class DefaultFakeLoadSchedulerTest {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadSchedulerTest.class);

    private DefaultFakeLoadScheduler scheduler;
    private MockInfrastructure infrastructure;

    @Before
    public void setUp() {
        infrastructure = new MockInfrastructure();
        scheduler = new DefaultFakeLoadScheduler(infrastructure);
    }


    @Test
    public void testScheduleMethod() {
        long duration = 5;
        TimeUnit unit = TimeUnit.SECONDS;
        long cpu = 20;
        long memory = 30;
        long diskIO = 40;
        long netIO = 50;

        FakeLoad fakeLoad = FakeLoads.createLoad().lasting(duration, unit)
                .withCpuLoad(cpu)
                .withMemoryLoad(memory, MemoryUnit.BYTES)
                .withDiskIOLoad(diskIO)
                .withNetIOLoad(netIO);

        assertInfrastructureValues(0, 0, 0, 0, infrastructure);

        try {
            Future<Void> future = scheduler.schedule(fakeLoad);
            Thread.sleep(10);
            assertInfrastructureValues(cpu, memory, diskIO, netIO, infrastructure);
            future.get();
            assertInfrastructureValues(0, 0, 0, 0, infrastructure);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testScheduleMethod2() {
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        FakeLoad fakeLoad = FakeLoads.createLoad().lasting(duration, unit)
                .withCpuLoad(99)
                .withMemoryLoad(9999, MemoryUnit.BYTES)
                .withDiskIOLoad(99)
                .withNetIOLoad(99);

        int noOfChildren = 10;
        int noOfGrandChildrenPerChild = 9;

        long startCPU = 1;
        long startMemory = 100;
        long startDiskIO = 100;
        long startNetIO = 100;

        List<long[]> loadList = new ArrayList<>();

        // create children
        for (int i=0; i<noOfChildren; i++) {
            add(loadList, startCPU, startMemory, startDiskIO, startNetIO);

            FakeLoad child = FakeLoads.createLoad().lasting(duration, unit)
                    .withCpuLoad(startCPU++)
                    .withMemoryLoad(startMemory++, MemoryUnit.BYTES)
                    .withDiskIOLoad(startDiskIO++)
                    .withNetIOLoad(startNetIO++);


            // create grand children
            for (int j=0; j<noOfGrandChildrenPerChild; j++) {
                add(loadList, startCPU, startMemory, startDiskIO, startNetIO);

                FakeLoad grandChild = FakeLoads.createLoad().lasting(duration, unit)
                        .withCpuLoad(startCPU++)
                        .withMemoryLoad(startMemory++, MemoryUnit.BYTES)
                        .withDiskIOLoad(startDiskIO++)
                        .withNetIOLoad(startNetIO++);

                child = child.addLoad(grandChild);
            }

            fakeLoad = fakeLoad.addLoad(child);

        }

        assertEquals(noOfChildren+noOfChildren*noOfGrandChildrenPerChild, loadList.size());

        try {
            Future<Void> future = scheduler.schedule(fakeLoad);
            Thread.sleep(100);
            assertInfrastructureValues(99, 9999, 99, 99, infrastructure);

            for (int i=0; i<loadList.size(); i++) {
                Thread.sleep(unit.toMillis(duration));
                long values[] = loadList.get(i);
                assertInfrastructureValues(values[0], values[1], values[2], values[3], infrastructure);
            }

            future.get();
            assertInfrastructureValues(0,0,0,0, infrastructure);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void add(List<long[]> loadList, long cpu, long memory, long diskIO, long netIO) {
        loadList.add(new long[]{cpu, memory, diskIO, netIO});
    }


    private void assertInfrastructureValues(long expectedCpu, long expectedMemory,
                                            long expectedDiskIO, long expectedNetIO,
                                            MockInfrastructure actual) {

        assertEquals(expectedCpu, actual.getCpu());
        assertEquals(expectedMemory, actual.getMemory());
        assertEquals(expectedDiskIO, actual.getDiskIO());
        assertEquals(expectedNetIO, actual.getNetIO());

    }

}

class MockInfrastructure implements SimulationInfrastructure {

    private static final Logger log = LoggerFactory.getLogger(MockInfrastructure.class);

    private AtomicLong cpu = new AtomicLong(0L);
    private AtomicLong memory = new AtomicLong(0L);
    private AtomicLong diskIO = new AtomicLong(0L);
    private AtomicLong netIO = new AtomicLong(0L);

    @Override
    public void start() {
        // ignore
    }

    @Override
    public void stop() {
        // ignore
    }

    @Override
    public synchronized void increaseCpu(long cpuLoad) throws MaximumLoadExceededException {
        if (cpu.get()+cpuLoad > 100) throw new MaximumLoadExceededException("CPU Exceeded");

        long newCpu = cpu.accumulateAndGet(cpuLoad, (a,b) -> a+b);
        log.debug("Increased CPU to {}", newCpu);
    }

    @Override
    public synchronized void increaseMemory(long memoryLoad) throws MaximumLoadExceededException {
        long newMemory = memory.accumulateAndGet(memoryLoad, (a,b) -> a+b);
        log.debug("Increased memory to {}", newMemory);
    }

    @Override
    public synchronized void increaseDiskIO(long diskIOLoad) throws MaximumLoadExceededException {
        long newDiskIO = diskIO.accumulateAndGet(diskIOLoad, (a,b) -> a+b);
        log.debug("Increased disk IO to {}", newDiskIO);
    }

    @Override
    public synchronized void increaseNetIO(long netIOLoad) throws MaximumLoadExceededException {
        long newNetIO = netIO.accumulateAndGet(netIOLoad, (a,b) -> a+b);
        log.debug("Increased net IO to {}", newNetIO);
    }

    @Override
    public synchronized void decreaseCpu(long cpuLoad) {
        if (cpu.get()-cpuLoad < 0) throw new RuntimeException("CPU negative");
        long newCpu = cpu.accumulateAndGet(cpuLoad, (a,b) -> a-b);
        log.debug("Decrease CPU to {}", newCpu);
    }

    @Override
    public synchronized void decreaseMemory(long memoryLoad) {
        if (memory.get()-memoryLoad < 0) throw new RuntimeException("Memory negative");
        long newMemory = memory.accumulateAndGet(memoryLoad, (a,b) -> a-b);
        log.debug("Decrease memory to {}", newMemory);
    }

    @Override
    public synchronized void decreaseDiskIO(long diskIOLoad) {
        if (diskIO.get()-diskIOLoad < 0) throw new RuntimeException("Disk IO negative");
        long newDiskIO = diskIO.accumulateAndGet(diskIOLoad, (a,b) -> a-b);
        log.debug("Decrease disk IO to {}", newDiskIO);
    }

    @Override
    public synchronized void decreaseNetIO(long netIOLoad) {
        if (netIO.get()-netIOLoad < 0) throw new RuntimeException("Net IO negative");
        long newNetIO = netIO.accumulateAndGet(netIOLoad, (a,b) -> a-b);
        log.debug("Decrease net IO to {}", newNetIO);
    }

    public long getCpu() {
        return cpu.get();
    }

    public long getMemory() {
        return memory.get();
    }

    public long getDiskIO() {
        return diskIO.get();
    }

    public long getNetIO() {
        return netIO.get();
    }
}
