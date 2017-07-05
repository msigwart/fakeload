package simulation.cpu;

import common.message.IWorkload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.LoadControlObject;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by martensigwart on 19.05.17.
 */
public abstract class AbstractCpuSimulator implements ICpuSimulator {

    private static final Logger log = LoggerFactory.getLogger(AbstractCpuSimulator.class);

    private static AtomicLong cpuSimId = new AtomicLong(0L);

    private final Long id;
    private LoadControlObject load;
    private Long workload;

    public AbstractCpuSimulator(LoadControlObject load) {
        this.id = cpuSimId.getAndIncrement();
        this.load = load;
        this.workload = load.getInitialWorkload().getValue();

    }

    @Override
    public String call() throws Exception {
        try {

            while (true) {

                // adopt changes to simulation load
                if (load.getAndDecrementPermits() > 0) {

                    switch (load.getAdjustmentType()) {
                        case INCREASE:
                            if (workload < 100) workload++;
                            log.debug("<{}> - Increased load to {}%", id, workload);
                            break;
                        case DECREASE:
                            if (workload > 0) workload--;
                            log.debug("<{}> - Decreased load to {}%", id, workload);
                            break;
                    }
                }

                long time = System.currentTimeMillis() + workload;
                while (System.currentTimeMillis() < time) {
                    simulateCpu();
                }
                Thread.sleep(100 - workload);
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
