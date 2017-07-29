package ac.at.tuwien.infosys.fakeload.internal.util;

import static org.junit.Assert.assertEquals;

/**
 * Created by martensigwart on 28.07.17.
 */
public class MoreAsserts {

    public static void assertEqualsTransitivity(Object o1, Object o2, Object f3) {
        if (o1.equals(o2) && o2.equals(f3)) {
            assertEquals(o1, f3);
        }
    }
}
