package ac.at.tuwien.infosys.fakeload.internal;

/**
 * Factory and utility methods for simulator classes defined in this package.
 *
 * <p> Contains methods that create and return instances of different system simulators.
 *
 * @since 1.0
 * @author Marten Sigwart
 */
public final class Simulators {

    public static CpuSimulator createCpuSimulator(LoadControl cpuControl) {
        return new FibonacciCpuSimulator(cpuControl);
    }

    // Suppress default constructor for non-instantiability
    private Simulators() {
        throw new AssertionError();
    }
}
