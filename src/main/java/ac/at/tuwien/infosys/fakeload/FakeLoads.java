package ac.at.tuwien.infosys.fakeload;

/**
 * Created by martensigwart on 20.07.17.
 */
public final class FakeLoads {

    public static FakeLoad createLoad() {
        return new MutableFakeLoad();
    }

    public static FakeLoad createLoad(long duration, String... loads) {
        return new ImmutableFakeLoad();
    }

    // Suppress default constructor for non-instantiability
    private FakeLoads() {
        throw new AssertionError();
    }
}
