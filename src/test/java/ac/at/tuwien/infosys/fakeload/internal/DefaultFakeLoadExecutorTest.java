package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoadExecutor;
import org.junit.Test;

/**
 * Tests for class {@link DefaultFakeLoadExecutor}
 *
 */
public class DefaultFakeLoadExecutorTest {

    @Test
    public void testSimpleLoadExecution() {
        SimulationInfrastructure infrastructure = new DefaultSimulationInfrastructure();
        FakeLoadExecutor executor = new DefaultFakeLoadExecutor(infrastructure);
    }

}
