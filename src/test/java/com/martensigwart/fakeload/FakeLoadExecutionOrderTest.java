package com.martensigwart.fakeload;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DefaultFakeLoadScheduler}.
 *
 * @author Marten Sigwart
 */
public class FakeLoadExecutionOrderTest {

    private DefaultFakeLoadExecutor executor;
    private SimulationInfrastructure mockedInfrastructure;
    private List<FakeLoad> loadList;

    @Before
    public void setUp() {
        mockedInfrastructure = mock(SimulationInfrastructure.class);

        executor = new DefaultFakeLoadExecutor(mockedInfrastructure);
        loadList = new ArrayList<>();
    }

    @Test
    public void testScheduleMethod() {
        long duration = 5;
        TimeUnit unit = TimeUnit.SECONDS;
        int cpu = 20;
        long memory = 30;
        long diskInput = 40;

        // Creation
        FakeLoad fakeLoad = FakeLoads.create().lasting(duration, unit)
                .withCpu(cpu)
                .withMemory(memory, MemoryUnit.BYTES)
                .withDiskInput(diskInput, MemoryUnit.BYTES);

        // Execution
        try {
            Future<Void> future = executor.executeAsync(fakeLoad);
            future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Verification
        InOrder inOrder = inOrder(mockedInfrastructure);
        try {
            inOrder.verify(mockedInfrastructure).increaseSystemLoadBy(fakeLoad);
            inOrder.verify(mockedInfrastructure).decreaseSystemLoadBy(fakeLoad);

        } catch (MaximumLoadExceededException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testScheduleMethod2() {
        long duration = 100;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // Creation
        FakeLoad fakeLoad = FakeLoads.create().lasting(duration, unit)
                .withCpu(99)
                .withMemory(9999, MemoryUnit.BYTES)
                .withDiskInput(99, MemoryUnit.BYTES);

        loadList.add(fakeLoad);

        int noOfChildren = 10;
        int noOfGrandChildrenPerChild = 9;

        int startCPU = 1;
        long startMemory = 100;
        long startDiskInput = 100;

        // create children
        for (int i=0; i<noOfChildren; i++) {


            FakeLoad child = FakeLoads.create().lasting(duration, unit)
                    .withCpu(startCPU++)
                    .withMemory(startMemory++, MemoryUnit.BYTES)
                    .withDiskInput(startDiskInput++, MemoryUnit.BYTES);

            loadList.add(child);

            // create grand children
            for (int j = 0; j < noOfGrandChildrenPerChild; j++) {

                FakeLoad grandChild = FakeLoads.create().lasting(duration, unit)
                        .withCpu(startCPU++)
                        .withMemory(startMemory++, MemoryUnit.BYTES)
                        .withDiskInput(startDiskInput++, MemoryUnit.BYTES);

                loadList.add(grandChild);

                child = child.addLoad(grandChild);
            }

            fakeLoad = fakeLoad.addLoad(child);

        }

        assertEquals(noOfChildren+noOfChildren*noOfGrandChildrenPerChild+1, loadList.size());

        // Execution
        try {
            Future<Void> future = executor.executeAsync(fakeLoad);
            future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Verification
        InOrder inOrder = inOrder(mockedInfrastructure);
        try {
            for (FakeLoad load : loadList) {
                inOrder.verify(mockedInfrastructure).increaseSystemLoadBy(load);
                inOrder.verify(mockedInfrastructure).decreaseSystemLoadBy(load);
            }

        } catch (MaximumLoadExceededException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testScheduleMethod3() {
        long duration = 2;
        TimeUnit unit = TimeUnit.SECONDS;

        // Creation
        FakeLoad load = FakeLoads.create().lasting(duration, unit)
                .withCpu(50);
        loadList.add(load);

        FakeLoad load2 = FakeLoads.create().lasting(duration, unit)
                .withCpu(60);
        loadList.add(load2);


        // Execution
        Thread t1 = new Thread(() -> {
            try {
                Future<Void> future = executor.executeAsync(load);
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(500);

                Future<Void> future = executor.executeAsync(load2);
                future.get();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verification
        InOrder inOrder = inOrder(mockedInfrastructure);
        try {
            inOrder.verify(mockedInfrastructure).increaseSystemLoadBy(loadList.get(0));
            inOrder.verify(mockedInfrastructure).increaseSystemLoadBy(loadList.get(1));
            inOrder.verify(mockedInfrastructure).decreaseSystemLoadBy(loadList.get(0));
            inOrder.verify(mockedInfrastructure).decreaseSystemLoadBy(loadList.get(1));

        } catch (MaximumLoadExceededException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRepetitions() {
        int noOfRepetitions = 3;

        // Creation
        FakeLoad fakeLoad = FakeLoads.create()
                .lasting(500, TimeUnit.MILLISECONDS)
                .withCpu(30)
                .repeat(noOfRepetitions);

        // Execution
        try {
            Future<Void> future = executor.executeAsync(fakeLoad);
            future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Verification
        InOrder inOrder = inOrder(mockedInfrastructure);
        try {
            for (int i = 0; i < noOfRepetitions; i++) {
                inOrder.verify(mockedInfrastructure).increaseSystemLoadBy(fakeLoad);
                inOrder.verify(mockedInfrastructure).decreaseSystemLoadBy(fakeLoad);
            }
        } catch (MaximumLoadExceededException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRepetitions2() {
        int parentRepetitions = 2;
        int childRepetitions = 3;

        // Creation
        FakeLoad parent = FakeLoads.create()
                .lasting(500, TimeUnit.MILLISECONDS)
                .withCpu(10);

        loadList.add(parent);

        // create child
        FakeLoad child = FakeLoads.create();

        // create grand children
        FakeLoad g1 = FakeLoads.create()
                .lasting(500, TimeUnit.MILLISECONDS)
                .withCpu(20);
        loadList.add(g1);

        FakeLoad g2 = FakeLoads.create()
                .lasting(500, TimeUnit.MILLISECONDS)
                .withCpu(30);
        loadList.add(g2);

        child = child.addLoad(g1).addLoad(g2).repeat(childRepetitions);

        parent = parent.addLoad(child).repeat(parentRepetitions);


        // Execution
        try {
            Future<Void> future = executor.executeAsync(parent);
            future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Verification
        InOrder inOrder = inOrder(mockedInfrastructure);
        try {
            for (int i = 0; i < parentRepetitions; i++) {
                inOrder.verify(mockedInfrastructure).increaseSystemLoadBy(loadList.get(0));
                inOrder.verify(mockedInfrastructure).decreaseSystemLoadBy(loadList.get(0));

                for (int j = 0; j < childRepetitions; j++) {
                    // grandchild 1
                    inOrder.verify(mockedInfrastructure).increaseSystemLoadBy(loadList.get(1));
                    inOrder.verify(mockedInfrastructure).decreaseSystemLoadBy(loadList.get(1));

                    // grandchild 2
                    inOrder.verify(mockedInfrastructure).increaseSystemLoadBy(loadList.get(2));
                    inOrder.verify(mockedInfrastructure).decreaseSystemLoadBy(loadList.get(2));

                }
            }
        } catch (MaximumLoadExceededException e) {
            e.printStackTrace();
        }
    }
}

