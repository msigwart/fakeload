package ac.at.tuwien.infosys.fakeload;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 22.07.17.
 */
public class ImmutableFakeLoadTest {

    @Test
    public void testContainsMethod() {
        FakeLoad child = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad same = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad other = new ImmutableFakeLoad(3, TimeUnit.SECONDS);
        FakeLoad parent = new ImmutableFakeLoad(10, TimeUnit.SECONDS)
                .addLoad(child);

        Assert.assertTrue(parent.contains(child));
        Assert.assertTrue(parent.contains(same));
        Assert.assertFalse(parent.contains(other));
    }

    @Test
    public void testEqualsAndHash() {
        FakeLoad f1 = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad f2 = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad f3 = new ImmutableFakeLoad(5, TimeUnit.SECONDS);
        FakeLoad f4 = new ImmutableFakeLoad(3, TimeUnit.SECONDS);

        // Reflexity
        Assert.assertTrue(f1.equals(f1));
        Assert.assertFalse(f1.equals(null));
        Assert.assertFalse(f1.equals(f4));

        // Symmetry
        Assert.assertTrue(f1.equals(f2));
        Assert.assertTrue(f2.equals(f1));

        Assert.assertFalse(f1.equals(f4));
        Assert.assertFalse(f4.equals(f1));

        // Transitivity
        if (f1.equals(f2) && f2.equals(f3)) {
            Assert.assertTrue(f1.equals(f3));
        }

        // Hashcode
        Assert.assertTrue(f1.hashCode() == f1.hashCode());
        Assert.assertTrue(f1.hashCode() == f2.hashCode());
        Assert.assertTrue(f1.hashCode() == f3.hashCode());

    }

}
