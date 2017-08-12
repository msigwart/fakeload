package com.martensigwart.fakeload;

/**
 * Factory and utility methods for simulator classes defined in this package.
 *
 * <p> Contains methods that create and return instances of different system simulators.
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public final class Simulators {

    public static CpuSimulator newCpuSimulator() {
        return new FibonacciCpuSimulator();
    }

    public static MemorySimulator newMemorySimulator() {
        return new MemorySimulator();
    }

    // Suppress default constructor for non-instantiability
    private Simulators() {
        throw new AssertionError();
    }
}
