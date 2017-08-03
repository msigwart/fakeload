package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * Represents the systemLoad to the infrastructure
     */
    private final SystemLoad systemLoad = new SystemLoad();

    /**
     * List containing all threads needed for the simulating system load
     */
    private final List<Callable<Void>> simulatorTasks;


    @GuardedBy("this") private boolean started;


    public DefaultSimulationInfrastructure() {

        int noOfCores = Runtime.getRuntime().availableProcessors();
        List<Callable<Void>> tasks = new ArrayList<>();

        // Create control thread for simulation
        SimulationControl simulationControl = new SimulationControl(systemLoad);
        tasks.add(simulationControl);

        // Create CPU simulation threads
        for (int i = 0; i< noOfCores; i++) {
            LoadControl cpuControl = simulationControl.getCpuControl();
            tasks.add(Simulators.createCpuSimulator(cpuControl));
        }

        // Create memory simulation thread
//        LoadControl memoryControl = simulationControl.getMemoryControl();
//        tasks.add(Simulators.createMemorySimulator(memoryControl));

        this.started            = false;
        this.simulatorTasks     = Collections.unmodifiableList(tasks);
        this.executorService    = Executors.newFixedThreadPool(simulatorTasks.size(),
                                            new ThreadFactoryBuilder().setDaemon(true).build());

        // Shutdown hook to end daemon threads gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

    }


    @Override
    public void increaseSystemLoadBy(FakeLoad load) throws MaximumLoadExceededException {
        start();
        systemLoad.increaseBy(load);
    }

    @Override
    public void decreaseSystemLoadBy(FakeLoad load) {
        systemLoad.decreaseBy(load);
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

            // Run simulation tasks
            Collection<Future> futures = new ArrayList<>();
            for (Callable task : simulatorTasks) {
                futures.add(executorService.submit(task));
            }

            started = true;

            //TODO use CompletableFuture
            // Could be useful in case of Memory Simulator task dying due to OutOfMemoryError.
            // Using CompletableFuture.exceptionally the Memory Simulator could be restarted immediately.

            // TODO save returned Future references
            // Could be used to cancel simulator tasks in time of inactivity.

            log.debug("Successfully started infrastructure");
        }
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
