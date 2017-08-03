package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * This class acts as the controlling entity of the simulation infrastructure.
 *
 * When the load simulation infrastructure is currently active this class runs as a separate thread to monitor the
 * actual load generated by the simulator threads. Whenever a significant deviation between target and actual load is
 * detected, the class adjusts the load slightly in the direction of target. This way the load generated by the
 * simulation actually reaches the desired level.
 *
 * The {@code SimulationControl} retrieves all load changes via a shared instance of type
 * {@link SystemLoad}. These changes are then propagated to the respective simulator threads.
 *
 * @author Marten Sigwart
 * @since 1.8
 */
public final class SimulationControl implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SimulationControl.class);


    private final SystemLoad systemLoad;

    private final List<CpuSimulator> cpuSimulators;
    private final MemorySimulator memorySimulator;

    SimulationControl(List<CpuSimulator> cpuSimulators, MemorySimulator memorySimulator) {
        this.systemLoad = new SystemLoad();
        this.cpuSimulators = Collections.unmodifiableList(cpuSimulators);
        this.memorySimulator = memorySimulator;
    }


    @Override
    public void run() {
        log.trace("Started");

        while(true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.warn("SimulationControl was interrupted");
                break;
            }
        }
    }


    public void increaseSystemLoadBy(FakeLoad load) throws MaximumLoadExceededException {
        systemLoad.increaseBy(load);

        for (CpuSimulator cpuSim: cpuSimulators) {
            cpuSim.setLoad(systemLoad.getCpu());
        }

        memorySimulator.setLoad(systemLoad.getMemory());
        //TODO propagate changes to simulators
    }

    public void decreaseSystemLoadBy(FakeLoad load) {
        systemLoad.decreaseBy(load);

        for (CpuSimulator cpuSim: cpuSimulators) {
            cpuSim.setLoad(systemLoad.getCpu());
        }
        memorySimulator.setLoad(systemLoad.getMemory());
        // TODO propagate changes to simulators
    }
}
