package com.martensigwart.fakeload;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Factory methods that create {@link FakeLoadExecutor} instances.
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public class FakeLoadExecutors {

    private static SimulationInfrastructure defaultInfrastructure;

    public static synchronized FakeLoadExecutor newDefaultExecutor() {
        if (defaultInfrastructure == null) {

            int noOfCores = Runtime.getRuntime().availableProcessors();

            // Create thread pool
            ExecutorService executorService = Executors.newFixedThreadPool(
                    noOfCores + 2,
                    new ThreadFactoryBuilder().setDaemon(true).build());

            // Create CPU Simulators
            List<CpuSimulator> cpuSimulators = new ArrayList<>();
            for (int i=0; i<noOfCores; i++) {
                cpuSimulators.add(new FibonacciCpuSimulator());
            }

            LoadController controller = new LoadController(new SystemLoad(), cpuSimulators, new MemorySimulator());

            defaultInfrastructure = new DefaultSimulationInfrastructure(executorService, controller);
        }
        return new DefaultFakeLoadExecutor(new DefaultFakeLoadScheduler(defaultInfrastructure));
    }

    //TODO factory method where CpuSimulator type and ExecutorService is passed

    // prevent instantiation by suppressing default constructor
    private FakeLoadExecutors() {
        throw new AssertionError();
    }
}
