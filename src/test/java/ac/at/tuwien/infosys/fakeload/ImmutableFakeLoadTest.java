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

}
