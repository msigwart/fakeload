package com.martensigwart.fakeload;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test class for class {@link DefaultSimulationInfrastructure}
 */
public class DefaultSimulationInfrastructureTest {

    LoadController mockController;
    DefaultSimulationInfrastructure infrastructure;

    @Before
    public void setUp() {

        mockController = mock(LoadController.class);

        infrastructure = new DefaultSimulationInfrastructure(Executors.newFixedThreadPool(1), mockController);
    }

    @Test
    public void increaseSystemLoadBy() throws Exception {
        FakeLoad fakeLoad = FakeLoads.create().lasting(10, TimeUnit.SECONDS).withCpu(50);

        infrastructure.increaseSystemLoadBy(fakeLoad);

        verify(mockController).increaseSystemLoadBy(fakeLoad);
    }

    @Test
    public void decreaseSystemLoadBy() throws Exception {
        FakeLoad fakeLoad = FakeLoads.create().lasting(10, TimeUnit.SECONDS).withCpu(50);

        infrastructure.decreaseSystemLoadBy(fakeLoad);

        verify(mockController).decreaseSystemLoadBy(fakeLoad);
    }

}