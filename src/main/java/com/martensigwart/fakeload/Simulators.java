package com.martensigwart.fakeload;

import java.util.ArrayList;
import java.util.List;

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

    public static List<CpuSimulator> createCpuSimulators(int noOfSimulators, Class<? extends CpuSimulator> cpuSimulatorType)
            throws IllegalAccessException, InstantiationException {

        List<CpuSimulator> cpuSimulators = new ArrayList<>();
        for (int i=0; i<noOfSimulators; i++) {
            cpuSimulators.add(cpuSimulatorType.newInstance());
        }
        return cpuSimulators;
    }

    // Suppress default constructor for non-instantiability
    private Simulators() {
        throw new AssertionError();
    }
}
