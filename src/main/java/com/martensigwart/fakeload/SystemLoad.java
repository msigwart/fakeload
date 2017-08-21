package com.martensigwart.fakeload;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Class representing system load.
 */
@ThreadSafe
final class SystemLoad {


    @GuardedBy("this") private long cpu;
    @GuardedBy("this") private long memory;
    @GuardedBy("this") private long diskIO;


    SystemLoad() {
        cpu     = 0L;
        memory  = 0L;
        diskIO  = 0L;
    }

    synchronized long getCpu() {
        return cpu;
    }

    synchronized long getMemory() {
        return memory;
    }

    synchronized long getDiskIO() {
        return diskIO;
    }



    synchronized void increaseBy(FakeLoad load) throws MaximumLoadExceededException {
        checkMaximumLoadNotExceeded(load);

        this.cpu    += load.getCpuLoad();
        this.memory += load.getMemoryLoad();
        this.diskIO += load.getDiskInputLoad();

    }

    synchronized void decreaseBy(FakeLoad load) {
        checkNotBelowMinimumLoad(load);

        this.cpu -= load.getCpuLoad();
        this.memory -= load.getMemoryLoad();
        this.diskIO -= load.getDiskInputLoad();

    }


    private synchronized void checkMaximumLoadNotExceeded(FakeLoad load) throws MaximumLoadExceededException {
        if (this.cpu + load.getCpuLoad() > 100)
            throw new MaximumLoadExceededException(load.getCpuLoad(), 100, SimulationType.CPU);

        // check other load limits
    }

    private synchronized void checkNotBelowMinimumLoad(FakeLoad load) {
        if (this.cpu - load.getCpuLoad() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative CPU load", load.getCpuLoad()));
        }

        if (this.memory - load.getMemoryLoad() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative memory load", load.getMemoryLoad()));
        }

        if (this.diskIO - load.getDiskInputLoad() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative disk IO load", load.getDiskInputLoad()));
        }

    }


}
