package ac.at.tuwien.infosys.fakeload.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by martensigwart on 19.05.17.
 */
public abstract class AbstractCpuSimulator implements CpuSimulator {

    private static final Logger log = LoggerFactory.getLogger(AbstractCpuSimulator.class);

    private static AtomicLong cpuSimId = new AtomicLong(0L);

    private final long id;
    private LoadControl loadControl;
    private long load;

    AbstractCpuSimulator(LoadControl load) {
        this.id = cpuSimId.getAndIncrement();
        this.loadControl = load;
        this.load = load.getLoadAdjustment();

    }

    @Override
    public Void call() {
        try {

            while (true) {

                // adjust current cpu load
                long adjustment = loadControl.getLoadAdjustment();

                if (adjustment > 0) {
                    // Increase load
                    long oldLoad = load;
                    load = ((load + adjustment) > 100) ? 100L : (load += adjustment);
                    log.debug("<{}> - Increased loadControl from {} to {}%", id, oldLoad, load);

                } else if (adjustment < 0) {
                    // Decrease load
                    long oldLoad = load;
                    load = ((load + adjustment) < 0) ? 0L : (load += adjustment);
                    log.debug("<{}> - Decreased loadControl from {} to {}%", id, oldLoad, load);
                }

                long time = System.currentTimeMillis() + load;
                while (System.currentTimeMillis() < time) {
                    simulateCpu();
                }
                Thread.sleep(100 - load);
            }

        } catch (InterruptedException e) {
            log.debug("<{}> - Interrupted", id);
        }

        return null;
    }


    public static void resetIdCounter() {
        cpuSimId.set(0L);
    }

}
