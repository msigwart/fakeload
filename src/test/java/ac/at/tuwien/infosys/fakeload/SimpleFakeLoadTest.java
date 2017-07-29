package ac.at.tuwien.infosys.fakeload;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by martensigwart on 22.07.17.
 */
public class SimpleFakeLoadTest extends AbstractFakeLoadTest {

    public SimpleFakeLoadTest() {
        super(SimpleFakeLoad.class);
    }

    //--------------------------------------------------------------------------------------------------
//   TEST CONSTRUCTORS
//--------------------------------------------------------------------------------------------------
    @Test
    public void testValidCreation() {
        String errorMessage = "";       // error message for exceptions

        // Zero Argument Constructor
        FakeLoad f1 = new SimpleFakeLoad();
        assertDefault(f1);

        // Duration Argument Constructor
        f1 = new SimpleFakeLoad(10L, TimeUnit.SECONDS);
        assertDuration(10L, TimeUnit.SECONDS, f1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentCreation() {
        FakeLoad f1 = new SimpleFakeLoad(-1L, TimeUnit.SECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentCreation() {
        FakeLoad f1 = new SimpleFakeLoad(20, null);
    }



}
