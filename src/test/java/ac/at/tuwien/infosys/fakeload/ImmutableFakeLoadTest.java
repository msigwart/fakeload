package ac.at.tuwien.infosys.fakeload;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by martensigwart on 22.07.17.
 */
public class ImmutableFakeLoadTest {

    @Test
    public void testContainsMethod() {
        FakeLoad child1 = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad child2 = new ImmutableFakeLoad(500, TimeUnit.MILLISECONDS);
        FakeLoad same = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad other = new ImmutableFakeLoad(3, TimeUnit.SECONDS);
        FakeLoad parent = new ImmutableFakeLoad(10, TimeUnit.SECONDS)
                .addLoad(child1)
                .addLoad(child2);

        assertTrue(parent.contains(child2));
        assertTrue(parent.contains(same));
        assertFalse(parent.contains(other));
    }

    @Test
    public void testEqualsAndHash() {
        FakeLoad f1 = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad f2 = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad f3 = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad f4 = new ImmutableFakeLoad(3, TimeUnit.SECONDS);

        // Reflexity
        assertTrue(f1.equals(f1));
        assertFalse(f1.equals(null));
        assertFalse(f1.equals(f4));

        // Symmetry
        assertTrue(f1.equals(f2));
        assertTrue(f2.equals(f1));

        assertFalse(f1.equals(f4));
        assertFalse(f4.equals(f1));

        // Transitivity
        if (f1.equals(f2) && f2.equals(f3)) {
            assertTrue(f1.equals(f3));
        }

        // Hashcode
        assertTrue(f1.hashCode() == f1.hashCode());
        assertTrue(f1.hashCode() == f2.hashCode());
        assertTrue(f1.hashCode() == f3.hashCode());

    }

    @Test
    public void testValidCreation() {
        String errorMessage = "";       // error message for exceptions

        // Zero Argument Constructor
        FakeLoad f1 = new ImmutableFakeLoad();
        assertEquals(0L, f1.getDuration());
        assertEquals(TimeUnit.MILLISECONDS, f1.getTimeUnit());
        assertEquals(0, f1.getCpuLoad());
        assertEquals(0L, f1.getMemoryLoad());
        assertEquals(0L, f1.getDiskIOLoad());
        assertEquals(0L, f1.getNetIOLoad());
        assertEquals(0, f1.getLoads().size());

        // Duration Argument Constructor
        f1 = new ImmutableFakeLoad(10L, TimeUnit.SECONDS);
        assertEquals(10L, f1.getDuration());
        assertEquals(TimeUnit.SECONDS, f1.getTimeUnit());
        assertEquals(0, f1.getCpuLoad());
        assertEquals(0L, f1.getMemoryLoad());
        assertEquals(0L, f1.getDiskIOLoad());
        assertEquals(0L, f1.getNetIOLoad());
        assertEquals(0, f1.getLoads().size());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentCreation() {
        FakeLoad f1 = new ImmutableFakeLoad(-1L, TimeUnit.SECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentCreation() {
        FakeLoad f1 = new ImmutableFakeLoad(20, null);
    }



    @Test
    public void testLastingMethod() {
        FakeLoad f1 = new ImmutableFakeLoad().lasting(10, TimeUnit.SECONDS);
        assertEquals(10L, f1.getDuration());
        assertEquals(TimeUnit.SECONDS, f1.getTimeUnit());
        assertEquals(0, f1.getCpuLoad());
        assertEquals(0L, f1.getMemoryLoad());
        assertEquals(0L, f1.getDiskIOLoad());
        assertEquals(0L, f1.getNetIOLoad());

        FakeLoad f2 = f1.lasting(10, TimeUnit.SECONDS);
        assertEquals(f2, f1);
        assertFalse(f1 == f2);

    }

    @Test
    public void testAddLoadMethod() {
        FakeLoad child1 = new ImmutableFakeLoad(100, TimeUnit.MILLISECONDS).withCpuLoad(20);
        FakeLoad child2 = new ImmutableFakeLoad(10, TimeUnit.SECONDS).withMemoryLoad(100, MemoryUnit.MB);

        FakeLoad f1 = new ImmutableFakeLoad();
        assertEquals(0, f1.getLoads().size());
        f1 = f1.addLoad(child1).addLoad(child2);
        assertEquals(2, f1.getLoads().size());
    }

    @Test
    public void testAddLoadsMethod() {
        FakeLoad child1 = new ImmutableFakeLoad(100, TimeUnit.MILLISECONDS).withCpuLoad(20);
        FakeLoad child2 = new ImmutableFakeLoad(10, TimeUnit.SECONDS).withMemoryLoad(100, MemoryUnit.MB);
        FakeLoad child3 = new ImmutableFakeLoad(10, TimeUnit.SECONDS).withMemoryLoad(300, MemoryUnit.MB);
        List<FakeLoad> children = new ArrayList<>();
        children.add(child2);
        children.add(child3);


        FakeLoad f1 = new ImmutableFakeLoad().addLoad(child1);
        assertEquals(1, f1.getLoads().size());
        f1 = f1.addLoads(children);
        assertEquals(3, f1.getLoads().size());

    }

}
