package ac.at.tuwien.infosys.fakeload.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class manages the simulation infrastructure of the fake load library.
 * The simulation infrastructure consists of several threads, which each have a specific simulation task.
 * There will be threads responsible for CPU load simulation, for memory simulation, and more.
 *
 * The InfrastructureManager is responsible for managing this infrastructure.
 * It contains methods to start, stop and pause said infrastructure.
 *
 * @Author Marten Sigwart
 * @Version 1.0
 */
public class InfrastructureManager {

    private static final Logger log = LoggerFactory.getLogger(InfrastructureManager.class);

    /** Executor service to run the infrastructure */
    private static ExecutorService executorService;


    /** Indicates whether or not the infrastructure is running */
    private boolean isRunning;

    /** Represents the connection to the infrastructure */
    private Connection connection;

    /** Number of available processors */
    private final int noOfCores;

    /** List containing all threads needed for the simulation infrastructure */
    private List<Callable<Void>> simulatorThreads;


    InfrastructureManager() {
        log.debug("Creating singleton instance...");
        this.isRunning = false;
        this.connection = new Connection();
        this.simulatorThreads = new ArrayList<>();
        this.noOfCores = Runtime.getRuntime().availableProcessors();
    }




    /**
     * Creates a connection to the simulation infrastructure.
     * @return
     */
    public Connection getConnection() {
        setupInfrastructure();
        return connection;
    }

    /**
     * Creates the AbstractFakeLoad infrastructure, i.e. creates the infrastructure which will simulate CPU, memory, I/O, etc.
     * In detail, this entails creating multiple threads, which each have specific tasks.
     */
    private void setupInfrastructure() {
        log.debug("Setting up infrastructure...");

        if (!isRunning) {
            startInfrastructure();
        } else {
            log.debug("Infrastructure already running");
        }
    }

    private synchronized void startInfrastructure() {
        log.debug("Starting infrastructure...");

        // create a thread pool for simulation infrastructure
        if (executorService.isShutdown()) {
            executorService = Executors.newFixedThreadPool(noOfCores + 2);
        }

        // Create control thread for simulation
        ControlTask simulationControl = new ControlTask(connection);
        simulatorThreads.add(simulationControl);

        // Create CPU simulation threads
        for (int i=0; i<noOfCores; i++) {
            LoadControl cpuControl = simulationControl.getCpuControl();
            simulatorThreads.add(Simulators.createCpuSimulator(cpuControl));
        }

        // Create memory simulation thread

        // Execute simulation threads
        try {
            executorService.invokeAll(simulatorThreads);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isRunning = true;

        log.debug("Successfully started infrastructure");
    }

    private synchronized void stopInfrastructure() {
        executorService.shutdownNow();
    }


}
