package ac.at.tuwien.infosys.fakeload.internal;

import ac.at.tuwien.infosys.fakeload.FakeLoad;

import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by martensigwart on 04.07.17.
 */
final class Connection {


    @GuardedBy("this") private long cpuLoad;
    @GuardedBy("this") private long memoryLoad;
    @GuardedBy("this") private long diskIOLoad;
    @GuardedBy("this") private long netIOLoad;


    Connection() {
        cpuLoad     = 0L;
        memoryLoad  = 0L;
        diskIOLoad  = 0L;
        netIOLoad   = 0L;
    }

    synchronized long getCpuLoad() {
        return cpuLoad;
    }

    synchronized long getMemoryLoad() {
        return memoryLoad;
    }

    synchronized long getDiskIOLoad() {
        return diskIOLoad;
    }

    synchronized long getNetIOLoad() {
        return netIOLoad;
    }

    // TODO is synchronized enough synchronization
    synchronized void increaseBy(FakeLoad load) throws MaximumLoadExceededException {
        checkMaximumLoadNotExceeded(load);

        this.cpuLoad    += load.getCpuLoad();
        this.memoryLoad += load.getMemoryLoad();
        this.diskIOLoad += load.getDiskIOLoad();
        this.netIOLoad  += load.getNetIOLoad();

    }

    // TODO is synchronized enough synchronization
    synchronized void decreaseBy(FakeLoad load) {
        checkNotBelowMinimumLoad(load);

        this.cpuLoad    -= load.getCpuLoad();
        this.memoryLoad -= load.getMemoryLoad();
        this.diskIOLoad -= load.getDiskIOLoad();
        this.netIOLoad  -= load.getNetIOLoad();

    }


    private synchronized void checkMaximumLoadNotExceeded(FakeLoad load) throws MaximumLoadExceededException {
        if (this.cpuLoad + load.getCpuLoad() > 100)
            throw new MaximumLoadExceededException(String.format("Increase by %d would cause a CPU load of over 100%", load.getCpuLoad()));

        // check other load limits
    }

    private synchronized void checkNotBelowMinimumLoad(FakeLoad load) {
        if (this.cpuLoad - load.getCpuLoad() < 0) {
            throw new RuntimeException(String.format("Decrease by %d would cause a negative CPU load", load.getCpuLoad()));
        }

        if (this.memoryLoad - load.getMemoryLoad() < 0) {
            throw new RuntimeException(String.format("Decrease by %d would cause a negative memory load", load.getMemoryLoad()));
        }

        if (this.diskIOLoad - load.getDiskIOLoad() < 0) {
            throw new RuntimeException(String.format("Decrease by %d would cause a negative disk IO load", load.getDiskIOLoad()));
        }

        if (this.netIOLoad - load.getNetIOLoad() < 0) {
            throw new RuntimeException(String.format("Decrease by %d would cause a negative net IO load", load.getNetIOLoad()));
        }
    }


}
