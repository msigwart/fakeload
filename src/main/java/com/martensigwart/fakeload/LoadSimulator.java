package com.martensigwart.fakeload;


public interface LoadSimulator extends Runnable {

    /**
     * Simulates load.
     *
     * @throws InterruptedException when the thread is interrupted during load simulation
     */
    void simulateLoad(long load) throws InterruptedException;

    /**
     * Represents the 'wait' condition of a {@code LoadSimulator}.
     * <p>
     * In order to save resources, a LoadSimulator should wait when
     * there is nothing to simulate. For example, when a Memory Simulator
     * already allocated the desired amount of memory, it can wait for new
     * memory instructions -> It has nothing to do. This method represents the state "nothing to do".
     * @return returns true if there is nothing to do and false otherwise.
     */
    boolean waitConditionFulfilled();

    long getLoad();
    void setLoad(long desiredLoad);
    void increaseLoad(long delta);
    void decreaseLoad(long delta);

}
