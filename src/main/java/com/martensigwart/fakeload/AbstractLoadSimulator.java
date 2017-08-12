package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.GuardedBy;

/**
 * Skeleton {@code LoadSimulator} implementation.
 */
abstract class AbstractLoadSimulator implements LoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(AbstractLoadSimulator.class);


    @GuardedBy("this") private long load;
    private final long maxValue;
    private final Object lock = new Object();


    AbstractLoadSimulator(long maxValue) {
        this.load = 0L;
        this.maxValue = (maxValue < 0) ? Long.MAX_VALUE : maxValue;
    }

    /**
     * Simulates load.
     *
     * @throws InterruptedException when the thread is interrupted during load simulation
     */
    abstract void simulateLoad() throws InterruptedException;

    /**
     * Represents the 'wait' condition of a {@code LoadSimulator}.
     * <p>
     * In order to save resources, a LoadSimulator should wait when
     * there is nothing to simulate. For example, when a Memory Simulator
     * already allocated the desired amount of memory, it can wait for new
     * memory instructions -> It has nothing to do. This method represents the state "nothing to do".
     * @return returns true if there is nothing to do and false otherwise.
     */
    abstract boolean waitConditionFulfilled();

    /**
     * Generates a pretty string representation of the specified load
     * @param load load to be formatted
     * @return Returns a pretty string
     */
    abstract String prettyFormat(long load);

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                synchronized (lock) {
                    while (waitConditionFulfilled()) {
                        log.trace("{}Waiting for something to do...", idString());
                        lock.wait();
                        log.trace("{}Woke up.", idString());
                    }
                }

                simulateLoad();

            } catch (InterruptedException e) {
                log.warn("{}Interrupted", idString());
                running = false;
            }
        }
    }


    @Override
    public synchronized long getLoad() {
        return load;
    }

    @Override
    public synchronized void setLoad(long desiredLoad) {
        this.load = (desiredLoad < 0) ? 0L : (desiredLoad > maxValue) ? maxValue : desiredLoad;
        log.trace("{}Set load to {}", idString(), prettyFormat(this.load));
        synchronized (lock) {
            lock.notify();
        }
    }

    String idString() {
        return "";
    }

    @Override
    public synchronized void increaseLoad(long delta) {
        setLoad(getLoad() + delta);
    }

    @Override
    public synchronized void decreaseLoad(long delta) {
        setLoad(getLoad() - delta);
    }

}
