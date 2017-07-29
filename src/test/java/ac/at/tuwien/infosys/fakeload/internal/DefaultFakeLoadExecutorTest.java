package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import ac.at.tuwien.infosys.fakeload.FakeLoadExecutor;
import ac.at.tuwien.infosys.fakeload.FakeLoads;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Tests for class {@link DefaultFakeLoadExecutor}
 *
 */
public class DefaultFakeLoadExecutorTest {

    private FakeLoadExecutor executor;

    @Before
    public void setUp() {
        SimulationInfrastructure infrastructure = new DefaultSimulationInfrastructure();
        FakeLoadScheduler scheduler = new DefaultFakeLoadScheduler(infrastructure);
        executor = new DefaultFakeLoadExecutor(scheduler);
    }

    @Test
    public void testSimpleLoadExecution() {
        long duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        FakeLoad fakeLoad = FakeLoads.createLoad().lasting(duration, unit).withCpuLoad(20);
        long startTime = System.nanoTime();
        executor.execute(fakeLoad);
        long stopTime = System.nanoTime();
        long executedTime = stopTime -startTime;
        Assert.assertTrue(executedTime >= unit.toNanos(duration));
    }

}
