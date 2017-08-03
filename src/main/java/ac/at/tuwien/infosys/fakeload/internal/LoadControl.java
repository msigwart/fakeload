package ac.at.tuwien.infosys.fakeload.internal;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Instances of this class are used as shared objects between a {@link SimulationControl} instance and one or more
 * simulation threads.
 *
 * <p>
 * The control task uses these objects to propagate changes of the load to the threads that do the
 * actual load simulation.
 * </p>
 *
 * @author Marten Sigwart
 * @since 1.8
 */
public class LoadControl {

    /**
     * Represents the adjustment amount.
     * Whenever simulation threads need to adjust the simulated system load, the adjust the load by this amount.
     */
    private AtomicLong adjustmentAmount = new AtomicLong(0L);

    /**
     * Represents the number of adjustment permits.
     * The number of permits determine how many simulation threads will be adjusting their loads.
     */
    private AtomicInteger adjustmentPermits = new AtomicInteger(0);


    /**
     * Sets adjustment instructions for the load the calling instance represents.
     * @param amount amount of how much the current load needs to be adjusted
     * @param permits the number of permits for this load adjustment. The number of permits determines how many simulation
     *                threads will actually be able to retrieve the current load adjustment.
     */
    public void adjustLoad(long amount, int permits) {
        this.adjustmentAmount.set(amount);
        this.adjustmentPermits.set(permits);
    }

    /**
     * Retrieves the current load adjustment amount if enough permits exist.
     *
     * The method first gets and decrements the number of current permits.
     * If the number of permits returned is bigger than zero (i.e. a permit exists) the adjustment amount is returned.
     * If the number of permits is smaller or equal zero, zero is returned.
     * @return the current load adjustment amount
     */
    public long getLoadAdjustment() {
        if (this.adjustmentPermits.getAndDecrement() > 0) {
            return this.adjustmentAmount.longValue();
        } else {
            return 0L;
        }
    }

}
