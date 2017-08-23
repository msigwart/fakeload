package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Skeleton implementation of a CPU Simulator
 */
public abstract class CpuSimulator extends AbstractLoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(CpuSimulator.class);
    private static final AtomicInteger cpuIDs = new AtomicInteger(0);

    private final int id;

    CpuSimulator() {
        super(100L);
        id = cpuIDs.getAndIncrement();
    }

    /**
     * Performs some calculation/operation to keep the CPU busy
     */
    abstract void simulateCpu();



    @Override
    public void simulateLoad(long load) throws InterruptedException {

        long time = System.currentTimeMillis() + load;
        while (System.currentTimeMillis() < time) {
            simulateCpu();
        }
        Thread.sleep(100 - load);
    }

    @Override
    public boolean waitConditionFulfilled() {
        return (getLoad() == 0);
    }


    @Override
    String prettyFormat(long load) {
        return String.format("%d%%", load);
    }

    @Override
    protected void cleanUp() {
        // Do nothing
    }

    @Override
    String idString() {
        return String.format("CPU Sim %d - ", id);
    }
}
