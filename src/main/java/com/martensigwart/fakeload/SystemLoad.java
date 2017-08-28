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
    @GuardedBy("this") private long diskInput;
    @GuardedBy("this") private long diskOutput;


    SystemLoad() {
        cpu     = 0L;
        memory  = 0L;
        diskInput = 0L;
    }

    synchronized long getCpu() {
        return cpu;
    }

    synchronized long getMemory() {
        return memory;
    }

    synchronized long getDiskInput() {
        return diskInput;
    }

    synchronized long getDiskOutput() {
        return diskOutput;
    }

    synchronized void increaseBy(FakeLoad load) throws MaximumLoadExceededException {
        checkMaximumLoadNotExceeded(load);

        this.cpu    += load.getCpuLoad();
        this.memory += load.getMemoryLoad();
        this.diskInput += load.getDiskInputLoad();
        this.diskOutput += load.getDiskOutputLoad();

    }


    synchronized void decreaseBy(FakeLoad load) {
        checkNotBelowMinimumLoad(load);

        this.cpu -= load.getCpuLoad();
        this.memory -= load.getMemoryLoad();
        this.diskInput -= load.getDiskInputLoad();
        this.diskOutput -= load.getDiskOutputLoad();

    }

    private synchronized void checkMaximumLoadNotExceeded(FakeLoad load) throws MaximumLoadExceededException {
        if (this.cpu + load.getCpuLoad() > 100)
            throw new MaximumLoadExceededException(load.getCpuLoad(), 100, SimulationType.CPU);

        // check other load limits
    }


    private synchronized void checkNotBelowMinimumLoad(FakeLoad load) {
        if (this.cpu - load.getCpuLoad() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative CPU load",
                    load.getCpuLoad()));
        }

        if (this.memory - load.getMemoryLoad() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative memory load",
                    load.getMemoryLoad()));
        }

        if (this.diskInput - load.getDiskInputLoad() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative disk input load",
                    load.getDiskInputLoad()));
        }

        if (this.diskOutput - load.getDiskOutputLoad() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative disk output load",
                    load.getDiskOutputLoad()));
        }

    }
}
