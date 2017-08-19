package com.martensigwart.fakeload;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Default implementation class of {@link SimulationInfrastructure}.
 */
@ThreadSafe
public final class DefaultSimulationInfrastructure implements SimulationInfrastructure {

    private static final Logger log = LoggerFactory.getLogger(SimulationInfrastructure.class);

    /**
     * Executor service to run the infrastructure
     */
    private final ExecutorService executorService;

    private final SimulationControl simulationControl;
    private final List<CpuSimulator> cpuSimulators;
    private final MemorySimulator memorySimulator;


    @GuardedBy("this") private boolean started;

    //TODO pass simulation threads as parameters (maybe as class type)
    public DefaultSimulationInfrastructure() {
        int noOfCores = Runtime.getRuntime().availableProcessors();

        List<CpuSimulator> cpuSimulators = new ArrayList<>();
        for (int i = 0; i< noOfCores; i++) {
            cpuSimulators.add(Simulators.newCpuSimulator());
        }
        this.cpuSimulators = Collections.unmodifiableList(cpuSimulators);
        this.memorySimulator = Simulators.newMemorySimulator();

        this.simulationControl = new SimulationControl(this.cpuSimulators, this.memorySimulator);


        this.started            = false;
        this.executorService    = Executors.newFixedThreadPool(noOfCores + 2,
                                            new ThreadFactoryBuilder().setDaemon(true).build());

        // Shutdown hook to end daemon threads gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

    }


    @Override
    public void increaseSystemLoadBy(FakeLoad load) throws MaximumLoadExceededException {
        start();
        simulationControl.increaseSystemLoadBy(load);
    }

    @Override
    public void decreaseSystemLoadBy(FakeLoad load) {
        simulationControl.decreaseSystemLoadBy(load);
        // TODO check if system load is now zero, if yes start timer to cancel simulator tasks.
    }


    /**
     * Starts the simulation infrastructure, if it was not already started.
     * <p>
     * Boolean {@code started} indicates whether or not the simulator threads
     * have already been submitted to the {@code ExecutorService}. If not, all
     * simulator tasks are submitted and {@code started} is set to true.
     */
    private synchronized void start() {
        if (!started) {
            log.debug("Starting infrastructure...");

            // Start simulators
            startSimulationControl();
            startCpuSimulators();
            startMemorySimulator();

            started = true;

            // TODO save returned Future references
            // --> Could be useful for cancelling simulator tasks in time of inactivity.

            log.debug("Successfully started infrastructure");
        }
    }

    private void startSimulationControl() {
        CompletableFuture.runAsync(simulationControl, executorService);
        log.debug("Started Simulation Control");
    }


    private void startCpuSimulators() {
        for (Runnable thread: cpuSimulators) {
            CompletableFuture.runAsync(thread, executorService);
        }
        log.debug("Started {} CPU Simulators", cpuSimulators.size());
    }

    private void startMemorySimulator() {

        // Could be useful in case of Memory Simulator task dying due to OutOfMemoryError.
        // Using CompletableFuture.exceptionally the Memory Simulator is restarted immediately.

        memorySimulator.setLoad(0);
        CompletableFuture<Void> future = CompletableFuture.runAsync(memorySimulator, executorService);
        future.exceptionally(e -> {
            log.warn("Memory Simulator died: {}, starting new one...", e.getMessage());
            startMemorySimulator();
            return null;
        });
        log.debug("Started Memory Simulator");
    }

    /**
     * Gracefully shuts down the simulation infrastructure.
     * <p>
     * This means shutting down the {@code ExecutorService} with which the simulation tasks are run.
     */
    private void shutdown() {
        log.debug("ExecutorService shutdown");
        executorService.shutdown();
    }




}
