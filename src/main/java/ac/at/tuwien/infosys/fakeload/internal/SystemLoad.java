package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Created by martensigwart on 04.07.17.
 */
@ThreadSafe
final class SystemLoad {


    @GuardedBy("this") private long cpu;
    @GuardedBy("this") private long memory;
    @GuardedBy("this") private long diskIO;
    @GuardedBy("this") private long netIO;


    SystemLoad() {
        cpu     = 0L;
        memory  = 0L;
        diskIO  = 0L;
        netIO   = 0L;
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

    synchronized long getNetIO() {
        return netIO;
    }


    synchronized void increaseBy(FakeLoad load) throws MaximumLoadExceededException {
        checkMaximumLoadNotExceeded(load);

        this.cpu    += load.getCpuLoad();
        this.memory += load.getMemoryLoad();
        this.diskIO += load.getDiskIOLoad();
        this.netIO  += load.getNetIOLoad();

    }

    synchronized void decreaseBy(FakeLoad load) {
        checkNotBelowMinimumLoad(load);

        this.cpu -= load.getCpuLoad();
        this.memory -= load.getMemoryLoad();
        this.diskIO -= load.getDiskIOLoad();
        this.netIO -= load.getNetIOLoad();

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

        if (this.diskIO - load.getDiskIOLoad() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative disk IO load", load.getDiskIOLoad()));
        }

        if (this.netIO - load.getNetIOLoad() < 0) {
            throw new RuntimeException(String.format("Decrease of %d would cause a negative net IO load", load.getNetIOLoad()));
        }
    }


}
