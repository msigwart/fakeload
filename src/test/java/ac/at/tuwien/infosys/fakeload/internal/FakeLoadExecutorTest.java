package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoadExecutor;
import org.junit.Test;

/**
 * Tests for class {@link DefaultFakeLoadExecutor}
 *
 */
public class FakeLoadExecutorTest {

    @Test
    public void test1() {
        SimulationInfrastructure infrastructure = new DefaultSimulationInfrastructure();
        FakeLoadExecutor executor = new DefaultFakeLoadExecutor(infrastructure);
    }

}
