package com.martensigwart.fakeload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.GuardedBy;

/**
 * Skeleton {@code LoadSimulator} implementation.
 *
 * <p>
 * Load is simulated by running a while loop inside the {@link #run()} method.
 * In the loop the executing thread waits when there is nothing to do as indicated by
 * {@link #waitConditionFulfilled()} method.
 * When there is something to simulate, load gets simulated via the {@link #simulateLoad(long)}
 * method which has to be implemented by subclasses.
 *
 * <p>
 * Note: When calling the constructor of this class from a subclass,
 * passing a value smaller or equal zero as {@code maxValue} will cause the
 * max value to be {@link java.lang.Long#MAX_VALUE}
 *
 * @author Marten Sigwart
 * @since 1.8
 */
public abstract class AbstractLoadSimulator implements LoadSimulator {

    private static final Logger log = LoggerFactory.getLogger(AbstractLoadSimulator.class);


    @GuardedBy("this") private long load;
    private final long maxValue;
    private final Object lock = new Object();


    public AbstractLoadSimulator(long maxValue) {
        this.load = 0L;
        this.maxValue = (maxValue <= 0) ? Long.MAX_VALUE : maxValue;
    }

    /**
     * Simulates the specified system load.
     *
     * <p>
     * Actual system load simulation happens inside this method.
     * However, the simulation logic implemented here should be kept as short and
     * concise as possible as this method is called from within a while loop internally.
     * Therefore, this method should not start any infinite loops, etc.
     *
     * @param load the load to be simulated
     * @throws InterruptedException when the thread is interrupted during load simulation
     */
    protected abstract void simulateLoad(long load) throws InterruptedException;

    /**
     * Represents the 'wait' condition of an {@code AbstractLoadSimulator}.
     * In order to save resources, the thread executing the {@link #run()}
     * method should wait when there is nothing to simulate.
     *
     * <p>
     * Take for example, a simulator which simulates memory. If it has already allocated
     * the desired amount of memory it has nothing more left to do. In this case the corresponding
     * thread should wait until it retrieves new simulation instructions.
     *
     * <p>
     * This method is responsible for checking if a simulator currently
     * has something to do or not.
     * @return returns true if there is nothing to do and false otherwise.
     */
    protected abstract boolean waitConditionFulfilled();

    /**
     * Clean up method which is called if the thread executing this {@code AbstractLoadSimulator}
     * encounters an exception, e.g. InterruptedException. This method should be used to clean up
     * any resources that are used by the simulator for load simulation purposes.
     */
    protected abstract void cleanUp();

    /**
     * Generates a pretty string representation of the specified load
     * @param load load to be formatted
     * @return Returns a pretty string
     *///TODO make private or outsource to a SimulationType private member
    protected abstract String prettyFormat(long load);

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

    /**
     * Returns an ID string, which is primarily used for logging purposes.
     * @return the ID string
     *///TODO pass a name(id) string in constructor and delete method
    protected String idString() {
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
