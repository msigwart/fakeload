package com.martensigwart.fakeload.internal;

import com.martensigwart.fakeload.FakeLoad;
import com.martensigwart.fakeload.FakeLoads;
import com.martensigwart.fakeload.MemoryUnit;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
            Thread.sleep(50);
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


    @Test
    public void testScheduleMethod3() {
        long duration = 2;
        TimeUnit unit = TimeUnit.SECONDS;

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FakeLoad load = FakeLoads.createLoad().lasting(duration, unit)
                            .withCpuLoad(50);

                    Future<Void> future = scheduler.schedule(load);
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread t2 = new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    FakeLoad load2 = FakeLoads.createLoad().lasting(duration, unit)
                            .withCpuLoad(60);

                    Thread.sleep(500);
                    Future<Void> future = scheduler.schedule(load2);
                    future.get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    assertEquals("java.lang.RuntimeException: Increase of 60 would exceed the maximum CPU load limit of 100 %", e.getMessage()); //TODO make message not hardcoded
                }
            }
        }));

        t1.start();
        t2.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
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

    private final SystemLoad systemLoad = new SystemLoad();


    @Override
    public void increaseSystemLoadBy(FakeLoad load) throws MaximumLoadExceededException {
        systemLoad.increaseBy(load);
    }

    @Override
    public void decreaseSystemLoadBy(FakeLoad load) {
        systemLoad.decreaseBy(load);
    }

    public long getCpu() {
        return systemLoad.getCpu();
    }

    public long getMemory() {
        return systemLoad.getMemory();
    }

    public long getDiskIO() {
        return systemLoad.getDiskIO();
    }

    public long getNetIO() {
        return systemLoad.getNetIO();
    }
}
