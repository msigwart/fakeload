package com.martensigwart.fakeload;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Tests for class {@link DefaultFakeLoadExecutor}
 *
 */
public class FakeLoadExecutionDurationTest {

    private static final Logger log = LoggerFactory.getLogger(FakeLoadExecutionDurationTest.class);

    private FakeLoadExecutor executor;
    private ScheduledExecutorService scheduler;

    @Before
    public void setup() {

        executor = new DefaultFakeLoadExecutor(new SimulationInfrastructure() {
            @Override
            public void increaseSystemLoadBy(FakeLoad load) {
                // do nothing
            }

            @Override
            public void decreaseSystemLoadBy(FakeLoad load) {
                // do nothing
            }

            @Override
            public void shutdown() {
                // do nothing
            }
        });

        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @After
    public void cleanup() {
        scheduler.shutdown();
    }

    @Test
    public void testSimpleLoadExecution() {
        long duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        FakeLoad fakeLoad = FakeLoads.create().lasting(duration, unit).withCpu(20);
        Future<Void> execution = executor.executeAsync(fakeLoad);
        assertFakeLoadExecution(duration, unit, execution);
    }



    @Test
    public void testComplexLoadExecution() {
        long duration = 100;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        FakeLoad parent = FakeLoads.create().lasting(duration, unit)
                .withCpu(99)
                .withMemory(9999, MemoryUnit.BYTES);

        int noOfChildren = 10;
        int noOfGrandChildrenPerChild = 9;

        int startCPU = 1;
        long startMemory = 1000;

        // create children
        for (int i = 0; i < noOfChildren; i++) {
            FakeLoad child = FakeLoads.create().lasting(duration, unit)
                    .withCpu(startCPU * i)
                    .withMemory(startMemory * i, MemoryUnit.KB);

            // create grand children
            for (int j = 0; j < noOfGrandChildrenPerChild; j++) {
                FakeLoad grandChild = FakeLoads.create().lasting(duration, unit)
                        .withCpu(startCPU + j)
                        .withMemory(startMemory * i + j, MemoryUnit.KB);

                child = child.addLoad(grandChild);
            }

            parent = parent.addLoad(child);

        }

        Future<Void> execution = executor.executeAsync(parent);

        assertFakeLoadExecution(duration + duration * noOfChildren * noOfGrandChildrenPerChild, unit, execution);
    }

    @Test
    public void testInterruptedLoadExecution() {
        FakeLoad fakeLoad = FakeLoads.create().lasting(10, TimeUnit.SECONDS).withCpu(20);
        Future<Void> execution = executor.executeAsync(fakeLoad);
        scheduler.schedule(() -> {
            execution.cancel(true);
        }, 5, TimeUnit.SECONDS);
        assertFakeLoadExecution(5, TimeUnit.SECONDS, execution);
    }


    private void assertFakeLoadExecution(long expectedDuration, TimeUnit unit, Future<Void> execution) {
        log.info("Test should run for {} {}", expectedDuration, unit);
        long startTime = System.nanoTime();
        try {
            execution.get();
        } catch (InterruptedException | ExecutionException | CancellationException e) {
            log.info("Execution interrupted");
        }
        long stopTime = System.nanoTime();
        long executedTime = stopTime - startTime;
        // check fake load execution time (with 2 second buffer)
        Assert.assertTrue(executedTime >= unit.toNanos(expectedDuration) - TimeUnit.SECONDS.toNanos(2));
        Assert.assertTrue(executedTime <= unit.toNanos(expectedDuration) + TimeUnit.SECONDS.toNanos(2));
    }

}
