package ac.at.tuwien.infosys.fakeload.internal;

import org.junit.Test;

/**
 * Tests for class {@link DefaultFakeLoadDispatcher}
 *
 */
public class FakeLoadDispatcherTest {

    @Test
    public void test1() {
        FakeLoadDispatcher dispatcher = new DefaultFakeLoadDispatcher(DefaultInfrastructure.INSTANCE);
    }

}
