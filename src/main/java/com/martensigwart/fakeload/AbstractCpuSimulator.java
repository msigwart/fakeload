package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Skeleton implementation of the CPU Simulator interface.
 *
 *
 */
public abstract class AbstractCpuSimulator implements CpuSimulator {

    private static final Logger log = LoggerFactory.getLogger(AbstractCpuSimulator.class);

    private static AtomicLong cpuSimId = new AtomicLong(0L);

    private final long id;
    private long load;

    AbstractCpuSimulator() {
        this.id = cpuSimId.getAndIncrement();
        this.load = 0L;

    }

    @Override
    public void run() {
        try {

            while (true) {

                long time = System.currentTimeMillis() + getLoad();
                while (System.currentTimeMillis() < time) {
                    simulateCpu();
                }
                Thread.sleep(100 - getLoad());
            }

        } catch (InterruptedException e) {
            log.debug("<{}> - Interrupted", id);
        }
    }

    public synchronized long getLoad() {
        return load;
    }

    public synchronized void setLoad(long desiredLoad) {
        // TODO better throw exception?
        this.load = (desiredLoad < 0) ? 0L : (desiredLoad > 100) ? 100L : desiredLoad;
        log.trace("<{}> - Set load to {}", id, desiredLoad);
    }

    @Override
    public synchronized void increaseLoad(long delta) {
        long oldLoad = load;
        load = (load + delta > 100) ? 100 : load + delta;
        log.trace("<{}> - Increased load from {} to {}%", id, oldLoad, load);
    }

    @Override
    public synchronized void decreaseLoad(long delta) {
        long oldLoad = load;
        load = (load - delta < 0) ? 0 : load - delta;
        log.trace("<{}> - Decreased load from {} to {}%", id, oldLoad, load);
    }
}
