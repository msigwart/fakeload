package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import ac.at.tuwien.infosys.fakeload.FakeLoads;
import ac.at.tuwien.infosys.fakeload.MemoryUnit;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for class {@link SystemLoad}
 */
public class SingleThreadedConnectionTest {

    SystemLoad systemLoad;

    @Before
    public void setUp() {
        systemLoad = new SystemLoad();
    }


    // TEST ADDITION AND SUBTRACTION
    @Test
    public void testCpu() {
        try {
            assertEquals(10, increaseAndGetCpu(10));
            assertEquals(30, increaseAndGetCpu(20));
            assertEquals(60, increaseAndGetCpu(30));
            assertEquals(100, increaseAndGetCpu(40));

            assertEquals(90, decreaseAndGetCpu(10));
            assertEquals(70, decreaseAndGetCpu(20));
            assertEquals(40, decreaseAndGetCpu(30));
            assertEquals(0, decreaseAndGetCpu(40));

        } catch (MaximumLoadExceededException e) {
            throw new AssertionError("Shouldn't happen");
        }

        // test negative value
        try {
            decreaseAndGetCpu(10);
        } catch (RuntimeException e) {
            assertEquals("Decrease of CPU to under 0%", e.getMessage());
            assertEquals(0, systemLoad.getCpu());
        }

        // test value too high
        try {
            increaseAndGetCpu(110);
        } catch (MaximumLoadExceededException e) {
            assertEquals("Increase of CPU to over 100%", e.getMessage());
            assertEquals(0, systemLoad.getCpu());
        }
    }


    @Test
    public void testMemory() {
        try {
            assertEquals(10, increaseAndGetMemory(10));
            assertEquals(30, increaseAndGetMemory(20));
            assertEquals(60, increaseAndGetMemory(30));
            assertEquals(100, increaseAndGetMemory(40));

            assertEquals(90, decreaseAndGetMemory(10));
            assertEquals(70, decreaseAndGetMemory(20));
            assertEquals(40, decreaseAndGetMemory(30));
            assertEquals(0, decreaseAndGetMemory(40));
        } catch (MaximumLoadExceededException e) {
            throw new AssertionError("Shouldn't happen");
        }

        // test negative value
        try {
            decreaseAndGetMemory(10);
        } catch (RuntimeException e) {
            assertEquals("Decrease of Memory to under 0%", e.getMessage());
            assertEquals(0, systemLoad.getMemory());
        }

        // test value too high TODO
//        try {
//            increaseAndGetMemory(110);
//        } catch (RuntimeException e) {
//            Assert.assertEquals("Increase of Memory to over 100%", e.getMessage());
//            Assert.assertEquals(0, systemLoadgetMemory());
//        }
    }


    @Test
    public void testDiskIO() {
        try {
            assertEquals(10, increaseAndGetDiskIO(10));
            assertEquals(30, increaseAndGetDiskIO(20));
            assertEquals(60, increaseAndGetDiskIO(30));
            assertEquals(100, increaseAndGetDiskIO(40));

            assertEquals(90, decreaseAndGetDiskIO(10));
            assertEquals(70, decreaseAndGetDiskIO(20));
            assertEquals(40, decreaseAndGetDiskIO(30));
            assertEquals(0, decreaseAndGetDiskIO(40));
        } catch (MaximumLoadExceededException e) {
            throw new AssertionError("Shouldn't happen");
        }
        // test negative value
        try {
            decreaseAndGetDiskIO(10);
        } catch (RuntimeException e) {
            assertEquals("Decrease of Disk IO to under 0%", e.getMessage());
            assertEquals(0, systemLoad.getDiskIO());
        }

        // test value too high TODO
//        try {
//            increaseAndGetDiskIO(110);
//        } catch (RuntimeException e) {
//            Assert.assertEquals("Increase of DiskIO to over 100%", e.getMessage());
//            Assert.assertEquals(0, systemLoadgetDiskIO());
//        }
    }

    @Test
    public void testNetIO() {
        try {
            assertEquals(10, increaseAndGetNetIO(10));
            assertEquals(30, increaseAndGetNetIO(20));
            assertEquals(60, increaseAndGetNetIO(30));
            assertEquals(100, increaseAndGetNetIO(40));

            assertEquals(90, decreaseAndGetNetIO(10));
            assertEquals(70, decreaseAndGetNetIO(20));
            assertEquals(40, decreaseAndGetNetIO(30));
            assertEquals(0, decreaseAndGetNetIO(40));
        } catch (MaximumLoadExceededException e) {
            throw new AssertionError("Shouldn't happen");
        }
        // test negative value
        try {
            decreaseAndGetNetIO(10);
        } catch (RuntimeException e) {
            assertEquals("Decrease of Net IO to under 0%", e.getMessage());
            assertEquals(0, systemLoad.getNetIO());
        }

        // test value too high TODO
//        try {
//            increaseAndGetNetIO(110);
//        } catch (RuntimeException e) {
//            Assert.assertEquals("Increase of NetIO to over 100%", e.getMessage());
//            Assert.assertEquals(0, systemLoad.getNetIO());
//        }
    }



    private long increaseAndGetCpu(int cpu) throws MaximumLoadExceededException {
        FakeLoad fakeLoad = FakeLoads.createLoad().withCpuLoad(cpu);
        systemLoad.increaseBy(fakeLoad);
        return systemLoad.getCpu();
    }

    private long decreaseAndGetCpu(int cpu) {
        FakeLoad fakeLoad = FakeLoads.createLoad().withCpuLoad(cpu);
        systemLoad.decreaseBy(fakeLoad);
        return systemLoad.getCpu();
    }

    private long increaseAndGetMemory(long memory) throws MaximumLoadExceededException {
        FakeLoad fakeLoad = FakeLoads.createLoad().withMemoryLoad(memory, MemoryUnit.BYTES);
        systemLoad.increaseBy(fakeLoad);
        return systemLoad.getMemory();
    }

    private long decreaseAndGetMemory(long memory) {
        FakeLoad fakeLoad = FakeLoads.createLoad().withMemoryLoad(memory, MemoryUnit.BYTES);
        systemLoad.decreaseBy(fakeLoad);
        return systemLoad.getMemory();
    }


    private long increaseAndGetDiskIO(long diskIO) throws MaximumLoadExceededException {
        FakeLoad fakeLoad = FakeLoads.createLoad().withDiskIOLoad(diskIO);
        systemLoad.increaseBy(fakeLoad);
        return systemLoad.getDiskIO();
    }

    private long decreaseAndGetDiskIO(long diskIO) {
        FakeLoad fakeLoad = FakeLoads.createLoad().withDiskIOLoad(diskIO);
        systemLoad.decreaseBy(fakeLoad);
        return systemLoad.getDiskIO();
    }

    private long increaseAndGetNetIO(long netIO) throws MaximumLoadExceededException {
        FakeLoad fakeLoad = FakeLoads.createLoad().withNetIOLoad(netIO);
        systemLoad.increaseBy(fakeLoad);
        return systemLoad.getNetIO();
    }

    private long decreaseAndGetNetIO(long netIO) {
        FakeLoad fakeLoad = FakeLoads.createLoad().withNetIOLoad(netIO);
        systemLoad.decreaseBy(fakeLoad);
        return systemLoad.getNetIO();
    }


}
