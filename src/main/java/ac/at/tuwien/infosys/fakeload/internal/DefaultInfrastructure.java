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
 * Represents a simulation infrastructure for the FakeLoad Library.
 *
 * <p>
 * The simulation infrastructure consists of different tasks. Each task serves a specific simulation purpose.
 * There will be tasks responsible for CPU load simulation, for memory simulation, and more.
 * The {@code DefaultInfrastructure} instance provides methods for starting and stopping the execution of simulator tasks.
 *
 * <p>
 * Further, there exists a task responsible for controlling the simulator tasks, that is, it checks whether the loads
 * created by the infrastructure actually match the desired system load. For further details see {@link ControlTask}.
 *
 * <p>
 * Multiple fake loads being executed simultaneously should produce a system load which is the aggregation of all
 * load instructions contained in these fake loads. If thread A executes a fake load of 20% CPU and thread B executes
 * a fake load of 30% CPU the resulting system load should be 20% + 30% = 50%.
 * The {@code DefaultFakeLoadDispatcher} is responsible for this aggregation as well as reporting any faults concerning
 * any passing of load limitations of the system. For example executing a CPU load of more than 100% is not possible,
 * therefore if accumulated CPU load of the FakeLoad instances being executed exceeds that an error should be thrown.
 *
 * <p> TODO maybe should not be singleton
 * Note: {@code DefaultInfrastructure} is singleton, thus, there only exists one instance per process.
 * The instance can be obtained via {@code DefaultInfrastructure.INSTANCE}.
 *
 * @author Marten Sigwart
 * @since 1.8
 *
 */
enum DefaultInfrastructure {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(DefaultInfrastructure.class);

    /** Executor service to run the infrastructure */
    private ExecutorService executorService;

    /** Represents the connection to the infrastructure */
    private final Connection connection = new Connection();

    /** Number of CPU cores */
    private final int noOfCores;

    /** List containing all threads needed for the simulating system load */
    private final List<Callable<Void>> simulatorTasks;


    DefaultInfrastructure() {
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




    synchronized void increaseCpu(long cpuLoad) throws MaximumLoadExceededException {
        connection.increaseCpu(cpuLoad);
    }

    synchronized void increaseMemory(long memoryLoad) throws MaximumLoadExceededException {
        connection.increaseMemory(memoryLoad);
    }

    synchronized void increaseDiskIO(long diskIOLoad) throws MaximumLoadExceededException {
        connection.increaseDiskIO(diskIOLoad);
    }

    synchronized void increaseNetIO(long netIOLoad) throws MaximumLoadExceededException {
        connection.increaseNetIO(netIOLoad);
    }

    synchronized void decreaseCpu(long cpuLoad) {
        connection.decreaseCpu(cpuLoad);
    }

    synchronized void decreaseMemory(long memoryLoad) {
        connection.decreaseMemory(memoryLoad);
    }

    synchronized void decreaseDiskIO(long diskIOLoad) {
        connection.decreaseDiskIO(diskIOLoad);
    }

    synchronized void decreaseNetIO(long netIOLoad) {
        connection.decreaseNetIO(netIOLoad);
    }
}
