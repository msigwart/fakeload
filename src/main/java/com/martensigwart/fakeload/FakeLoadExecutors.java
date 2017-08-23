package com.martensigwart.fakeload;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
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
public final class FakeLoadExecutors {

    private static final Logger log = LoggerFactory.getLogger(FakeLoadExecutors.class);
    private static final String DEFAULT_DISK_INPUT_PATH = "/tmp/test.tmp";
    private static SimulationInfrastructure defaultInfrastructure;

    public static synchronized FakeLoadExecutor newDefaultExecutor() {
        // create infrastructure if it hasn't been created yet
        if (defaultInfrastructure == null) {

            try {
                int noOfCores = Runtime.getRuntime().availableProcessors();

                // Create DiskInput Simulator
                DiskInputSimulator diskInputSimulator;
                diskInputSimulator = new DiskInputSimulator(DEFAULT_DISK_INPUT_PATH);

                // Create Memory Simulator
                MemorySimulator memorySimulator = new MemorySimulator();

                // Create CPU Simulators
                List<CpuSimulator> cpuSimulators = new ArrayList<>();
                for (int i = 0; i < noOfCores; i++) {
                    cpuSimulators.add(new FibonacciCpuSimulator());
                }

                // Inject dependencies for LoadController
                LoadController controller;
                controller = new LoadController(new SystemLoad(),
                        cpuSimulators, memorySimulator, diskInputSimulator);

                // Create thread pool
                ExecutorService executorService = Executors.newFixedThreadPool(
                        noOfCores + 3,
                        new ThreadFactoryBuilder().setDaemon(true).build());

                defaultInfrastructure = new DefaultSimulationInfrastructure(executorService, controller);

            } catch (FileNotFoundException e) {
                log.warn("File {} doesn't exist", DEFAULT_DISK_INPUT_PATH);
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        return new DefaultFakeLoadExecutor(new DefaultFakeLoadScheduler(defaultInfrastructure));
    }

        //TODO factory method where CpuSimulator type and ExecutorService is passed

        // prevent instantiation by suppressing default constructor
    private FakeLoadExecutors() {
            throw new AssertionError();
        }
    }
