package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.GuardedBy;

/**
 * Skeleton {@code LoadSimulator} implementation.
 *
 * <p>
 * In order to simulate load this class has to be started either as thread directly
 * or with an {@link java.util.concurrent.ExecutorService}.
 * Load is simulated by running a while loop inside the class' {@link #run()} method.
 * In the loop the thread waits when there is nothing to do as indicated by the class'
 * {@link #waitConditionFulfilled()} method.
 * When there is something to simulate, load gets simulated via the {@link #simulateLoad(long)} method
 * which has to be implemented by subclasses.
 *
 * <p>
 * Note: When calling the constructor of this class from a subclass,
 * passing a value smaller or equal zero as {@code maxValue} will cause the
 * max value to be {@link java.lang.Long#MAX_VALUE}
 *
 * @author Marten Sigwart
 * @since 1.8
 */
abstract class AbstractLoadSimulator implements LoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(AbstractLoadSimulator.class);


    @GuardedBy("this") private long load;
    private final long maxValue;
    private final Object lock = new Object();


    AbstractLoadSimulator(long maxValue) {
        this.load = 0L;
        this.maxValue = (maxValue <= 0) ? Long.MAX_VALUE : maxValue;
    }

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

                simulateLoad(getLoad());

            } catch (InterruptedException e) {
                log.warn("{}Interrupted", idString());
                cleanUp();
                running = false;
            }
        }
    }

    protected abstract void cleanUp();


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
