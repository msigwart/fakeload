package ac.at.tuwien.infosys.fakeload.internal;

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
 * Represents the simulation infrastructure of the FakeLoad Library.
 *
 * <p>
 * The simulation infrastructure consists of different tasks. Each task serves a specific simulation purpose.
 * There will be tasks responsible for CPU load simulation, for memory simulation, and more.
 * The {@code SimulationInfrastructure} instance provides methods for starting and stopping the execution of simulator tasks.
 *
 * <p>
 * Further, there exists a task responsible for controlling the simulator tasks, that is, it checks whether the loads
 * created by the infrastructure actually match the desired system load. For further details see {@link ControlTask}.
 *
 * <p>
 * Note: {@code SimulationInfrastructure} is singleton, thus, there only exists one instance per process.
 * The instance can be obtained via {@code SimulationInfrastructure.INSTANCE}.
 *
 * @author Marten Sigwart
 * @since 1.8
 *
 */
enum SimulationInfrastructure {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(SimulationInfrastructure.class);

    /** Executor service to run the infrastructure */
    private ExecutorService executorService;

    /** Represents the connection to the infrastructure */
    private final Connection connection = new Connection();

    /** Number of CPU cores */
    private final int noOfCores;

    /** List containing all threads needed for the simulating system load */
    private final List<Callable<Void>> simulatorTasks;


    SimulationInfrastructure() {
        this.noOfCores = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(this.noOfCores+2);

        List<Callable<Void>> tasks = new ArrayList<>();

        // Create control thread for simulation
        ControlTask simulationControl = new ControlTask(connection);
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
     * Starts the simulation infrastructure.
     *
     * <p>
     * First, it checks whether the thread pool used for execution of simulator tasks
     * has been shutdown. If it has been shutdown in the meantime, it creates a new one.
     * Then, all necessary simulation tasks are started.
     */
    synchronized void start() {
        log.debug("Starting infrastructure...");

        // Create new executor service if null or has been shutdown
        if (executorService.isShutdown()) {
            executorService = Executors.newFixedThreadPool(this.noOfCores + 2);
        }

        // Run simulation tasks
        Collection<Future<Void>> futures = new ArrayList<>();
        for (Callable task: simulatorTasks) {
            futures.add(executorService.submit(task));
        }

        log.debug("Successfully started infrastructure");
    }

    /**
     * Stops the simulation infrastructure.
     *
     * <p>
     * In particular, the current thread pool is shutdown.
     */
    synchronized void stop() {      //TODO What happens if not started yet?
        executorService.shutdownNow();
    }

    /**
     * @return returns the connection to the simulation infrastructure.
     */
    synchronized Connection getConnection() {
        return connection;
    }


}
