package com.martensigwart.fakeload;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class DefaultFakeLoadSchedulerTest {

    private static final Logger log = LoggerFactory.getLogger(DefaultFakeLoadSchedulerTest.class);

    private DefaultFakeLoadScheduler scheduler;
    private SimulationInfrastructure mockedInfrastructure;
    private List<FakeLoad> loadList;

    @Before
    public void setUp() {
        mockedInfrastructure = mock(SimulationInfrastructure.class);

        scheduler = new DefaultFakeLoadScheduler(mockedInfrastructure);
        loadList = new ArrayList<>();
    }

    //TODO test with a mocking framework

    @Test
    public void testScheduleMethod() {
        long duration = 5;
        TimeUnit unit = TimeUnit.SECONDS;
        long cpu = 20;
        long memory = 30;
        long diskIO = 40;
        long netIO = 50;

        FakeLoad fakeLoad = FakeLoads.createLoad().lasting(duration, unit)
                .withCpu(cpu)
                .withMemory(memory, MemoryUnit.BYTES)
                .withDiskInput(diskIO);

        try {
            Future<Void> future = scheduler.schedule(fakeLoad);
            future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // verification
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
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        FakeLoad fakeLoad = FakeLoads.createLoad().lasting(duration, unit)
                .withCpu(99)
                .withMemory(9999, MemoryUnit.BYTES)
                .withDiskInput(99);

        loadList.add(fakeLoad);

        int noOfChildren = 10;
        int noOfGrandChildrenPerChild = 9;

        long startCPU = 1;
        long startMemory = 100;
        long startDiskIO = 100;

        // create children
        for (int i=0; i<noOfChildren; i++) {


            FakeLoad child = FakeLoads.createLoad().lasting(duration, unit)
                    .withCpu(startCPU++)
                    .withMemory(startMemory++, MemoryUnit.BYTES)
                    .withDiskInput(startDiskIO++);

            loadList.add(child);

            // create grand children
            for (int j=0; j<noOfGrandChildrenPerChild; j++) {

                FakeLoad grandChild = FakeLoads.createLoad().lasting(duration, unit)
                        .withCpu(startCPU++)
                        .withMemory(startMemory++, MemoryUnit.BYTES)
                        .withDiskInput(startDiskIO++);

                loadList.add(grandChild);

                child = child.addLoad(grandChild);
            }

            fakeLoad = fakeLoad.addLoad(child);

        }

        assertEquals(noOfChildren+noOfChildren*noOfGrandChildrenPerChild+1, loadList.size());

        try {
            Future<Void> future = scheduler.schedule(fakeLoad);
            future.get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // verification
        InOrder inOrder = inOrder(mockedInfrastructure);
        try {
            for (int i=0; i<loadList.size(); i++){
                inOrder.verify(mockedInfrastructure).increaseSystemLoadBy(loadList.get(i));
                inOrder.verify(mockedInfrastructure).decreaseSystemLoadBy(loadList.get(i));
            }

        } catch (MaximumLoadExceededException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testScheduleMethod3() {
        long duration = 2;
        TimeUnit unit = TimeUnit.SECONDS;

        FakeLoad load = FakeLoads.createLoad().lasting(duration, unit)
                .withCpu(50);
        loadList.add(load);

        FakeLoad load2 = FakeLoads.createLoad().lasting(duration, unit)
                .withCpu(60);
        loadList.add(load2);


        Thread t1 = new Thread(() -> {
            try {
                Future<Void> future = scheduler.schedule(load);
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread((() -> {
            try {
                Thread.sleep(500);

                Future<Void> future = scheduler.schedule(load2);
                future.get();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }));

        t1.start();
        t2.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // verification
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




}

