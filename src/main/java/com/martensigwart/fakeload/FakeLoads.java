package com.martensigwart.fakeload;

import java.util.List;

/**
 * Factory and utility methods for {@link FakeLoad} classes defined in this package.
 *
 * <p> Contains methods that create and return instances of the {@link FakeLoad} interface.
 * The actual type of the objects returned depends on the parameters passed to the factory methods.
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public final class FakeLoads {

    /**
     * Creates a new empty {@code FakeLoad} instance.
     * @return a newly created {@code LoadPattern} instance
     */
    public static FakeLoad createLoad() {
        return new SimpleFakeLoad();
    }


    /**
     * Creates a new {@code FakeLoad} object with the specified parameters.
     *
     * <p> Parameter <b>loads</b> is a String array containing load instructions for different simulation aspects.
     * <ul>
     *  <li> CPU load is specified as percentage, e.g. for a CPU load of 60 percent specify "60%"</li>
     *  <li> Memory can be specified as Bytes(b), kB(k), MB(m) or GB(g), e.g. for a memory allocation of 1024 MB specify "1024m"</li>
     *  <li>TODO Network IO</li>
     *  <li>TODO File IO</li>
     * </ul>
     * </p>
     *
     * @param duration how long the specified loads shall be executed in milliseconds (ms)
     * @param loads strings containing specific load instructions for different aspects.
     */
    public static FakeLoad createLoad(long duration, String... loads) {
        // TODO return FakeLoad with provided parameters
        return new SimpleFakeLoad();
    }

    public static FakeLoad createLoad(List<FakeLoad> newLoads, int repetitions) {
        return new CompositeFakeLoad(newLoads, repetitions);
    }




    // Suppress default constructor for non-instantiability
    private FakeLoads() {
        throw new AssertionError();
    }


}
