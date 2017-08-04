package com.martensigwart.fakeload.internal;

import com.martensigwart.fakeload.FakeLoad;
import com.martensigwart.fakeload.FakeLoadExecutor;
import com.martensigwart.fakeload.FakeLoads;
import com.martensigwart.fakeload.MemoryUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * Tests for class {@link DefaultFakeLoadExecutor}
 *
 */
public class DefaultFakeLoadExecutorTest {

    private FakeLoadExecutor executor;

    @Before
    public void setUp() {

        executor = new DefaultFakeLoadExecutor(new FakeLoadScheduler() {
            @Override
            public Future<Void> schedule(FakeLoad fakeLoad) {

                ExecutorService executorService = Executors.newSingleThreadExecutor();

                return executorService.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        for (FakeLoad f: fakeLoad) {
                            try {
                                Thread.sleep(f.getTimeUnit().toMillis(f.getDuration()));
                            } catch (InterruptedException e) {
                                throw new RuntimeException("Should not happen");
                            }
                        }
                        return null;
                    }
                });

            }
        });
    }

    @Test
    public void testSimpleLoadExecution() {
        long duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        FakeLoad fakeLoad = FakeLoads.createLoad().lasting(duration, unit).withCpuLoad(20);
        assertFakeLoadExecution(duration, unit, fakeLoad);
    }



    @Test
    public void testComplexLoadExecution() {
        long duration = 100;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        FakeLoad parent = FakeLoads.createLoad().lasting(duration, unit)
                .withCpuLoad(99)
                .withMemoryLoad(9999, MemoryUnit.BYTES);

        int noOfChildren = 10;
        int noOfGrandChildrenPerChild = 9;

        long startCPU = 1;
        long startMemory = 1000;

        // create children
        for (int i=0; i<noOfChildren; i++) {
            FakeLoad child = FakeLoads.createLoad().lasting(duration, unit)
                    .withCpuLoad(startCPU*i)
                    .withMemoryLoad(startMemory*i, MemoryUnit.KB);

            // create grand children
            for (int j=0; j<noOfGrandChildrenPerChild; j++) {
                FakeLoad grandChild = FakeLoads.createLoad().lasting(duration, unit)
                        .withCpuLoad(startCPU+j)
                        .withMemoryLoad(startMemory*i+j, MemoryUnit.KB);

                child = child.addLoad(grandChild);
            }

            parent = parent.addLoad(child);

        }

        assertFakeLoadExecution(duration+duration*noOfChildren*noOfGrandChildrenPerChild, unit, parent);
    }


    private void assertFakeLoadExecution(long expectedDuration, TimeUnit unit, FakeLoad fakeLoad) {
        long startTime = System.nanoTime();
        executor.execute(fakeLoad);
        long stopTime = System.nanoTime();
        long executedTime = stopTime -startTime;
        Assert.assertTrue(executedTime >= unit.toNanos(expectedDuration));
    }

}
