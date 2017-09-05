package com.martensigwart.fakeload;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory methods that create {@link FakeLoadExecutor} instances.
 *
 * @since 1.8
 * @author Marten Sigwart
 */
public final class FakeLoadExecutors {

    private static final Logger log = LoggerFactory.getLogger(FakeLoadExecutors.class);
    private static final String DISK_INPUT_FILE = "input.tmp";
    private static final String DISK_OUTPUT_FILE = "output.tmp";
    private static final String DEFAULT_DISK_INPUT_PATH = System.getProperty("java.io.tmpdir") + "/" + DISK_INPUT_FILE;
    private static final String DEFAULT_DISK_OUTPUT_PATH = System.getProperty("java.io.tmpdir") + "/" + DISK_OUTPUT_FILE;
    private static SimulationInfrastructure defaultInfrastructure;

    /**
     * Creates a {@link DefaultFakeLoadExecutor} instance for executing {@link FakeLoad} objects.
     *
     * <p>
     * The created executor contains everything necessary to perform system load simulation of CPU,
     * memory and disk I/O.
     *
     * <p>
     * The returned executor is thread safe and can be used concurrently.
     * However, clients can create as many executors as they want using this method,
     * without compromising concurrent behavior. All {@code DefaultFakeLoadExecutor}
     * instances returned by this method, use the same {@link SimulationInfrastructure}
     * below the surface.
     *
     * <p>
     * <b>Important:</b> Disk input is simulated by reading from a file called "input.tmp"
     * located in the temporary directory as indicated by system property "java.io.tmpdir".
     * This directory is typically "/tmp", or "/var/tmp" on Unix-like platforms.
     * On Microsoft Windows systems the java.io.tmpdir property is typically "C:\WINNT\TEMP".
     * You can set the property with <i>-Djava.io.tmpdir=/your-tmpdirectory</i>.
     * The file needs to be created prior to disk input simulation.
     * To prevent caching of the file system, the file should be at least twice as big as the
     * available RAM.
     *
     * @return a {@code DefaultFakeLoadExecutor} instance
     */
    public static synchronized FakeLoadExecutor newDefaultExecutor() {
        // create infrastructure if it hasn't been created yet
        if (defaultInfrastructure == null) {

            try {
                int noOfCores = Runtime.getRuntime().availableProcessors();

                // Create DiskInput Simulator
                DiskInputSimulator diskInputSimulator;
                diskInputSimulator = new RandomAccessDiskInputSimulator(DEFAULT_DISK_INPUT_PATH);

                // Create DiskOutput Simulator
                DiskOutputSimulator diskOutputSimulator;
                diskOutputSimulator = new RandomAccessDiskOutputSimulator(DEFAULT_DISK_OUTPUT_PATH);

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
                        cpuSimulators, memorySimulator, diskInputSimulator, diskOutputSimulator);

                // Create thread pool
                ExecutorService executorService = Executors.newFixedThreadPool(
                        noOfCores + 4,
                        new ThreadFactoryBuilder().setDaemon(true).build());

                defaultInfrastructure = new DefaultSimulationInfrastructure(executorService, controller);


            /*
             * Catch blocks in case paths can be passed as parameters.
             */
            } catch (IOException e) {
                log.error("File {} used for simulating disk output could not be created.", DEFAULT_DISK_OUTPUT_PATH);
                throw new IllegalArgumentException(e);
            }
        }

        return new DefaultFakeLoadExecutor(new DefaultFakeLoadScheduler(defaultInfrastructure));
    }

        //TODO factory method where CpuSimulator type and ExecutorService or DiskInput is passed
        //TODO utitily method to reset or kill infrastructure

        // prevent instantiation by suppressing default constructor
    private FakeLoadExecutors() {
            throw new AssertionError();
        }
    }
