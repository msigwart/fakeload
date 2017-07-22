package ac.at.tuwien.infosys.fakeload;

/**
 * Factory and utility methods for {@link FakeLoad} classes defined in this package.
 *
 * <p> Contains methods that create and return instances of the {@link FakeLoad} interface.
 * The actual type of the objects returned depends on the parameters passed to the factory methods.
 *
 * @since 1.0
 * @author Marten Sigwart
 */
public final class FakeLoads {

    /**
     * Creates a new empty {@code FakeLoad} instance.
     * @return a newly created {@code LoadPattern} instance
     */
    public static FakeLoad createLoad() {
        return new ImmutableFakeLoad();
    }

    public static FakeLoad createLoad(long duration, String... loads) {
        // TODO return FakeLoad with provided parameters
        return new ImmutableFakeLoad();
    }



    // Suppress default constructor for non-instantiability
    private FakeLoads() {
        throw new AssertionError();
    }
}
