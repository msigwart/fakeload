package com.martensigwart.fakeload;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.concurrent.*;

/**
 * Default implementation class of {@link SimulationInfrastructure}.
 */
@ThreadSafe
public final class DefaultSimulationInfrastructure implements SimulationInfrastructure {

    private static final Logger log = LoggerFactory.getLogger(SimulationInfrastructure.class);

    /**
     * Executor service which executes the simulator threads
     */
    private final ExecutorService executorService;
    private final LoadController controller;


    @GuardedBy("this") private boolean started;

    /**
     *
     * @param executorService
     * @param controller
     */
    public DefaultSimulationInfrastructure(ExecutorService executorService,
                                           LoadController controller) {

        this.executorService = executorService;
        this.controller = controller;
        this.started = false;

    }


    @Override
    public void increaseSystemLoadBy(FakeLoad load) throws MaximumLoadExceededException {
        start();
        controller.increaseSystemLoadBy(load);
    }

    @Override
    public void decreaseSystemLoadBy(FakeLoad load) {
        controller.decreaseSystemLoadBy(load);
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

            startLoadController();
            startCpuSimulators();
            startMemorySimulator();

            started = true;

            // TODO save returned Future references
            // --> Could be useful for cancelling simulator tasks in time of inactivity.

            log.debug("Successfully started infrastructure");
        }
    }

    private void startLoadController() {
        CompletableFuture.runAsync(controller, executorService);
        log.debug("Started Simulation Control");
    }


    private void startCpuSimulators() {
        List<CpuSimulator> cpuSimulators = controller.getCpuSimulators();

        for (Runnable thread: cpuSimulators) {
            CompletableFuture.runAsync(thread, executorService);
        }
        log.debug("Started {} CPU Simulators", cpuSimulators.size());
    }

    private void startMemorySimulator() {

        MemorySimulator memorySimulator = controller.getMemorySimulator();
        if (memorySimulator == null)
            return;

        memorySimulator.setLoad(0);
        CompletableFuture<Void> future = CompletableFuture.runAsync(memorySimulator, executorService);

        // In case of MemorySimulator thread dies because OutOfMemoryError,
        // it is restarted immediately using CompletableFuture.exceptionally.
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
