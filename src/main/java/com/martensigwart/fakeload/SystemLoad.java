package com.martensigwart.fakeload;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Class representing system load.
 *
 * This class keeps variables for representing different system load parameters like CPU, memory, disk I/O, etc.
 * The class is thread-safe and used within the {@link DefaultSimulationInfrastructure} to aggregate {@link FakeLoad}
 * objects that were submitted concurrently.
 *
 */
@ThreadSafe
public final class SystemLoad {


    @GuardedBy("this") private long cpu;
    @GuardedBy("this") private long memory;
    @GuardedBy("this") private long diskInput;
    @GuardedBy("this") private long diskOutput;


    public SystemLoad() {
        cpu     = 0L;
        memory  = 0L;
        diskInput = 0L;
        diskOutput = 0L;
    }

    public synchronized long getCpu() {
        return cpu;
    }

    public synchronized long getMemory() {
        return memory;
    }

    public synchronized long getDiskInput() {
        return diskInput;
    }

    public synchronized long getDiskOutput() {
        return diskOutput;
    }

    public synchronized void increaseBy(FakeLoad load) throws MaximumLoadExceededException {
        checkMaximumLoadNotExceeded(load);

        this.cpu    += load.getCpu();
        this.memory += load.getMemory();
        this.diskInput += load.getDiskInput();
        this.diskOutput += load.getDiskOutput();

    }


    public synchronized void decreaseBy(FakeLoad load) {
        checkNotBelowMinimumLoad(load);

        this.cpu -= load.getCpu();
        this.memory -= load.getMemory();
        this.diskInput -= load.getDiskInput();
        this.diskOutput -= load.getDiskOutput();

    }

    private synchronized void checkMaximumLoadNotExceeded(FakeLoad load) throws MaximumLoadExceededException {
        if (this.cpu + load.getCpu() > 100)
            throw new MaximumLoadExceededException(String.format("Increase of %d would cause a CPU load of over 100%%", load.getCpu()));

        // check other load limits
    }


    private synchronized void checkNotBelowMinimumLoad(FakeLoad load) {
        if (this.cpu - load.getCpu() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative CPU load",
                    load.getCpu()));
        }

        if (this.memory - load.getMemory() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative memory load",
                    load.getMemory()));
        }

        if (this.diskInput - load.getDiskInput() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative disk input load",
                    load.getDiskInput()));
        }

        if (this.diskOutput - load.getDiskOutput() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative disk output load",
                    load.getDiskOutput()));
        }

    }
}
