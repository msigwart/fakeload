package com.martensigwart.fakeload;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for class {@link SystemLoad}
 */
public class SingleThreadSystemLoadTest {

    private SystemLoad systemLoad;

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
            assertEquals("Decrease of 10 would cause a negative CPU load", e.getMessage());
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
            assertEquals("Decrease of 10 would cause a negative memory load", e.getMessage());
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
            assertEquals("Decrease of 10 would cause a negative disk IO load", e.getMessage());
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


    private long increaseAndGetCpu(int cpu) throws MaximumLoadExceededException {
        FakeLoad fakeLoad = FakeLoads.createLoad().withCpu(cpu);
        systemLoad.increaseBy(fakeLoad);
        return systemLoad.getCpu();
    }

    private long decreaseAndGetCpu(int cpu) {
        FakeLoad fakeLoad = FakeLoads.createLoad().withCpu(cpu);
        systemLoad.decreaseBy(fakeLoad);
        return systemLoad.getCpu();
    }

    private long increaseAndGetMemory(long memory) throws MaximumLoadExceededException {
        FakeLoad fakeLoad = FakeLoads.createLoad().withMemory(memory, MemoryUnit.BYTES);
        systemLoad.increaseBy(fakeLoad);
        return systemLoad.getMemory();
    }

    private long decreaseAndGetMemory(long memory) {
        FakeLoad fakeLoad = FakeLoads.createLoad().withMemory(memory, MemoryUnit.BYTES);
        systemLoad.decreaseBy(fakeLoad);
        return systemLoad.getMemory();
    }


    private long increaseAndGetDiskIO(long diskIO) throws MaximumLoadExceededException {
        FakeLoad fakeLoad = FakeLoads.createLoad().withDiskIO(diskIO);
        systemLoad.increaseBy(fakeLoad);
        return systemLoad.getDiskIO();
    }

    private long decreaseAndGetDiskIO(long diskIO) {
        FakeLoad fakeLoad = FakeLoads.createLoad().withDiskIO(diskIO);
        systemLoad.decreaseBy(fakeLoad);
        return systemLoad.getDiskIO();
    }




}
