package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Default implementation class of {@link SimulationInfrastructure}.
 */
public final class DefaultSimulationInfrastructure implements SimulationInfrastructure {

    private static final Logger log = LoggerFactory.getLogger(SimulationInfrastructure.class);

    /** Executor service to run the infrastructure */
    private ExecutorService executorService;

    /** Represents the systemLoad to the infrastructure */
    private final SystemLoad systemLoad = new SystemLoad();

    /** Number of CPU cores */
    private final int noOfCores;

    /** List containing all threads needed for the simulating system load */
    private final List<Callable<Void>> simulatorTasks;


    public DefaultSimulationInfrastructure() {
        this.noOfCores = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(this.noOfCores+2);

        List<Callable<Void>> tasks = new ArrayList<>();

        // Create control thread for simulation
        ControlTask simulationControl = new ControlTask(systemLoad);
        tasks.add(simulationControl);

        // Create CPU simulation threads
        for (int i=0; i<noOfCores; i++) {
            LoadControl cpuControl = simulationControl.getCpuControl();
            tasks.add(Simulators.createCpuSimulator(cpuControl));
        }

        // Create memory simulation thread
        LoadControl memoryControl = simulationControl.getMemoryControl();
        tasks.add(Simulators.createMemorySimulator(memoryControl));

        this.simulatorTasks = Collections.unmodifiableList(tasks);

    }


    /**
     * {@inheritDoc}
     *
     * <p>
     * First, it checks whether the thread pool used for execution of simulator tasks
     * has been shutdown. If it has been shutdown in the meantime, it creates a new one.
     * Then, all necessary simulation tasks are started.
     */
    @Override
    public synchronized void start() {
        log.debug("Starting infrastructure...");

        // Create new executor service if null or has been shutdown
        if (executorService.isShutdown()) {
            executorService = Executors.newFixedThreadPool(this.noOfCores + 2);
        }

        // Run simulation tasks
        Collection<Future> futures = new ArrayList<>();
        for (Callable task: simulatorTasks) {
            futures.add(executorService.submit(task));
        }

        //TODO maybe start a periodic isAlive thread to check if all simulator tasks are still running?
        // Could be useful in case of Memory Simulator task dying due to OutOfMemoryError.

        log.debug("Successfully started infrastructure");
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * In particular, the current thread pool is shutdown.
     */
    @Override
    public synchronized void stop() {      //TODO What happens if not started yet?
        executorService.shutdownNow();
    }

    @Override
    public void increaseSystemLoadBy(FakeLoad load) throws MaximumLoadExceededException {
        systemLoad.increaseBy(load);
    }

    @Override
    public void decreaseSystemLoadBy(FakeLoad load) {
        systemLoad.decreaseBy(load);
    }


}
